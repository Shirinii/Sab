package planbox;

import static org.testng.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Duration;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class baseClasses {
	public static ExtentTest test;
	public static ExtentReports report;

	public static WebDriver driver;
	public static WebDriverWait wait;
	public static JavascriptExecutor jse;

	@DataProvider(name = "URLS")
	public Object[][] urlsList() throws Exception {

		Object[][] testObjArray = ExcelUtils.getTableArray("/Users/shirin/eclipse-workspace/planbox/TestData.xlsx",
				"Sheet1");
		
		return (testObjArray);
		

	}

	public void analyzeLog() {
		LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
		for (LogEntry entry : logEntries) {
			//if (entry.getLevel().getName() == "SEVERE") {
				Reporter.log("<p>" + new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.toJson());
			//}
		}
		for (LogEntry entry : logEntries) {
			if (entry.getLevel().getName() == "SEVERE") {
				//fail("THRER IS A SEVERE ERROR");
			}
		}

	}

	@BeforeTest
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "/Users/shirin/Downloads/chromedriver");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		jse = (JavascriptExecutor) driver;
		Dimension dimension = new Dimension(1440, 800);
		driver.manage().window().setSize(dimension);
		}

	@AfterTest
	public void tearDown() {
	  driver.quit();
	}

	@AfterMethod
	public void screenShot(ITestResult result) {
		if (ITestResult.FAILURE == result.getStatus()) {
			try {
				TakesScreenshot screenshoot = (TakesScreenshot) driver;
				File scr = screenshoot.getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scr,
						new File("./Screenshots/" + result.getName() + System.currentTimeMillis() + ".png"));
				System.out.println("Successfully captured a screenshot");
			} catch (Exception error) {
				System.out.println("Exception happened in getting screenshots " + error.getMessage());
			}
		}
	}

}
