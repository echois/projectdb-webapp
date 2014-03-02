package pm.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import pm.pojo.Adviser;
import pm.pojo.AdviserAction;
import pm.pojo.ProjectWrapper;

@Controller
public class AdviserActionController extends GlobalController {

    @RequestMapping(value = "deleteadviseraction", method = RequestMethod.GET)
    public RedirectView delete(final Integer id, final Integer projectId)
            throws Exception {
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        final List<AdviserAction> tmp = new LinkedList<AdviserAction>();
        for (final AdviserAction aa : pw.getAdviserActions()) {
            if (!aa.getId().equals(id)) {
                tmp.add(aa);
            }
        }
        pw.setAdviserActions(tmp);
        tempProjectManager.update(projectId, pw);
        return new RedirectView("editproject?id=" + projectId
                + "#adviseractions");
    }

    @RequestMapping(value = "editadviseraction", method = RequestMethod.GET)
    public ModelAndView edit(final Integer id, final Integer projectId)
            throws Exception {
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        final ModelAndView mav = new ModelAndView();
        AdviserAction aa = new AdviserAction();
        final Adviser a = projectDao
                .getAdviserByTuakiriUniqueId(getTuakiriUniqueIdFromRequest());
        aa.setAdviserId(a.getId());
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        aa.setDate(df.format(new Date()));
        for (final AdviserAction aac : pw.getAdviserActions()) {
            if (aac.getId().equals(id)) {
                aa = aac;
            }
        }
        mav.addObject("adviserAction", aa);
        mav.addObject("adviserId", aa.getAdviserId());
        return mav;
    }

    @RequestMapping(value = "editadviseraction", method = RequestMethod.POST)
    public RedirectView editPost(final AdviserAction aa) throws Exception {
        final Integer projectId = aa.getProjectId();
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        aa.setAdviserName(projectDao.getAdviserById(aa.getAdviserId())
                .getFullName());
        if (aa.getId() == null) {
            aa.setId(random.nextInt());
            pw.getAdviserActions().add(aa);
        } else {
            for (int i = 0; i < pw.getAdviserActions().size(); i++) {
                if (pw.getAdviserActions().get(i).getId().equals(aa.getId())) {
                    aa.setAttachments(pw.getAdviserActions().get(i)
                            .getAttachments());
                    pw.getAdviserActions().set(i, aa);
                }
            }
        }
        tempProjectManager.update(projectId, pw);
        return new RedirectView("editproject?id=" + projectId
                + "#adviseractions");
    }
}
