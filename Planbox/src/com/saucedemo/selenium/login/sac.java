package com.saucedemo.selenium.login;


import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class sac {
	protected RemoteWebDriver driver;
	public static ExtentTest test;
	public static ExtentReports report;
	public static WebDriverWait wait;
	public static JavascriptExecutor jse;
	public static ExtentSparkReporter spark;
	public static ExtentReports extent;
	public static ExtentTest logger;

	@BeforeTest
	public void setup() throws MalformedURLException {
		ChromeOptions browserOptions = new ChromeOptions();
		browserOptions.setPlatformName("Windows 10");
		browserOptions.setBrowserVersion("latest");
		Map<String, Object> sauceOptions = new HashMap<>();
		sauceOptions.put("build", "selenium-build-9SHE9");
		sauceOptions.put("name", "planbox");
		browserOptions.setCapability("sauce:options", sauceOptions);

		URL url = new URL("https://oauth-shirin.rezaei-88a0a:bc80cb32-95ee-420c-97cd-4d9031274a9d@ondemand.us-west-1.saucelabs.com:443/wd/hub");
		driver = new RemoteWebDriver(url, browserOptions);
	}

	public static String getScreenShot(WebDriver driver, String screenshotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
        String separator = System.getProperty("file.separator"); 
        String destination = System.getProperty("user.dir") + separator + "Screenshotstest" + separator + screenshotName + dateName + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

	@DisplayName("Swag Labs Login with Selenium")
	
	@Test
	public void swagLabsLoginTest() {
		driver.get("https://www.google.com");
	}
	
	@AfterTest
	public void tearDown() {
		//extent.flush();
		driver.quit();

	}

	@AfterMethod

	public void getResult(ITestResult result) throws Exception {
		if ((result.getStatus() == ITestResult.FAILURE)) {
			logger.log(Status.FAIL,
					MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
			String screenshotPath = getScreenShot(driver, result.getName());
			logger.fail("Test Case Failed Snapshot is attached " + logger.addScreenCaptureFromPath(screenshotPath));
		} else if (result.getStatus() == ITestResult.SKIP) {
			logger.log(Status.SKIP,
					MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));

		} else if (result.getStatus() == ITestResult.SKIP) {
			logger.log(Status.SKIP,
					MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
		}
	}

}