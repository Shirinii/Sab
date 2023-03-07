package planbox;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import static org.testng.Assert.fail;

public class SanityTest extends baseClasses {

	@Test(priority = 1, dataProvider = "URLS")
	public void openingPage(String url, String key, String chaListUrl, String ideaUrl, String homePageUrl)
			throws InterruptedException {
		driver.get(url);
		try {
			WebElement invitationKey = driver.findElement(By.id("secretkey"));
			if (invitationKey.isDisplayed()) {
				analyzeLog();
			}
		} catch (Exception notInvitationKey) {
			analyzeLog();
			fail("THE INVITATION KEY SECTION IS NOT DISPLAYED");
		}

	}

	@Test(priority = 2, dataProvider = "URLS", dependsOnMethods = { "openingPage" })
	public void login(String url, String key, String chaListUrl, String ideaUrl, String homePageUrl)
			throws InterruptedException {
		driver.get(url);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='secretkey']"))).sendKeys(key);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-secrectkey-login"))).click();
		analyzeLog();
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("is-boxes")));

	}

/*	@Test(priority = 3, dataProvider = "URLS", dependsOnMethods = { "openingPage" })
	public void homePage(String url, String key, String chaListUrl, String ideaUrl, String homePageUrl)
			throws InterruptedException {
		driver.get(homePageUrl);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("is-boxes")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("footer")));
		analyzeLog();
		scroll.scrollDownAndUp();
	}*/

	@Test(priority = 4, dataProvider = "URLS")

	public void challengeList(String url, String key, String challengeUrl, String ideaUrl, String homePageUrl)
			throws InterruptedException {
		driver.get(challengeUrl);
	//	wait.until(ExpectedConditions
	//			.visibilityOfAllElementsLocatedBy(By.xpath("//*[contains(@class,'challenge-thumbnail')]")));
		scroll.scrollDownAndUp();
		analyzeLog();
	}

	@Test(priority = 4, dataProvider = "URLS", dependsOnMethods = { "challengeList" })
	public void challengeView(String url, String key, String challengeUrl, String ideaUrl, String homePageUrl)
			throws InterruptedException {
		driver.get(challengeUrl);
	//	wait.until(ExpectedConditions
	//			.visibilityOfAllElementsLocatedBy(By.xpath("//*[contains(@class,'challenge-thumbnail')]")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[contains(@class,'changeChallengeLayoutDropdown')]//*[@class='dropdown-toggle']")))
				.click();
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("changeChallengeViewLayout")))
					.click();
		} catch (Exception notClickable) {
		}
		try {
			WebElement text = driver.findElement(By.xpath(
					"//*[@id='challenge-list-container']//*[contains(text(),'No Challenges match your criteria')]"));
			wait.until(ExpectedConditions.visibilityOf(text));
			if (text.isDisplayed()) {
				fail("no challenges");
			}
		} catch (Exception e) {

			WebElement challengeList = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("new-challenge-back")));
			List<WebElement> items = challengeList.findElements(By.className("equalize-title"));
			for (WebElement i : items) {
				WebElement cardStatus = i.findElement(By.className("challenge-stat-card"));
				WebElement ideaNum = cardStatus.findElement(By.className("big-info-text"));
				int number = Integer.parseInt(ideaNum.getText());
				if (number >= 1) {
					WebElement challengeCard = wait
							.until(ExpectedConditions.visibilityOfElementLocated(By.className("general-tooltip")));
					jse.executeScript("arguments[0].scrollIntoView(true);", challengeCard);
					i.findElement(By.className("general-tooltip")).click();
					scroll.scrollDownAndUp();
					Thread.sleep(1000);
					driver.manage().window().fullscreen();
					WebElement challengeIdeaElement = driver.findElement(By.id("challengeIdeas"));
					challengeIdeaElement.click();
					try {
						WebElement toggleChallengeIdeaMenue = driver
								.findElement(By.xpath("//*[contains(@class,'changeIdeaLayoutDropdown')]"));
						toggleChallengeIdeaMenue.click();
						WebElement toggleChallengeIdeaTile = toggleChallengeIdeaMenue
								.findElement(By.className("changeIdeaViewLayout"));
						toggleChallengeIdeaTile.click();
					} catch (Exception notClickable) {
					}
					wait.until(ExpectedConditions
							.visibilityOfAllElementsLocatedBy(By.xpath("//*[contains(@class,'idea-thumbnail')]")));
					scroll.scrollDownAndUp();
					WebElement ideaCart = driver.findElement(By.xpath("//*[contains(@href,'/Ideas/')]"));
					jse.executeScript("arguments[0].scrollIntoView(true);", ideaCart);
					ideaCart.click();
					scroll.scrollDownAndUp();
					break;
				}
			}
		}

	}

	@Test(priority = 5, dataProvider = "URLS", dependsOnMethods = { "login" })
	public void ideaList(String url, String key, String challengeUrl, String ideaUrl, String homePageUrl) throws InterruptedException {
		driver.get(ideaUrl);
		wait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(By.xpath("//*[contains(@class,'idea-thumbnail')]")));
		scroll.scrollDownAndUp();
	}

	@Test(priority = 6, dataProvider = "URLS", dependsOnMethods = { "ideaList" })
	public void ideaView(String url, String key, String challengeUrl, String ideaUrl, String homePageUrl) throws InterruptedException {
		driver.get(ideaUrl);
		wait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(By.xpath("//*[contains(@class,'idea-thumbnail')]")));
		WebElement toggleIdeaMenue = driver.findElement(By.xpath("//*[contains(@class,'changeIdeaLayoutDropdown')]"));
		WebElement toggleIdeaTile = toggleIdeaMenue
				.findElement(By.xpath("//*[contains(@class,'changeIdeaViewLayout')]"));
		try {
			wait.until(ExpectedConditions.visibilityOf(toggleIdeaMenue)).click();
			toggleIdeaTile.click();
			wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//*[contains(@class,'idea-thumbnail')]")));
		} catch (Exception notClickable) {
		}

		WebElement ideaCart = driver.findElement(By.xpath("//*[contains(@href,'/Ideas/')]"));
		jse.executeScript("arguments[0].scrollIntoView(true);", ideaCart);
		ideaCart.click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("main-content-inner")));
		scroll.scrollDownAndUp();
	}

}