package com.civica.stepDefinitions;

import com.civica.pojos.AddPlace;
import com.civica.resources.TestDataBuild;
import com.civica.resources.Utilities;
import com.civica.resources.mockedServices.mockedServices1;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.stream.JsonReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class addPlaceStepDefs extends Utilities
//Since it extends Utilities, do not need to create a Utilites class to access its methods
{

    //Define these variables at class level so that they are available to all methods
    Map<String, String> parameters;
    String resourceUrl;

    RequestSpecification request;
    ResponseSpecification thenReqSpec; //May not need this
    Response latestResponse;
    AddPlace addPlaceObject; //May need a different version that does not have a setter?
    TestDataBuild data = new TestDataBuild();

    //Need to create class instance to access methods
    mockedServices1 mocks = new mockedServices1();

    public static String place_id;
    //Using static means the variable will not be recreated between tests, so it will now be shared between
    //test cases in the same run (Similar to test context?)

    //DB Variables
    Connection MySQLConnection;
    ResultSet dbResults;


    @Given("I set proxy")
    public  void i_set_proxy()
    {
        RestAssured.proxy("webproxy.civica.com",8080);
        RestAssured.useRelaxedHTTPSValidation();

    }

    @Given("I set parameters")
    public void i_set_parameters(Map<String,String> params)
    {
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>.
        parameters = params;
    }

    @Given("I set resource url to {string}")
    public void i_set_resource_url_to(String url)
    {
        System.out.println("in I set resource url");
        resourceUrl = url;
        System.out.println("resourceUrl is "+ resourceUrl);
    }
    @Given("I set payload using file {string}")
    public void i_set_payload_using_file(String inputFileName) throws IOException
    {
        System.out.println("in I set payload");

        //Will take the given file and turn the contents into an addPlaceObject based on
        //AddPlace class (i.e. this class is a model of how the data should look)
        JsonReader reader = new JsonReader(new FileReader(inputFileName));
        addPlaceObject = new Gson().fromJson(reader, AddPlace.class);
    }

    @And("I create a GET request")
    public void i_create_a_GET_request() throws IOException
    {
        //Get baseRequestSpecificationForGiven() from Utilities
        request = given().spec(baseRequestSpecificationForGiven(parameters));
    }

    @And("I create a POST request")
    public void i_create_a_POST_request() throws IOException
    {
        System.out.println("in I create POST request");
        //Get baseRequestSpecificationForGiven() from Utilities
        request = given().spec(baseRequestSpecificationForGiven(parameters)).body(addPlaceObject);
    }

    @And("I create a mocked POST request")
    public void i_create_a_mocked_POST_request() throws IOException
    {
        System.out.println("in I create mocked POST request");
        //Get baseRequestSpecificationForGivenMocks() from Utilities
        request = given().spec(baseRequestSpecificationForGivenMocks()).body(addPlaceObject);


    }

    @And("I create a mocked GET request")
    public void i_create_a_mocked_GET_request() throws IOException
    {
        //Get baseRequestSpecificationForGivenMocks() from Utilities
        request = given().spec(baseRequestSpecificationForGivenMocks());
    }


    @When("user calls given URL with {string} http request")
    public void user_calls_given_url_with_http_request(String httpMethod)
    {
        System.out.println("in user calls url:" + resourceUrl);

        if (httpMethod.equalsIgnoreCase("POST"))
            latestResponse = request.when().post(resourceUrl).then().extract().response();
        else if (httpMethod.equalsIgnoreCase("GET"))
            latestResponse = request.when().get(resourceUrl).then().extract().response();


    }

    @When("user calls given mocked URL with {string} http request")
    public void user_calls_given_mocked_url_with_http_request(String httpMethod)
    {
        System.out.println("in user calls mocked url:" + resourceUrl);

        if (httpMethod.equalsIgnoreCase("POST"))
            latestResponse = request.when().post(resourceUrl).then().extract().response();
        else if (httpMethod.equalsIgnoreCase("GET"))
            latestResponse = request.when().get(resourceUrl).then().extract().response();


    }

    @Then("the API call is successful with a status code of {int}")
    public void the_api_call_is_successful_with_a_status_code_of(int expectedStatusCode)
    {
        System.out.println("in check status code");
        Assert.assertEquals(expectedStatusCode, latestResponse.getStatusCode());

    }
    @Then("{string} in the response body is {string}")
    public void in_the_response_body_is(String node, String expectedValue)
    {
        System.out.println("in check value in response");

        Assert.assertEquals(expectedValue, getNodeValueFromResponse(latestResponse, node));
    }

    @Then("place_Id created maps correctly to {string} using {string}")
    public void place_id_created_maps_correctly_to_using(String expectedName, String resource) throws IOException
    {
        System.out.println("in check place id maps correctly");

        //Prepare request spec
        place_id = getNodeValueFromResponse(latestResponse, "place_id");
        request =
                given().spec(baseRequestSpecificationForGivenDefaultParams() ) //get this method from Utilities
                .queryParam("place_id", place_id);

        //Can reuse a method within the StepDefs calls in other methods, just as you would in any other class
        user_calls_given_url_with_http_request("GET");

        String actualName = getNodeValueFromResponse(latestResponse, "name");
        Assert.assertEquals(expectedName, actualName);

    }

    @And("the following values are checked using hashmaps")
    public void theFollowingValuesAreCheckedUsingHashmaps(List<Map<String, String>> validationItems)
    {
        for (Map<String, String> item : validationItems)
            //Takes a list of hashmaps as input parameter
            //This represents a table. The map represents a series of header value pairs, which are the columns of
            //the table. Each line in the table is represented by a new item in the list.

            //I think this loop is for each set of header value pairs in a row, and the loop above is going
            // round each row in the table
            for (Map <String, String> validationRow : validationItems)
            {
                //This method does the actual validation of the row and calls another method to verify each individual
                //sub node
                verifyJsonPathSubItems(validationRow, latestResponse);
            }
    }

    //###### Database Steps #####

    @Given("I connect to the DB with {string}, {string} and {string}")
    public void i_connect_to_the_db_with_and(String URL, String username, String password) throws SQLException
    {
        MySQLConnection = getDBConnection(URL,username, password);
    }

    @When("I execute the SQL query {string}")
    public void i_execute_the_sql_query(String queryString) throws SQLException
    {
        PreparedStatement selectStatement = MySQLConnection.prepareStatement(queryString);
        dbResults = selectStatement.executeQuery();
    }

    @When("I execute the SQL query in file {string}")
    public void i_execute_the_sql_query_in_file(String fileName) throws SQLException, FileNotFoundException
    {
        String queryString = readSQLFile(fileName);

        PreparedStatement selectStatement = MySQLConnection.prepareStatement(queryString);
        dbResults = selectStatement.executeQuery();
    }

    @Then("I print out the names")
    public void i_print_out_the_names() throws SQLException
    {
        // will traverse through all found rows
        while (dbResults.next())
        {
            String firstName = dbResults.getString("first_name");
            String lastName = dbResults.getString("last_name");
            System.out.println("firstName = " + firstName + "," + "lastName= " + lastName);
        }
    }


    //  ## Wire Mock Steps ##
    @Given("I start a new service {string}")
    public void i_start_a_new_service(String serviceName)
    {
        switch (serviceName.toLowerCase())
        {
            case "helloworld":
                mocks.startHelloWorldService();
                break;
            case "baeldung":
                mocks.startBaeldungService();
                break;
            case "headers":
                mocks.startHeadersService();
                break;
            case "unauthorised":
                    mocks.startUnauthorisedService();
                break;
            default:
                System.out.println("Invalid service selected");


        }
    }

    @And("I stop the mocked service")
    public void i_stop_mocked_service()
    {
        mocks.stopMockedService();
    }
}
