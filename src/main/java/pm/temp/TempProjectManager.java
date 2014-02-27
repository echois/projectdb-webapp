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

    private final Logger log = Logger.getLogger(TempProjectManager.class
            .getName());
    private ProjectDao projectDao;
    private String remoteUserHeader;
    @Value("${session.duration.seconds}")
    private Integer sessionDuration;
    private TempProjectDao tempDao;

    public synchronized ProjectWrapper get(final Integer projectId)
            throws Exception {
        tempDao.deleteExpiredProjects(sessionDuration);
        if (!isRegistered(projectId)) {
            throw new CustomException(
                    "Session lifetime for editing project expired.");
        }
        final TempProject tp = tempDao.getProject(projectId);
        verifyCurrentUserIsOwner(tp.getOwner());
        tempDao.updateLastVisited(projectId);
        return (ProjectWrapper) new XStream().fromXML(tp.getProjectString());
    }

    private Integer getNextTempId() {
        return tempDao.getNextNewProjectId();
    }

    public ProjectDao getProjectDao() {
        return projectDao;
    }

    public String getRemoteUserHeader() {
        return remoteUserHeader;
    }

    public Integer getSessionDuration() {
        return sessionDuration;
    }

    public TempProjectDao getTempDao() {
        return tempDao;
    }

    private String getTuakiriUniqueIdFromRequest() {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String user = (String) request.getAttribute(remoteUserHeader);
        if (user == null) {
            user = "NULL";
        }
        return user;
    }

    public synchronized Boolean isRegistered(final Integer pid)
            throws Exception {
        tempDao.deleteExpiredProjects(sessionDuration);
        return tempDao.projectExists(pid);
    }

    public synchronized void register(final Integer projectId,
            final ProjectWrapper pw) throws Exception {
        tempDao.deleteExpiredProjects(sessionDuration);
        final String currentUser = getTuakiriUniqueIdFromRequest();
        TempProject tp = tempDao.getProject(projectId);

        if (tp == null) {
            tp = new TempProject();
            tp.setId(projectId);
            tp.setLastVisited(System.currentTimeMillis() / 1000);
            tp.setOwner(currentUser);
            tp.setProjectString(new XStream().toXML(pw));
            tempDao.createProject(tp);
        } else {
            if (currentUser.equals(tp.getOwner())) {
                tp.setLastVisited(System.currentTimeMillis() / 1000);
                tempDao.updateProject(tp);
            } else {
                String message = "This project is currently being edited";
                try {
                    final String owner = projectDao
                            .getAdviserByTuakiriUniqueId(currentUser)
                            .getFullName();
                    message += " by " + owner;
                } catch (final Exception e) {
                    log.error(e.getMessage(), e);
                }
                throw new CustomException(message);
            }
        }
    }

    public synchronized void register(final ProjectWrapper pw) throws Exception {
        final Integer projectId = getNextTempId();
        pw.getProject().setId(projectId);
        this.register(projectId, pw);
    }

    public void setProjectDao(final ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public void setRemoteUserHeader(final String remoteUserHeader) {
        this.remoteUserHeader = remoteUserHeader;
    }

    public void setSessionDuration(final Integer sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public void setTempDao(final TempProjectDao tempDao) {
        this.tempDao = tempDao;
    }

    public synchronized void unregister(final Integer projectId)
            throws Exception {
        tempDao.deleteExpiredProjects(sessionDuration);
        if (!isRegistered(projectId)) {
            return;
        }
        verifyCurrentUserIsOwner(tempDao.getOwner(projectId));
        tempDao.deleteProject(projectId);
    }

    public synchronized void update(final Integer projectId,
            final ProjectWrapper pw) throws Exception {
        tempDao.deleteExpiredProjects(sessionDuration);
        if (!isRegistered(projectId)) {
            throw new CustomException(
                    "Session lifetime for editing project expired.");
        }
        final TempProject tp = tempDao.getProject(projectId);
        verifyCurrentUserIsOwner(tp.getOwner());
        tp.setLastVisited(System.currentTimeMillis() / 1000);
        tp.setProjectString(new XStream().toXML(pw));
        tempDao.updateProject(tp);
    }

    private void verifyCurrentUserIsOwner(final String owner)
            throws CustomException {
        if (!owner.equals(getTuakiriUniqueIdFromRequest())) {
            String message = "This project is currently being edited";
            try {
                final String fullName = projectDao.getAdviserByTuakiriUniqueId(
                        owner).getFullName();
                message += " by " + fullName;
            } catch (final Exception e) {
            }
            throw new CustomException(message);
        }
    }

}
