package signup.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pm.controller.GlobalController;
import signup.pojo.RequestAccount;

@Controller
public class RequestAccountController extends GlobalController {
	
	private Logger log = Logger.getLogger(RequestAccountController.class.getName()); 

	@RequestMapping(value = "requestaccount", method = RequestMethod.GET)
	public String edit(ModelMap modelMap) throws Exception {
		modelMap.addAttribute("requestaccount", new RequestAccount());
		return "requestaccount";
	}

    @RequestMapping(value="requestaccount", method=RequestMethod.POST)
    public String onSubmit(@Valid @ModelAttribute("requestaccount") RequestAccount requestAccount, BindingResult result) {
        System.err.println("---" + requestAccount.getInstitution());
    	if(result.hasErrors()) {
            return "requestaccount";
        }
        return "requestaccount";
    }
}
