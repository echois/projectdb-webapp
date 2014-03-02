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

import pm.pojo.APLink;
import pm.pojo.Adviser;
import pm.pojo.AdviserRole;
import pm.pojo.ProjectWrapper;

@Controller
public class APLinkController extends GlobalController {

    @RequestMapping(value = "deleteaplink", method = RequestMethod.GET)
    protected RedirectView delete(final Integer projectId, final Integer aid)
            throws Exception {
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        final List<APLink> tmp = new LinkedList<APLink>();
        for (final APLink al : pw.getApLinks()) {
            if (!al.getAdviserId().equals(aid)
                    || !al.getProjectId().equals(projectId)) {
                tmp.add(al);
            }
        }
        pw.setApLinks(tmp);
        tempProjectManager.update(projectId, pw);
        return new RedirectView("editproject?id=" + projectId + "#advisers");
    }

    @RequestMapping(value = "editaplink", method = RequestMethod.GET)
    protected ModelAndView edit(final Integer projectId, final Integer aid)
            throws Exception {
        final ModelAndView mav = new ModelAndView("editAPLink");
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        APLink apLink = new APLink();
        mav.addObject("action", "Create");
        if (aid != null) {
            for (final APLink a : pw.getApLinks()) {
                if (a.getAdviserId().equals(aid)) {
                    apLink = a;
                    mav.addObject("action", "Edit");
                }
            }
        }
        final List<AdviserRole> aRolesTmp = projectDao.getAdviserRoles();
        final HashMap<Integer, String> adviserRoles = new LinkedHashMap<Integer, String>();
        if (aRolesTmp != null) {
            for (final AdviserRole ar : aRolesTmp) {
                adviserRoles.put(ar.getId(), ar.getName());
            }
        }
        final List<Adviser> advisersTmp = projectDao.getAdvisers();
        final HashMap<Integer, String> advisers = new LinkedHashMap<Integer, String>();
        if (advisersTmp != null) {
            for (final Adviser a : advisersTmp) {
                advisers.put(a.getId(), a.getFullName());
            }
        }
        mav.addObject("apLink", apLink);
        mav.addObject("advisers", advisers);
        mav.addObject("adviserRoles", adviserRoles);
        return mav;
    }

    @RequestMapping(value = "editaplink", method = RequestMethod.POST)
    public RedirectView editPost(final APLink apLink, final Integer aid)
            throws Exception {
        final Integer projectId = apLink.getProjectId();
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        apLink.setAdviser(projectDao.getAdviserById(apLink.getAdviserId()));
        apLink.setAdviserRoleName(projectDao.getAdviserRoleById(
                apLink.getAdviserRoleId()).getName());
        boolean found = false;
        for (int i = 0; i < pw.getApLinks().size(); i++) {
            final Integer currentAdviserId = pw.getApLinks().get(i)
                    .getAdviserId();
            // Either we modified an existing adviser, or we changed an adviser
            // record to a new adviser (original adviser id (aid) !=
            // apLink.adviserId)
            if (currentAdviserId.equals(apLink.getAdviserId())
                    || currentAdviserId.equals(aid)) {
                found = true;
                pw.getApLinks().set(i, apLink);
            }
        }
        if (!found) {
            pw.getApLinks().add(apLink);
        }
        tempProjectManager.update(projectId, pw);
        return new RedirectView("editproject?id=" + projectId + "#advisers");
    }
}
