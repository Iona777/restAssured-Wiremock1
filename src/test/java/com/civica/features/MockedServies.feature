Feature: Mocked Services


  Scenario: Verify hello world mocked service
    Given I start a new service "HelloWorld"
    Given I set resource url to "/hello"
    And I create a mocked GET request
    When user calls given URL with "GET" http request
    Then the API call is successful with a status code of 200
    And I stop the mocked service
  #- Stopping the service in @After does not seem to work

  Scenario: Verify baeldung mocked service
    Given I start a new service "Baeldung"
    Given I set resource url to "/baeldung/wiremock"
    And I create a mocked GET request
    When user calls given URL with "GET" http request
    Then the API call is successful with a status code of 200
    And I stop the mocked service

  Scenario: Verify unauthorised mocked service
    Given I start a new service "unauthorised"
    Given I set resource url to "/sorry-no"
    And I create a mocked GET request
    When user calls given URL with "GET" http request
    Then the API call is successful with a status code of 407
    And I stop the mocked service


   #Does not work with header = Content-Type. Seems it needs to be ContentType
   #Which contradicts docs and is not normal name for the header
  Scenario Outline: Verify headers mocked service
    Given I start a new service "Headers"
    Given I set resource url to "/with/headers"
    And I set payload using file "<payloadFile>"
    And I create a mocked POST request
    When user calls given URL with "POST" http request
    Then the API call is successful with a status code of 200
    And I stop the mocked service
    Examples:
      | payloadFile                                                   |
      | src/test/java/com/civica/resources/payloadFiles/addPlace.json |

