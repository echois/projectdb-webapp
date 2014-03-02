package pm.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import pm.pojo.Adviser;
import pm.pojo.FollowUp;
import pm.pojo.ProjectWrapper;

@Controller
public class FollowUpController extends GlobalController {

    @RequestMapping(value = "deletefollowup", method = RequestMethod.GET)
    public RedirectView delete(final Integer id, final Integer projectId)
            throws Exception {
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        final List<FollowUp> tmp = new LinkedList<FollowUp>();
        for (final FollowUp f : pw.getFollowUps()) {
            if (!f.getId().equals(id)) {
                tmp.add(f);
            }
        }
        pw.setFollowUps(tmp);
        tempProjectManager.update(projectId, pw);
        return new RedirectView("editproject?id=" + projectId + "#followups");
    }

    @RequestMapping(value = "editfollowup", method = RequestMethod.GET)
    protected ModelAndView edit(final Integer id, final Integer projectId)
            throws Exception {
        final ModelAndView mav = new ModelAndView();
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        final Adviser a = projectDao
                .getAdviserByTuakiriUniqueId(getTuakiriUniqueIdFromRequest());
        FollowUp f = new FollowUp();
        f.setAdviserId(a.getId());
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        f.setDate(df.format(new Date()));
        for (final FollowUp fu : pw.getFollowUps()) {
            if (fu.getId().equals(id)) {
                f = fu;
            }
        }
        mav.addObject("adviserId", f.getAdviserId());
        mav.addObject("followUp", f);
        return mav;
    }

    @RequestMapping(value = "editfollowup", method = RequestMethod.POST)
    public RedirectView editPost(final FollowUp f) throws Exception {
        final Integer projectId = f.getProjectId();
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        // Set next FollowUp to 3 months from now
        final Date now = new Date();
        final Date nextFollowUp = new Date(now.getTime()
                + TimeUnit.DAYS.toMillis(30 * 3));
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        pw.getProject().setNextFollowUpDate(format.format(nextFollowUp));
        f.setAdviserName(projectDao.getAdviserById(f.getAdviserId())
                .getFullName());
        if (f.getId() == null) {
            f.setId(random.nextInt());
            pw.getFollowUps().add(f);
        } else {
            for (int i = 0; i < pw.getFollowUps().size(); i++) {
                if (pw.getFollowUps().get(i).getId().equals(f.getId())) {
                    f.setAttachments(pw.getFollowUps().get(i).getAttachments());
                    pw.getFollowUps().set(i, f);
                }
            }
        }
        tempProjectManager.update(projectId, pw);
        return new RedirectView("editproject?id=" + projectId + "#followups");
    }
}
