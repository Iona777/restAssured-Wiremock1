package com.civica.resources;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

import io.restassured.response.Response;
import org.junit.Assert;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

public class Utilities extends DatabaseUtilities
{
    //If you don't make this variable static then it will be set to null in subsequent runs
    public static RequestSpecification baseGivenReqSpec;

    public static Map<String, String> environmentVariables = new HashMap<>();


    //Try to replicate setting and getting environment variables like in Postman.
    //If this does not work, them just have class variables with getters and setters
    //If I made them properies rather than variables, would that give them build in getters/setters?
    public void setEnvironmentVariable(String variable, String value)
    {
        environmentVariables.put(variable, value);
    }

    public String getEnvironmentVariable(String variable)
    {
        return environmentVariables.get(variable);
    }

    //NOTE:
    //In order to use POJO classed for deserialization of a response (i.e. response-> POJO object), see
    //Section 11, lessons 59-63, especially 63 of RestAssured course.
    //This will cover creating POJOs for the response and getting the response back as PJO object instance.
    //Then you can ue the getters of the class for interrogating the response.
    //It is less generic than the methods used here as you would need a POJO for reach endpoint response,
    //but once set up it may have advantages.

    /**
     * Sets the baseUri, query parameters and content type for request specification to be used by given()
     * @param params the list of query parameters
     * @return RequestSpecification type
     * @throws IOException throws this sort of exception
     */
    public RequestSpecification baseRequestSpecificationForGiven(Map<String, String> params) throws IOException
    {
        if (baseGivenReqSpec ==null)
        {
            //This PrintStream object is used later for the logging filters
            PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));

            baseGivenReqSpec = new RequestSpecBuilder()
                    .setBaseUri(getGlobalPropsValue("baseUrl")) //This value is held in global.properties file
                    .addQueryParams(params)
                    .addFilter(RequestLoggingFilter.logRequestTo(log))
                    .addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .setContentType(ContentType.JSON)
                    .build();

            return baseGivenReqSpec;
        }
        //Return existing baseGivenReqSpec if there is one
        return baseGivenReqSpec;
    }

    /**
     * Takes no parameters but uses default values.  Note use of addQueryParam, not addQueryParams(), plural
     * Sets the baseUri, query parameters and content type for request specification to be used by given()
     * @return RequestSpecification type
     * @throws IOException throws this sort of exception
     */
    public RequestSpecification baseRequestSpecificationForGivenDefaultParams() throws IOException
    {
        if (baseGivenReqSpec ==null)
        {
            //This PrintStream object is used later for the logging filters
            PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));

            baseGivenReqSpec = new RequestSpecBuilder()
                    .setBaseUri(getGlobalPropsValue("baseUrl")) //This value is held in global.properties file
                    .addQueryParam("key","qaclikc123")
                    .addFilter(RequestLoggingFilter.logRequestTo(log))
                    .addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .setContentType(ContentType.JSON)
                    .build();

            return baseGivenReqSpec;
        }
        //Return existing baseGivenReqSpec if there is one
        return baseGivenReqSpec;
    }


    public RequestSpecification baseRequestSpecificationForGivenMocks() throws IOException
    {
        if (baseGivenReqSpec ==null)
        {
            //This PrintStream object is used later for the logging filters
            PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));

            baseGivenReqSpec = new RequestSpecBuilder()
                    .setBaseUri(getGlobalPropsValue("baseUrlMock")) //This value is held in global.properties file
                    .addFilter(RequestLoggingFilter.logRequestTo(log))
                    .addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .addHeader("etag","2134")
                    .addHeader("ContentType","application/json")
                    .setContentType(ContentType.JSON)
                    .build();


            return baseGivenReqSpec;
        }
        //Return existing baseGivenReqSpec if there is one
        return baseGivenReqSpec;
    }


    /**
     * This will get the value for the given key parameters from the global.properties file
     * @param key the key for which you want to retrieve the value
     * @return the property value as a String
     * @throws IOException if it cannot find the file
     */
    public String getGlobalPropsValue(String key) throws IOException
    {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/java/com/civica/resources/global.properties");
        prop.load(fis);
        return prop.getProperty(key);
    }

    /**
     * This will get the value of the given node from the given response, if the response is already a string
     * @param response the response to get value from
     * @param node the node location you want value of
     * @return the node value as a String
     */
    public String getNodeValueFromResponse(@NotNull String response, String node)
    {
        JsonPath js = new JsonPath(response);
        return js.get(node).toString();
    }

    /**
     * * This will get the value of the given node from the given response, if the response is NOT already a string
     * @param response the response to get value from
     * @param node the node location you want value of
     * @return the node value as a String
     */
    public String getNodeValueFromResponse(@NotNull Response response, String node)
    {
        String resp = response.asString();
        JsonPath js = new JsonPath(resp);
        return js.get(node).toString();
    }

    /**
     * Get the size of the given node. Note: this only works on arrays
     * @param response the response to get value from, as a Response type
     * @param node the node location you want size of
     * @return the size of the array as int
     */
    public int getNodeArraySize(@NotNull Response response, String node)
    {
        String resp = response.asString();
        JsonPath js = new JsonPath(resp);

        String jsonPath = node + ".size()";
        int count = js.getInt(jsonPath);

        return  count;
    }

    /*
    ### Verification Methods Using Hashmaps   ####
     */


    /**
     * Receives a HashMap in the form of <"node" node name, "value" value of node> and a
     * response to search for this node and value pair.
     * For this to work, the column in the examples table needs to be called "node"
     * @param validationItem the node and value ot search for
     * @param response the response in which ot search
     */
    public void verifyAsubNode(Map<String, String>validationItem, Response response)
    {
        //The value of the "node" column. This will be a json path, e.g.  header.invoice
        String path = validationItem.get("node");

        //Gets the expected value of the node at location path
        String expectedItemValue = validationItem.get("value");

        //Gets actual value from the response
        String actualItemValue = getNodeValueFromResponse(response, path);

        Assert.assertEquals("Expected value of node does not match actual", expectedItemValue, actualItemValue);
    }

    /**
     * Searches for and validates json sub items
     * @param validationRow a whole row with headers containing sub items to be validatged
     * @param response he response in which ot search
     */
    public void verifyJsonPathSubItems(Map<String, String> validationRow, Response response)
    {
        /*
        validationRow is a whole row with headers, e.g.
        |node                   |value      |status |message|
        |header.invoinceNumber  |1928571    |VALID  |null   |

        keySet is the set of keys, i.e. the header row, e.g.
        |mode                   |value    |status   |message|
         */

        //This will create a new HashMap containing the path to each sub node in turn and its expected value
        //So, in above example we would have
        //header.invoiceNumber.value , 1928572
        //header.invoiceNumber.status, VALID
        //header.invoiceNumber.message, null
        //Each key, value pair will be passed in turn the the verifyAsubNode() method for checking expected value
        //against actual value in the response for each pair.

        Map<String, String> validationSet = new HashMap<>();
        Set<String> keySet = validationRow.keySet();

        //We loop foreach key in keySet , so item value, status, message (and ony others that are present)
        //If the key is "node" then we jump over it using the first if()

        //Get path to the sub node
        for (String key: keySet)
            {
                if (!keySet.equals("node"))
                {
                    //Checks that the contents of the "node" key is not null (header.invoiceNumber in this case)
                    if (validationRow.get("node") !=null)
                        //Then it will put the string "node" and the contents of the "node" key plus "." plus the
                        //current key from keySet into the first map entry of the new validationSet map. This is
                        //then a path to te sub node
                        //e.g. node.header.invoiceNumber.message
                        validationSet.put("node", validationRow.get("node") + "." + key);
                    else
                        fail("'node' column not found in table");
                }

                //Get expected value for the sub node

                //In order to get the expected value for the sub node, it then puts the contents of the current key
                //(e.g. message) for validationRow and puts it ito validationMapValue String
                String validationMapValue = validationRow.get(key);

                //Then it puts the string "value" and the contents of validationMapValue into the second map entry
                //of the new validationSet map
                //e.g. for the message key it would be
                //value, null
                validationSet.put("value",validationMapValue);

                //It then passes this validationSet map to the verifyJsonPathSubItems() method so it can verify
                //(in this case) that in the response the header.invoiceNumber.value node contains 1928572.
                //It then repeats this for each key in keySet. So, the next one would add an entry in
                //validationSet map of:
                //item, header.invoiceNumber.status
                //value, VALID

                //This method would be called again in each pass of the loop in the calling method
                //so that the process is repeated for each line in the table

                verifyAsubNode(validationSet, response);

            }
    }



}
