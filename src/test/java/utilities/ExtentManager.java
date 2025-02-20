package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>(); // ThreadLocal to handle multiple threads

    public static ExtentReports getInstance() {
        if (extent == null) {
        	String reportpath=System.getProperty("user.dir")+"/test-output/ExtentReport.html";
            ExtentSparkReporter reporter = new ExtentSparkReporter(reportpath);
            reporter.config().setReportName("Daily Updatation report of Naukri Profile");
            reporter.config().setDocumentTitle("Automation Test report");
            reporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
           // reporter.config().setTheme(Theme.DARK);
             reporter.config().setTimelineEnabled(true);
          // Custom Styling (Colors, Fonts, etc.)
             String customJs = "document.body.style.backgroundColor = 'lightblue';"
                             + "document.getElementsByClassName('chart-container')[0].style.display='block';"
                             + "document.body.style.fontFamily = 'Arial, sans-serif';";
             reporter.config().setJs(customJs);
            
            
            extent = new ExtentReports();
            extent.attachReporter(reporter);
            
            
            
         // Add System Information
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Tester Name", "Mahima Kumari");
           
        }
        return extent;
        
    }

 // Create and set the current test instance
    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        testThread.set(test);
        return test;
    }
    
    
    // Get the current test instance
    public static ExtentTest getTest() {
    	return testThread.get();
    }
}

