package pm.authz;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import pm.db.ProjectDao;
import pm.pojo.Adviser;

import common.util.CustomException;

public class AuthzAspect {

    private final Log log = LogFactory.getLog(AuthzAspect.class.getName());
    private ProjectDao projectDao;
    private String remoteUserHeader;

    public Integer getAdviserId() throws Exception {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String user = request.getHeader(remoteUserHeader);
        return projectDao.getAdviserByTuakiriUniqueId(user).getId();
    }

    public ProjectDao getProjectDao() {
        return projectDao;
    }

    public String getRemoteUserHeader() {
        return remoteUserHeader;
    }

    public String getTuakiriUniqueIdFromRequest() {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String user = request.getHeader(remoteUserHeader);
        if (user == null) {
            user = "NULL";
        }
        return user;
    }

    public void setProjectDao(final ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public void setRemoteUserHeader(final String remoteUserHeader) {
        this.remoteUserHeader = remoteUserHeader;
    }

    public void verifyUserIsAdmin() throws CustomException {
        log.info("verifying user " + getTuakiriUniqueIdFromRequest()
                + " is admin");
        try {
            final String tuakiriUniqueId = getTuakiriUniqueIdFromRequest();
            if (tuakiriUniqueId != null && !tuakiriUniqueId.trim().equals("")) {
                final Adviser adviser = projectDao
                        .getAdviserByTuakiriUniqueId(tuakiriUniqueId);
                if (adviser.getIsAdmin() > 0) {
                    return;
                }
            }
        } catch (final Exception e) {
            throw new CustomException(e.getMessage());
        }
        throw new CustomException("Only an admin can perform this operation.");
    }

    public void verifyUserIsAdviser() throws CustomException {
        log.info("verifying user " + getTuakiriUniqueIdFromRequest()
                + " is adviser");
        try {
            final String tuakiriUniqueId = getTuakiriUniqueIdFromRequest();
            final List<Adviser> advisers = projectDao.getAdvisers();
            if (advisers != null) {
                for (final Adviser a : advisers) {
                    final String tid = a.getTuakiriUniqueId();
                    if (tid != null && !tid.trim().equals("")
                            && tid.equals(tuakiriUniqueId)) {
                        return;
                    }
                }
            }
        } catch (final Exception e) {
            throw new CustomException(e.getMessage());
        }
        throw new CustomException("Only an adviser can perform this operation.");
    }

    public void verifyUserIsAdviserOnProject(final Integer projectId)
            throws CustomException {
        try {
            if (projectId < 1) {
                return;
            }
            final String tuakiriUniqueId = getTuakiriUniqueIdFromRequest();
            final Adviser tmp = projectDao
                    .getAdviserByTuakiriUniqueId(tuakiriUniqueId);
            if (tmp != null) {
                if (tmp.getIsAdmin() > 0) {
                    return;
                }
            }

            final List<Adviser> advisers = projectDao
                    .getAdvisersOnProject(projectId);
            if (advisers != null) {
                for (final Adviser a : advisers) {
                    final String tid = a.getTuakiriUniqueId();
                    if (tid != null && !tid.trim().equals("")
                            && tid.equals(tuakiriUniqueId)) {
                        return;
                    }
                }
            }
        } catch (final Exception e) {
            throw new CustomException(e.getMessage());
        }
        throw new CustomException(
                getTuakiriUniqueIdFromRequest()
                        + " is unauthorised. Only an adviser of this project or an admin can perform this operation.");
    }
}
