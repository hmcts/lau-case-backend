Feature: The application's health status

  Scenario: The backend is able to process caseSearch POST requests
    Given LAU backend application is healthy
    And Service authorisation provider is healthy
    When I request POST caseSearch endpoint using s2s
    Then  caseSearch response body is returned
