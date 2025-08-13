import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import io.github.bonigarcia.wdm.WebDriverManager;
public class facebookSignUp {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		 WebDriverManager.chromedriver().setup();
		 WebDriver driver = new ChromeDriver();
//    options.addArguments("--start-maximized");
//    driver.manage().window().maximize();
	    driver.get("https://en-gb.facebook.com/reg/");
	    Options op = driver.manage();
	    Window w = op.window();
	    w.maximize();
	    driver.findElement(By.xpath("//input[@name='firstname']")).sendKeys("Mary");
	    driver.findElement(By.xpath("//input[@name='lastname']")).sendKeys("Mrs");
	    WebElement day =driver.findElement(By.cssSelector("select#day"));
	    day.click();
	    Select s = new Select(day);
	    s.selectByIndex(2);
	    WebElement month = driver.findElement(By.cssSelector("select#month"));
	    month.click();
	    Select s1 = new Select(month);
	    s1.selectByValue("4");
	    WebElement year =driver.findElement(By.cssSelector("select#year"));
	    year.click();
	    Select s2 = new Select(year);
	    s2.selectByVisibleText("2017");
	    driver.findElement(By.xpath("//label[contains(text(),'Female')]")).click();
	    //driver.wait(2000);
	    //driver.findElement(By.xpath("div#u_0_c_gI input[value='2']")).click();
	    driver.findElement(By.cssSelector("input[name='reg_email__']")).sendKeys("9875647868");
	    driver.findElement(By.cssSelector("input[type='password']")).sendKeys("Char@5354");
	    driver.findElement(By.cssSelector("div._1lch button[type='submit']")).click();
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    WebElement errorMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Create a new account')]")));
	    String actualText = errorMessageElement.getText();
	    System.out.println("actualText is---->:"+actualText);
	    String expectedText = "Create a new account";
	    if (!expectedText.equals(actualText)) {
	        throw new RuntimeException("Error message does not match! expected: " + expectedText + ", actual: " + actualText);
	    }
	}

}
