package pack1;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FirefoxEx {

	public static void main(String[] args) {
		System.setProperty("webdriver.gecko.driver", "H:\\LTI\\Training docs\\geckodriver.exe");
        WebDriver driver=new FirefoxDriver();
        
        driver.get("https://www.google.com/");
	}
}
