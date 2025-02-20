package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import base.BaseTest;


public class CustomListeners extends BaseTest implements ITestListener {
	private WebDriver driver;
	 public ExtentReports extent = ExtentManager.getInstance();
		//public static ExtentTest test;
	// Initialize Logger
	    private static final Logger logger = (Logger) LogManager.getLogger(CustomListeners.class);
	    @Override
	    public void onTestStart(ITestResult result) {
	    	
	            // Create a new test instance when a test starts
	            String testName = result.getName();
	            String description = result.getMethod().getDescription();
	            // Creating a test in Extent Reports
	            ExtentManager.createTest(testName, description);

	            // Log the start of the test
	            logger.info("Test start: " + testName);
	            ExtentManager.getTest().log(Status.INFO, "Test started: " + testName);
	    }
	    @Override
	    public void onTestSuccess(ITestResult result) {
	        logger.info("Test passed: " + result.getName());
	        ExtentManager.getTest().log(Status.PASS, "Test Passed Successfully");
	    }

	    @Override
	    public void onTestFailure(ITestResult result) {
	    	 logger.info("Test failed: " + result.getName());
	    	 ExtentManager.getTest().log(Status.FAIL, "Test Failed");
	         // Take screenshot on test failure
	         Object testInstance = result.getInstance();
	         if (testInstance instanceof BaseTest) {  
	             this.driver = ((BaseTest) testInstance).getDriver();
	          
	             takeScreenshot(result.getName());
	          // Take screenshot and get path
	             String screenshotPath = takeScreenshot(result.getName()); // Store the path here
	             
	          // Attach screenshot to Extent Report
	             try {
	                
	            	
					ExtentManager.getTest().fail("Test Failed: " + result.getThrowable(),
	                         MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath ).build());
	             } catch (Exception e) {
	                 logger.error("Error attaching screenshot to Extent Report", e);
	             }
	             
	         }
	        
	    }

	    @Override
	    public void onTestSkipped(ITestResult result) {
	    	 logger.info("Test skipped: " + result.getName());
	    	 ExtentManager.getTest().log(Status.SKIP, "Test Skipped: " + result.getThrowable());
	    }

	    @Override
	    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	        // This method can be used if you care about tests failing within a success percentage
	    }

	    @Override
	    public void onStart(ITestContext context) {
	    	 logger.info("Test suite started: " + context.getName());
	    }

	    @Override
	    public void onFinish(ITestContext context) {
	    	 logger.info("Test suite finished: " + context.getName());
	    	 ExtentManager.getInstance().flush();
	    }
	/*    
	 // Method to take screenshot(already added in base class)
	    public String takeScreenshot(String testName) {
	        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	        String screenshotPath = System.getProperty("user.dir") + "/screenshots/" + testName + ".png";

	        try {
	            FileUtils.copyFile(srcFile, new File(screenshotPath));
	            logger.info("Screenshot saved: " + screenshotPath);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return screenshotPath;
	    }
	*/

	    
}

