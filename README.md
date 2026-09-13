# Playwright Java Framework

My Playwright + Java test automation setup. Page objects, JUnit 5, parallel
test classes, Excel-driven test data. I deliberately left out Cucumber and any
API layer — for a UI framework, plain Playwright with a good base test class
turns out to be all you need.

Most of the suite runs against
[the-internet.herokuapp.com](https://the-internet.herokuapp.com) — I ported
those over from an old Cypress project of mine, spec by spec. There's also a
DuckDuckGo search demo and an IMDb scrape-to-Excel demo in there.

(This used to be a Cucumber + REST-assured hybrid. I stripped all of that out;
the git history still has it if you're curious.)

## Getting started

You need Java 21 and Maven. Browsers don't come bundled, so install Chromium
once up front:

```bash
mvn compile
java -cp "target/classes:$(mvn -q dependency:build-classpath -Dmdep.outputFile=/dev/stdout)" \
    com.microsoft.playwright.CLI install chromium
```

Then:

```bash
mvn test                              # the whole suite, headless, parallel
mvn test -Dtest=LoginTest             # one class while you're working on it
mvn test -Dheadless=false             # watch it run
mvn test -Dbrowser=firefox            # chromium | chrome | firefox | webkit
mvn test -Dheadless=false -Dslow.mo.ms=500   # slow-motion debugging
```

Config lives in `src/test/resources/config.properties` (defaults in
`src/main/resources/application.properties`). Anything in there can be
overridden with `-Dkey=value`, and `browser`, `headless`, `base.url`,
`heroku.url` also respect `BROWSER`, `HEADLESS`, `BASE_URL`, `HEROKU_URL`
environment variables — handy for CI. The knobs you'll actually touch:
`browser`, `headless`, `timeout.ms`, and `screenshot.dir`.

## How it's laid out

```
src/main/java/io/github/mb1lal/playwright/
├── base/TestContext.java          # per-test shared state (no statics, so parallel is safe)
├── config/ConfigManager.java      # system props > env > config file > built-in defaults
├── playwright/BrowserManager.java # owns the browser lifecycle, all ThreadLocal
├── ui/pages/                      # BasePage + DuckDuckGoPage + ImdbPage + heroku/ (16 pages)
└── utils/                         # AssertJ helpers, ExcelReader, ExcelWriter

src/test/java/io/github/mb1lal/playwright/
├── support/BaseUiTest.java        # starts the browser, screenshots on failure, cleans up
└── tests/                         # SearchTest, ImdbCastTest, ExcelUtilsTest + heroku/ (17 classes)

src/test/resources/
├── config.properties              # your test-run settings go here
├── junit-platform.properties      # test classes run concurrently (4 threads)
├── data-files/testData.xlsx       # search terms + sample cast table
└── fixtures/sample-upload.txt     # attached by FileUploadTest
```

After a run: JUnit reports in `target/surefire-reports/`, failure screenshots
(`<Test>-<method>-<timestamp>.png`) in `target/screenshots/`, IMDb exports and
downloads under `target/test-output/` and `target/downloads/`. Exports always
go to `target/` — committed test data never gets mutated.

## Adding a test

New page object under `ui/pages/` extending `BasePage` (you inherit `click`,
`fill`, `assertIsVisible`, … — don't wrap Playwright twice), then a test class
extending `BaseUiTest`, which hands you a started `browser` and a fresh
`context`:

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

Two rules I try to stick to: locators live in page objects, assertions about
outcomes live in tests. And if you need fake data, Datafaker is already a test
dependency — see `ExcelUtilsTest` for the pattern.

## Things I learned the hard way

All baked into the code, written down so future-me doesn't rediscover them:

- `getByRole` matches names by **substring**. `goTo("Frames")` happily matches
  "Nested Frames" too and blows up on strict mode — hence `setExact(true)` in
  the nav helper.
- `<option>` elements are never "visible" as far as Playwright is concerned,
  so the selected dropdown value is read via JS, not the usual helpers.
- The Herokuapp TinyMCE demo key is out of editor loads, so that editor is
  permanently read-only with no content. The iframe test just checks the frame
  is there.
- The ported Cypress specs needed a few corrections: the live JS prompt says
  `I am a JS prompt` (lowercase p), and the A/B page now deterministically
  renders `No A/B Test` with the opt-out cookie. The file-download spec was
  `it.skip`ped in Cypress for needing a plugin — here it just uses the
  download event, so it runs.
- `SearchTest` and `ImdbCastTest` are `@Disabled`. Google throws reCAPTCHAs,
  DuckDuckGo throws a "prove you're human" challenge, and IMDb plain 403s —
  all three fingerprint datacenter IPs. They pass fine from a home connection;
  re-enable there with `-Dtest=SearchTest,ImdbCastTest`.

## CI

Nothing exotic — install browsers with OS deps, run Maven, keep the screenshots
when it fails:

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

## What's inside

- Playwright 1.62, JUnit 5 (BOM-managed), AssertJ
- Logback + SLF4J for logging, Lombok for the boilerplate
- Datafaker for generated test data, Apache POI for the Excel bits
- Maven Surefire running test classes concurrently
