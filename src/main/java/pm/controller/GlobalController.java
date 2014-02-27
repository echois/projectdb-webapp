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

import common.util.AffiliationUtil;

@Controller
public class GlobalController {
    @Autowired
    protected AffiliationUtil affiliationUtil;
    @Autowired
    protected AuthzAspect authzAspect;
    @Value("${heatmapBaseUserUrl}")
    protected String heatmapBaseUserUrl;
    @Value("${jobauditBaseProjectUrl}")
    protected String jobauditBaseProjectUrl;
    @Value("${jobauditBaseUserUrl}")
    protected String jobauditBaseUserUrl;
    protected Log log = LogFactory.getLog(this.getClass().getName());
    @Value("${profile.default.picture}")
    protected String profileDefaultPicture;
    @Autowired
    protected ProjectDao projectDao;
    @Value("${proxy}")
    protected String proxy;
    protected Random random = new Random();
    @Value("${remoteUserHeader}")
    protected String remoteUserHeader;
    @Autowired
    protected TempProjectManager tempProjectManager;

    protected String getTuakiriUniqueIdFromRequest() {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String user = (String) request.getAttribute(remoteUserHeader);
        if (user == null) {
            user = "NULL";
        }
        return user;
    }
}
