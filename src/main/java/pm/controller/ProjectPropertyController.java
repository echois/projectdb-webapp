package pm.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import pm.pojo.Adviser;
import pm.pojo.Facility;
import pm.pojo.ProjectProperty;
import pm.pojo.ProjectWrapper;
import pm.pojo.Review;
import pm.pojo.Site;

@Controller
public class ProjectPropertyController extends GlobalController {
	
	@RequestMapping(value = "deleteprojectproperty", method = RequestMethod.GET)
	public RedirectView delete(Integer id, Integer projectId) throws Exception {
		this.projectDao.deleteProjectProperty(id);
		return new RedirectView ("editproject?id=" + projectId + "#projectproperties");
	}
	
	@RequestMapping(value = "editprojectproperty", method = RequestMethod.GET)
	public ModelAndView edit(Integer id, Integer projectId) throws Exception {
		ModelAndView mav = new ModelAndView();
		ProjectProperty p = new ProjectProperty();
		p.setProjectId(projectId);
		if (id!=null) {
			p = this.projectDao.getProjectProperty(id);
		}
		HashMap<Integer,String> facilities = new LinkedHashMap<Integer, String>();
    	for (Facility f: this.projectDao.getFacilities()) {
    		facilities.put(f.getId(), f.getName());
    	}
		mav.addObject("property", p);
		mav.addObject("facilities", facilities);
		mav.addObject("propnames", this.projectDao.getPropnames());
		return mav;
	}
		
	@RequestMapping(value = "editprojectproperty", method = RequestMethod.POST)
	public RedirectView editPost(ProjectProperty p) throws Exception {
    	this.projectDao.upsertProjectProperty(p);
		return new RedirectView ("editproject?id=" + p.getProjectId() + "#projectproperties");
	}
}
