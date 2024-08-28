Feature: The application's POST /audit/accessRequest endpoint

  Scenario: The backend is able to process accessRequest POST requests
    Given LAU backend application is healthy
    When I POST access request using "/audit/accessRequest" endpoint using s2s
    Then accessRequest response body is returned

  Scenario: The backend is unable to process accessRequest POST requests due to missing mandatory parameter
    Given LAU backend application is healthy
    Then POST to "/audit/accessRequest" endpoint with bad request body using s2s returns Bad Request
      | requestType    | user-id | caseRef          | reason | action   | requestStartTimestamp    | requestEndTimestamp      | timestamp                |
      | challenged     | user-id | bad-case-ref     | reason | approved | 2024-08-15T12:23:43.123Z | 2024-08-16T12:23:43.123Z | 2024-08-15T12:23:43.123Z |
      | invalidReqType | user-id | 1234567890123456 | reason | approved | 2024-08-15T12:23:43.123Z | 2024-08-16T12:23:43.123Z | 2024-08-15T12:23:43.123Z |
      | challenged     |         | 1234567890123456 | reason | approved | 2024-08-15T12:23:43.123Z | 2024-08-16T12:23:43.123Z | 2024-08-15T12:23:43.123Z |
      | challenged     | user-id | 1234567890123456 |        | approved | 2024-08-15T12:23:43.123Z | 2024-08-16T12:23:43.123Z | 2024-08-15T12:23:43.123Z |
      | challenged     | user-id | 1234567890123456 | reason | invalid  | 2024-08-15T12:23:43.123Z | 2024-08-16T12:23:43.123Z | 2024-08-15T12:23:43.123Z |
      | challenged     | user-id | 1234567890123456 | reason | approved | 2024-08-15               | 2024-08-16T12:23:43.123Z | 2024-08-15T12:23:43.123Z |
      | challenged     | user-id | 1234567890123456 | reason | approved | 2024-08-15T12:23:43.123Z | 2024-08-16T12:23:43.123Z |                          |
      | challenged     | user-id | 1234567890123456 | reason | approved | 2024-08-15T12:23:43.123Z | 2024-08-16T12:23:43.123Z | 2024-08-15               |
      | challenged     | user-id | 1234567890123456 | reason | approved | 2024-08-15T12:23:43.123Z | 2024-08-16               | 2024-08-15T12:23:43.123Z |
  Scenario: The backend is unable to process accessRequest POST requests due to missing s2s authorisation header
    Given LAU backend application is healthy
    When I POST accessRequest "/audit/accessRequest" endpoint with missing s2s header
    Then http forbidden response is returned for POST accessRequest
