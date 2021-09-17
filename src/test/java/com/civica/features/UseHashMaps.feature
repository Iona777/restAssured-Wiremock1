Feature: Using HashMaps for Validating Place APIs

  Background:
    Given I set proxy

  Scenario: Verify if Place is being successfully added using addPlaceAPI
    Given I set parameters
      |key|qaclick123|
    And I set resource url to "/maps/api/place/add/json"
    And I set payload using file "<payloadFile>"
    And I create a POST request
    When user calls given URL with "POST" http request
    Then the API call is successful with a status code of 200

    Scenario: Get the location of added place
      #Will need to find correct value for place_id from running previous scenario and put in here
      Given I set parameters
        | key      | qaclick123                       |
        | place_id | 15590dc255ef204ea7e6a54b512d42fb |
      And I set resource url to "/maps/api/place/add/json"
      And I create a GET request
      When user calls given URL with "GET" http request
      Then the API call is successful with a status code of 200
      And "accuracy" in the response body is "50"
      And the following values are checked using hashmaps
        | node     | latitude   | longitude |
        | location | -38.383494 | 33.427362 |
