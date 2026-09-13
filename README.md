# Playwright Java Framework

UI test-automation frameworks in **two twin implementations** over the same demo
targets — [`the-internet.herokuapp.com`](https://the-internet.herokuapp.com)
(17 Cypress-parity specs), DuckDuckGo search and an IMDb scrape/export demo:

| Twin | Stack | Docs | Run from |
|---|---|---|---|
| [`java/`](java/README.md) | Playwright 1.62, Java 21, JUnit 5, AssertJ, POI, Datafaker | [README](java/README.md) | `java/` via Maven |
| [`typescript/`](typescript/README.md) | Playwright Test, TypeScript, exceljs, Faker | [README](typescript/README.md) | `typescript/` via npm |

```bash
# Java twin
cd java && mvn test

# TypeScript twin
cd typescript && npm install && npx playwright install chromium && npm test
```

Shared CI ([`.github/workflows/ci.yml`](.github/workflows/ci.yml)) runs both suites
on every push/PR. Per-twin READMEs cover configuration, authoring guides, the
Cypress parity notes, and the bot-blocked search/IMDb demos (skipped on
datacenter IPs in both twins).
