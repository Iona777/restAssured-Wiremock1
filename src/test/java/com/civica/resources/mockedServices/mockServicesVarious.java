package com.civica.resources.mockedServices;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.MatcherAssert.assertThat;


public class mockServicesVarious
{
    WireMockServer wmServer = new WireMockServer();

    public void startHeadersService()
    {

        if (!wmServer.isRunning())
            wmServer.start();

        System.out.println("Starting Header wire mock service");

        //Define the stub
        configureFor("localhost", 8080);

        //NOTE: Do NOT import the rest assured POST or it will get confused
        stubFor(post(urlEqualTo("/with/headers"))
                .withHeader("Content-Type", equalTo("text/xml"))
                .withHeader("Accept", matching("text/.*"))
                .withHeader("etag", notMatching("abcd.*"))
                .withHeader("etag", containing("2134"))
                .willReturn(aResponse().withStatus(200)));
    }
}
