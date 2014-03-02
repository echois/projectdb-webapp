package signup.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pm.db.ProjectDao;
import pm.pojo.InstitutionalRole;
import signup.pojo.RequestAccount;

import common.util.AffiliationUtil;

@Controller
public class RequestAccountController {

    @Autowired
    private AffiliationUtil affiliationUtil;
    private final Logger log = Logger.getLogger(RequestAccountController.class
            .getName());
    @Autowired
    private ProjectDao projectDao;

    private void augmentModel(final Model m) throws Exception {
        final List<InstitutionalRole> ir = projectDao.getInstitutionalRoles();
        final HashMap<Integer, String> institutionalRoles = new LinkedHashMap<Integer, String>();
        if (ir != null) {
            for (final InstitutionalRole role : ir) {
                institutionalRoles.put(role.getId(), role.getName());
            }
        }
        m.addAttribute("institutionalRoles", institutionalRoles);
        m.addAttribute("affiliations", affiliationUtil.getAffiliationStrings());
    }

    @RequestMapping(value = "requestaccount", method = RequestMethod.GET)
    public String edit(final Model m) throws Exception {
        augmentModel(m);
        m.addAttribute("requestaccount", new RequestAccount());
        return "requestaccount";
    }

    @RequestMapping(value = "requestaccount", method = RequestMethod.POST)
    public String onSubmit(
            final Model m,
            @Valid @ModelAttribute("requestaccount") final RequestAccount requestAccount,
            final BindingResult result) throws Exception {
        augmentModel(m);
        if (result.hasErrors()) {
            return "requestaccount";
        }
        return "requestaccount";
    }
}
