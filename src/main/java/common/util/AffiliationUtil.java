package common.util;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pm.db.ProjectDao;
import pm.pojo.Affiliation;

public class AffiliationUtil {

    private final Log log = LogFactory.getLog(AffiliationUtil.class.getName());
    private ProjectDao projectDao;
    private final String SEPARATOR = " -- ";

    public String createAffiliationString(final String institution,
            final String division, final String department) {
        String returnValue = "";
        if (institution != null && !institution.trim().isEmpty()) {
            returnValue += institution;
            if (division != null && !division.trim().isEmpty()) {
                returnValue += SEPARATOR + division.trim();
                if (department != null && !department.trim().isEmpty()) {
                    returnValue += SEPARATOR + department.trim();
                }
            }
        }
        return returnValue;
    }

    public List<Affiliation> getAffiliations() throws Exception {
        return projectDao.getAffiliations();
    }

    public List<String> getAffiliationStrings() throws Exception {
        final List<Affiliation> affiliations = projectDao.getAffiliations();
        final List<String> affiliationStrings = new LinkedList<String>();
        if (affiliations != null) {
            for (final Affiliation a : affiliations) {
                String tmp = a.getInstitution().trim();
                if (!a.getDivision().trim().isEmpty()) {
                    tmp += SEPARATOR + a.getDivision().trim();
                    if (!a.getDepartment().trim().isEmpty()) {
                        tmp += SEPARATOR + a.getDepartment().trim();
                    }
                }
                affiliationStrings.add(tmp);
            }
        }
        return affiliationStrings;
    }

    public String getDepartmentFromAffiliationString(
            final String affiliationString) {
        String returnValue = "";
        if (affiliationString != null) {
            final String[] parts = affiliationString.split(SEPARATOR);
            if (parts.length > 2) {
                returnValue = parts[2];
            }
        }
        return returnValue;
    }

    public String getDivisionFromAffiliationString(
            final String affiliationString) {
        String returnValue = "";
        if (affiliationString != null) {
            final String[] parts = affiliationString.split(SEPARATOR);
            if (parts.length > 1) {
                returnValue = parts[1];
            }
        }
        return returnValue;
    }

    public String getInstitutionFromAffiliationString(
            final String affiliationString) {
        String returnValue = "";
        if (affiliationString != null) {
            returnValue = affiliationString.split(SEPARATOR)[0];
        }
        return returnValue;
    }

    public void setProjectDao(final ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

}
