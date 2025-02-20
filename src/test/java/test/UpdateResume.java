package test;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import base.BaseTest;

public class UpdateResume extends BaseTest{
	@Test(priority=1)
	public void naukriLogin()
	{
		
		
		String emailENV=System.getenv("NaukriLOGIN_EMAIL");
		String passwordENV=System.getenv("NaukriLOGIN_PASSWORD");
		test=extent.createTest("Automate Resume Updation","Run Update Resume");
		WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			
			WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(or.getProperty("loginBtn"))));
	        loginBtn.click();
	        logger.info("Clicked on login button");
	        test.log(Status.PASS, "Login button clicked");

	        // Wait for email input field and enter credentials
	        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(or.getProperty("email"))));
	        emailField.sendKeys(emailENV);
	        logger.info("Email entered");

	        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(or.getProperty("password"))));
	        passwordField.sendKeys(passwordENV);
	        logger.info("Password entered");

	        // Click login button
	        driver.findElement(By.xpath(or.getProperty("Login"))).click();
             Thread.sleep(10000);
	        // Wait for profile button to confirm successful login
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(or.getProperty("viewprofileBtn"))));

	        logger.info("Login is Successful !!!");
	        test.log(Status.PASS, "Login is Successful !!!");

			
		}catch(Exception e)
		{
			logger.error("Unable to login", e);
		}
	}
		@Test(priority=2)
		public void updateNaukriResume()
		
		
		{
			
			 WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(10));
			try {
				
				// Wait for and click 'View Profile' button
		        WebElement profileBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(or.getProperty("viewprofileBtn"))));
		        profileBtn.click();
		        logger.info("Clicked on  View profile button");
		       

		        // Wait for resume upload input field
		        WebElement updateResumeBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(or.getProperty("updateResumeInput"))));
		        // Debugging: Check if the input field is correct
	            System.out.println("Tag Name: " + updateResumeBtn.getTagName());
	            System.out.println("Type Attribute: " + updateResumeBtn.getAttribute("type"));

	            // Ensure it's an actual file input field
	            if (!"file".equals(updateResumeBtn.getAttribute("type"))) {
	                throw new IllegalStateException("The located element is not an <input type='file'>. Check your XPath.");
	            }

	            // File path correction
	            String resumePath = new File("src/test/resources/resume/MAHIMA-TEST Engineer.pdf").getAbsolutePath();
	            System.out.println("Resume Path: " + resumePath);
	            logger.info("Resume Path: " + resumePath);

	            // If file input is hidden, make it visible using JavaScript
	            JavascriptExecutor js = (JavascriptExecutor) driver;
	            js.executeScript("arguments[0].style.display='block';", updateResumeBtn);

	            // Upload Resume using sendKeys
	            updateResumeBtn.sendKeys(resumePath);
	            logger.info("Resume Uploaded !!!");
	            test.log(Status.PASS, "Resume updated successfully!");

	            // Wait and capture success message
	            Thread.sleep(5000);
	            String successMessageText = getSuccessMessage("//div[contains(text(),'Resume has been successfully uploaded')]");
	            if (!successMessageText.isEmpty()) {
	                System.out.println("Success Message: " + successMessageText);
	                logger.info("Resume upload confirmed: " + successMessageText);
	                test.log(Status.PASS, "Resume upload confirmation received: " + successMessageText);
	            } else {
	                logger.error("Success message not found.");
	                test.log(Status.FAIL, "Success message not found.");
	            }

	        } catch (Exception e) {
	            logger.error("Unable to update resume", e);
	            test.log(Status.FAIL, "Resume update failed!");
	        }
	    }

	    //  Helper method to get text from a disappearing element
	    private String getSuccessMessage(String xpath) {
	        try {
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Short wait time
	            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

	            //  Use JavaScript to capture the message before it disappears
	            JavascriptExecutor js = (JavascriptExecutor) driver;
	            return (String) js.executeScript("return arguments[0].innerText;", element);

	        } catch (TimeoutException e) {
	            System.out.println("Message disappears too fast");
	            return ""; // Return empty string if message disappears too fast
	        }
	    }

	}