package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.ExtentManager;

public class BaseTest {
	public static WebDriver driver;
	public static Properties or=new Properties();
	public static Properties config=new Properties();
	public static FileInputStream fis;
	public static FileInputStream fis1;
	public static WebDriverWait wait;
	public ExtentReports extent = ExtentManager.getInstance();
	public static ExtentTest test;
	
	// Create Logger for Selenium logs
			public static final Logger seleniumLogger = LogManager.getLogger("selenium");

			// Create Logger for Application logs
			public static final Logger logger = LogManager.getLogger(BaseTest.class);
	
			
			@BeforeSuite
			public void setup() throws IOException {
				
				
				// Force Log4j2 to load configuration
				System.setProperty("log4j.configurationFile",
						System.getProperty("user.dir") + "\\src\\test\\resources\\logs\\log4j2.xml");

				// Log4j2 manual initialization (for debugging)
				Configurator.initialize(null, System.getProperty("log4j.configurationFile"));

				// Print Log4j2 configuration path to console
				System.out.println("Log4j2 Config Path: " + System.getProperty("log4j.configurationFile"));
				logger.info("Log4j2 configuration loaded successfully.");
				
				
				
				
				
				if (driver == null) {
					// Load config file
					fis = new FileInputStream(
							System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\config.properties");
					config.load(fis);

					logger.info("Config file loaded successfully.");

					// Load OR file
					fis1 = new FileInputStream(
							System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\or.properties");
					or.load(fis1);

					logger.info("OR file loaded successfully.");

				}
				// Initialize ExtentTest
				test = extent.createTest("Naukri profile automation", "Run Base tests");
				

				// Select Browser and configure performance logging
		        if (config.getProperty("browser").equals("chrome")) {
		            WebDriverManager.chromedriver().setup();
		            ChromeOptions chromeOptions = new ChromeOptions();
		            chromeOptions.setCapability("goog:loggingPrefs", java.util.Collections.singletonMap(LogType.PERFORMANCE, "ALL"));
		            driver = new ChromeDriver(chromeOptions);
		          test.log(Status.INFO,"Chrome browser opened");
		            logger.info("Chrome Browser Launched.");
		        } else if (config.getProperty("browser").equals("firefox")) {
		            WebDriverManager.firefoxdriver().setup();
		            FirefoxOptions firefoxOptions = new FirefoxOptions();
		            firefoxOptions.setCapability("moz:firefoxOptions", java.util.Collections.singletonMap(LogType.PERFORMANCE, "ALL"));
		            driver = new FirefoxDriver(firefoxOptions);
		            logger.info("Firefox Browser Launched.");
		        } else if (config.getProperty("browser").equals("edge")) {
		            WebDriverManager.edgedriver().setup();
		            EdgeOptions edgeOptions = new EdgeOptions();
		            edgeOptions.setCapability("ms:loggingPrefs", java.util.Collections.singletonMap(LogType.PERFORMANCE, "ALL"));
		            driver = new EdgeDriver(edgeOptions);
		            logger.info("Edge Browser Launched.");
		        }

		        // Open URL
		        driver.get(config.getProperty("testsiteurl"));
		      test.log(Status.INFO, "Navigated to: " + config.getProperty("testsiteurl"));  //extent report
		        logger.info("Navigated to: " + config.getProperty("testsiteurl"));
		        

		        driver.manage().window().maximize();
		        logger.info("Browser window maximized.");
		      test.log(Status.INFO, "Browser window maximized.");
     
		      
		   WebDriverWait wait=  new WebDriverWait(driver, Duration.ofSeconds(05));

		     //   driver.manage().timeouts().implicitlyWait(Integer.parseInt(config.getProperty("Implicit.wait")),
		             //   TimeUnit.SECONDS);
		        logger.info("Implicit wait set to " + config.getProperty("Implicit.wait") + " seconds.");
		     test.log(Status.INFO, "Implicit wait set to " + config.getProperty("Implicit.wait") + " seconds.");
		        // Capture Network Logs (Requests and Responses)
		        captureNetworkLogs();
		    }
			public void captureNetworkLogs() {
		        // Get the network logs for each request/response captured
		        LogEntries logEntries = driver.manage().logs().get(LogType.PERFORMANCE);
		        for (org.openqa.selenium.logging.LogEntry entry : logEntries) {
		            // Log network requests and responses to Selenium.log file
		            seleniumLogger.info("Network Log: " + entry.getMessage());
		        }
		    }
			
			
			public boolean isElementPresent(By by)
			{
				try {
					
					driver.findElement(by);
					test.log(Status.PASS, "Element is present: " + by.toString());
					return true;
					
				}catch(NoSuchElementException e)
				{
					logger.error("Element not present");
				 test.log(Status.FAIL, "Element not found: " + by.toString());
					return false;
				}
			}
			public WebDriver getDriver() {
		        return driver;
		    }
			
			 // Method to take screenshot
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
			
			
			
			

			@AfterSuite
			public void teardown() {
				 if (driver != null) {
			            driver.quit();
			            logger.info("Browser closed.");
			        }
			        ExtentManager.getInstance().flush(); // Generate the Extent report after all tests finish
			        logger.info("Test execution completed.");
			    }

			   
			}


