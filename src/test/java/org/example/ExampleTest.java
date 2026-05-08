package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ExampleTest {

    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeClass
    public void openBrowser() {
        playwright = Playwright.create();

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setSlowMo(700)
        );

        context = browser.newContext();
        page = context.newPage();

        page.setDefaultTimeout(60000);

        page.navigate("https://dev.portal.denowatts.com/signin");
    }

    @Test(priority = 1)
    public void loginAndProjectSectionTest() {
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
        ).fill("solar_02");

        page.getByRole(
                AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("* Who owns this project?")
        ).fill("NiftyIT");

        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue to Location")
        ).click();
    }

    @Test(priority = 2)
    public void locationAndDetailsSectionTest() {
        page.getByRole(
                AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("* Project Address")
        ).fill("Basabo");

        page.getByRole(
                AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("* City")
        ).fill("Dhaka");

        page.getByRole(
                AriaRole.COMBOBOX,
                new Page.GetByRoleOptions().setName("* States")
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

        // Ant Design checkbox fix
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
    }

    @Test(priority = 3)
    public void servicesAndReviewSectionTest() {
        page.getByText("4", new Page.GetByTextOptions().setExact(true)).click();

        // Ant Design checkbox fix
        page.locator("label")
                .filter(new Locator.FilterOptions().setHasText("Do you need a capacity test?"))
                .click();

        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue to Review")
        ).click();

        assertThat(page.getByText("Review & Customize Your Quote")).isVisible();

        page.locator(".ant-table-cell.ant-table-cell-row-hover > .flex.w-full > .flex > svg:nth-child(3)").click();

        page.getByRole(
                AriaRole.IMG,
                new Page.GetByRoleOptions().setName("Profile menu")
        ).click();
    }

    @AfterClass
    public void closeBrowser() {
        context.close();
        browser.close();
        playwright.close();
    }
}