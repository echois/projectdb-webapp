package pm.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import pm.pojo.APLink;
import pm.pojo.Project;
import pm.pojo.ProjectStatus;
import pm.pojo.ProjectType;
import pm.pojo.ProjectWrapper;
import pm.pojo.RPLink;

@Controller
public class ProjectController extends GlobalController {
    @RequestMapping(value = "deleteproject", method = RequestMethod.GET)
    public RedirectView delete(final Integer id) throws Exception {
        authzAspect.verifyUserIsAdviserOnProject(id);
        tempProjectManager.unregister(id);
        projectDao.deleteProjectWrapper(id);
        return new RedirectView("viewprojects");
    }

    @RequestMapping(value = "editproject", method = RequestMethod.GET)
    protected ModelAndView edit(final Integer id) throws Exception {
        final ModelAndView mav = new ModelAndView();
        final List<ProjectType> projectTypes = projectDao.getProjectTypes();
        final Map<Integer, String> pTypes = new LinkedHashMap<Integer, String>();
        if (projectTypes != null) {
            for (final ProjectType pt : projectTypes) {
                pTypes.put(pt.getId(), pt.getName());
            }
        }
        final List<ProjectStatus> st = projectDao.getProjectStatuses();
        final Map<Integer, String> statuses = new LinkedHashMap<Integer, String>();
        if (st != null) {
            for (final ProjectStatus s : st) {
                statuses.put(s.getId(), s.getName());
            }
        }
        ProjectWrapper pw = null;
        if (id == null) {
            pw = new ProjectWrapper();
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            final Date startDate = new Date();
            final Date nextReview = new Date();
            final Date nextFollowUp = new Date();
            nextReview.setYear(nextReview.getYear() + 1);
            nextFollowUp.setMonth(nextFollowUp.getMonth() + 3);
            pw.getProject().setStartDate(df.format(startDate));
            pw.getProject().setNextFollowUpDate(df.format(nextFollowUp));
            pw.getProject().setNextReviewDate(df.format(nextReview));
            tempProjectManager.register(pw);
        } else {
            if (id < 0) {
                if (tempProjectManager.isRegistered(id)) {
                    pw = tempProjectManager.get(id);
                } else {
                    pw = new ProjectWrapper();
                    pw.getProject().setId(id);
                    tempProjectManager.register(id, pw);
                }
            } else {
                authzAspect.verifyUserIsAdviserOnProject(id);
                if (tempProjectManager.isRegistered(id)) {
                    pw = tempProjectManager.get(id);
                } else {
                    pw = projectDao.getProjectWrapperById(id);
                }
                tempProjectManager.register(id, pw);
            }
        }
        pw.setSecondsLeft(tempProjectManager.getSessionDuration());
        mav.addObject("projectWrapper", pw);
        mav.addObject("projectTypes", pTypes);
        mav.addObject("statuses", statuses);
        mav.addObject("institutions", projectDao.getInstitutions());
        mav.addObject("properties", projectDao.getProjectProperties(id));
        return mav;
    }

    @RequestMapping(value = "editproject", method = RequestMethod.POST)
    public RedirectView editPost(final ProjectWrapper newPw) throws Exception {
        final String op = newPw.getOperation();
        final Integer pid = newPw.getProject().getId();
        authzAspect.verifyUserIsAdviserOnProject(pid);
        final ProjectWrapper pw = tempProjectManager.get(pid);
        // newPw.getProject().setProjectCode(pw.getProject().getProjectCode());
        pw.setProject(newPw.getProject());
        pw.setRedirect(newPw.getRedirect());
        pw.setSecondsLeft(tempProjectManager.getSessionDuration());

        if (pw.getProject().getProjectCode() == null
                || pw.getProject().getProjectCode().equals("")) {
            final String projectCode = projectDao.getNextProjectCode(pw
                    .getProject().getHostInstitution());
            pw.getProject().setProjectCode(projectCode);
        }

        if (op.equals("CANCEL")) {
            return handleCancel(pid);
        } else if (op.equals("RESET")) {
            tempProjectManager.unregister(pid);
            return new RedirectView("editproject?id=" + pid);
        } else if (op.equals("UPDATE")) {
            return handleUpdate(pw);
        } else if (op.equals("SAVE_AND_CONTINUE_EDITING")) {
            return handleSaveAndContinue(pw);
        } else if (op.equals("SAVE_AND_FINISH_EDITING")) {
            return handleSaveAndFinish(pw);
        } else {
            throw new Exception("Unknown operation: " + op);
        }
    }

    protected RedirectView handleCancel(final Integer pid) throws Exception {
        tempProjectManager.unregister(pid);
        if (pid < 0) { // new project
            return new RedirectView("viewprojects");
        } else { // old project
            return new RedirectView("viewproject?id=" + pid);
        }
    }

    protected synchronized RedirectView handleSaveAndContinue(
            final ProjectWrapper pw) throws Exception {
        final Project p = pw.getProject();
        final Integer pidOld = p.getId();
        Integer pid = pidOld;
        final ProjectWrapper pwNew = tempProjectManager.get(pidOld);
        pwNew.setProject(p);
        if (isProjectValid(pwNew)) {
            pwNew.setErrorMessage("");
            if (pidOld < 0) {
                pid = projectDao.createProjectWrapper(pwNew);
                pwNew.getProject().setId(pid);
                tempProjectManager.register(pwNew);
                tempProjectManager.unregister(pidOld);
            } else {
                tempProjectManager.update(pidOld, pwNew);
                projectDao.updateProjectWrapper(pidOld, pwNew);
            }
        } else {
            // save error message
            tempProjectManager.update(pid, pwNew);
        }
        return new RedirectView("editproject?id=" + pid);
    }

    protected synchronized RedirectView handleSaveAndFinish(ProjectWrapper pw)
            throws Exception {
        Integer pid = pw.getProject().getId();
        if (isProjectValid(pw)) {
            final Project p = pw.getProject();
            pw = tempProjectManager.get(pid);
            pw.setProject(p);
            if (pid < 0) {
                pid = projectDao.createProjectWrapper(pw);
            } else {
                projectDao.updateProjectWrapper(pid, pw);
            }
            tempProjectManager.unregister(pid);
            return new RedirectView("viewproject?id=" + pid);
        } else {
            tempProjectManager.update(pid, pw);
            return new RedirectView("editproject?id=" + pid);
        }
    }

    protected RedirectView handleUpdate(final ProjectWrapper pwCommand)
            throws Exception {
        final Project p = pwCommand.getProject();
        final String redirect = pwCommand.getRedirect();
        final Integer pid = p.getId();
        final ProjectWrapper pwTemp = tempProjectManager.get(pid);
        pwTemp.setProject(p);
        pwTemp.setErrorMessage("");
        tempProjectManager.update(pid, pwTemp);
        return new RedirectView(redirect);
    }

    private boolean isProjectValid(final ProjectWrapper pw) {
        if (pw.getProject().getName().trim().equals("")) {
            pw.setErrorMessage("A project must have a title");
            return false;
        }

        // Exactly one PI?
        int count = 0;
        for (final RPLink rp : pw.getRpLinks()) {
            if (rp.getResearcherRoleId() == 1) {
                count += 1;
            }
        }
        if (count == 0 || count > 1) {
            pw.setErrorMessage("There must be exactly 1 project owner on a project");
            return false;
        }

        // Exactly one primary adviser?
        count = 0;
        for (final APLink ap : pw.getApLinks()) {
            if (ap.getAdviserRoleId() == 1) {
                count += 1;
            }
        }
        if (count == 0 || count > 1) {
            pw.setErrorMessage("There must be exactly 1 primary adviser on a project");
            return false;
        }
        // At least one HPC
        if (pw.getProjectFacilities().isEmpty()) {
            pw.setErrorMessage("There must be at least one HPC facility associated with the project");
            return false;
        }
        return true;
    }

    // See one project
    @RequestMapping(value = "viewproject", method = RequestMethod.GET)
    public ModelAndView viewproject(final Integer id) throws Exception {
        final ModelAndView mav = new ModelAndView();
        final ProjectWrapper pw = projectDao.getProjectWrapperById(id);
        mav.addObject("pw", pw);
        final Date now = new Date();
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (pw.getProject().getStatusId().equals(4)) {
            mav.addObject("expired", true);
        }
        String poEmail = "";
        String poName = "";
        String othersEmails = "";
        for (final RPLink r : pw.getRpLinks()) {
            if (!r.getResearcher().getEndDate().trim().equals("")) {
                if (r.getResearcher().getStatusId().equals(2)) {
                    r.getResearcher().setFullName(
                            r.getResearcher().getFullName() + " (expired)");
                }
            }
            if (r.getResearcherRoleId().equals(1)) {
                poEmail = r.getResearcher().getEmail();
                poName = r.getResearcher().getFullName().split(" ")[0];
            } else {
                othersEmails += r.getResearcher().getEmail() + ',';
            }
        }
        for (final APLink a : pw.getApLinks()) {
            if (a.getAdviser().getEndDate() != null
                    && !a.getAdviser().getEndDate().trim().equals("")) {
                if (now.after(df.parse(a.getAdviser().getEndDate()))) {
                    a.getAdviser().setFullName(
                            a.getAdviser().getFullName() + " (expired)");
                }
            }
        }
        final String mailto = "mailto:" + poEmail + "?subject="
                + pw.getProject().getProjectCode() + "&cc=" + othersEmails
                + "&body=Dear " + poName + ",";
        mav.addObject("mailto", mailto);
        mav.addObject("jobauditBaseProjectUrl", jobauditBaseProjectUrl);
        mav.addObject("properties", projectDao.getProjectProperties(id));
        return mav;
    }

    // See a filterable list of all projects
    @RequestMapping(value = "viewprojects", method = RequestMethod.GET)
    public ModelAndView viewprojects(final String query) throws Exception {
        final ModelAndView mav = new ModelAndView();
        final List<Project> ps = projectDao.getProjects();
        final List<Project> filtered = new LinkedList<Project>();
        String q = null;
        if (query != null && !query.equals("")) {
            q = query.toLowerCase();
        }
        // mark projects as due if a review or follow-up is due
        final Date now = new Date();
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (final Project p : ps) {
            final String nextFollowUpDate = p.getNextFollowUpDate().trim();
            final String nextReviewDate = p.getNextReviewDate().trim();
            if (!nextFollowUpDate.equals("")
                    && now.after(df.parse(p.getNextFollowUpDate()))) {
                p.setNextFollowUpDate(p.getNextFollowUpDate() + " (due)");
            }
            if (!nextReviewDate.equals("")
                    && now.after(df.parse(p.getNextReviewDate()))) {
                p.setNextReviewDate(p.getNextReviewDate() + " (due)");
            }
            if (q != null) {
                if (p.getName().toLowerCase().contains(q)
                        || p.getDescription().toLowerCase().contains(q)
                        || p.getHostInstitution().toLowerCase().contains(q)
                        || p.getNotes().toLowerCase().contains(q)
                        || p.getProjectCode().toLowerCase().contains(q)
                        || p.getProjectTypeName().toLowerCase().contains(q)
                        || p.getRequirements().toLowerCase().contains(q)
                        || p.getTodo() != null
                        && p.getTodo().toLowerCase().contains(q)) {
                    filtered.add(p);
                }
            }
        }
        if (q == null) {
            mav.addObject("projects", ps);
        } else {
            mav.addObject("projects", filtered);
            mav.addObject("query", q);
        }
        mav.addObject("user", getTuakiriUniqueIdFromRequest());
        return mav;
    }
}
