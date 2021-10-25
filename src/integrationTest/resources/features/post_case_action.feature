Feature: The application's POST caseAction endpoint

  Scenario: The backend is able to process caseSearch POST requests
    Given LAU backend application is healthy
    When I POST case action using "/audit/caseAction" endpoint using s2s
    Then  caseAction response body is returned

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

