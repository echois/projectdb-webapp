package pm.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import pm.authz.AuthzAspect;
import pm.db.ProjectDao;
import pm.temp.TempProjectManager;
import pm.util.AffiliationUtil;

@Controller
public class GlobalController {
	@Autowired
	protected ProjectDao projectDao;
	@Autowired
	protected TempProjectManager tempProjectManager;
	@Autowired
	protected AuthzAspect authzAspect;
	@Autowired
	protected AffiliationUtil affiliationUtil;
	@Value("${proxy}")
	protected String proxy;
	@Value("${profile.default.picture}")
	protected String profileDefaultPicture;
	@Value("${remoteUserHeader}")
	protected String remoteUserHeader;
	@Value("${heatmapBaseUserUrl}")
	protected String heatmapBaseUserUrl;
	@Value("${jobauditBaseUserUrl}")
	protected String jobauditBaseUserUrl;
	@Value("${jobauditBaseProjectUrl}")
	protected String jobauditBaseProjectUrl;
	protected Log log = LogFactory.getLog(this.getClass().getName());
	protected Random random = new Random();
	
	protected String getTuakiriUniqueIdFromRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String user = (String) request.getAttribute(this.remoteUserHeader);
		if (user == null) {
			user = "NULL";
		}
		return user;
	}
}
