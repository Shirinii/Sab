package com.lambdatest;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.google.common.base.Function;

import planbox.ExcelUtils;

public class baseReport1 {
	public static ExtentTest test;
	public static ExtentReports report;

	public static WebDriver driver;
	public static WebDriverWait wait;
	public static JavascriptExecutor jse;
	public static ExtentSparkReporter spark;
	public static ExtentReports extent;
	public static ExtentTest logger;
	public static long startTime;
	public static long duration;

	@DataProvider(name = "URLS")
	public Object[][] urlsList() throws Exception {

		Object[][] testObjArray = ExcelUtils.getTableArray("/Users/shirin/eclipse-workspace/planbox/TestData.xlsx",
				"Sheet1");

		return (testObjArray);

	}

	public static void analyzeLog() {

		wait.until(new Function<WebDriver, Object>() {
			public Object apply(WebDriver driver) {
				final String currentUrl = driver.getCurrentUrl();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
				List<LogEntry> logEntryList = new ArrayList<>(logEntries.getAll());
				for (LogEntry entry : logEntryList) {
					if (entry.getLevel().getName().equals("SEVERE") && ((entry.toJson().toString().contains("fonts")
							|| entry.toJson().toString().contains("The specified blob does not exist")))) {
						// logger.warning();
						logger.log(Status.WARNING,
								MarkupHelper
										.createLabel(
												("<p>" + "THERE IS A SEVER ERROR ON THIS URL" + "<p>" + currentUrl
														+ "<p>" + entry.getLevel() + " " + entry.toJson()),
												ExtentColor.ORANGE));
					} else if (entry.getLevel().getName().equals("SEVERE")
							&& ((!entry.toJson().toString().contains("fonts")
									|| !entry.toJson().toString().contains("The specified blob does not exist")))) {
						// logger.warning("<p>" + entry.getLevel() + " " + entry.toJson());
						logger.log(Status.FAIL,
								MarkupHelper
										.createLabel(
												("<p>" + "THERE IS A SEVER ERROR ON THIS URL" + "<p>" + currentUrl
														+ "<p>" + entry.getLevel() + " " + entry.toJson()),
												ExtentColor.RED));
					}
				}
				return true;
			}

		});
	}

	public void missingTransaction() {

		String script = "return document.getElementsByClassName('main-content-inner')[0].innerText;";
		String text = (String) jse.executeScript(script);
		String currentUrl = driver.getCurrentUrl();
		if (text.contains("missing")) {
			System.out.println("Missing Transaction" + driver.getCurrentUrl());
			logger.log(Status.WARNING, MarkupHelper
					.createLabel(("<p>" + "THERE IS A Missing Transaction" + "<p>" + currentUrl), ExtentColor.ORANGE));

		}
	}

	@BeforeTest
	public void setup(Method m, ITestContext ctx) throws MalformedURLException {
        String username = System.getenv("LT_USERNAME") == null ? "shirin.rezaei" : System.getenv("LT_USERNAME");
        String authkey = System.getenv("LT_ACCESS_KEY") == null ? "uH1xyyffA7A4L9Xgtq7dqOdo5xBsAaja6UnIZxXxFc8p6j3IFc" : System.getenv("LT_ACCESS_KEY");
        ;
        String hub = "@hub.lambdatest.com/wd/hub";

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platform", "MacOS Catalina");
        caps.setCapability("browserName", "Safari");
        caps.setCapability("version", "latest");
        caps.setCapability("build", "TestNG With Java");
        caps.setCapability("name", m.getName() + " - " + this.getClass().getName());
        caps.setCapability("plugin", "git-testng");

        String[] Tags = new String[] { "Feature", "Falcon", "Severe" };
        caps.setCapability("tags", Tags);

        driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + hub), caps);

    }
	public void setUp() {
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		jse = (JavascriptExecutor) driver;
		Dimension dimension = new Dimension(1440, 800);
		driver.manage().window().setSize(dimension);
		// for report
		extent = new ExtentReports();
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

		spark = new ExtentSparkReporter("./test-output/ExtentReport" + dateName + ".html");
		extent.attachReporter(spark);
		extent.setSystemInfo("Host Name", "Planbox");
		extent.setSystemInfo("Environment", "Production");
		spark.config().setDocumentTitle("Report");
		spark.config().setReportName("Sanity");
		spark.config().setTheme(Theme.STANDARD);
		spark.config().getReporter();
		startTime = System.currentTimeMillis();
	}

	public static String getScreenShot(WebDriver driver, String screenshotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String destination = System.getProperty("user.dir") + "/Screenshotstest/" + screenshotName + dateName + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

	@AfterTest
	public void tearDown() {
		extent.flush();
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
