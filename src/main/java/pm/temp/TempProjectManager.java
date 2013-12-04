package pm.temp;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import pm.db.ProjectDao;
import pm.db.TempProjectDao;
import pm.pojo.ProjectWrapper;
import pm.pojo.TempProject;

import com.thoughtworks.xstream.XStream;
import common.util.CustomException;

public class TempProjectManager {

	private Logger log = Logger.getLogger(TempProjectManager.class.getName()); 
	private String remoteUserHeader;
	private ProjectDao projectDao;
	private TempProjectDao tempDao;
	@Value("${session.duration.seconds}")
	private Integer sessionDuration;

	public synchronized void register(ProjectWrapper pw) throws Exception {
		Integer projectId = this.getNextTempId();
		pw.getProject().setId(projectId);
		this.register(projectId, pw);
	}

	public synchronized void register(Integer projectId, ProjectWrapper pw) throws Exception {
		tempDao.deleteExpiredProjects(this.sessionDuration);
		String currentUser=this.getTuakiriUniqueIdFromRequest();
		TempProject tp = this.tempDao.getProject(projectId);

		if (tp == null) {
			tp = new TempProject();
			tp.setId(projectId);
			tp.setLastVisited(System.currentTimeMillis()/1000);
			tp.setOwner(currentUser);
			tp.setProjectString(new XStream().toXML(pw));		
			tempDao.createProject(tp);
		} else {
			if (currentUser.equals(tp.getOwner())) {
				tp.setLastVisited(System.currentTimeMillis()/1000);
				this.tempDao.updateProject(tp);
			} else {
				String message = "This project is currently being edited";
				try {
					String owner = projectDao.getAdviserByTuakiriUniqueId(currentUser).getFullName();
					message += " by " + owner;
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				throw new CustomException(message);
			}
		}
	}

	public synchronized ProjectWrapper get(Integer projectId) throws Exception {
		tempDao.deleteExpiredProjects(this.sessionDuration);
		if (!this.isRegistered(projectId)) {
			throw new CustomException("Session lifetime for editing project expired.");
		}
		TempProject tp = tempDao.getProject(projectId);
		this.verifyCurrentUserIsOwner(tp.getOwner());
		tempDao.updateLastVisited(projectId);
		return (ProjectWrapper) new XStream().fromXML(tp.getProjectString());
	}

	public synchronized void update(Integer projectId, ProjectWrapper pw) throws Exception {
		tempDao.deleteExpiredProjects(this.sessionDuration);
		if (!this.isRegistered(projectId)) {
			throw new CustomException("Session lifetime for editing project expired.");
		}
		TempProject tp = tempDao.getProject(projectId);
		this.verifyCurrentUserIsOwner(tp.getOwner());
		tp.setLastVisited(System.currentTimeMillis()/1000);
		tp.setProjectString(new XStream().toXML(pw));		
		tempDao.updateProject(tp);
	}

	public synchronized void unregister(Integer projectId) throws Exception {
		tempDao.deleteExpiredProjects(this.sessionDuration);
		if (!this.isRegistered(projectId)) {
			return;
		}
		this.verifyCurrentUserIsOwner(tempDao.getOwner(projectId));
		tempDao.deleteProject(projectId);		
	}

	public synchronized Boolean isRegistered(Integer pid) throws Exception {
		tempDao.deleteExpiredProjects(this.sessionDuration);
		return tempDao.projectExists(pid);
	}
	
	private Integer getNextTempId() {
		return tempDao.getNextNewProjectId();
	}

	private String getTuakiriUniqueIdFromRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String user = (String) request.getAttribute(this.remoteUserHeader);
		if (user == null) {
			user = "NULL";
		}
		return user;
	}

	private void verifyCurrentUserIsOwner(String owner) throws CustomException {
		if (!owner.equals(this.getTuakiriUniqueIdFromRequest())) {
			String message = "This project is currently being edited";
			try {
			    String fullName = projectDao.getAdviserByTuakiriUniqueId(owner).getFullName();
			    message += " by " + fullName;
			} catch(Exception e) {}
			throw new CustomException(message);
		}		
	}

	public String getRemoteUserHeader() {
		return remoteUserHeader;
	}

	public void setRemoteUserHeader(String remoteUserHeader) {
		this.remoteUserHeader = remoteUserHeader;
	}

	public ProjectDao getProjectDao() {
		return projectDao;
	}

	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public TempProjectDao getTempDao() {
		return tempDao;
	}

	public void setTempDao(TempProjectDao tempDao) {
		this.tempDao = tempDao;
	}

	public Integer getSessionDuration() {
		return sessionDuration;
	}

	public void setSessionDuration(Integer sessionDuration) {
		this.sessionDuration = sessionDuration;
	}

}
