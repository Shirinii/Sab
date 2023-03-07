package planbox;

public class scroll extends baseReport {

	public static void scrollDownAndUp() throws InterruptedException {
		long lastHeight = ((Number) jse.executeScript("return document.body.scrollHeight")).longValue();
		while (true) {
			jse.executeScript("window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });");
		    Thread.sleep(1000);
			long newHeight = ((Number) jse.executeScript("return document.body.scrollHeight")).longValue();
			if (newHeight == lastHeight) {
				break;
			}
			lastHeight = newHeight;
		}
		//driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(1000));
		jse.executeScript("window.scrollTo({ top: 0, behavior: 'smooth' });");

	}

}
