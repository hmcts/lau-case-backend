Feature: The application's GET accessRequest endpoint

  Background:
    Given LAU backend application is healthy
    When I POST multiple Access Request records to "/audit/accessRequest" endpoint once using data:
      |requestType|userId|caseRef         |reason|action        |requestEndTimestamp     |timestamp               |
      |SPECIFIC   |123   |1234567890123456|nosy  |CREATED       |                        |2024-02-25T22:20:05.023Z|
      |SPECIFIC   |123   |1234567890123456|nosy  |REJECTED      |                        |2024-02-25T22:20:05.023Z|
      |CHALLENGED |456   |1234567890123456|fun   |AUTO-APPROVED |2024-02-03T22:20:05.023Z|2024-03-20T22:20:05.023Z|
      |CHALLENGED |456   |6543210987654321|meh   |AUTO-APPROVED |2024-02-03T22:20:05.023Z|2024-04-15T22:20:05.023Z|
      |SPECIFIC   |789   |6543210987654321|meh   |CREATED       |                        |2024-05-10T22:20:05.023Z|
      |SPECIFIC   |789   |6543210987654321|      |APPROVED      |2024-02-03T22:20:05.023Z|2024-05-10T22:20:05.023Z|
      |CHALLENGED |789   |1122334455667788|nosy  |AUTO-APPROVED |2024-02-03T22:20:05.023Z|2024-06-05T22:20:05.023Z|
      |CHALLENGED |789   |1122334455667788|nosy  |AUTO-APPROVED |2024-02-03T22:20:05.023Z|2024-08-02T22:20:05.023Z|
      |CHALLENGED |789   |1122334455667788|nosy  |AUTO-APPROVED |2024-02-03T22:20:05.023Z|2024-08-05T22:20:05.023Z|
      |CHALLENGED |789   |1122334455667788|nosy  |AUTO-APPROVED |2024-02-03T22:20:05.023Z|2024-08-08T22:20:05.023Z|
    And I am logged in with the CFT-AUDIT-INVESTIGATOR role

  Scenario: The backend is able to process accessRequest GET requests by userId
    When I GET "/audit/accessRequest" using params:
      |userId        |123                |
      |startTimestamp|2024-02-20T22:00:00|
      |endTimestamp  |2024-03-02T22:01:00|
    Then the list of accessRequest records returned is (expected total 2):
      |requestType|userId|caseRef         |reason|action        |requestEndTimestamp     |timestamp               |
      |SPECIFIC   |123   |1234567890123456|nosy  |REJECTED      |                        |2024-02-25T22:20:05.023Z|
      |SPECIFIC   |123   |1234567890123456|nosy  |CREATED       |                        |2024-02-25T22:20:05.023Z|


  Scenario: The backend is able to process accessRequest GET requests by caseRef
    When I GET "/audit/accessRequest" using params:
      |caseRef       |6543210987654321   |
      |startTimestamp|2024-02-20T22:00:00|
      |endTimestamp  |2024-06-02T22:00:00|
    Then the list of accessRequest records returned is (expected total 3):
      |requestType|userId|caseRef         |reason|action       |requestEndTimestamp     |timestamp               |
      |SPECIFIC   |789   |6543210987654321|      |APPROVED     |2024-02-03T22:20:05.023Z|2024-05-10T22:20:05.023Z|
      |SPECIFIC   |789   |6543210987654321|meh   |CREATED      |                        |2024-05-10T22:20:05.023Z|
      |CHALLENGED |456   |6543210987654321|meh   |AUTO-APPROVED|2024-02-03T22:20:05.023Z|2024-04-15T22:20:05.023Z|

  Scenario: The backend is able to process accessRequest GET requests by requestType
    When I GET "/audit/accessRequest" using params:
      |requestType   |CHALLENGED         |
      |startTimestamp|2024-02-20T22:00:00|
      |endTimestamp  |2024-06-06T22:00:00|
    Then the list of accessRequest records returned is (expected total 3):
      |requestType|userId|caseRef         |reason|action       |requestEndTimestamp     |timestamp               |
      |CHALLENGED |789   |1122334455667788|nosy  |AUTO-APPROVED|2024-02-03T22:20:05.023Z|2024-06-05T22:20:05.023Z|
      |CHALLENGED |456   |6543210987654321|meh   |AUTO-APPROVED|2024-02-03T22:20:05.023Z|2024-04-15T22:20:05.023Z|
      |CHALLENGED |456   |1234567890123456|fun   |AUTO-APPROVED|2024-02-03T22:20:05.023Z|2024-03-20T22:20:05.023Z|

  Scenario: The backend is able to process accessRequest GET requests by requestType and userId
    When I GET "/audit/accessRequest" using params:
      |requestType   |CHALLENGED         |
      |userId        |456                |
      |startTimestamp|2024-02-20T22:00:00|
      |endTimestamp  |2024-06-06T22:00:00|
    Then the list of accessRequest records returned is (expected total 2):
      |requestType|userId|caseRef         |reason|action       |requestEndTimestamp     |timestamp               |
      |CHALLENGED |456   |6543210987654321|meh   |AUTO-APPROVED|2024-02-03T22:20:05.023Z|2024-04-15T22:20:05.023Z|
      |CHALLENGED |456   |1234567890123456|fun   |AUTO-APPROVED|2024-02-03T22:20:05.023Z|2024-03-20T22:20:05.023Z|

  Scenario: The backend is able to process accessRequest GET requests by caseRef and userId
    When I GET "/audit/accessRequest" using params:
      |caseRef       |1234567890123456   |
      |userId        |456                |
      |startTimestamp|2024-02-20T22:00:00|
      |endTimestamp  |2024-06-06T22:00:00|
    Then the list of accessRequest records returned is (expected total 1):
      |requestType|userId|caseRef         |reason|action       |requestEndTimestamp     |timestamp               |
      |CHALLENGED |456   |1234567890123456|fun   |AUTO-APPROVED|2024-02-03T22:20:05.023Z|2024-03-20T22:20:05.023Z|

  Scenario: The backend is able to return accessRequest GET requests paged
    When I GET "/audit/accessRequest" using params:
      |requestType   |CHALLENGED         |
      |startTimestamp|2024-02-20T22:00:00|
      |endTimestamp  |2024-10-06T22:00:00|
      |page          |2                  |
      |size          |2                  |
    Then the list of accessRequest records returned is (expected total 6):
      |requestType|userId|caseRef         |reason|action        |requestEndTimestamp     |timestamp               |
      |CHALLENGED |789   |1122334455667788|nosy  |AUTO-APPROVED |2024-02-03T22:20:05.023Z|2024-08-02T22:20:05.023Z|
      |CHALLENGED |789   |1122334455667788|nosy  |AUTO-APPROVED |2024-02-03T22:20:05.023Z|2024-06-05T22:20:05.023Z|


  Scenario: The backend is unable to process accessRequest GET requests due to missing s2s
    Given LAU backend application is healthy
    When And I GET "/audit/accessRequest" without service authorization header
    Then HTTP "403" Forbidden response is returned

  Scenario: The backend is unable to process accessRequest GET requests due to missing authorization
    Given LAU backend application is healthy
    When And I GET "/audit/accessRequest" without authorization header
    Then HTTP "401" Unauthorized response is returned

  Scenario: The backend is unable to process accessRequest GET requests due to missing search params
    Given LAU backend application is healthy
    When I request GET "/audit/accessRequest" endpoint without mandatory params
    Then HTTP "400" Bad Request response is returned
