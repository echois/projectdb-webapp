package pm.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import pm.pojo.Adviser;
import pm.pojo.ProjectWrapper;
import pm.pojo.ResearchOutput;
import pm.pojo.ResearchOutputType;

@Controller
public class ResearchOutputController extends GlobalController {
    @RequestMapping(value = "deleteresearchoutput", method = RequestMethod.GET)
    public RedirectView delete(final Integer id, final Integer projectId)
            throws Exception {
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        final List<ResearchOutput> tmp = new LinkedList<ResearchOutput>();
        for (final ResearchOutput ro : pw.getResearchOutputs()) {
            if (!ro.getId().equals(id)) {
                tmp.add(ro);
            }
        }
        pw.setResearchOutputs(tmp);
        tempProjectManager.update(projectId, pw);
        return new RedirectView("editproject?id=" + projectId + "#outputs");
    }

    @RequestMapping(value = "editresearchoutput", method = RequestMethod.GET)
    public ModelAndView edit(final Integer id, final Integer projectId)
            throws Exception {
        ResearchOutput r = new ResearchOutput();
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        if (id != null) {
            for (final ResearchOutput ro : pw.getResearchOutputs()) {
                if (ro.getId().equals(id)) {
                    r = ro;
                }
            }
        } else {
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            r.setDate(df.format(new Date()));
        }
        final List<ResearchOutputType> researchOutputTypesTmp = projectDao
                .getResearchOutputTypes();
        final HashMap<Integer, String> researchOutputTypes = new LinkedHashMap<Integer, String>();
        if (researchOutputTypesTmp != null) {
            for (final ResearchOutputType rot : researchOutputTypesTmp) {
                researchOutputTypes.put(rot.getId(), rot.getName());
            }
        }
        final ModelAndView mav = new ModelAndView();
        mav.addObject("researchOutput", r);
        mav.addObject("adviserId", r.getAdviserId());
        mav.addObject("researchOutputTypes", researchOutputTypes);
        return mav;
    }

    @RequestMapping(value = "editresearchoutput", method = RequestMethod.POST)
    public RedirectView editPost(final ResearchOutput r) throws Exception {
        final Integer projectId = r.getProjectId();
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        r.setType(projectDao.getResearchOutputTypeById(r.getTypeId()).getName());
        if (r.getId() == null) {
            final Adviser a = projectDao
                    .getAdviserByTuakiriUniqueId(getTuakiriUniqueIdFromRequest());
            r.setAdviserId(a.getId());
            r.setAdviserName(a.getFullName());
            r.setId(random.nextInt());
            pw.getResearchOutputs().add(r);
        } else {
            for (int i = 0; i < pw.getResearchOutputs().size(); i++) {
                if (pw.getResearchOutputs().get(i).getId().equals(r.getId())) {
                    r.setAdviserName(projectDao
                            .getAdviserById(r.getAdviserId()).getFullName());
                    pw.getResearchOutputs().set(i, r);
                }
            }
        }

        tempProjectManager.update(projectId, pw);
        return new RedirectView("editproject?id=" + projectId + "#outputs");
    }

    @RequestMapping(value = "viewresearchoutput", method = RequestMethod.GET)
    public ModelAndView viewresearchoutput() throws Exception {
        final ModelAndView mav = new ModelAndView();
        mav.addObject("researchOutputs", projectDao.getResearchOutput());
        return mav;
    }
}
