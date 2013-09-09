package pm.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import pm.db.ProjectDao;
import pm.pojo.APLink;
import pm.pojo.Advisor;
import pm.pojo.AdvisorRole;
import pm.util.Util;

public class CreateAPLinkController extends SimpleFormController {

	private Log log = LogFactory.getLog(CreateAPLinkController.class.getName()); 
	private ProjectDao projectDao;
	private String proxy;
	
	@Override
	public ModelAndView onSubmit(Object o) throws Exception {
		APLink apLink = (APLink) o;
    	ModelAndView mav = new ModelAndView(super.getSuccessView());
    	Integer projectId = apLink.getProjectId();
		mav.addObject("id", projectId);
		mav.addObject("proxy", this.proxy);
		this.projectDao.createAPLink(projectId, apLink);
		new Util().addProjectInfosToMav(mav, this.projectDao, projectId);
		return mav;
	}

	@Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
		ModelMap modelMap = new ModelMap();
		Integer pid = Integer.valueOf(request.getParameter("id"));
		List<Advisor> notOnProject = this.projectDao.getAllAdvisorsNotOnProject(pid);
		List<AdvisorRole> aRolesTmp = this.projectDao.getAllAdvisorRoles();
		HashMap<Integer,String> advisorRoles = new LinkedHashMap<Integer, String>();
		if (aRolesTmp != null) {
			for (AdvisorRole ar: aRolesTmp) {
				advisorRoles.put(ar.getId(), ar.getName());
			}
		}
		Map<Integer,String> aNotOnProject = new LinkedHashMap<Integer,String>();
		if (notOnProject != null) {
			for (Advisor a : notOnProject) {
				aNotOnProject.put(a.getId(), a.getFullName());
			}
		}
		modelMap.put("pid", pid);
        modelMap.put("aNotOnProject", aNotOnProject);
        modelMap.put("advisorRoles", advisorRoles);
        return modelMap;
    }
	
	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
}
