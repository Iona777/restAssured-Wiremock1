package com.civica.resources.mockedServices;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.civica.resources.Utilities;
import com.github.tomakehurst.wiremock.junit.WireMockRule;


import org.junit.Rule;
import org.junit.Test;

public class mockedServices1
{


    WireMockServer wireMockServer = new WireMockServer();

    //This does not seem to work inside rest assured, but works OK in stand alone test. Why?
    //@Rule
    //public WireMockRule wmRule = new WireMockRule();

//    void configureSystemUnderTest() {
//        this.wireMockServer = new WireMockServer(
//                .bindAddress("127.0.0.1")
//        );



    public void startHelloWorldService()
    {

        if (!wireMockServer.isRunning())
            wireMockServer.start();

        System.out.println("Starting hello world wire mock service");


        //Define the stub
        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/hello")).willReturn(aResponse().withBody("Hello world")));

    }

    @Test
    public void startBaeldungService()
    {

        if (!wireMockServer.isRunning())
            wireMockServer.start();

        System.out.println("Starting baeldung wire mock service ");
        //Define the stub
        configureFor("localhost", 8080);
        stubFor(get(urlPathMatching("/baeldung/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("\"testing-library\": \"WireMock\"")));
    }


    public void startUnauthorisedService()
    {

        if (!wireMockServer.isRunning())
            wireMockServer.start();

        System.out.println("Starting unauthorised wire mock service ");

        //Define the stub
        configureFor("localhost", 8080);


        stubFor(get(urlPathMatching("/sorry-no"))
                .willReturn(aResponse()
                        .withStatus(407)));


    }

    public void startHeadersService()
    {

        if (!wireMockServer.isRunning())
            wireMockServer.start();

        System.out.println("Starting Header wire mock service");

        //Define the stub
        configureFor("localhost", 8080);

        //NOTE: Do NOT import the rest assured POST or it will get confused
        stubFor(post(urlEqualTo("/with/headers"))
              //  .withHeader("Content-Type", equalTo("application/json"))
            //    .withHeader("Accept", matching("text/.*"))
                .withHeader("ContentType", equalTo("application/json"))
                .withHeader("etag", containing("2134"))
                .willReturn(aResponse().withStatus(200)));

    }



    public void stopMockedService()
    {
        wireMockServer.stop();
    }


}
