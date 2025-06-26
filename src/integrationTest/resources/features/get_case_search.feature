Feature: The application's GET caseSearch endpoint

  Scenario: The backend is able to process caseSearch GET requests using userId query
    Given LAU backend application is healthy
    When I POST multiple caseSearch records to "/audit/caseSearch" endpoint using userIds "1,2,3"
    And I GET "/audit/caseSearch" using userId "2" query parameter
    Then a single caseSearch response body is returned for userId "2"

  Scenario: The backend is able to process caseSearch GET requests using caseRef query
    Given LAU backend application is healthy
    When I POST multiple caseSearch records to "/audit/caseSearch" endpoint using caseRefs "1615817621019876,1615817621019877,1615817621019878"
    And I GET "/audit/caseSearch" using caseRef "1615817621019877" query parameter
    Then a single caseSearch response body is returned for caseRef "1615817621019877"

  Scenario: The backend is able to process caseSearch GET requests using startTimestamp endTimestamp query
    Given LAU backend application is healthy
    When I POST multiple caseSearch records to "/audit/caseSearch" endpoint endpoint using "2021-08-23T22:20:05.023Z,2022-08-23T22:20:05.023Z,2023-08-23T22:20:05.023Z" timestamp
    And And I GET "/audit/caseSearch" using startTimestamp "2022-08-22T22:20:05" endTimestamp "2022-08-25T22:20:05" caseRef "3769509556751473" query parameter
    Then a single caseSearch response body is returned for startTimestamp "2022-08-23T22:20:05.023Z"


  Scenario: The backend is unable to process caseSearch GET requests due to missing s2s
    Given LAU backend application is healthy
    When And I GET "/audit/caseSearch" without service authorization header
    Then HTTP "403" Forbidden response is returned

  Scenario: The backend is unable to process caseAction GET requests due to missing authorization
    Given LAU backend application is healthy
    When And I GET "/audit/caseSearch" without authorization header
    Then HTTP "401" Unauthorized response is returned
    And authorization End Point is called only once

  Scenario: The backend is unable to process caseSearch GET requests due to missing search params
    Given LAU backend application is healthy
    When I request GET "/audit/caseSearch" endpoint without mandatory params
    Then HTTP "400" Bad Request response is returned
    And authorization End Point is called only once




