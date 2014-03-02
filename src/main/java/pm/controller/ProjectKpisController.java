package pm.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import pm.pojo.Adviser;
import pm.pojo.Kpi;
import pm.pojo.KpiCode;
import pm.pojo.ProjectKpi;
import pm.pojo.ProjectWrapper;

@Controller
public class ProjectKpisController extends GlobalController {
    @RequestMapping(value = "deleteprojectkpi", method = RequestMethod.GET)
    public RedirectView delete(final Integer id, final Integer projectId)
            throws Exception {
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        final List<ProjectKpi> tmp = new LinkedList<ProjectKpi>();
        for (final ProjectKpi pk : pw.getProjectKpis()) {
            if (!pk.getId().equals(id)) {
                tmp.add(pk);
            }
        }
        pw.setProjectKpis(tmp);
        tempProjectManager.update(projectId, pw);
        return new RedirectView("editproject?id=" + projectId + "#kpis");
    }

    @RequestMapping(value = "editprojectkpi", method = RequestMethod.GET)
    public ModelAndView edit(final Integer projectId, final Integer id)
            throws Exception {
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        ProjectKpi k = new ProjectKpi();
        k.setProjectId(projectId);
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        k.setDate(df.format(new Date()));
        final ModelAndView mav = new ModelAndView();
        for (final ProjectKpi pk : pw.getProjectKpis()) {
            if (pk.getId().equals(id)) {
                k = pk;
            }
        }
        List<Kpi> kpis = new LinkedList<Kpi>();
        // Types (NESI-8, NESI-9)
        kpis = projectDao.getKpis();
        final Map<Integer, String> tmpkpis = new HashMap<Integer, String>();
        for (final Kpi kpi : kpis) {
            tmpkpis.put(kpi.getId(), kpi.getType() + "-" + kpi.getId() + ": "
                    + kpi.getTitle());
        }
        // For NESI-9 : throughput, cpucores etc
        final List<KpiCode> codes = projectDao.getKpiCodes();
        final Map<Integer, String> tmpcodes = new HashMap<Integer, String>();
        for (final KpiCode c : codes) {
            tmpcodes.put(c.getId(), c.getCode());
        }
        mav.addObject("projectkpi", k);
        mav.addObject("adviserId", k.getAdviserId());
        mav.addObject("kpis", tmpkpis);
        mav.addObject("codes", tmpcodes);
        return mav;

    }

    @RequestMapping(value = "editprojectkpi", method = RequestMethod.POST)
    public RedirectView editPost(final ProjectKpi pk) throws Exception {
        final Integer projectId = pk.getProjectId();
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        pk.setKpiTitle(projectDao.getKpiById(pk.getKpiId()).getTitle());
        pk.setKpiType(projectDao.getKpiById(pk.getKpiId()).getType());
        if (pk.getKpiId().equals(9)) {
            pk.setCodeName(projectDao.getKpiCodeNameById(pk.getCode()));
        } else {
            pk.setCode(0);
        }
        if (pk.getId() == null) {
            final Adviser a = projectDao
                    .getAdviserByTuakiriUniqueId(getTuakiriUniqueIdFromRequest());
            pk.setAdviserId(a.getId());
            pk.setAdviserName(a.getFullName());
            pk.setId(random.nextInt());
            pw.getProjectKpis().add(pk);
        } else {
            for (int i = 0; i < pw.getProjectKpis().size(); i++) {
                if (pw.getProjectKpis().get(i).getId().equals(pk.getId())) {
                    pk.setAdviserName(projectDao.getAdviserById(
                            pk.getAdviserId()).getFullName());
                    pw.getProjectKpis().set(i, pk);
                }
            }
        }
        tempProjectManager.update(projectId, pw);
        return new RedirectView("editproject?id=" + projectId + "#kpis");
    }

    @RequestMapping(value = "viewprojectkpis", method = RequestMethod.GET)
    public ModelAndView viewprojectkpis() throws Exception {
        final ModelAndView mav = new ModelAndView();
        mav.addObject("projectKpis", projectDao.getProjectKpis());
        return mav;
    }
}
