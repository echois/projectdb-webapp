package nz.org.nesi.researchHub.control;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import pm.authz.AuthzAspect;
import pm.db.ProjectDao;

import common.util.AffiliationUtil;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 5/12/13 Time: 11:08 AM
 */
@Controller
public class AbstractControl {

    @Autowired
    protected AffiliationUtil affiliationUtil;
    // @Autowired
    // protected TempProjectManager tempProjectManager;
    @Autowired
    public AuthzAspect authzAspect;
    // @Value("${remoteUserHeader}")
    // protected String remoteUserHeader;
    // @Value("${heatmapBaseUserUrl}")
    // protected String heatmapBaseUserUrl;
    // @Value("${jobauditBaseUserUrl}")
    // protected String jobauditBaseUserUrl;
    // @Value("${jobauditBaseProjectUrl}")
    // protected String jobauditBaseProjectUrl;
    protected Log log = LogFactory.getLog(this.getClass().getName());
    // @Value("${proxy}")
    // protected String proxy;
    @Value("${profile.default.picture}")
    protected String profileDefaultPicture;
    @Autowired
    protected ProjectDao projectDao;
    protected Random random = new Random();

}
