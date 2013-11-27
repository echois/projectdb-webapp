Feature: Create an Adviser, a Researcher, a ResearchOutput, a KPI, and a Project, and link them all together
  In order to create a meaningful project
  I need to link it to an Adviser and a Researcher etc
  
  @nojs @authenticated @researcher @create
  Scenario: Try to create a researcher as an authenticated user who is not an adviser/admin
    When I'm logged in as authenticated
    When I go to "/viewresearchers"
    And I follow "Create new researcher"
    And I fill in the following <formdetails>
      | field_type | form_id                | value                                                                |
      | text       | fullName               | `Chuck Norris                                                        |
      | text       | pictureUrl             | http://images.wikia.com/tesfanon/images/5/5c/Chuck_Norris.jpg        |
      | text       | email                  | chuck@space.com                                                      |
      | text       | phone                  | 1234                                                                 |
      | text       | institution            | University of Auckland -- Faculty of Science -- Centre for eResearch |
      | select     | institutionalRoleId    | Other Student                                                        |
      | text       | startDate              | 2009-01-01                                                           |
      | text       | notes                  | Chuck Norris can instantiate an abstract class                       |
    Then I press "Save"
    Then I should not see "already exists in the database"
    Then I should see "Only an adviser can perform this operation"
  
  @nojs @researcher @create
  Scenario: Create a researcher
    When I'm logged in as adviser
    When I go to "/viewresearchers"
    And I follow "Create new researcher"
    And I fill in the following <formdetails>
      | field_type | form_id                | value                                                                |
      | text       | fullName               | `Chuck Norris                                                        |
      | text       | pictureUrl             | http://images.wikia.com/tesfanon/images/5/5c/Chuck_Norris.jpg        |
      | text       | email                  | chuck@space.com                                                      |
      | text       | phone                  | 1234                                                                 |
      | text       | institution            | University of Auckland -- Faculty of Science -- Centre for eResearch |
      | select     | institutionalRoleId    | Other Student                                                        |
      | text       | startDate              | 2009-01-01                                                           |
      | text       | notes                  | Chuck Norris can instantiate an abstract class                       |
    Then I press "Save"
    Then I should not see "already exists in the database"
    Then I wait until I see "Edit"
    
  @nojs @adviser @create
  Scenario: Create an adviser
    When I'm logged in as adviser
    When I go to "/viewadvisers"
    And I follow "Create new adviser"
    And I fill in the following <formdetails>
      | field_type | form_id                | value                                                                                                  |
      | text       | fullName               | `Batman                                                                                                |
      | text       | pictureUrl             | http://images2.wikia.nocookie.net/__cb20080223195117/batman/images/4/4a/BatmanChristianBale.jpg        |
      | text       | email                  | batman@thebatcave.com                                                                                  |
      | text       | phone                  | 1234                                                                                                   |
      | text       | institution            | University of Auckland -- Faculty of Science -- Centre for eResearch                                   |
      | text       | startDate              | 2009-01-01                                                                                             |
      | text       | notes                  | I am the night                                                                                         |
    Then I press "Save"
    Then I should not see "already exists in the database"
    Then I wait until I see "Edit"
    
  @nojs @authenticated @adviser @delete
  Scenario: Try to delete an adviser as an adviser
    When I'm logged in as adviser
    When I go to "/viewadvisers"
    And I follow "`Batman"
    Then I follow "Delete"
    Then I wait until I see "Only an admin can perform this operation"
  
  @javascript @project @create
  Scenario: Create a project
    # Selenium 2 doesn't support Request Header modification :(
    When I go to "/viewprojects"
    And I follow "Create new project"
    Then I wait until I see "Create Project"
    And I fill in the following <formdetails>
      | field_type | form_id                     | value                                                |
      | text       | project.name                | `Save the world                                      |
      | text       | project.description         | Some men just want to watch the world burn           |
      | select     | project.hostInstitution     | University of Auckland                               |
      | select     | project.projectTypeId       | Proposal Development                                 |
      | text       | project.startDate           | 2009-01-01                                           |
      | text       | project.nextReviewDate      | 2010-01-01                                           |
      | text       | project.nextFollowUpDate    | 2010-01-01                                           |
      | text       | project.requirements        | 30GB RAM, 5TB Disk Space                             |
      | text       | project.notes               | It goes ding when there's stuff                      |
      | text       | project.todo                | Write a better test suite                            |
    Then I press "Add researcher to project"
    Then I wait until I see "`Chuck Norris"
    And I fill in the following <formdetails>
      | field_type | form_id                  | value                          |
      | select     | researcherId             | `Chuck Norris                  |
      | select     | researcherRoleId         | 1                              |
      | text       | notes                    | Watch for the roundhouse kick  |
    Then I press "Submit"
    #Then print last response
    Then I wait until I see "Delete researcher from project"
    Then I press "Add adviser to project"
    Then I wait until I see "`Batman" 
    And I fill in the following <formdetails>
      | field_type | form_id                  | value                          |
      | select     | adviserId                | `Batman                        |
      | select     | adviserRoleId            | 2                              |
      | text       | notes                    | AKA Bruce Wayne                |
    Then I press "Submit"
    Then I wait until I see "Delete adviser from project"
    Then I press "Add adviser to project"
    Then I wait until I see "Behat Adviser" 
    And I fill in the following <formdetails>
      | field_type | form_id                  | value                          |
      | select     | adviserId                | Behat Adviser                  |
      | select     | adviserRoleId            | 1                              |
    Then I press "Submit"
    Then I wait until I see "Delete adviser from project"
    Then I wait until I see "Add HPC facility"
    And I press "Add HPC facility"
    Then I wait until I see "Pan"
    Then I fill in the following <formdetails>
      | field_type | form_id                  | value                                                                               |
      | select     | facilityId               | Pan                                                                                 |
    Then I press "Submit"
    Then I wait until I see "Pan"
    Then I press "Save & Finish Editing"
    Then I wait until I see "Edit"
    
  @javascript @project @edit @kpi
  Scenario: Edit the project and add a KPI
    When I go to "/viewprojects"
    And I follow "`Save the world"
    Then I wait until I see "Edit"
    And I follow "Edit"
    Then I wait until I see "Add KPI"
    And I press "Add KPI"
    And I wait until I see "Notes"
    Then I fill in the following <formdetails>
      | field_type | form_id                  | value                                                                               |
      | select     | kpiId                    | NESI-8: Number of users with computations scaled up through parallelisation of code |
      | text       | value                    | 9001                                                                                |
      | text       | notes                    | Number of enemies taken down multiplied by 10 by enlisting the help of Robin        |
    Then I press "Submit"
    Then I wait until I see "NESI-8"
    Then I press "Save & Finish Editing"
    Then I wait until I see "Edit"
  
  @javascript @project @edit @ro
  Scenario: Edit the project and add a Research Output
    When I go to "/viewprojects"
    And I follow "`Save the world"
    Then I wait until I see "Edit"
    And I follow "Edit"
    Then I wait until I see "Add research output"
    And I press "Add research output"
    And I wait until I see "Thesis"
    Then I fill in the following <formdetails>
      | field_type | form_id                  | value                                                                               |
      | select     | typeId                   | Talk                                                                                |
      | text       | description              | (Norris, 2013) Defending Gotham: A how-to                                           |
    Then I press "Submit"
    Then I wait until I see "Gotham"
    Then I press "Save & Finish Editing"
    Then I wait until I see "Edit"
  
  @javascript @project @edit @review
  Scenario: Edit the project and add a Review
    When I go to "/viewprojects"
    And I follow "`Save the world"
    Then I wait until I see "Edit"
    And I follow "Edit"
    Then I wait until I see "Add review"
    And I press "Add review"
    Then I wait until I see "Notes"
    Then I fill in the following <formdetails>
      | field_type | form_id                  | value                                                                               |
      | text       | notes                    | Crime is down 10%                                                                   |
    Then I press "Submit"
    Then I wait until I see "Crime"
    Then I press "Save & Finish Editing"
    Then I wait until I see "Edit"
  
  @javascript @project @edit @fu
  Scenario: Edit the project and add a Follow-up
    When I go to "/viewprojects"
    And I follow "`Save the world"
    Then I wait until I see "Edit"
    And I follow "Edit"
    Then I wait until I see "Add follow-up"
    And I press "Add follow-up"
    Then I wait until I see "Notes"
    Then I fill in the following <formdetails>
      | field_type | form_id                  | value                                                                               |
      | text       | notes                    | Tore a hole in the spacetime continuum                                              |
    Then I press "Submit"
    Then I wait until I see "spacetime"
    Then I press "Save & Finish Editing"
    Then I wait until I see "Project Details"
    
  @javascript @project @edit @aa
  Scenario: Edit the project and add an Adviser Action
    When I go to "/viewprojects"
    And I follow "`Save the world"
    Then I wait until I see "Edit"
    And I follow "Edit"
    Then I wait until I see "Add adviser action"
    And I press "Add adviser action"
    Then I wait until I see "Action"
    Then I fill in the following <formdetails>
      | field_type | form_id                  | value                                                                               |
      | text       | action                   | Spoke to Alfred                                                                     |
    Then I press "Submit"
    Then I wait until I see "Alfred"
    Then I press "Save & Finish Editing"
    Then I wait until I see "Edit"
  
  @nojs @project @authz
  Scenario: Attempt to a edit a project that I don't own, when I'm not admin
    When I'm logged in as adviser2
    When I go to "/viewprojects"
    And I follow "`Save the world"
    Then I wait until I see "Edit"
    And I follow "Edit"
    Then I should see "Only an adviser of this project or an admin can perform this operation"
    
  @nojs @project @edit @collision
  Scenario: Open a session, cause an edit collision
    When I'm logged in as adviser
    When I go to "/viewprojects"
    And I follow "`Save the world"
    Then I wait until I see "Edit"
    And I follow "Edit"
    Then I'm logged in as admin
    When I go to "/viewprojects"
    And I follow "`Save the world"
    Then I wait until I see "Edit"
    And I follow "Edit"
    Then I should see "This project is currently being edited by Behat Adviser"
  
  @nojs @ro
  Scenario: View all research outputs
    When I'm logged in as authenticated
    When I go to "/viewresearchoutput"
    Then I wait until I see "Gotham"
  
  @nojs @cleanup @researcher
  Scenario: Delete the test researcher
    When I'm logged in as admin
    When I go to "/viewresearchers"
    And I follow "`Chuck Norris"
    Then I follow "Delete"
    Then I wait until I see "Create new researcher"
  
  @nojs @cleanup @adviser
  Scenario: Delete the test adviser
    When I'm logged in as admin
    When I go to "/viewadvisers"
    And I follow "`Batman"
    Then I follow "Delete"
    Then I wait until I see "Create new adviser"
  
  @nojs @cleanup @project
  Scenario: Delete the test project
    When I'm logged in as adviser
    When I go to "/viewprojects"
    And I follow "`Save the world"
    Then I follow "Delete"
    Then I wait until I see "Create new project"
    
  