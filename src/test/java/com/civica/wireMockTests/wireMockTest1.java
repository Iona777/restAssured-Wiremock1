package com.civica.wireMockTests;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import com.github.tomakehurst.wiremock.WireMockServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class wireMockTest1
{


    @Test
    public void test1() throws IOException {
        WireMockServer wireMockServer = new WireMockServer();



        wireMockServer.start();

        System.out.println("in wire mock");


        //Define the stub
        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/hello")).willReturn(aResponse().withBody("Hello world")));

        //Make the request
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8080/hello");
        HttpResponse httpResponse = httpClient.execute(request);

        //Get the response as a String
        // WHY DO WE NEED TO DO THIS CONVERSION WITH CUSTOM METHOD? SEE REST ASSURED PROJECT TO SEE WHAT
        // WAS DONE THERE

        //Try
        //import io.restassured.response.ResponseBodyData
        // and used     String resp = response.asString();

        //String responseString = httpResponse.toString();
        String responseString = convertResponseToString(httpResponse);

        //Check the response
        verify(getRequestedFor(urlEqualTo("/hello")));
        assertEquals("Hello world", responseString);

        wireMockServer.stop();


    }

    private String convertResponseToString(HttpResponse response) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseString;
    }

}
