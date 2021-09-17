Feature: Validating Place API
  Background:
    Given I set proxy
    Given I set parameters
    |key|qaclick123|

    Scenario Outline: Verify if Place is being successfully added using addPlaceAPI
      Given I set resource url to "/maps/api/place/add/json"
      And I set payload using file "<payloadFile>"
      And I create a POST request
      When user calls given URL with "POST" http request
      Then the API call is successful with a status code of 200
      And "status" in the response body is "OK"
      And "scope" in the response body is "APP"
      And place_Id created maps correctly to "<name>" using "getplaceAPI"
      Examples:
        | name            | payloadFile                                                   |
        | Frontline house | src/test/java/com/civica/resources/payloadFiles/addPlace.json |