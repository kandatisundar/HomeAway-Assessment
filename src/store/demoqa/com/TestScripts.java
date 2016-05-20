package store.demoqa.com;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class TestScripts {

	public static WebDriver driver;
	public static String product_price;
	public static String cart_price;
	public static String empty_cart_message;
	public static String product_name = "Apple iPhone 4S 16GB SIM-Free – Black";
	public static String stored_name;
	public static String expected_cart_message = "Oops, there is nothing in your cart.";

	/*public static void main(String[] args) {
		LaunchBrowser("firefox");
		Login("tester321", "n5@YAUjGRa1Ll7qD");
		Purchase();
		validate_cart();
		manage_account();
		Logout();
		LaunchBrowser("FF");
		Login("tester321", "n5@YAUjGRa1Ll7qD");
		validate_account();

	}*/

	@Parameters("Browser")
	@Test
	public static void LaunchBrowser(String Browser) {// This Method is to
														// Launch the desired
														// browser
		if (Browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else if (Browser.trim() == "GC") {

		}

		driver.get("http://store.demoqa.com/");

	}

	@Test(dataProvider = "credentials")
	public static void Login(String user, String pwd) {
		driver.switchTo().defaultContent();
		driver.findElement(By.xpath("//a[@href='http://store.demoqa.com/products-page/your-account/']")).click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		driver.findElement(By.id("log")).sendKeys(user);
		driver.findElement(By.id("pwd")).sendKeys(pwd);
		driver.findElement(By.id("login")).click();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test // (dependsOnMethods = { "Login" })
	public static void Purchase() {
		driver.switchTo().defaultContent();
		driver.findElement(By.xpath("//input[@value='Search Products']")).sendKeys("iPhone", Keys.ENTER);
		driver.findElement(By.linkText(product_name)).click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		product_price = driver.findElement(By.xpath("//span[@class='currentprice pricedisplay product_price_96']"))
				.getText();
		driver.findElement(By.xpath("//input[@value='Add To Cart']")).click();
		driver.findElement(By.linkText("Go to Checkout")).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		cart_price = driver.findElement(By.xpath("//table/tbody/tr[@class='product_row product_row_0 alt']/td[5]"))
				.getText();
		Assert.assertEquals(product_price, cart_price);
	}

	@Test // (dependsOnMethods = { "Purchase" })
	public static void validate_cart() {
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector("form>input[value=Remove]")).click();
		empty_cart_message = driver.findElement(By.xpath("//div[@class='entry-content']")).getText();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		Assert.assertEquals(expected_cart_message, empty_cart_message);

	}

	@Parameters("fname")
	@Test // (dependsOnMethods = { "Login" })
	public static void manage_account(String fname) {
		// Adding first name in the Billing details

		try {
			driver.switchTo().defaultContent();
			//driver.findElement(By.linkText("Account")).click();
			driver.findElement(By.xpath("//a[@href='http://store.demoqa.com/products-page/your-account/']")).click();
			Thread.sleep(5000);
			driver.findElement(By.linkText("Your Details")).click();
			driver.findElement(By.id("wpsc_checkout_form_2")).sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END),
					fname, Keys.ENTER);
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		} catch (NoSuchElementException | InterruptedException n) {
			// TODO Auto-generated catch block
			n.printStackTrace();
		}

	}

	@Test // (dependsOnMethods = { "manage_account" })
	public static void validate_account() {

		driver.switchTo().defaultContent();
		
		driver.findElement(By.xpath("//a[@href='http://store.demoqa.com/products-page/your-account/']")).click();
		driver.findElement(By.linkText("Your Details")).click();
		stored_name = driver.findElement(By.id("wpsc_checkout_form_2")).getText();

	}

	@Test // (dependsOnMethods = { "validate_cart" })
	public static void Logout() {

		driver.switchTo().defaultContent();
		driver.findElement(By.linkText("Log out")).click();
		driver.quit();

	}

	@DataProvider
	public Object[][] credentials() {
		Object[][] data = new Object[1][2];
		data[0][0] = "tester321";
		data[0][1] = "n5@YAUjGRa1Ll7qD";
		return data;

	}

}
