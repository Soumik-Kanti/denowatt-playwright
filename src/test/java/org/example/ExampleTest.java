package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.UUID;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ExampleTest {

    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    private String getRequiredEnv(String key) {
        String value = System.getenv(key);

        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Missing required GitHub Secret or environment variable: " + key);
        }

        return value;
    }

    @BeforeClass
    public void openBrowser() {
        playwright = Playwright.create();

        boolean isGithubAction = "true".equalsIgnoreCase(System.getenv("CI"));

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(isGithubAction)
                        .setSlowMo(isGithubAction ? 0 : 700)
        );

        context = browser.newContext();
        page = context.newPage();

        page.setDefaultTimeout(60000);

        page.navigate("https://dev.portal.denowatts.com/signin");
    }

    @Test
    public void createNewSiteFullFlowTest() {
        String email = getRequiredEnv("DENOWATT_EMAIL");
        String password = getRequiredEnv("DENOWATT_PASSWORD");

        String projectName = "solar_" + UUID.randomUUID().toString().substring(0, 6);

        page.getByRole(
                AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("Enter your email address")
        ).fill("soumikkanti1995@gmail.com");

        page.getByRole(
                AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("Enter your password")
        ).fill("soumik5991");

        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Login")
        ).click();

        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("ORDER A NEW SITE")
        ).click();

        page.getByRole(
                AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("* What's the name of this")
        ).fill(projectName);

        page.getByRole(
                AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("* Who owns this project?")
        ).fill("NiftyIT");

        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue to Location")
        ).click();

        page.getByRole(
                AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("* Project Address")
        ).fill("Basabo");

        page.getByRole(
                AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("* City")
        ).fill("Dhaka");

        // Important:
        // If the correct requirement is "State", this test will fail if developer writes "States".
        page.getByRole(
                AriaRole.COMBOBOX,
                new Page.GetByRoleOptions().setName(Pattern.compile("^\\*\\s*State$"))
        ).click();

        page.getByText("Alabama").click();

        page.getByRole(
                AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("* ZIP Code")
        ).fill("57767");

        page.getByRole(
                AriaRole.SPINBUTTON,
                new Page.GetByRoleOptions().setName("* What's the AC nameplate")
        ).fill("6");

        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue to Details")
        ).click();

        page.locator("label")
                .filter(new Locator.FilterOptions().setHasText("Rooftop"))
                .click();

        page.locator("label")
                .filter(new Locator.FilterOptions().setHasText("Monofacial"))
                .click();

        page.getByRole(
                AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("* When do you need the")
        ).click();

        page.getByText("15").click();

        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue to Services")
        ).click();

        page.getByText("4", new Page.GetByTextOptions().setExact(true)).click();

        page.locator("label")
                .filter(new Locator.FilterOptions().setHasText("Do you need a capacity test?"))
                .click();

        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue to Review")
        ).click();

        assertThat(page.getByText("Review & Customize Your Quote")).isVisible();
    }

    @AfterClass(alwaysRun = true)
    public void closeBrowser() {
        if (context != null) {
            context.close();
        }

        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }
    }
}