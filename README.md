## Naukri Profile Auto-Update
# Overview
  This project automates the process of logging into Naukri.com, navigating to the profile section, and updating the resume. The automation is implemented using Java, Selenium WebDriver, TestNG, and Maven. Continuous Integration (CI) is managed using Jenkins, which runs the job:
     - On every code push.
     - Daily at 9 AM, ensuring the profile remains updated without manual intervention.
# Key Features
  - Automates Naukri login and profile update.
  - Uses environment variables for storing credentials securely.
  - Integrated with Jenkins for scheduled and triggered executions.
  - Uses Maven for build management.
# Technologies Used
 - Java - Programming language for automation scripts.
 - Selenium WebDriver - Browser automation.
 - TestNG - Test framework.
 - Maven - Build and dependency management.
 - Jenkins - Continuous Integration (CI).
# Setup Instructions
  # Prerequisites
    - Java (JDK 8 or later)
    - Maven
    - Google Chrome(or any browser)
    - Jenkins ( for CI/CD)
  # Clone the Repository
     ```git clone https://github.com/MahimaYadowansi/NaukriProfileUpdate.git
cd NaukriProfileUpdate ```
# Configure Environment Variables
 - ``` set NAUKRI_USERNAME=your_email@example.com
       set NAUKRI_PASSWORD=your_password ```
# Run the Automation Script
 - Use the following Maven command to execute the tests:
 -  ``` mvn clean test ```
# Configure Jenkins
 - Install Jenkins and configure it with a Maven Job.
 -  In Jenkins, set environment variables for credentials under Manage Jenkins > Configure System.
 -  Schedule the job to run daily at 9 AM using Build Triggers > Schedule with the cron expression:
        0 9 * * *
    
   
    
