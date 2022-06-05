package runner;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features/frontend"},
        glue = {"frontend"},
        tags = "@test and not @ignore",
        stepNotifications = true,
        plugin = {
                "json:target/cucumber-report/cucumber.json"
        }
)
public class FrontendTestRunner {
}
