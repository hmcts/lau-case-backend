Feature: The application's DELETE caseSearch endpoint

  Scenario: The backend is able to process caseSearch DELETE requests
    Given LAU backend application is healthy
    When I request POST "/audit/caseSearch" endpoint using userId "77777"
    And I request DELETE "/audit/caseSearch/deleteCaseSearchRecord" endpoint
    And I GET "/audit/caseSearch" using userId "77777"
    Then An empty result is returned

  Scenario: The backend is unable to process caseSearch DELETE requests due to missing s2s header
    Given LAU backend application is healthy
    And I request DELETE "/audit/caseSearch/deleteCaseSearchRecord" endpoint with missing s2s header
    Then http "403" response is returned for DELETE caseSearch

  Scenario: The backend is unable to process caseSearch DELETE requests due to invalid authorization header
    Given LAU backend application is healthy
    And I request DELETE "/audit/caseSearch/deleteCaseSearchRecord" endpoint with missing authorization header
    Then http "401" response is returned for DELETE caseSearch

  Scenario: The backend is unable to process caseSearch DELETE requests due invalid case search id
    Given LAU backend application is healthy
    And I request DELETE "/audit/caseSearch/deleteCaseSearchRecord" endpoint with invalid caseSearchId "000000"
    Then http "404" response is returned for DELETE caseSearch

  Scenario: The backend is unable to process caseSearch DELETE requests due invalid case search id
    Given LAU backend application is healthy
    And I request DELETE "/audit/caseSearch/deleteCaseSearchRecord" endpoint with missing caseSearchId
    Then http "400" response is returned for DELETE caseSearch



