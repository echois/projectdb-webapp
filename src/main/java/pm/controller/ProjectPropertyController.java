package pm.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import pm.pojo.Facility;
import pm.pojo.ProjectProperty;

@Controller
public class ProjectPropertyController extends GlobalController {

    @RequestMapping(value = "deleteprojectproperty", method = RequestMethod.GET)
    public RedirectView delete(final Integer id, final Integer projectId)
            throws Exception {
        projectDao.deleteProjectProperty(id);
        return new RedirectView("editproject?id=" + projectId
                + "#projectproperties");
    }

    @RequestMapping(value = "editprojectproperty", method = RequestMethod.GET)
    public ModelAndView edit(final Integer id, final Integer projectId)
            throws Exception {
        final ModelAndView mav = new ModelAndView();
        ProjectProperty p = new ProjectProperty();
        p.setProjectId(projectId);
        if (id != null) {
            p = projectDao.getProjectProperty(id);
        }
        final HashMap<Integer, String> facilities = new LinkedHashMap<Integer, String>();
        for (final Facility f : projectDao.getFacilities()) {
            facilities.put(f.getId(), f.getName());
        }
        mav.addObject("property", p);
        mav.addObject("facilities", facilities);
        mav.addObject("propnames", projectDao.getPropnames());
        return mav;
    }

    @RequestMapping(value = "editprojectproperty", method = RequestMethod.POST)
    public RedirectView editPost(final ProjectProperty p) throws Exception {
        projectDao.upsertProjectProperty(p);
        return new RedirectView("editproject?id=" + p.getProjectId()
                + "#projectproperties");
    }
}
