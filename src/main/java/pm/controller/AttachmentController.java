package pm.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import pm.pojo.AdviserAction;
import pm.pojo.Attachment;
import pm.pojo.FollowUp;
import pm.pojo.ProjectWrapper;
import pm.pojo.Review;

@Controller
public class AttachmentController extends GlobalController {

    @RequestMapping(value = "deleteattachment", method = RequestMethod.GET)
    public RedirectView delete(final Integer id, final Integer projectId,
            final String type, final Integer typeId) throws Exception {
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        if (type.equals("reviews")) {
            for (final Review r : pw.getReviews()) {
                if (r.getId().equals(typeId)) {
                    final List<Attachment> tmp = new LinkedList<Attachment>();
                    for (final Attachment a : r.getAttachments()) {
                        if (!a.getId().equals(id)) {
                            tmp.add(a);
                        }
                    }
                    r.setAttachments(tmp);
                }
            }
        } else if (type.equals("followups")) {
            for (final FollowUp f : pw.getFollowUps()) {
                if (f.getId().equals(typeId)) {
                    final List<Attachment> tmp = new LinkedList<Attachment>();
                    for (final Attachment a : f.getAttachments()) {
                        if (!a.getId().equals(id)) {
                            tmp.add(a);
                        }
                    }
                    f.setAttachments(tmp);
                }
            }
        } else if (type.equals("adviseractions")) {
            for (final AdviserAction aa : pw.getAdviserActions()) {
                if (aa.getId().equals(typeId)) {
                    final List<Attachment> tmp = new LinkedList<Attachment>();
                    for (final Attachment a : aa.getAttachments()) {
                        if (!a.getId().equals(id)) {
                            tmp.add(a);
                        }
                    }
                    aa.setAttachments(tmp);
                }
            }
        }
        tempProjectManager.update(projectId, pw);
        return new RedirectView("editproject?id=" + projectId + "#" + type);
    }

    @RequestMapping(value = "editattachment", method = RequestMethod.GET)
    public ModelAndView edit(final Integer id, final Integer projectId,
            final String type, final Integer typeId) throws Exception {
        Attachment a = new Attachment();
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        a.setDate(df.format(new Date()));
        final ProjectWrapper pw = tempProjectManager.get(projectId);
        if (id != null) {
            if (type.equals("reviews")) {
                for (final Review r : pw.getReviews()) {
                    if (r.getId().equals(typeId)) {
                        for (final Attachment at : r.getAttachments()) {
                            if (at.getId().equals(id)) {
                                a = at;
                            }
                        }
                    }
                }
            } else if (type.equals("followups")) {
                for (final FollowUp f : pw.getFollowUps()) {
                    if (f.getId().equals(typeId)) {
                        for (final Attachment at : f.getAttachments()) {
                            if (at.getId().equals(id)) {
                                a = at;
                            }
                        }
                    }
                }
            } else if (type.equals("adviseractions")) {
                for (final AdviserAction aa : pw.getAdviserActions()) {
                    if (aa.getId().equals(typeId)) {
                        for (final Attachment at : aa.getAttachments()) {
                            if (at.getId().equals(id)) {
                                a = at;
                            }
                        }
                    }
                }
            }
        }
        final ModelAndView mav = new ModelAndView();
        mav.addObject("attachment", a);
        return mav;
    }

    @RequestMapping(value = "editattachment", method = RequestMethod.POST)
    public RedirectView editPost(final Attachment a, final String type,
            final Integer typeId) throws Exception {
        final ProjectWrapper pw = tempProjectManager.get(a.getProjectId());
        if (a.getId() == null) {
            a.setId(random.nextInt());
            if (type.equals("reviews")) {
                for (final Review r : pw.getReviews()) {
                    if (r.getId().equals(typeId)) {
                        a.setReviewId(typeId);
                        r.getAttachments().add(a);
                    }
                }
            } else if (type.equals("followups")) {
                for (final FollowUp f : pw.getFollowUps()) {
                    if (f.getId().equals(typeId)) {
                        a.setFollowUpId(typeId);
                        f.getAttachments().add(a);
                    }
                }
            } else if (type.equals("adviseractions")) {
                for (final AdviserAction aa : pw.getAdviserActions()) {
                    if (aa.getId().equals(typeId)) {
                        a.setAdviserActionId(typeId);
                        aa.getAttachments().add(a);
                    }
                }
            }
        } else {
            if (type.equals("reviews")) {
                for (final Review r : pw.getReviews()) {
                    if (r.getId().equals(typeId)) {
                        for (int i = 0; i < r.getAttachments().size(); i++) {
                            if (r.getAttachments().get(i).getId()
                                    .equals(a.getId())) {
                                a.setReviewId(typeId);
                                r.getAttachments().set(i, a);
                            }
                        }
                    }
                }
            } else if (type.equals("followups")) {
                for (final FollowUp f : pw.getFollowUps()) {
                    if (f.getId().equals(typeId)) {
                        for (int i = 0; i < f.getAttachments().size(); i++) {
                            if (f.getAttachments().get(i).getId()
                                    .equals(a.getId())) {
                                a.setFollowUpId(typeId);
                                f.getAttachments().set(i, a);
                            }
                        }
                    }
                }
            } else if (type.equals("adviseractions")) {
                for (final AdviserAction aa : pw.getAdviserActions()) {
                    if (aa.getId().equals(typeId)) {
                        for (int i = 0; i < aa.getAttachments().size(); i++) {
                            if (aa.getAttachments().get(i).getId()
                                    .equals(a.getId())) {
                                a.setAdviserActionId(typeId);
                                aa.getAttachments().set(i, a);
                            }
                        }
                    }
                }
            }
        }
        tempProjectManager.update(a.getProjectId(), pw);
        return new RedirectView("editproject?id=" + a.getProjectId() + "#"
                + type);
    }
}
