package pm.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import pm.pojo.InstitutionalRole;
import pm.pojo.Project;
import pm.pojo.Researcher;
import pm.pojo.ResearcherProperty;
import pm.pojo.ResearcherStatus;

@Controller
public class ResearcherController extends GlobalController {
    @RequestMapping(value = "deleteresearcher", method = RequestMethod.GET)
    public RedirectView delete(final Integer id) throws Exception {
        projectDao.deleteResearcher(id);
        return new RedirectView("viewresearchers");
    }

    @RequestMapping(value = "editresearcher", method = RequestMethod.GET)
    protected ModelAndView edit(final Integer id) throws Exception {
        final ModelAndView mav = new ModelAndView();
        final List<InstitutionalRole> iRolesTmp = projectDao
                .getInstitutionalRoles();
        final HashMap<Integer, String> iRoles = new LinkedHashMap<Integer, String>();
        if (iRolesTmp != null) {
            for (final InstitutionalRole ir : iRolesTmp) {
                iRoles.put(ir.getId(), ir.getName());
            }
        }
        Researcher r = new Researcher();
        if (id != null) {
            r = projectDao.getResearcherById(id);
            r.setInstitution(affiliationUtil.createAffiliationString(
                    r.getInstitution(), r.getDivision(), r.getDepartment()));
        } else {
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            r.setStartDate(df.format(new Date()));
            r.setPictureUrl(profileDefaultPicture);
        }
        final List<ResearcherStatus> st = projectDao.getResearcherStatuses();
        final Map<Integer, String> statuses = new LinkedHashMap<Integer, String>();
        if (st != null) {
            for (final ResearcherStatus rs : st) {
                statuses.put(rs.getId(), rs.getName());
            }
        }
        mav.addObject("statuses", statuses);
        mav.addObject("researcher", r);
        mav.addObject("affiliations", affiliationUtil.getAffiliationStrings());
        mav.addObject("institutionalRoles", iRoles);
        return mav;
    }

    @RequestMapping(value = "editresearcher", method = RequestMethod.POST)
    public ModelAndView editPost(final Researcher r) throws Exception {
        final String valid = isResearcherValid(r);
        if (valid.equals("true")) {
            final String affiliationString = r.getInstitution();
            r.setInstitution(affiliationUtil
                    .getInstitutionFromAffiliationString(affiliationString));
            r.setDivision(affiliationUtil
                    .getDivisionFromAffiliationString(affiliationString));
            r.setDepartment(affiliationUtil
                    .getDepartmentFromAffiliationString(affiliationString));
            if (r.getId() != null) {
                projectDao.updateResearcher(r);
            } else {
                projectDao.createResearcher(r);
            }
            return new ModelAndView(new RedirectView("viewresearcher?id="
                    + r.getId()));
        } else {
            final ModelAndView mav = new ModelAndView();
            mav.addObject("researcher", r);
            mav.addObject("error", valid);
            final List<InstitutionalRole> iRolesTmp = projectDao
                    .getInstitutionalRoles();
            final HashMap<Integer, String> iRoles = new LinkedHashMap<Integer, String>();
            if (iRolesTmp != null) {
                for (final InstitutionalRole ir : iRolesTmp) {
                    iRoles.put(ir.getId(), ir.getName());
                }
            }
            mav.getModelMap().put("institutionalRoles", iRoles);
            mav.getModelMap().put("affiliations",
                    affiliationUtil.getAffiliationStrings());
            return mav;
        }
    }

    private String isResearcherValid(final Researcher r) throws Exception {
        if (r.getFullName().trim().equals("")) {
            return "Researcher name cannot be empty";
        }
        for (final Researcher other : projectDao.getResearchers()) {
            if (r.getFullName().equals(other.getFullName())
                    && (r.getId() == null || !r.getId().equals(other.getId()))) {
                return r.getFullName() + " already exists in the database";
            }
        }
        return "true";
    }

    @RequestMapping(value = "viewresearcher", method = RequestMethod.GET)
    public ModelAndView viewresearcher(final Integer id) throws Exception {
        if (id == null) {
            return new ModelAndView(new RedirectView("viewresearchers"));
        }
        final ModelAndView mav = new ModelAndView();
        final Researcher r = projectDao.getResearcherById(id);
        final List<Project> ps = projectDao.getProjectsForResearcherId(r
                .getId());
        String linuxUsername = "";
        for (final ResearcherProperty rp : projectDao
                .getResearcherProperties(id)) {
            if (rp.getPropname().equals("linuxUsername")) {
                linuxUsername = rp.getPropvalue();
            }
        }
        mav.addObject("heatmapBaseUserUrl", heatmapBaseUserUrl);
        mav.addObject("jobauditBaseUserUrl", jobauditBaseUserUrl);
        mav.addObject("linuxUsername", linuxUsername);
        mav.addObject("researcher", r);
        mav.addObject("projects", ps);
        return mav;
    }

    @RequestMapping(value = "viewresearchers", method = RequestMethod.GET)
    public ModelAndView viewresearchers() throws Exception {
        final ModelAndView mav = new ModelAndView();
        final List<Researcher> rl = projectDao.getResearchers();
        mav.addObject("researchers", rl);
        return mav;
    }
}
