Feature: Perform REST actions and check that they respond as expected
  In order to create a meaningful project
  I need to link it to an Adviser and a Researcher etc
  
  @nojs @adviser @edit
  Scenario: Create an adviser
    When I "POST" "{'fullName':'!Batman', 'phone':1234, 'email':'bat@cave.com', 'pictureUrl':'http://static.comicvine.com/uploads/original/0/40/3207042-batman_arkham_origins-wide.jpg'}" at "advisers/"
    Then the response doesn't contain "already exists"
    Then I "GET" "" at "advisers/id"
    Then the response contains "bat@cave"
    
  @nojs @adviser @edit
  Scenario: Edit the adviser, check it was changelogged
    When I load the "adviser" with name "!Batman"
    When I "GET" "" at "advisers/id"
    Then the response contains "bat@cave"
    Then I "POST" "!Superman" at "advisers/id/FullName/force/"
    Then I "GET" "" at "advisers/changes/id"
    Then the response contains "!Superman"
    And the response contains "!Batman"
    
  @nojs @adviser @rollback
  Scenario: Perform a rollback
    When I load the "adviser" with name "!Superman"
    When I load the "advisers/change" with name "!Superman"
    Then I "GET" "" at "advisers/rollback/id/rev"
    When I load the "adviser" with name "!Batman"
    When I "GET" "" at "advisers/id"
    Then the response contains "bat@cave"
    
    
    
  @nojs @researcher @edit
  Scenario: Create an researcher
    When I "POST" "{'fullName':'!Chuck Norris', 'preferredName': 'Chuck', 'phone':'1234', 'email':'chuck@space.com', 'startDate':'2020-02-20', 'endDate':'never', 'affiliation':'Milky Way -- Earth -- Antartica', 'institutionalRoleId': 1, 'notes':'Watch for the roundhouse kick', 'statusId':1, 'pictureUrl':'http://img2.wikia.nocookie.net/__cb20081118030612/tesfanon/images/5/5c/Chuck_Norris.jpg'}" at "researchers/"
    Then the response doesn't contain "already exists"
    Then I "GET" "" at "researchers/id"
    Then the response contains "chuck@space"
    
  @nojs @researcher @edit
  Scenario: Edit the researcher, check it was changelogged
    When I load the "researcher" with name "!Chuck Norris"
    When I "GET" "" at "researchers/id"
    Then the response contains "chuck@space"
    Then I "POST" "!Flash Gordon" at "researchers/id/FullName/force/"
    Then I "GET" "" at "researchers/changes/id"
    Then the response contains "!Chuck Norris"
    And the response contains "!Flash Gordon"
    
  @nojs @researcher @rollback
  Scenario: Perform a rollback
    When I load the "researcher" with name "!Flash Gordon"
    When I load the "researchers/change" with name "!Flash Gordon"
    Then I "GET" "" at "researchers/rollback/id/rev"
    When I load the "researcher" with name "!Chuck Norris"
    When I "GET" "" at "researchers/id"
    Then the response contains "chuck@space"
    
    
    
  @nojs @project @edit
  Scenario: Create an project
    When I "POST" "{'project':{'name':'!Save The World', 'description': 'Nothing to fear but fear itself', 'startDate':'', 'statusId':'1', 'projectTypeId':'1', 'projectCode':'test00001', 'hostInstitution':'Planet Earth', 'endDate': '', 'nextReviewDate':'', 'nextFollowUpDate':'', 'requirements':'A lot of firepower', 'notes':'~~~~~', 'todo':'First, do no harm'},'projectFacilities':[{'facilityId':1}], 'apLinks':[{'adviserId':1, 'adviserRoleId':1}], 'rpLinks':[{'researcherId':1, 'researcherRoleId':1}]}" at "projects/"
    Then I "GET" "" at "projects/id"
    Then the response contains "test00001"
    
  @nojs @project @edit
  Scenario: Edit the project, check it was changelogged
    When I load the "project" with name "!Save The World"
    When I "GET" "" at "projects/id"
    Then the response contains "test00001"
    Then I "POST" "!Save some of the world" at "projects/id/Project/Name/force/"
    Then I "GET" "" at "projects/changes/id"
    Then the response contains "!Save some of the world"
    And the response contains "!Save The World"
    
  @nojs @project @rollback
  Scenario: Perform a rollback
    When I load the "project" with name "!Save some of the world"
    When I load the "projects/change" with name "!Save some of the world"
    Then I "GET" "" at "projects/rollback/id/rev"
    When I load the "project" with name "!Save The World"
    When I "GET" "" at "projects/id"
    Then the response contains "test00001"
    
    
    
  @nojs @adviser @delete @cleanup
  Scenario: Delete the adviser
    When I load the "adviser" with name "!Batman"
    Then I "DELETE" "" at "advisers/id"
    
  @nojs @researcher @delete @cleanup
  Scenario: Delete the researcher
    When I load the "researcher" with name "!Chuck Norris"
    Then I "DELETE" "" at "researchers/id"
    
  @nojs @project @delete @cleanup
  Scenario: Delete the project
    When I load the "project" with name "!Save The World"
    Then I "DELETE" "" at "projects/id"
    