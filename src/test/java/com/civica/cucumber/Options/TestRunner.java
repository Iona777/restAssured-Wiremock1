package com.civica.cucumber.Options;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/com/civica/features",
        glue = {"com.civica.stepDefinitions"}
)
public class TestRunner
{

}
