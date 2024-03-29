Feature: The application's POST caseAction endpoint

  Scenario: The backend is able to process caseAction POST requests
    Given LAU backend application is healthy
    When I POST case action using "/audit/caseAction" endpoint using s2s
    Then  caseAction response body is returned

  Scenario: The backend is able to process caseAction POST requests with invalid caseRef
    Given LAU backend application is healthy
    When I POST case action using "/audit/caseAction" endpoint using invalid caseRef using s2s
    Then  caseAction response body is returned with valid caseRef response

  Scenario: The backend is able to process caseAction POST requests with DELETE case action
    Given LAU backend application is healthy
    When I POST case action using "/audit/caseAction" endpoint using case action delete
    Then  caseAction response body is returned as a delete action

  Scenario: The backend is able to process caseAction POST requests without case jurisdiction
    Given LAU backend application is healthy
    When I POST case action using "/audit/caseAction" endpoint without case jurisdiction
    Then caseAction response body is returned with missing jurisdiction

  Scenario: The backend is unable to process caseAction POST requests due to missing mandatory parameter
    Given LAU backend application is healthy
    When I POST "/audit/caseAction" endpoint with missing request body parameter using s2s
    Then http bad request response is returned for POST caseAction

  Scenario: The backend is unable to process caseAction POST requests due to invalid parameter
    Given LAU backend application is healthy
    When I POST "/audit/caseAction" endpoint with missing invalid body parameter using s2s
    Then http bad request response is returned for POST caseAction

  Scenario: The backend is unable to process caseAction POST requests due to missing s2s authorisation header
    Given LAU backend application is healthy
    When I POST "/audit/caseAction" endpoint with missing s2s header
    Then http forbidden response is returned for POST caseAction

