package pack1;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

public class GridExample {
	@Test
	public void mailTest() throws MalformedURLException, InterruptedException{
	DesiredCapabilities dr=null;
	dr=DesiredCapabilities.chrome(); //can change browser to IE or firefox
	dr.setBrowserName("chrome");
	dr.setPlatform(Platform.WINDOWS);
	RemoteWebDriver driver=new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dr);
	driver.navigate().to("https://blazedemo.com/");
	Thread.sleep(5000);
	driver.close();
	}
}
