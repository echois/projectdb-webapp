package pm.authz;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import common.util.CustomException;

import pm.db.ProjectDao;
import pm.pojo.Adviser;

public class AuthzAspect {

	private Log log = LogFactory.getLog(AuthzAspect.class.getName()); 
	private String remoteUserHeader;
	private ProjectDao projectDao;

	public void verifyUserIsAdviser() throws CustomException {
		log.info("verifying user " + this.getTuakiriUniqueIdFromRequest() + " is adviser");
		try {
			String tuakiriUniqueId = this.getTuakiriUniqueIdFromRequest();
			List<Adviser> advisers = this.projectDao.getAdvisers();
			if (advisers != null) {
				for (Adviser a: advisers) {
					String tid = a.getTuakiriUniqueId();
					if (tid != null && !tid.trim().equals("") && tid.equals(tuakiriUniqueId)) {
						return;
					}
				}
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
		throw new CustomException("Only an adviser can perform this operation.");
	}

	public void verifyUserIsAdviserOnProject(Integer projectId) throws CustomException {
		try {
			if (projectId < 1) {
				return;
			}
			String tuakiriUniqueId = this.getTuakiriUniqueIdFromRequest();
			Adviser tmp = this.projectDao.getAdviserByTuakiriUniqueId(tuakiriUniqueId);
			if (tmp != null) {
				if (tmp.getIsAdmin() > 0) {
					return;
				}
			}

			List<Adviser> advisers = this.projectDao.getAdvisersOnProject(projectId);
			if (advisers != null) {
				for (Adviser a: advisers) {
					String tid = a.getTuakiriUniqueId();
					if (tid != null && !tid.trim().equals("") && tid.equals(tuakiriUniqueId)) {
						return;
					}
				}
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
		throw new CustomException("Only an adviser of this project or an admin can perform this operation.");
	}

	public void verifyUserIsAdmin() throws CustomException {		
		log.info("verifying user " + this.getTuakiriUniqueIdFromRequest() + " is admin");
		try {
			String tuakiriUniqueId = this.getTuakiriUniqueIdFromRequest();
			if (tuakiriUniqueId != null && !tuakiriUniqueId.trim().equals("")) {
				Adviser adviser = this.projectDao.getAdviserByTuakiriUniqueId(tuakiriUniqueId);
				if (adviser.getIsAdmin() > 0) {
					return;
				}				
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
		throw new CustomException("Only an admin can perform this operation.");
	}

	public ProjectDao getProjectDao() {
		return projectDao;
	}

	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public String getRemoteUserHeader() {
		return remoteUserHeader;
	}

	public void setRemoteUserHeader(String remoteUserHeader) {
		this.remoteUserHeader = remoteUserHeader;
	}

	private String getTuakiriUniqueIdFromRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String user = (String) request.getAttribute(this.remoteUserHeader);
		if (user == null) {
			user = "NULL";
		}
		return user;
	}
}
