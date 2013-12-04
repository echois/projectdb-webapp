package common.util;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pm.db.ProjectDao;
import pm.pojo.Affiliation;

public class AffiliationUtil {

	private Log log = LogFactory.getLog(AffiliationUtil.class.getName()); 
	private ProjectDao projectDao;
	private String SEPARATOR = " -- ";

	public List<Affiliation> getAffiliations() throws Exception {
		return this.projectDao.getAffiliations();
	}

	public List<String> getAffiliationStrings() throws Exception {
		List<Affiliation> affiliations = this.projectDao.getAffiliations();
		List<String> affiliationStrings = new LinkedList<String>();
		if (affiliations != null) {
			for (Affiliation a: affiliations) {
				String tmp = a.getInstitution().trim();
				if (!a.getDivision().trim().isEmpty()) {
					tmp += this.SEPARATOR + a.getDivision().trim();
					if (!a.getDepartment().trim().isEmpty()) {
						tmp += this.SEPARATOR + a.getDepartment().trim();
					}
				}
				affiliationStrings.add(tmp);
			}
		}
		return affiliationStrings;
	}

	public String getInstitutionFromAffiliationString(String affiliationString) {
		String returnValue = "";
		if (affiliationString != null) {
			returnValue = affiliationString.split(this.SEPARATOR)[0];
		}
		return returnValue;
	}

	public String getDivisionFromAffiliationString(String affiliationString) {
		String returnValue = "";
		if (affiliationString != null) {
			String[] parts = affiliationString.split(this.SEPARATOR);
			if (parts.length > 1) {
				returnValue = parts[1];
			}			
		}
		return returnValue;
	}

	public String getDepartmentFromAffiliationString(String affiliationString) {
		String returnValue = "";
		if (affiliationString != null) {
			String[] parts = affiliationString.split(this.SEPARATOR);
			if (parts.length > 2) {
				returnValue = parts[2];
			}
		}
		return returnValue;
	}

	public String createAffiliationString(String institution, String division, String department) {
		String returnValue = "";
		if (institution != null && !institution.trim().isEmpty()) {
			returnValue += institution;
			if (division != null && !division.trim().isEmpty()) {
				returnValue += this.SEPARATOR + division.trim();
				if (department != null && !department.trim().isEmpty()) {
					returnValue += this.SEPARATOR + department.trim();
				}
			}
		}
		return returnValue;
	}

	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

}
