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
import pm.pojo.ResearcherStatus;

@Controller
public class ResearcherController extends GlobalController {
	@RequestMapping(value = "viewresearcher", method = RequestMethod.GET)
	public ModelAndView viewresearcher(Integer id) throws Exception {
		if (id==null) return new ModelAndView(new RedirectView("viewresearchers"));
    	ModelAndView mav = new ModelAndView();
    	Researcher r = projectDao.getResearcherById(id);
    	List<Project> ps = projectDao.getProjectsForResearcherId(r.getId());
    	mav.addObject("heatmapBaseUserUrl",heatmapBaseUserUrl);
    	mav.addObject("jobauditBaseUserUrl",jobauditBaseUserUrl);
    	mav.addObject("linuxUsername", projectDao.getLinuxUsername(r.getId()));
    	mav.addObject("researcher", r);
    	mav.addObject("projects", ps);
		return mav;
	}
	@RequestMapping(value = "viewresearchers", method = RequestMethod.GET)
	public ModelAndView viewresearchers() throws Exception {
    	ModelAndView mav = new ModelAndView();
    	List<Researcher> rl = projectDao.getResearchers();
    	mav.addObject("researchers", rl);
		return mav;
	}
	@RequestMapping(value = "deleteresearcher", method = RequestMethod.GET)
	public RedirectView delete(Integer id) throws Exception {
    	this.projectDao.deleteResearcher(id);
    	return new RedirectView("viewresearchers");
	}
	@RequestMapping(value = "editresearcher", method = RequestMethod.GET)
    protected ModelAndView edit(Integer id) throws Exception {
		ModelAndView mav = new ModelAndView();
		List<InstitutionalRole> iRolesTmp = this.projectDao.getInstitutionalRoles();
		HashMap<Integer,String> iRoles = new LinkedHashMap<Integer, String>();
		if (iRolesTmp != null) {
			for (InstitutionalRole ir: iRolesTmp) {
				iRoles.put(ir.getId(), ir.getName());
			}
		}
		Researcher r = new Researcher();
		if (id!=null) {
			r = this.projectDao.getResearcherById(id);
			r.setInstitution(affiliationUtil.createAffiliationString(r.getInstitution(), r.getDivision(), r.getDepartment()));
		} else {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			r.setStartDate(df.format(new Date()));
			r.setPictureUrl(this.profileDefaultPicture);
		}
		List<ResearcherStatus> st = projectDao.getResearcherStatuses();
		Map<Integer,String> statuses = new LinkedHashMap<Integer,String>();
        if (st != null) {
            for (ResearcherStatus rs : st) {
            	statuses.put(rs.getId(), rs.getName());
            }    
        }
        mav.addObject("statuses",statuses);
		mav.addObject("researcher",r);
		mav.addObject("affiliations", this.affiliationUtil.getAffiliationStrings());
		mav.addObject("institutionalRoles", iRoles);
        return mav;
	}
	@RequestMapping(value = "editresearcher", method = RequestMethod.POST)
	public ModelAndView editPost(Researcher r) throws Exception {
		String valid = isResearcherValid(r);
		if (valid.equals("true")) {
			String affiliationString = r.getInstitution();
			r.setInstitution(this.affiliationUtil.getInstitutionFromAffiliationString(affiliationString));
			r.setDivision(this.affiliationUtil.getDivisionFromAffiliationString(affiliationString));
			r.setDepartment(this.affiliationUtil.getDepartmentFromAffiliationString(affiliationString));
	        if (r.getId()!=null) this.projectDao.updateResearcher(r);
	        else this.projectDao.createResearcher(r);
			return new ModelAndView(new RedirectView("viewresearcher?id=" + r.getId()));
		} else {
			ModelAndView mav = new ModelAndView();
			mav.addObject("researcher", r);
			mav.addObject("error", valid);
			List<InstitutionalRole> iRolesTmp = this.projectDao.getInstitutionalRoles();
			HashMap<Integer,String> iRoles = new LinkedHashMap<Integer, String>();
			if (iRolesTmp != null) {
				for (InstitutionalRole ir: iRolesTmp) {
					iRoles.put(ir.getId(), ir.getName());
				}
			}
			mav.getModelMap().put("institutionalRoles", iRoles);
			mav.getModelMap().put("affiliations", this.affiliationUtil.getAffiliationStrings());
			return mav;
		}
	}
	private String isResearcherValid(Researcher r) throws Exception {
		if (r.getFullName().trim().equals("")) {
			return "Researcher name cannot be empty";
		}
		for (Researcher other:projectDao.getResearchers()) {
			if (r.getFullName().equals(other.getFullName()) && (r.getId()==null || !r.getId().equals(other.getId()))) {
				return r.getFullName() + " already exists in the database";
			}
		}
		return "true";
	}
}
