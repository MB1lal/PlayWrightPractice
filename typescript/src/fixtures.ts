import { test as base } from '@playwright/test';
import { AbTestPage } from './pages/heroku/ab-test.page';
import { AddRemoveElementsPage } from './pages/heroku/add-remove-elements.page';
import { BrokenImagesPage } from './pages/heroku/broken-images.page';
import { CheckboxesPage } from './pages/heroku/checkboxes.page';
import { ContextMenuPage } from './pages/heroku/context-menu.page';
import { DragDropPage } from './pages/heroku/drag-drop.page';
import { DropdownPage } from './pages/heroku/dropdown.page';
import { DuckDuckGoPage } from './pages/duckduckgo.page';
import { DynamicLoadingPage } from './pages/heroku/dynamic-loading.page';
import { FileDownloadPage } from './pages/heroku/file-download.page';
import { FileUploadPage } from './pages/heroku/file-upload.page';
import { FramesPage } from './pages/heroku/frames.page';
import { HerokuHomePage } from './pages/heroku/heroku-home.page';
import { HoversPage } from './pages/heroku/hovers.page';
import { ImdbPage } from './pages/imdb.page';
import { JsAlertsPage } from './pages/heroku/js-alerts.page';
import { LoginPage } from './pages/heroku/login.page';
import { MultipleWindowsPage } from './pages/heroku/multiple-windows.page';
import { NotificationMessagesPage } from './pages/heroku/notification-messages.page';

/**
 * Shared fixtures — the equivalent of the Java twin's BaseUiTest setup.
 * Every fixture is function-scoped (fresh per test) and Playwright Test
 * handles parallel isolation, teardown, screenshots, traces and videos,
 * so no manual lifecycle code is needed.
 */
type PageFixtures = {
  herokuHome: HerokuHomePage;
  loginPage: LoginPage;
  checkboxesPage: CheckboxesPage;
  dropdownPage: DropdownPage;
  dynamicLoadingPage: DynamicLoadingPage;
  addRemovePage: AddRemoveElementsPage;
  brokenImagesPage: BrokenImagesPage;
  contextMenuPage: ContextMenuPage;
  jsAlertsPage: JsAlertsPage;
  hoversPage: HoversPage;
  framesPage: FramesPage;
  windowsPage: MultipleWindowsPage;
  uploadPage: FileUploadPage;
  downloadPage: FileDownloadPage;
  dragDropPage: DragDropPage;
  notificationsPage: NotificationMessagesPage;
  abTestPage: AbTestPage;
  searchPage: DuckDuckGoPage;
  imdbPage: ImdbPage;
};

export const test = base.extend<PageFixtures>({
  herokuHome: ({ page }, use) => use(new HerokuHomePage(page)),
  loginPage: ({ page }, use) => use(new LoginPage(page)),
  checkboxesPage: ({ page }, use) => use(new CheckboxesPage(page)),
  dropdownPage: ({ page }, use) => use(new DropdownPage(page)),
  dynamicLoadingPage: ({ page }, use) => use(new DynamicLoadingPage(page)),
  addRemovePage: ({ page }, use) => use(new AddRemoveElementsPage(page)),
  brokenImagesPage: ({ page }, use) => use(new BrokenImagesPage(page)),
  contextMenuPage: ({ page }, use) => use(new ContextMenuPage(page)),
  jsAlertsPage: ({ page }, use) => use(new JsAlertsPage(page)),
  hoversPage: ({ page }, use) => use(new HoversPage(page)),
  framesPage: ({ page }, use) => use(new FramesPage(page)),
  windowsPage: ({ page }, use) => use(new MultipleWindowsPage(page)),
  uploadPage: ({ page }, use) => use(new FileUploadPage(page)),
  downloadPage: ({ page }, use) => use(new FileDownloadPage(page)),
  dragDropPage: ({ page }, use) => use(new DragDropPage(page)),
  notificationsPage: ({ page }, use) => use(new NotificationMessagesPage(page)),
  abTestPage: ({ page }, use) => use(new AbTestPage(page)),
  searchPage: ({ page }, use) => use(new DuckDuckGoPage(page)),
  imdbPage: ({ page }, use) => use(new ImdbPage(page)),
});

export { expect } from '@playwright/test';
