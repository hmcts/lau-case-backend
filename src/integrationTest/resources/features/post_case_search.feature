Feature: The application's POST caseSearch endpoint

  Scenario: The backend is able to process caseSearch POST requests
    Given LAU backend application is healthy
    When I request POST "/audit/caseSearch" endpoint using s2s
    Then  caseSearch response body is returned

  Scenario: The backend is able to process caseSearch POST requests with invalid caseRefs
    Given LAU backend application is healthy
    When I request POST "/audit/caseSearch" endpoint using invalid caseRefs using s2s
    Then  caseSearch response body is returned with valid caseRefs response

  Scenario: The backend is able to process caseSearch POST requests without caseRefs
    Given LAU backend application is healthy
    When I request POST "/audit/caseSearch" endpoint using s2s and missing caseRefs
    Then  caseSearch response body with missing caseRefs is returned

  Scenario: The backend is unable to process caseSearch POST requests due to missing mandatory parameter
    Given LAU backend application is healthy
    When I request POST "/audit/caseSearch" endpoint with missing request body parameter using s2s
    Then http bad request response is returned

  Scenario: The backend is unable to process caseSearch POST requests due to invalid parameter
    Given LAU backend application is healthy
    When I request POST "/audit/caseSearch" endpoint with invalid body parameter using s2s
    Then http bad request response is returned

  Scenario: The backend is unable to process caseSearch POST requests due to missing s2s authorisation header
    Given LAU backend application is healthy
    When I request POST "/audit/caseSearch" endpoint with missing s2s header
    Then http forbidden response is returned for POST caseSearch

  Scenario: The backend is able to process caseSearch POST requests with 503 failure
    Given LAU backend application is healthy
    When I POST a request to "/audit/caseSearch" endpoint with s2s 503 failure
    Then   it should try making retry call for authorisation details

  Scenario: The backend is able to process caseSearch POST requests with failure but without retry
    Given LAU backend application is healthy
    When I POST a request to "/audit/caseSearch" endpoint with s2s 500 failure
    Then   it should try making retry call for authorisation details
