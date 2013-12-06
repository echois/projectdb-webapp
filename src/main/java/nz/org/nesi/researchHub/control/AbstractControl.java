package nz.org.nesi.researchHub.control;

import common.util.AffiliationUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import pm.db.ProjectDao;
import pm.temp.TempProjectManager;

import java.util.Random;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 5/12/13
 * Time: 11:08 AM
 */
@Controller
public class AbstractControl {

    @Autowired
	protected ProjectDao projectDao;
	@Autowired
	protected TempProjectManager tempProjectManager;
//	@Autowired
//	protected AuthzAspect authzAspect;
	@Autowired
	protected AffiliationUtil affiliationUtil;
//	@Value("${proxy}")
//	protected String proxy;
	@Value("${profile.default.picture}")
	protected String profileDefaultPicture;
//	@Value("${remoteUserHeader}")
//	protected String remoteUserHeader;
//	@Value("${heatmapBaseUserUrl}")
//	protected String heatmapBaseUserUrl;
//	@Value("${jobauditBaseUserUrl}")
//	protected String jobauditBaseUserUrl;
//	@Value("${jobauditBaseProjectUrl}")
//	protected String jobauditBaseProjectUrl;
	protected Log log = LogFactory.getLog(this.getClass().getName());
	protected Random random = new Random();

}
