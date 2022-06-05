package runner;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features/backend"},
        glue = {"backend"},
        tags = "@test and @backend and not @ignore",
        stepNotifications = false,
        plugin = {
                "json:target/cucumber-report/cucumber.json"
        }
)
public class backendTestRunner {
}
