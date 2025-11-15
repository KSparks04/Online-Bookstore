package org.project.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features ="src/test/resources/features",
        glue ={"org.project.cucumber.steps","org.project.cucumber"},
        plugin = {"pretty", "html:target/cucumber-html-report"},
        monochrome = true
)
public class RunCucumberTest {
}
