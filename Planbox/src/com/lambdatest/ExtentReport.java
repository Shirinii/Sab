
package com.lambdatest;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ExtentReport extends planbox.baseReport {

	//private String Status = "failed";

	@Test(priority = 1, dataProvider = "URLS")
	public void openingPage(String url, String key, String challengeUrl, String ideaUrl, String homePageUrl,
			String name) {
		logger = extent.createTest(name);
		long startTime = System.currentTimeMillis();

		driver.get(url);
		wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
		wait.until(ExpectedConditions.or(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class,'container')]")),
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='row-fluid']"))));
		analyzeLog();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='secretkey']"))).sendKeys(key);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-secrectkey-login"))).click();
		wait.until(ExpectedConditions.or(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='is-boxes']")),
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class, 'header-navbar')]"))));
		analyzeLog();
		driver.get(homePageUrl);
		wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class,'home-page')]")));
		analyzeLog();
		missingTransaction();
	}

//	@AfterMethod
//	public void tearDown() {
//		driver.executeScript("lambda-status=" + Status);
//		driver.quit();
//	}

}
