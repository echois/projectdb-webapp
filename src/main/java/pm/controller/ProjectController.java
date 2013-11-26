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
	// See one project
	@RequestMapping(value = "viewproject", method = RequestMethod.GET)
	public ModelAndView viewproject(Integer id) throws Exception {
    	ModelAndView mav = new ModelAndView();
		ProjectWrapper pw = projectDao.getProjectWrapperById(id);
		mav.addObject("pw", pw);
		Date now = new Date();
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (!pw.getProject().getEndDate().trim().equals("")) {
			boolean expired = now.after(df.parse(pw.getProject().getEndDate()));
			mav.addObject("expired",expired);
		}
		for (RPLink r:pw.getRpLinks()) {
			if (!r.getResearcher().getEndDate().trim().equals("")) {
				if (now.after(df.parse(r.getResearcher().getEndDate()))) {
					r.getResearcher().setFullName(r.getResearcher().getFullName() + " (expired)");
				}
			}
		}
		for (APLink a:pw.getApLinks()) {
			if (a.getAdviser().getEndDate()!=null && !a.getAdviser().getEndDate().trim().equals("")) {
				if (now.after(df.parse(a.getAdviser().getEndDate()))) {
					a.getAdviser().setFullName(a.getAdviser().getFullName() + " (expired)");
				}
			}
		}
		return mav;
	}
	// See a filterable list of all projects
	@RequestMapping(value = "viewprojects", method = RequestMethod.GET)
	public ModelAndView viewprojects(String query) throws Exception {
		ModelAndView mav = new ModelAndView();
		List<Project> ps = projectDao.getProjects();
		List<Project> filtered = new LinkedList<Project>();
		String q = null;
		if (query!=null && !query.equals("")) q=query.toLowerCase();
		// mark projects as due if a review or follow-up is due
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (Project p: ps) {
			String nextFollowUpDate = p.getNextFollowUpDate().trim();
			String nextReviewDate = p.getNextReviewDate().trim();
			if (!nextFollowUpDate.equals("") && now.after(df.parse(p.getNextFollowUpDate()))) {
				p.setNextFollowUpDate(p.getNextFollowUpDate() + " (due)");
			}
			if (!nextReviewDate.equals("") && now.after(df.parse(p.getNextReviewDate()))) {
				p.setNextReviewDate(p.getNextReviewDate() + " (due)");
			}
			if (q!=null) {
				if (p.getName().toLowerCase().contains(q) || p.getDescription().toLowerCase().contains(q) || 
						p.getHostInstitution().toLowerCase().contains(q) || p.getNotes().toLowerCase().contains(q) ||
						p.getProjectCode().toLowerCase().contains(q) || p.getProjectTypeName().toLowerCase().contains(q) ||
						p.getRequirements().toLowerCase().contains(q) || p.getTodo()!=null && p.getTodo().toLowerCase().contains(q))
					filtered.add(p);
			}
		}
		if (q==null) {
			mav.addObject("projects", ps);
		} else {
			mav.addObject("projects", filtered);
			mav.addObject("query", q);
		}
		return mav;
	}
	
	@RequestMapping(value = "editproject", method = RequestMethod.GET)
    protected ModelAndView edit(Integer id) throws Exception {
		ModelAndView mav = new ModelAndView();
		List<ProjectType> projectTypes = projectDao.getProjectTypes();
		Map<Integer,String> pTypes = new LinkedHashMap<Integer,String>();
        if (projectTypes != null) {
            for (ProjectType pt : projectTypes) {
            	pTypes.put(pt.getId(), pt.getName());
            }    
        }
        List<ProjectStatus> st = projectDao.getProjectStatuses();
		Map<Integer,String> statuses = new LinkedHashMap<Integer,String>();
        if (st != null) {
            for (ProjectStatus s : st) {
            	statuses.put(s.getId(), s.getName());
            }    
        }
		ProjectWrapper pw = null;
		if (id == null) {
			pw = new ProjectWrapper();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = new Date();
			Date nextReview = new Date();
			Date nextFollowUp = new Date();
			nextReview.setYear(nextReview.getYear() + 1);
			nextFollowUp.setMonth(nextFollowUp.getMonth() + 3);
			pw.getProject().setStartDate(df.format(startDate));
			pw.getProject().setNextFollowUpDate(df.format(nextFollowUp));
			pw.getProject().setNextReviewDate(df.format(nextReview));
	  	    this.tempProjectManager.register(pw);
		} else {
			if (id < 0) {
				if (this.tempProjectManager.isRegistered(id)) {
					pw = tempProjectManager.get(id);
				} else {
					pw = new ProjectWrapper();
					pw.getProject().setId(id);
					this.tempProjectManager.register(id, pw);
				}
			} else {
				this.authzAspect.verifyUserIsAdviserOnProject(id);
				if (this.tempProjectManager.isRegistered(id)) {
					pw = tempProjectManager.get(id);
				} else {
					pw = this.projectDao.getProjectWrapperById(id);
				}
		  	    this.tempProjectManager.register(id, pw);
			}
		}
		pw.setSecondsLeft(this.tempProjectManager.getSessionDuration());
		mav.addObject("projectWrapper",pw);
        mav.addObject("projectTypes", pTypes);
        mav.addObject("statuses", statuses);
        mav.addObject("institutions", this.projectDao.getInstitutions());
        return mav;
    }
	
	@RequestMapping(value = "editproject", method = RequestMethod.POST)
	public RedirectView editPost(ProjectWrapper newPw) throws Exception {
		String op = newPw.getOperation();
		Integer pid = newPw.getProject().getId();
		this.authzAspect.verifyUserIsAdviserOnProject(pid);
		ProjectWrapper pw = tempProjectManager.get(pid);
		newPw.getProject().setProjectCode(pw.getProject().getProjectCode());
		pw.setProject(newPw.getProject());
		pw.setRedirect(newPw.getRedirect());
		pw.setSecondsLeft(this.tempProjectManager.getSessionDuration());
		if (pw.getProject().getStatusId().equals(4)) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			pw.getProject().setEndDate(df.format(new Date()));
		}

		if (op.equals("CANCEL")) {
			return handleCancel(pid);
		} else if (op.equals("RESET")) {
			this.tempProjectManager.unregister(pid);
			return new RedirectView("editproject?id="+pid);
		} else if (op.equals("UPDATE")) {
			return this.handleUpdate(pw);
		} else if (op.equals("SAVE_AND_CONTINUE_EDITING")) {
			return this.handleSaveAndContinue(pw);
		} else if (op.equals("SAVE_AND_FINISH_EDITING")) {
			return this.handleSaveAndFinish(pw);
		} else {
			throw new Exception("Unknown operation: " + op);
		}
	}
	
	@RequestMapping(value = "deleteproject", method = RequestMethod.GET)
	public RedirectView delete (Integer id) throws Exception {
		this.authzAspect.verifyUserIsAdviserOnProject(id);
        this.tempProjectManager.unregister(id);
    	this.projectDao.deleteProjectWrapper(id);
    	return new RedirectView("viewprojects");
	}
	
	protected RedirectView handleCancel(Integer pid) throws Exception {
		this.tempProjectManager.unregister(pid);			
		if (pid < 0) { // new project
			return new RedirectView("viewprojects");
		} else { // old project
			return new RedirectView("viewproject?id="+pid);
		}
	}

	protected RedirectView handleUpdate(ProjectWrapper pwCommand) throws Exception{
		Project p = pwCommand.getProject();
		String redirect = pwCommand.getRedirect();
		Integer pid = p.getId();
		ProjectWrapper pwTemp = this.tempProjectManager.get(pid);
		pwTemp.setProject(p);
		pwTemp.setErrorMessage("");
        this.tempProjectManager.update(pid, pwTemp);
		return new RedirectView(redirect);
	}
	
	protected synchronized RedirectView handleSaveAndFinish(ProjectWrapper pw) throws Exception {
		Integer pid = pw.getProject().getId();
		if (this.isProjectValid(pw)) {
			Project p = pw.getProject();
			pw = this.tempProjectManager.get(pid);
			pw.setProject(p);
			if (pid < 0) {
				String projectCode = this.projectDao.getNextProjectCode(p.getHostInstitution());
				pw.getProject().setProjectCode(projectCode);
				pid = this.projectDao.createProjectWrapper(pw);
			} else {
				this.projectDao.updateProjectWrapper(pid, pw);
			}
			this.tempProjectManager.unregister(pid);
			return new RedirectView("viewproject?id="+pid);
		} else {
			this.tempProjectManager.update(pid, pw);
			return new RedirectView("editproject?id="+pid);
		}
	}
	
	protected synchronized RedirectView handleSaveAndContinue(ProjectWrapper pw) throws Exception{
		Project p = pw.getProject();
		Integer pidOld = p.getId();
		Integer pid = pidOld;
		ProjectWrapper pwNew = this.tempProjectManager.get(pidOld);
		pwNew.setProject(p);
		if (this.isProjectValid(pwNew)) {
			pwNew.setErrorMessage("");
			if (pidOld < 0) {
				String projectCode = this.projectDao.getNextProjectCode(p.getHostInstitution());
				pw.getProject().setProjectCode(projectCode);
				pid = this.projectDao.createProjectWrapper(pwNew);
				pwNew.getProject().setId(pid);
				this.tempProjectManager.register(pwNew);
				this.tempProjectManager.unregister(pidOld);
			} else {
				this.tempProjectManager.update(pidOld, pwNew);
				this.projectDao.updateProjectWrapper(pidOld,pwNew);
			}
		} else {
			// save error message
			this.tempProjectManager.update(pid, pwNew);
		}
		return new RedirectView("editproject?id="+pid);
	}
	
	private boolean isProjectValid(ProjectWrapper pw) {
		if (pw.getProject().getName().trim().equals("")) {
			pw.setErrorMessage("A project must have a title");
			return false;				
		}		

		// Exactly one PI?
		int count = 0;
		for (RPLink rp: pw.getRpLinks()) {
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
		for (APLink ap: pw.getApLinks()) {
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
}
