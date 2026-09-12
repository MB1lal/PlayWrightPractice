# PlayWright Practice

A comprehensive test automation framework using **Playwright** and **Cucumber** for both API and frontend testing in Java.

## 📋 Overview

This project demonstrates best practices for test automation using:
- **Playwright** - Modern cross-browser automation
- **Cucumber** - BDD (Behavior Driven Development) testing
- **Java 21** - Latest LTS Java version
- **Maven** - Build and dependency management

The framework supports testing both backend APIs and frontend UI components with parallel execution capabilities.

## 🚀 Quick Start

### Prerequisites

- **Java 21** or higher
- **Maven 3.6.0** or higher
- **Git**

### Installation

1. Clone the repository:
```bash
git clone https://github.com/MB1lal/PlayWrightPractice.git
cd PlayWrightPractice
```

2. Install dependencies:
```bash
mvn clean install
```

### Running Tests

#### Run all tests:
```bash
mvn verify
```

#### Run with specific base URL:
```bash
mvn verify -Dwebdriver.base.url=https://your-app.com
```

#### Run specific test runner:
```bash
mvn verify -Dtest=allTestRunner
```

## 📁 Project Structure

```
PlayWrightPractice/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── [Page Objects, Utilities, Base Classes]
│   └── test/
│       ├── java/
│       │   ├── steps/          # Cucumber step definitions
│       │   ├── runners/        # Test runners (e.g., allTestRunner.java)
│       │   └── utils/          # Test utilities and helpers
│       └── resources/
│           └── features/       # Cucumber feature files
├── pom.xml                     # Maven configuration
├── README.md                   # This file
└── .gitignore
```

## 🛠️ Technology Stack

### Core Dependencies
- **Playwright Java** - Browser automation
- **Cucumber Java** - BDD framework
- **JUnit** - Test runner
- **AssertJ** - Fluent assertions
- **Lombok** - Boilerplate reduction

### Utilities
- **Easy Random** - Test data generation
- **JavaFaker** - Realistic fake data
- **Apache POI** - Excel file handling
- **Log4j 2** - Logging
- **Jackson** - XML data binding
- **jMDNS** - Java Multicast DNS

## ⚙️ Configuration

### Maven Parallel Execution
Tests run in parallel with the following configuration:
- **Thread Count:** 8
- **Class Thread Count:** 3
- **Execution Mode:** Methods in parallel

Configure in `pom.xml` under `maven-failsafe-plugin`:
```xml
<parallel>methods</parallel>
<useUnlimitedThreads>true</useUnlimitedThreads>
<threadCount>8</threadCount>
<threadCountClasses>3</threadCountClasses>
```

### Base URL Configuration
Pass the application base URL via system property:
```bash
-Dwebdriver.base.url=https://your-app-url.com
```

## 📊 Test Reports

After running tests, reports are generated in:
```
target/failsafe-reports/
```

## 🐛 Troubleshooting

### Issue: Tests not running
- Ensure `allTestRunner.java` exists in the test sources
- Check that feature files are in `src/test/resources/features/`
- Verify Java 21+ is installed: `java -version`

### Issue: Dependency conflicts
```bash
mvn dependency:tree
mvn clean install -U
```

### Issue: Parallel execution issues
- Reduce `threadCount` in pom.xml
- Check for thread-unsafe test implementations

## 🔄 CI/CD

This project is configured to run with Maven and can be integrated with:
- GitHub Actions
- Jenkins
- GitLab CI
- Any CI/CD platform supporting Maven

Example GitHub Actions workflow:
```yaml
- name: Run Tests
  run: mvn verify -Dwebdriver.base.url=${{ secrets.BASE_URL }}
```

## 📝 Writing Tests

### Cucumber Feature File Example
```gherkin
Feature: User Login
  Scenario: Successful login
    Given the user is on the login page
    When the user enters valid credentials
    Then the user should be logged in
```

### Step Definition Example
```java
@Given("the user is on the login page")
public void userOnLoginPage() {
    // Implementation
}
```

## 🤝 Contributing

Contributions are welcome! Please:
1. Create a feature branch
2. Make your changes
3. Submit a pull request with a clear description

See [CONTRIBUTING.md](CONTRIBUTING.md) for detailed guidelines.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📧 Support

For issues, questions, or suggestions, please:
- Open an [Issue](https://github.com/MB1lal/PlayWrightPractice/issues)
- Create a [Discussion](https://github.com/MB1lal/PlayWrightPractice/discussions)

## 🔗 Resources

- [Playwright Documentation](https://playwright.dev/java/)
- [Cucumber Documentation](https://cucumber.io/docs/cucumber/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [Java 21 Features](https://www.oracle.com/java/technologies/javase/21-relnotes.html)

---

**Last Updated:** 2026  
**Maintainer:** [MB1lal](https://github.com/MB1lal)
