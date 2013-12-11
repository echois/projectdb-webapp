package nz.org.nesi.researchHub.model;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 10/12/13
 * Time: 3:30 PM
 */
public class Person {

    private Integer id;
    private String nesiUniqueId;

    private String firstName;
    private String middleNames;
    private String lastName;
	private String email;
	private String phone;
	private String institution;
	private String division;
	private String department;
	private String startDate;

    private Map<Group, List<Role>> groups = Maps.newHashMap();


}
