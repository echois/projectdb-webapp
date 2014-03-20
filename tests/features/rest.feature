Feature: Perform REST actions and check that they respond as expected
  In order to create a meaningful project
  I need to link it to an Adviser and a Researcher etc
  
  @nojs @adviser @edit
  Scenario: Create an adviser
    When I "POST" "{'fullName':'!Batman', 'phone':1234, 'email':'bat@cave.com', 'pictureUrl':'http://static.comicvine.com/uploads/original/0/40/3207042-batman_arkham_origins-wide.jpg'}" to "advisers/"
    Then the response doesn't contain "already exists"
    Then I "GET" "" to "advisers/id"
    Then the response contains "bat@cave"
    #Then I print last response
    
  @nojs @adviser @edit
  Scenario: Edit the adviser, check it was changelogged
    When I load the "adviser" with name "!Batman"
    When I "GET" "" to "advisers/id"
    Then the response contains "bat@cave"
    Then I "POST" "!Superman" to "advisers/id/FullName/force/"
    Then I "GET" "" to "advisers/changes/id"
    Then I print last response
    Then the response contains "!Superman"
    And the response contains "!Batman"
    
  @nojs @adviser @rollback
  Scenario: Perform a rollback
    When I load the "adviser" with name "!Superman"
    Then I "GET" "" to "advisers/changes/id"
    # This loads the revision id
    Then I "GET" "" to "advisers/rollback/id"
    Then I print last response
    
  @nojs @adviser @delete
  Scenario: Delete the adviser
    When I load the "adviser" with name "!Batman"
    Then I "DELETE" "" to "advisers/id"
    Then I print last response
    