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
	// See one adviser
	@RequestMapping(value = "viewadviser", method = RequestMethod.GET)
	public ModelAndView viewadviser(Integer id) throws Exception {
    	if (id==null) return new ModelAndView(new RedirectView("viewadvisers"));
		ModelAndView mav = new ModelAndView();
    	Adviser a = projectDao.getAdviserById(id);
    	List<Project> ps = projectDao.getProjectsForAdviserId(a.getId());
    	Map<Integer,String> ar = new HashMap<Integer,String>();
    	// mark projects as due if a review or follow-up is due
    	Date now = new Date();
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	for (Project p: ps) {
    		for (APLink l: projectDao.getProjectWrapperById(p.getId()).getApLinks()) {
    			if (l.getAdviserId().equals(id)) {
    				ar.put(p.getId(),l.getAdviserRoleName());
    			}
    		}
        	String fuDate = p.getNextFollowUpDate();
        	String rDate = p.getNextReviewDate();
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
    	ModelAndView mav = new ModelAndView();
    	List<Adviser> al = projectDao.getAdvisers();
    	mav.addObject("advisers", al);
		return mav;
	}
	@RequestMapping(value = "deleteadviser", method = RequestMethod.GET)
	public RedirectView delete(Integer id) throws Exception {
    	this.projectDao.deleteAdviser(id);
    	return new RedirectView("viewadvisers");
	}
	@RequestMapping(value = "editadviser", method = RequestMethod.GET)
	protected ModelAndView edit(Integer id) throws Exception {
		Adviser a = new Adviser();
		ModelAndView mav = new ModelAndView();
		if (id!=null) {
			a = this.projectDao.getAdviserById(id);
			a.setInstitution(affiliationUtil.createAffiliationString(a.getInstitution(), a.getDivision(), a.getDepartment()));
		} else {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			a.setStartDate(df.format(new Date()));
			a.setPictureUrl(this.profileDefaultPicture);
		}
		mav.addObject("affiliations", this.affiliationUtil.getAffiliationStrings());
		mav.addObject("adviser", a);
		return mav;
	}
	
	@RequestMapping(value = "editadviser", method = RequestMethod.POST)
	public ModelAndView editPost(Adviser a) throws Exception {
		String valid = isAdviserValid(a);
		if (valid.equals("true")) {
		String affiliationString = a.getInstitution();
			a.setInstitution(this.affiliationUtil.getInstitutionFromAffiliationString(affiliationString));
			a.setDivision(this.affiliationUtil.getDivisionFromAffiliationString(affiliationString));
			a.setDepartment(this.affiliationUtil.getDepartmentFromAffiliationString(affiliationString));
			if (a.getId()!=null) this.projectDao.updateAdviser(a);
			else this.projectDao.createAdviser(a);
	    	ModelAndView mav = new ModelAndView("viewadviser");
			mav.addObject("adviser", a);
			return mav;
		} else {
			ModelAndView mav = new ModelAndView();
			mav.addObject("adviser",a);
			mav.addObject("error", valid);
			mav.getModelMap().put("affiliations", this.affiliationUtil.getAffiliationStrings());
			return mav;
		}
	}
	
	private String isAdviserValid(Adviser a) throws Exception {
		if (a.getFullName().trim().equals("")) {
			return "Adviser name cannot be empty";
		}
		for (Adviser other:projectDao.getAdvisers()) {
			if (a.getFullName().equals(other.getFullName()) && (a.getId()==null || !a.getId().equals(other.getId()))) {
				return a.getFullName() + " already exists in the database";
			}
		}
		return "true";
	}
}
