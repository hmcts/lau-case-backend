Feature: The application's DELETE caseAction endpoint

  Scenario: The backend is able to process caseAction DELETE requests
    Given LAU backend application is healthy
    When I request POST "/audit/caseAction" caseAction endpoint using userId "77777"
    And I request DELETE "/audit/caseAction/deleteCaseActionRecord" caseAction endpoint
    And I GET "/audit/caseAction" caseAction using userId "77777"
    Then An empty result for DELETE caseAction is returned

  Scenario: The backend is unable to process caseAction DELETE requests due to missing s2s header
    Given LAU backend application is healthy
    And I request DELETE "/audit/caseAction/deleteCaseActionRecord" caseAction endpoint with missing s2s header
    Then http "403" response is returned for DELETE caseAction

  Scenario: The backend is unable to process caseAction DELETE requests due to invalid authorization header
    Given LAU backend application is healthy
    And I request DELETE "/audit/caseAction/deleteCaseActionRecord" caseAction endpoint with missing authorization header
    Then http "401" response is returned for DELETE caseAction

  Scenario: The backend is unable to process caseAction DELETE requests due invalid case search id
    Given LAU backend application is healthy
    And I request DELETE "/audit/caseAction/deleteCaseActionRecord" caseAction endpoint with invalid caseActionId "000000"
    Then http "404" response is returned for DELETE caseAction

  Scenario: The backend is unable to process caseAction DELETE requests due invalid case search id
    Given LAU backend application is healthy
    And I request DELETE "/audit/caseAction/deleteCaseActionRecord" caseAction endpoint with missing caseActionId
    Then http "400" response is returned for DELETE caseAction



