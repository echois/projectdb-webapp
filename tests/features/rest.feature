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
    When I "POST" "{'fullName':'!Chuck Norris', 'phone':1234, 'email':'chuck@space.com', 'startDate':'2020-02-20', 'endDate':'never', 'institution':'Milky Way', 'divison':'Earth', 'department':'Antartica', 'notes':'Watch for the roundhouse kick', 'statusId':1, 'pictureUrl':'http://www.oassf.com/en/media/images/Chuck-Norris-Card.jpg'}" at "researchers/"
    Then I print last response
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
    
  @nojs @adviser @delete
  Scenario: Delete the adviser
    When I load the "adviser" with name "!Batman"
    Then I "DELETE" "" at "advisers/id"
    
  @nojs @researcher @delete
  Scenario: Delete the researcher
    When I load the "researcher" with name "!Chuck Norris"
    Then I "DELETE" "" at "advisers/id"
    