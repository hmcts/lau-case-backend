Feature: The application's GET caseAction endpoint

  Scenario: The backend is able to process userId caseAction GET requests
    Given LAU backend application is healthy
    When I POST multiple records to "/audit/caseAction" endpoint using "1,2,3" userIds
    And I am logged in with the CFT-AUDIT-INVESTIGATOR role
    And I GET "/audit/caseAction" using userId "1" query param
    Then a single caseAction response body is returned for userId "1"

  Scenario: The backend is able to process caseTypeId caseAction GET requests
    Given LAU backend application is healthy
    When I POST multiple records to "/audit/caseAction" endpoint using "4,5,6" caseTypeId
    And I am logged in with the CFT-AUDIT-INVESTIGATOR role
    And And I GET "/audit/caseAction" using caseTypeId "6" query param
    Then a single caseAction response body is returned for caseTypeId "6"

  Scenario: The backend is able to process caseAction caseAction GET requests
    Given LAU backend application is healthy
    When I POST multiple records to "/audit/caseAction" endpoint using "VIEW,UPDATE" caseAction
    And I am logged in with the CFT-AUDIT-INVESTIGATOR role
    And And I GET "/audit/caseAction" using caseAction "UPDATE" query param
    Then a single caseAction response body is returned for caseAction "UPDATE"

  Scenario: The backend is able to process caseAction caseAction GET requests as a service user for deleted cases
    Given LAU backend application is healthy
    When I POST multiple records to "/audit/caseAction" endpoint using "DELETE,CREATE" caseAction
    And I am logged in with the CFT-SERVICE-LOGS role
    And And I GET "/audit/caseAction" using caseAction "DELETE" query param
    Then a single caseAction response body is returned for caseAction "DELETE"

  Scenario: The backend is able to process caseRef caseAction GET requests
    Given LAU backend application is healthy
    When I POST multiple records to "/audit/caseAction" endpoint using "1615817621019876,1615817621019877,1615817621019878" caseRefs
    And I am logged in with the CFT-AUDIT-INVESTIGATOR role
    And And I GET "/audit/caseAction" using caseRef "1615817621019876" query param
    Then a single caseAction response body is returned for caseRef "1615817621019876"

  Scenario: The backend is able to process caseJurisdictionId caseAction GET requests
    Given LAU backend application is healthy
    When I POST multiple records to "/audit/caseAction" endpoint using "10,11,12" caseJurisdictionId
    And I am logged in with the CFT-AUDIT-INVESTIGATOR role
    And And I GET "/audit/caseAction" using caseJurisdictionId "12" query param
    Then a single caseAction response body is returned for caseJurisdictionId "12"

  Scenario: The backend is able to process startTime caseAction GET requests
    Given LAU backend application is healthy
    When I POST multiple records to "/audit/caseAction" endpoint using "2021-08-23T22:20:05.023Z,2022-08-23T22:20:05.023Z,2023-08-23T22:20:05.023Z" timestamp
    And And I GET "/audit/caseAction" using startTimestamp "2022-08-24T22:20:05" query param
    Then a single caseAction response body is returned for startTimestamp "2023-08-23T22:20:05.023Z"

  Scenario: The backend is able to process endTime caseAction GET requests
    Given LAU backend application is healthy
    When I POST multiple records to "/audit/caseAction" endpoint using "2018-08-23T22:20:05.023Z,2019-08-23T22:20:05.023Z,2020-08-23T22:20:05.023Z" timestamp
    And I am logged in with the CFT-AUDIT-INVESTIGATOR role
    And And I GET "/audit/caseAction" using endTimestamp "2019-08-23T21:20:05" query param
    Then a single caseAction response body is returned for endTimestamp "2018-08-23T22:20:05.023Z"

  Scenario: The backend is able to process case insensitive caseTypeId caseAction GET requests
    Given LAU backend application is healthy
    When I POST multiple records to "/audit/caseAction" endpoint using "TEST1,TEST2,TEST3" caseTypeId
    And I am logged in with the CFT-AUDIT-INVESTIGATOR role
    And And I GET "/audit/caseAction" using caseTypeId "test1" query param
    Then a single caseAction response body is returned for caseTypeId "TEST1"

  Scenario: The backend is able to process case insensitive caseJurisdictionId caseAction GET requests
    Given LAU backend application is healthy
    When I POST multiple records to "/audit/caseAction" endpoint using "TEST1,TEST2,TEST3" caseJurisdictionId
    And I am logged in with the CFT-AUDIT-INVESTIGATOR role
    And And I GET "/audit/caseAction" using caseJurisdictionId "test1" query param
    Then a single caseAction response body is returned for caseJurisdictionId "TEST1"

  Scenario: The backend is unable to process caseAction GET requests due to missing s2s
    Given LAU backend application is healthy
    When And I GET "/audit/caseAction" without service authorization header
    Then HTTP "403" Forbidden response is returned

  Scenario: The backend is unable to process caseAction GET requests due to missing authorization
    Given LAU backend application is healthy
    When And I GET "/audit/caseAction" without authorization header
    Then HTTP "401" Unauthorized response is returned

  Scenario: The backend is unable to process caseAction GET requests due to missing search params
    Given LAU backend application is healthy
    When I request GET "/audit/caseAction" endpoint without mandatory params
    Then HTTP "400" Bad Request response is returned

  Scenario: The backend is unable to process caseAction GET requests as a service user for non deleted cases
    Given LAU backend application is healthy
    When I POST multiple records to "/audit/caseAction" endpoint using "VIEW,CREATE" caseAction
    And I am logged in with the CFT-SERVICE-LOGS role
    And And I GET "/audit/caseAction" using caseAction "CREATE" query param
    Then HTTP "401" Unauthorized response is returned for get request
