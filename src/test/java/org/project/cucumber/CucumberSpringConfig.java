package org.project.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.project.BookstoreLocalApp;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class CucumberSpringConfig {
}
