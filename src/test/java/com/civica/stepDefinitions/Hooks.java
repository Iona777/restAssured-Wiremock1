package com.civica.stepDefinitions;

import io.cucumber.java.After;
import com.civica.resources.mockedServices.mockedServices1;

public class Hooks
{
    @After
    public void AfterScenario()
    {
        mockedServices1 mocks = new mockedServices1();
        System.out.print("do this after each scenario");
        mocks.stopMockedService();
    }
}
