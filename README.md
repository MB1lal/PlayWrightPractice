# Playwright Practice

A Playwright UI test-automation framework in **Java 21** — Page Objects, **JUnit 5**,
parallel execution, and Excel-driven test data. No Cucumber, no API layer: just the
modern, industry-standard Playwright stack.

It covers two demo targets:

| Target | Tests | Origin |
|---|---|---|
| `https://the-internet.herokuapp.com` | 17 test classes, 27 tests | Ported from the Cypress specs in `CypressAutomation/cypress/e2e` |
| DuckDuckGo search + IMDb scrape/export | 2 test classes, 4 tests | Original Excel-driven demos (see “Disabled tests” below) |

## Prerequisites

- **Java 21+** (`java -version`)
- **Maven 3.9+** (`mvn -version`)
- Browsers are downloaded on first use — no manual install needed:
  ```bash
  mvn compile
  java -cp "target/classes:$(mvn -q dependency:build-classpath -Dmdep.outputFile=/dev/stdout)" \
      com.microsoft.playwright.CLI install chromium
  ```

## Running tests

```bash
# Full suite (headless Chromium, 4 parallel classes)
mvn test

# One test class
mvn test -Dtest=LoginTest

# Headed mode / different browser
mvn test -Dheadless=false
mvn test -Dbrowser=firefox -Dheadless=false   # chromium | chrome | firefox | webkit

# Slow-motion debugging
mvn test -Dheadless=false -Dslow.mo.ms=500

# Skip the slow IMDb demo / run only fast tests
mvn test -Dtest='!ImdbCastTest'
```

Configuration keys live in `src/test/resources/config.properties` (defaults in
`src/main/resources/application.properties`). Every key can be overridden with
`-Dkey=value`; `browser`, `headless`, `base.url` and `heroku.url` additionally
honor the `BROWSER`, `HEADLESS`, `BASE_URL`, `HEROKU_URL` environment variables.

| Key | Default | Meaning |
|---|---|---|
| `base.url` | `https://duckduckgo.com` | Search-demo entry point |
| `heroku.url` | `https://the-internet.herokuapp.com` | Cypress-port target |
| `browser` | `chromium` | `chromium` (OSS build) \| `chrome` (branded) \| `firefox` \| `webkit` |
| `headless` | `true` | Headless browser |
| `slow.mo.ms` | `0` | Delay between actions (debugging) |
| `timeout.ms` | `30000` | Navigation/action/assertion timeout |
| `viewport.width/height` | `1920x1080` | Browser viewport |
| `testdata.dir` | `src/test/resources/data-files` | Excel test data |
| `screenshot.dir` | `target/screenshots` | Failure screenshots |

## Project structure

```
src/main/java/com/example/
├── base/TestContext.java          # Per-test shared state (replaces static SharedState)
├── config/ConfigManager.java      # system props > env > config.properties > application.properties
├── playwright/BrowserManager.java # ThreadLocal Playwright lifecycle (parallel-safe)
├── ui/pages/
│   ├── BasePage.java              # Click/fill/assert building blocks for all pages
│   ├── DuckDuckGoPage.java        # Search demos
│   ├── ImdbPage.java              # IMDb title + full-credits scraping
│   └── heroku/                    # One page object per Herokuapp feature (17 pages)
└── utils/
    ├── AssertionHelper.java       # AssertJ helpers with logging
    ├── ExcelReader.java           # testData.xlsx -> List<List<String>> (+ A1-style cell refs)
    └── ExcelWriter.java           # Tables -> workbooks under target/ (never mutates test data)

src/test/java/com/example/
├── support/BaseUiTest.java        # @BeforeEach browser start, failure screenshot, @AfterEach cleanup
└── tests/
    ├── SearchTest.java            # Parameterized, Excel-driven search smoke tests
    ├── ImdbCastTest.java          # Search -> IMDb -> scrape cast -> export + verify round-trip
    ├── ExcelUtilsTest.java        # Hermetic unit tests (Datafaker-generated round-trips)
    └── heroku/                    # 17 classes mirroring the Cypress specs 1:1

src/test/resources/
├── config.properties              # Test-run configuration
├── junit-platform.properties      # Parallelism: test classes run concurrently (4 threads)
├── data-files/testData.xlsx       # Search terms (input) + sample cast table
└── fixtures/sample-upload.txt     # Attached by FileUploadTest
```

Test reports and artifacts:

```
target/surefire-reports/   # JUnit reports
target/screenshots/        # Failure screenshots (<Test>-<method>-<timestamp>.png)
target/test-output/        # IMDb cast exports (timestamped, parallel-safe)
target/downloads/          # Downloaded files
```

## Writing a new test

1. Add a page object under `src/main/java/com/example/ui/pages/` extending `BasePage`
   (inherit `click`, `fill`, `assertIsVisible`, … — don't wrap Playwright twice).
2. Add a test class under `src/test/java/com/example/tests/` extending `BaseUiTest`
   — you get a fresh `browser` (started) and `context` for free:

```java
@Tag("ui")
@DisplayName("Login")
class LoginTest extends BaseUiTest {

    @Test
    @DisplayName("Valid credentials log into the secure area")
    void loginWithValidCredentials() {
        LoginPage login = new LoginPage(browser.getPage(), context);
        browser.navigate(config.getHerokuUrl() + "/login");
        login.login("tomsmith", "SuperSecretPassword!");
        login.assertFlashContains("You logged into a secure area!");
    }
}
```

Conventions worth keeping:

- **Locators live in page objects, assertions about business outcomes in tests.**
- `getByRole(…).setExact(true)` for navigation links — role-name lookup is
  substring-based (`Frames` also matches `Nested Frames`).
- Never `waitFor visible` on inherently hidden nodes (`<option>`); read state via
  `evaluate`/`inputValue` instead (see `DropdownPage`).
- Never write into `src/test/resources` at runtime — exports go to `target/`.
- Need fake data? `net.datafaker:datafaker` is already a test dependency
  (see `ExcelUtilsTest`).

## Cypress parity notes

The `heroku/` tests mirror `CypressAutomation/cypress/e2e/*.cy.js` behavior-for-behavior,
with Playwright-native upgrades where Cypress needed workarounds:

- **Multiple windows** — uses `waitForPopup()` instead of stripping `target`.
- **File download** — uses the download event (the Cypress spec is `it.skip`ped and
  needs an extra plugin; here it runs).
- **File upload** — `setInputFiles`, no `cypress-file-upload` plugin.
- **Basic auth (invalid creds)** — asserted at UI level (401 page) instead of `cy.request`.
- **A/B test** — expectations updated: with the opt-out cookie the live site now
  deterministically renders `No A/B Test` (verified against the server).
- **JS prompt** — the live page says `I am a JS prompt` (lowercase p), unlike the
  stale Cypress expectation.
- **Iframe editor** — asserts iframe presence only: the demo's TinyMCE cloud key is
  out of editor loads, so the editor renders read-only with no content.

## Disabled tests

`SearchTest` (3 tests) and `ImdbCastTest` (1 test) are `@Disabled`: Google answers
automation with reCAPTCHA, DuckDuckGo with a human-challenge, and IMDb with HTTP 403
— all three fingerprint datacenter IPs. They pass on residential networks or headed
local runs; re-enable there with `-Dtest=SearchTest,ImdbCastTest`. Failure screenshots
in `target/screenshots/` document each block.

## CI example (GitHub Actions)

```yaml
- uses: actions/setup-java@v4
  with: { java-version: '21', distribution: 'temurin' }
- run: mvn test -Dbrowser=chromium
- uses: actions/upload-artifact@v4
  if: failure()
  with:
    name: failure-screenshots
    path: target/screenshots/
```

## Tech stack

| Library | Version | Managed by |
|---|---|---|
| Playwright | 1.62.0 | `playwright.version` |
| JUnit 5 (BOM) | 5.14.2 | `junit-bom.version` |
| AssertJ | 3.27.7 | `assertj.version` |
| Logback / SLF4J | 1.5.18 / 2.0.17 | `logback.version` / `slf4j.version` |
| Lombok | 1.18.48 | `lombok.version` |
| Datafaker | 2.7.0 | `datafaker.version` |
| Apache POI | 5.5.1 | `poi.version` |
| Surefire / Compiler plugin | 3.5.4 / 3.14.0 | properties |

Removed in this rebuild: Cucumber, TestNG, JUnit 4, RestAssured-era API
connectors/models/stepdefs, `SharedState` statics, `PropertiesReader`/`Environment`
(replaced by `ConfigManager`), and the `log4j2.xml` test config (unified on Logback).
