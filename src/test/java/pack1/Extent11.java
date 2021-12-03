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
import org.testng.annotations.*;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Extent11 {
	
	ExtentReports extent;
	ExtentTest logger;
	WebDriver driver;
	
	@BeforeTest
	public void startReport() {
		extent=new ExtentReports(System.getProperty("user.dir")+"/test-output/LTIExtentReport.html",true);
		
		extent
		.addSystemInfo("Host Name","L&T Infotech")
		.addSystemInfo("Environment","QA")
		.addSystemInfo("UserName","Tarun");
		
		extent.loadConfig(new File(System.getProperty("user.dir")+"/extent-config.xml"));
	}	
		
		public static String getScreenshot(WebDriver driver, String screenshotName) throws Exception {
			
			String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			TakesScreenshot ts=(TakesScreenshot) driver;
			File source=ts.getScreenshotAs(OutputType.FILE);
			String destination=System.getProperty("user.dir")+"/FailedTestsScreenshots/"+screenshotName + dateName +".png";
			File finalDestination=new File(destination);
			FileUtils.copyFile(source, finalDestination);
			return destination;
			
		}
		
		@Test
		public void passTest() {
			logger=extent.startTest("passTest");
			Assert.assertTrue(true);
			logger.log(LogStatus.PASS, "The test case passTest has passed");
		}
		
		@Test
		public void failTest() {
			logger=extent.startTest("failTest");
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			
			driver.get("https://blazedemo.com");
			String URL=driver.getCurrentUrl();
			Assert.assertEquals(URL, "abc");
			logger.log(LogStatus.FAIL, "Test case failTest has failed");
		}
		
		@Test
		public void skipTest() {
			logger=extent.startTest("SkipTest");
			throw new SkipException("Skipping this method as it is not ready for testing");
		}
		
		@AfterMethod
		public void getResult(ITestResult result) throws Exception {
			if(result.getStatus()==ITestResult.FAILURE) {
				logger.log(LogStatus.FAIL, "Test Case which failed is: "+result.getName());
				logger.log(LogStatus.FAIL, "Test Case which failed is: "+result.getThrowable());
				String screenshotpath = Extent11.getScreenshot(driver, result.getName());
				
				logger.log(LogStatus.FAIL, logger.addScreenCapture(screenshotpath));
				
			}
			else if(result.getStatus()==ITestResult.SKIP){
				logger.log(LogStatus.SKIP, "Test Case skipped is: "+result.getName());
			}
			
			extent.endTest(logger);
		}
		
		@AfterTest
		public void endRepor() {
			extent.flush();
			extent.close();
		}
	}

