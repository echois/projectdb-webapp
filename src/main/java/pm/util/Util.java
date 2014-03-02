package pm.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import pm.db.ProjectDao;
import pm.pojo.ProjectType;
import pm.pojo.ProjectWrapper;
import pm.pojo.Site;

public class Util {

    private final Log log = LogFactory.getLog(Util.class.getName());

    public void addProjectInfosToMav(final ModelAndView mav,
            final ProjectDao dao, final Integer projectId) throws Exception {
        final ProjectWrapper pw = dao.getProjectWrapperById(projectId);
        final List<ProjectType> projectTypes = dao.getProjectTypes();
        final List<Site> sites = dao.getSites();
        mav.addObject("projectwrapper", pw);
        mav.addObject("sites", sites);
        mav.addObject("projectTypes", projectTypes);
    }
}
