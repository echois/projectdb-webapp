package pm.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import pm.pojo.ProjectWrapper;
import pm.pojo.RPLink;
import pm.pojo.Researcher;
import pm.pojo.ResearcherRole;

@Controller
public class RPLinkController extends GlobalController {

    @RequestMapping(value = "/deleterplink", method = RequestMethod.GET)
    protected RedirectView delete(final Integer projectId, final Integer rid)
            throws Exception {
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        final List<RPLink> tmp = new LinkedList<RPLink>();
        for (final RPLink rl : pw.getRpLinks()) {
            if (!rl.getResearcherId().equals(rid)
                    || !rl.getProjectId().equals(projectId)) {
                tmp.add(rl);
            }
        }
        pw.setRpLinks(tmp);
        tempProjectManager.update(projectId, pw);
        return new RedirectView("editproject?id=" + projectId + "#researchers");
    }

    @RequestMapping(value = "/editrplink", method = RequestMethod.GET)
    protected ModelAndView edit(final Integer projectId, final Integer rid)
            throws Exception {
        final ModelAndView mav = new ModelAndView("editRPLink");
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        RPLink rpLink = new RPLink();
        mav.addObject("action", "Create");
        if (rid != null) {
            for (final RPLink r : pw.getRpLinks()) {
                if (r.getResearcherId().equals(rid)) {
                    rpLink = r;
                    mav.addObject("action", "Edit");
                }
            }
        }
        final List<ResearcherRole> rRolesTmp = projectDao.getResearcherRoles();
        final HashMap<Integer, String> researcherRoles = new LinkedHashMap<Integer, String>();
        if (rRolesTmp != null) {
            for (final ResearcherRole rr : rRolesTmp) {
                researcherRoles.put(rr.getId(), rr.getName());
            }
        }
        final List<Researcher> researchersTmp = projectDao.getResearchers();
        final HashMap<Integer, String> researchers = new LinkedHashMap<Integer, String>();
        if (researchersTmp != null) {
            for (final Researcher r : researchersTmp) {
                researchers.put(r.getId(), r.getFullName());
            }
        }
        mav.addObject("rpLink", rpLink);
        mav.addObject("researchers", researchers);
        mav.addObject("researcherRoles", researcherRoles);
        return mav;
    }

    @RequestMapping(value = "/editrplink", method = RequestMethod.POST)
    public RedirectView editPost(final RPLink rpLink, final Integer rid)
            throws Exception {
        final Integer projectId = rpLink.getProjectId();
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        rpLink.setResearcher(projectDao.getResearcherById(rpLink
                .getResearcherId()));
        rpLink.setResearcherRoleName(projectDao.getResearcherRoleById(
                rpLink.getResearcherRoleId()).getName());
        boolean found = false;
        for (int i = 0; i < pw.getRpLinks().size(); i++) {
            final Integer currentResearcherId = pw.getRpLinks().get(i)
                    .getResearcherId();
            // Either we modified an existing adviser, or we changed an adviser
            // record to a new adviser (original adviser id (aid) !=
            // apLink.adviserId)
            if (currentResearcherId.equals(rpLink.getResearcherId())
                    || currentResearcherId.equals(rid)) {
                found = true;
                pw.getRpLinks().set(i, rpLink);
            }
        }
        if (!found) {
            pw.getRpLinks().add(rpLink);
        }
        tempProjectManager.update(projectId, pw);
        return new RedirectView("editproject?id=" + projectId + "#researchers");
    }
}
