import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TestMDAddProfessionalClaim {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments(System.getenv().getOrDefault("HEADLESS", "true").equalsIgnoreCase("true") ? "--headless=new" : "--start-maximized");
        options.addArguments("--user-data-dir=/tmp/chrome-profile-" + System.currentTimeMillis());
        String chromeBinary = System.getenv().getOrDefault("CHROME_BIN", "/usr/bin/chromium-browser");
        options.setBinary(chromeBinary);

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            String baseUrl = System.getenv().getOrDefault("TESTMD_BASE_URL", "https://testappa.collaboratemd.com/claims");
            driver.get(baseUrl);

            // If a login screen appears, attempt a basic login using env vars
            // This block is resilient: it checks for username input, otherwise proceeds
            try {
                WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='username'], input#username")));
                WebElement passwordField = driver.findElement(By.cssSelector("input[name='password'], input#password"));
                usernameField.clear();
                usernameField.sendKeys(System.getenv().getOrDefault("TESTMD_USERNAME", ""));
                passwordField.clear();
                passwordField.sendKeys(System.getenv().getOrDefault("TESTMD_PASSWORD", ""));
                driver.findElement(By.cssSelector("button[type='submit'], input[type='submit']")).click();
            } catch (Exception ignored) {
                // No login required or already logged in
            }

            // Ensure we are on the Claims page by verifying the header search bar exists
            wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[placeholder*='Search by name'], input[aria-label*='Search']")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(.,'Claim') or contains(.,'Claims')]"))
            ));

            // Click on + Add Professional Claim
            // The button may be a span/button with exact text or containing a plus icon
            By addProfessionalClaimLocator = By.xpath("//button[contains(normalize-space(),'+ Add Professional Claim')] | //span[contains(normalize-space(),'+ Add Professional Claim')] | //div[contains(@class,'btn')][contains(normalize-space(),'Add Professional Claim')] | //*[self::button or self::a or self::div or self::span][contains(.,'Add Professional Claim')]");

            try {
                WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(addProfessionalClaimLocator));
                addButton.click();
            } catch (Exception primaryFail) {
                // Fallback using CSS contains with partial match on attribute/title
                WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button, a, div.btn")
                ));
                // Try clicking the element that best matches text
                for (WebElement el : driver.findElements(By.cssSelector("button, a, div.btn, span"))) {
                    try {
                        String text = el.getText();
                        if (text != null && text.toLowerCase().contains("add professional claim")) {
                            if (el.isDisplayed() && el.isEnabled()) {
                                el.click();
                                break;
                            }
                        }
                    } catch (Exception ignored) { }
                }
            }

            // Wait for the Professional Claim form to be visible
            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(.,'Professional Claim') and (self::h1 or self::h2 or self::h3 or self::div)]")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(.,'Patient') or contains(.,'Provider')]"))
            ));

            System.out.println("Successfully opened the Add Professional Claim form.");
        } finally {
            // Keep browser open briefly for inspection in local runs
            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            driver.quit();
        }
    }
}