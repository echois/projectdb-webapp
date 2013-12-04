package signup.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pm.controller.GlobalController;
import pm.pojo.InstitutionalRole;
import signup.pojo.RequestAccount;

@Controller
public class RequestAccountController extends GlobalController {
	
	private Logger log = Logger.getLogger(RequestAccountController.class.getName());

	@RequestMapping(value = "requestaccount", method = RequestMethod.GET)
	public String edit(Model m) throws Exception {
		this.augmentModel(m);
		m.addAttribute("requestaccount", new RequestAccount());
		return "requestaccount";
	}

    @RequestMapping(value="requestaccount", method=RequestMethod.POST)
    public String onSubmit(Model m, @Valid @ModelAttribute("requestaccount") RequestAccount requestAccount, BindingResult result) throws Exception {
    	this.augmentModel(m);
		if(result.hasErrors()) {
            return "requestaccount";
        }
        return "requestaccount";
    }
    
    private void augmentModel(Model m) throws Exception {
		List<InstitutionalRole> ir = this.projectDao.getInstitutionalRoles();
		HashMap<Integer,String> institutionalRoles = new LinkedHashMap<Integer, String>();
		if (ir != null) {
			for (InstitutionalRole role: ir) {
				institutionalRoles.put(role.getId(), role.getName());
			}
		}
		m.addAttribute("institutionalRoles", institutionalRoles);
		m.addAttribute("affiliations", this.affiliationUtil.getAffiliationStrings());    	
    }
}
