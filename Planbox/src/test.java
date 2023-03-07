import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class test {
		public static WebDriver driver;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "/Users/shirin/Downloads/chromedriver");
		driver = new ChromeDriver();
		driver.get("https://www.softwaretestinghelp.com/");
		WebElement textDemo = driver.findElement(By.xpath("//*[text()='Write and Earn']"));
		if(textDemo.isDisplayed())
		{
		System.out.println("Element found using text");
		}
		else
		System.out.println("Element not found");
		driver.quit();
		}
	}
