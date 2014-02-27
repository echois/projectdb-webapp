package pm.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import pm.pojo.APLink;
import pm.pojo.Adviser;
import pm.pojo.Project;

@Controller
public class AdviserController extends GlobalController {
    @RequestMapping(value = "deleteadviser", method = RequestMethod.GET)
    public RedirectView delete(final Integer id) throws Exception {
        projectDao.deleteAdviser(id);
        return new RedirectView("viewadvisers");
    }

    @RequestMapping(value = "editadviser", method = RequestMethod.GET)
    protected ModelAndView edit(final Integer id) throws Exception {
        Adviser a = new Adviser();
        final ModelAndView mav = new ModelAndView();
        if (id != null) {
            a = projectDao.getAdviserById(id);
            a.setInstitution(affiliationUtil.createAffiliationString(
                    a.getInstitution(), a.getDivision(), a.getDepartment()));
        } else {
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            a.setStartDate(df.format(new Date()));
            a.setPictureUrl(profileDefaultPicture);
        }
        mav.addObject("affiliations", affiliationUtil.getAffiliationStrings());
        mav.addObject("adviser", a);
        return mav;
    }

    @RequestMapping(value = "editadviser", method = RequestMethod.POST)
    public ModelAndView editPost(final Adviser a) throws Exception {
        final String valid = isAdviserValid(a);
        if (valid.equals("true")) {
            final String affiliationString = a.getInstitution();
            a.setInstitution(affiliationUtil
                    .getInstitutionFromAffiliationString(affiliationString));
            a.setDivision(affiliationUtil
                    .getDivisionFromAffiliationString(affiliationString));
            a.setDepartment(affiliationUtil
                    .getDepartmentFromAffiliationString(affiliationString));
            if (a.getId() != null) {
                projectDao.updateAdviser(a);
            } else {
                projectDao.createAdviser(a);
            }
            final ModelAndView mav = new ModelAndView("viewadviser");
            mav.addObject("adviser", a);
            return mav;
        } else {
            final ModelAndView mav = new ModelAndView();
            mav.addObject("adviser", a);
            mav.addObject("error", valid);
            mav.getModelMap().put("affiliations",
                    affiliationUtil.getAffiliationStrings());
            return mav;
        }
    }

    private String isAdviserValid(final Adviser a) throws Exception {
        if (a.getFullName().trim().equals("")) {
            return "Adviser name cannot be empty";
        }
        for (final Adviser other : projectDao.getAdvisers()) {
            if (a.getFullName().equals(other.getFullName())
                    && (a.getId() == null || !a.getId().equals(other.getId()))) {
                return a.getFullName() + " already exists in the database";
            }
        }
        return "true";
    }

    // See one adviser
    @RequestMapping(value = "viewadviser", method = RequestMethod.GET)
    public ModelAndView viewadviser(final Integer id) throws Exception {
        if (id == null) {
            return new ModelAndView(new RedirectView("viewadvisers"));
        }
        final ModelAndView mav = new ModelAndView();
        final Adviser a = projectDao.getAdviserById(id);
        final List<Project> ps = projectDao.getProjectsForAdviserId(a.getId());
        final Map<Integer, String> ar = new HashMap<Integer, String>();
        // mark projects as due if a review or follow-up is due
        final Date now = new Date();
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (final Project p : ps) {
            for (final APLink l : projectDao.getProjectWrapperById(p.getId())
                    .getApLinks()) {
                if (l.getAdviserId().equals(id)) {
                    ar.put(p.getId(), l.getAdviserRoleName());
                }
            }
            final String fuDate = p.getNextFollowUpDate();
            final String rDate = p.getNextReviewDate();
            if (!fuDate.equals("") && now.after(df.parse(fuDate))) {
                if (now.after(df.parse(fuDate))) {
                    p.setNextFollowUpDate(fuDate + " (due)");
                }
            }
            if (!rDate.equals("") && now.after(df.parse(rDate))) {
                if (now.after(df.parse(rDate))) {
                    p.setNextReviewDate(rDate + " (due)");
                }
            }
        }
        mav.addObject("adviserRole", ar);
        mav.addObject("adviser", a);
        mav.addObject("projects", ps);
        return mav;
    }

    // See all advisers
    @RequestMapping(value = "viewadvisers", method = RequestMethod.GET)
    public ModelAndView viewadvisers() throws Exception {
        final ModelAndView mav = new ModelAndView();
        final List<Adviser> al = projectDao.getAdvisers();
        mav.addObject("advisers", al);
        return mav;
    }
}
