package pack1;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ExtentReportEx {

	ExtentReports extent;
	ExtentTest logger;
	WebDriver driver;

	@BeforeTest
	public void startReport() {
		extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/LTIExtentReport.html", true);
		extent.addSystemInfo("Host name", "L&T Infotech").addSystemInfo("Environment", "QA").addSystemInfo("Username",
				"Maya");

		extent.loadConfig(new File(System.getProperty("user.dir") + "/extent-config.xml"));

	}

	public static String getScreenshot(WebDriver driver, String screenshotName) throws Exception {

		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;

		File source = ts.getScreenshotAs(OutputType.FILE);
		String destination = System.getProperty("user.dir") + "/FailedTestsScreenshots/" + screenshotName + dateName
				+ ".png";

		File fileDestination = new File(destination);

		FileUtils.copyFile(source, fileDestination);
		return destination;
	}

	@Test
	public void passTest() {

		logger = extent.startTest("passTest");
		Assert.assertTrue(true);

		logger.log(LogStatus.PASS, "Test Case passed...");
	}

	@Test
	public void failTest() {

		logger = extent.startTest("failTest");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();

		driver.get("https://blazedemo.com/");

		String url = driver.getCurrentUrl();
		Assert.assertEquals(url, "Abc");

		logger.log(LogStatus.FAIL, "Test Case failed");
	}

	@Test
	public void skipTest() {
		logger = extent.startTest("Skip test");
		throw new SkipException("Skipped test");
	}

	@AfterMethod
	public void getResult(ITestResult result) throws Exception {

		if (result.getStatus() == ITestResult.FAILURE) {
			logger.log(LogStatus.FAIL, "Test case is failed-" + result.getName());
			logger.log(LogStatus.FAIL, "Test case is -" + result.getThrowable());

			String screenshotpath = ExtentReportEx.getScreenshot(driver, result.getName());

			logger.log(LogStatus.FAIL, logger.addScreenCapture(screenshotpath));

		} else if (result.getStatus() == ITestResult.SKIP) {
			logger.log(LogStatus.SKIP, "Test case is-" + result.getName());
		}
		extent.endTest(logger);
	}

	@AfterTest
	public void endTest() {
		extent.flush();
		extent.close();
	}
}
