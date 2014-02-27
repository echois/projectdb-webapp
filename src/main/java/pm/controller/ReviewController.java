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
import pm.pojo.ProjectWrapper;
import pm.pojo.Review;

@Controller
public class ReviewController extends GlobalController {

    @RequestMapping(value = "deletereview", method = RequestMethod.GET)
    public RedirectView delete(final Integer id, final Integer projectId)
            throws Exception {
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        final List<Review> tmp = new LinkedList<Review>();
        for (final Review r : pw.getReviews()) {
            if (!r.getId().equals(id)) {
                tmp.add(r);
            }
        }
        pw.setReviews(tmp);
        tempProjectManager.update(projectId, pw);
        return new RedirectView("editproject?id=" + projectId + "#reviews");
    }

    @RequestMapping(value = "editreview", method = RequestMethod.GET)
    public ModelAndView edit(final Integer id, final Integer projectId)
            throws Exception {
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        final ModelAndView mav = new ModelAndView();
        final Adviser a = projectDao
                .getAdviserByTuakiriUniqueId(getTuakiriUniqueIdFromRequest());
        Review r = new Review();
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        r.setDate(df.format(new Date()));
        r.setAdviserId(a.getId());
        for (final Review rv : pw.getReviews()) {
            if (rv.getId().equals(id)) {
                r = rv;
            }
        }
        mav.addObject("review", r);
        mav.addObject("adviserId", a.getId());
        return mav;
    }

    @RequestMapping(value = "editreview", method = RequestMethod.POST)
    public RedirectView editPost(final Review r) throws Exception {
        final Integer projectId = r.getProjectId();
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        // Set next Review to a year from now
        final Date now = new Date();
        final Date nextReview = new Date(now.getTime()
                + TimeUnit.DAYS.toMillis(365));
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        pw.getProject().setNextReviewDate(format.format(nextReview));
        r.setAdviserName(projectDao.getAdviserById(r.getAdviserId())
                .getFullName());
        if (r.getId() == null) {
            r.setId(random.nextInt());
            pw.getReviews().add(r);
        } else {
            for (int i = 0; i < pw.getReviews().size(); i++) {
                if (pw.getReviews().get(i).getId().equals(r.getId())) {
                    r.setAttachments(pw.getReviews().get(i).getAttachments());
                    pw.getReviews().set(i, r);
                }
            }
        }
        tempProjectManager.update(projectId, pw);
        return new RedirectView("editproject?id=" + projectId + "#reviews");
    }
}
