package Utilities;


import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;

import jxl.Sheet;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import PageMethods.AdactinApplication;
import PageMethods.AdactinHomePage;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;




public class Common
{
	public static String testName;
	public static int testCaseDataNo;
	private AdactinApplication adactinApplication;  

	public Common() {}

	public static int testCaseexecutionNo = 0;

	public static String getConfigProperty(String keyword) {
		Properties properties = new Properties();
		try
		{
			properties.load(new FileInputStream("./Config/TestConfiguration.properties"));
		}
		catch (FileNotFoundException e)
		{
			writeToLogFile("ERROR", "File Not Found Exception thrown while getting value of " + keyword + " from Test Configuration file");
		}
		catch (IOException e) {
			writeToLogFile("ERROR", "IO Exception thrown while getting value of " + keyword + " from Test Configuration file");
		}
		writeToLogFile("INFO", "Getting value of " + keyword + " from Test Configuration file : " + properties.getProperty(keyword));

		return properties.getProperty(keyword);
	}




	public static void writeToLogFile(String type, String message)
	{
		String t = type.toUpperCase();
		if (t.equalsIgnoreCase("DEBUG"))
		{
			GenericKeywords.logger.debug(message);
		}
		else if (t.equalsIgnoreCase("INFO"))
		{
			GenericKeywords.logger.info(message);
		}
		else if (t.equalsIgnoreCase("WARN"))
		{
			GenericKeywords.logger.warn(message);
		}
		else if (t.equalsIgnoreCase("ERROR"))
		{
			GenericKeywords.logger.error(message);
		}
		else if (t.equalsIgnoreCase("FATAL"))
		{
			GenericKeywords.logger.fatal(message);
		}
		else if (t.equalsIgnoreCase("PASS"))
		{
			GenericKeywords.logger.info(message);
		}
		else {
			GenericKeywords.logger.error("Invalid log Type :" + type + ". Unable to log the message.");
		}
	}


	public static void startup()
	{
		try
		{
			OutputAndLog.createOutputDirectory();    	
			PropertiesFile.properties();
			loadTestCaseData(); 
			GenericKeywords.extent = new ExtentReports(GenericKeywords.outputDirectory+"/Results.html", true);
			java.io.File curdir = new java.io.File(".");
			GenericKeywords.extent.loadConfig(new File(curdir.getCanonicalPath()+"\\config\\extent-config.xml"));
			GenericKeywords.extent.addSystemInfo("Browser", Common.getConfigProperty("Browser"));
		}
		catch (Exception e)
		{
			Common.writeToLogFile("ERROR","Exception Caught in startup activities , Message is ->"+e.getMessage());
		}
	}


	public static void cleanup()
	{
		try{    	    	
			if (Common.getConfigProperty("SendMail").trim().equalsIgnoreCase("Always")){ 
				ZipReportFile.zipReport();            
				writeToLogFile("INFO", "<<<<<<<<<<<<<Sending mail...>>>>>>>>>>>>>>>>>");
				Mailing.sendMail();
			}else if (Common.getConfigProperty("SendMail").trim().equalsIgnoreCase("OnlyWhenFailed")){ 
				if(GenericKeywords.testFailureCount>0){
					ZipReportFile.zipReport();            
					writeToLogFile("INFO", "<<<<<<<<<<<<<Sending mail...>>>>>>>>>>>>>>>>>");
					Mailing.sendMail();
				}
			}if (Common.getConfigProperty("SendMsg(Yes/No)").trim().equalsIgnoreCase("yes")){
				Texting.sendMsg();
			}if (Common.getConfigProperty("VoiceCall(Yes/No)").trim().equalsIgnoreCase("yes")){
				VoiceCall.voiceCall();
			}
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}




	public static void testReporter(String color, String report)
	{
		colorTypes ct = colorTypes.valueOf(color.toLowerCase());
		if (!color.contains("white"))
		{
			GenericKeywords.currentStep += 1;
		}

		switch (ct)
		{
		case green: 
			GenericKeywords.child.log(LogStatus.PASS,"<font color=green>" + GenericKeywords.currentStep + ". " + report + "</font><br/>");writeToLogFile("PASS", "Report step generation success : " + report);System.out.println("green" + GenericKeywords.currentStep); break;
		case blue:  GenericKeywords.child.log(LogStatus.INFO,"<font color=blue>" + GenericKeywords.currentStep + ". " + report + "</font><br/>");writeToLogFile("INFO", "Report step generation success : " + report);System.out.println("blue" + GenericKeywords.currentStep); break;
		case orange:  GenericKeywords.child.log(LogStatus.WARNING,"<font color=orange>" + GenericKeywords.currentStep + ". " + report + "</font><br/>");writeToLogFile("WARN", "Report step generation success : " + report); break;
		case red:  GenericKeywords.child.log(LogStatus.FAIL,"<font color=red>" + GenericKeywords.currentStep + ". " + report + "</font><br/>");writeToLogFile("ERROR", "Report step generation success : " + report);System.out.println("red" + GenericKeywords.currentStep); break;
		case white:  GenericKeywords.child.log(LogStatus.INFO,report);writeToLogFile("WARN", "Report step generation success : " + report);
		}

	}

	public static enum colorTypes
	{
		green, 
		red, 
		blue, 
		orange,  white;
	}

	public static void screenShot(String filename)
	{
		String scrPath = GenericKeywords.outputDirectory + "\\Screenshots";
		File file = new File(scrPath);
		file.mkdir();
		try {
			Robot robot = new Robot();
			Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage bufferedImage = robot.createScreenCapture(captureSize);
			File outputfile = new File(scrPath + "\\" + filename + ".png");
			ImageIO.write(bufferedImage, "png", outputfile);
			Common.writeToLogFile("INFO", "Taken screenshot of failing screen");
		}
		catch (AWTException e) {
			Common.writeToLogFile("ERROR", "AWT Exception : While taking screenshot of the failing test case");
		} catch (IOException e) {
			Common.writeToLogFile("ERROR", "IO Exception : While taking screenshot of the failing test case");
		}
	}




	public static void testFailed() {

	}



	public static void captureScreenShot(String filename)
	{
		File scrFile = null;
		String scrPath = GenericKeywords.outputDirectory + "\\Screenshots";
		File file = new File(scrPath);
		file.mkdir();
		try{
			scrFile = ((RemoteWebDriver) GenericKeywords.driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(scrPath + "\\" + filename + ".png"));
		}catch (Exception e){
			testReporter("Red", e.toString()); return;

		}finally{
			if (scrFile == null) {
				System.out.println("This WebDriver does not support screenshots");
				return;
			}
		}
	}



	public static void captureFullScreenShot(String filename)
	{
		File scrFile = null;
		String scrPath = GenericKeywords.outputDirectory + "\\Screenshots";
		File file = new File(scrPath);
		file.mkdir();
		try {
			/*Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
					.takeScreenshot(GenericKeywords.driver);
			ImageIO.write(screenshot.getImage(), "PNG", new File(scrPath + "\\" + filename + ".png"));*/

			Shutterbug.shootPage(GenericKeywords.driver,ScrollStrategy.BOTH_DIRECTIONS)	                       
			.withName(filename)
			.save(scrPath);

		} catch (Exception e) {
			testReporter("Red", e.toString());
			return;
		} finally {
			((JavascriptExecutor) GenericKeywords.driver).executeScript("scroll(0,0)");
		}
	}



	public static void testStepFailed(String errMessage)
	{
		try{
			GenericKeywords.testFailure = true;
			GenericKeywords.failureNo += 1;

			writeToLogFile("Error", errMessage);
			testReporter("Red", errMessage);
			if (!GenericKeywords.windowreadyStateStatus){
				screenShot("TestFailure" + GenericKeywords.failureNo);
				GenericKeywords.windowreadyStateStatus = true;
			}else{
				String strBrowser = Common.getConfigProperty("Browser").toLowerCase();
				if(strBrowser.contains("internetexplorer") || strBrowser.contains("ie")){
					captureScreenShot("TestFailure" + GenericKeywords.failureNo);
				}else{
					captureFullScreenShot("TestFailure" + GenericKeywords.failureNo);
				}				
			}	  
			String pathAndFile = GenericKeywords.outputDirectory + "\\Screenshots\\TestFailure" + GenericKeywords.failureNo+ ".png";
			GenericKeywords.child.log(LogStatus.INFO, "Check ScreenShot Below: " + GenericKeywords.child.addScreenCapture(pathAndFile));  	   
		}catch(Exception ex){
			writeToLogFile("Error", "Unable to report Failures to Extent Html Report->"+ex.getMessage());
		}
	}

	public static void testAssertFail(String errMessage){
		try{
			GenericKeywords.testFailure = true;
			GenericKeywords.failureNo += 1;
			writeToLogFile("Error", errMessage);
			testReporter("Red", errMessage);
			if (!GenericKeywords.windowreadyStateStatus){
				screenShot("TestFailure" + GenericKeywords.failureNo);
				GenericKeywords.windowreadyStateStatus = true;
			}else{
				String strBrowser = Common.getConfigProperty("Browser").toLowerCase();
				if(strBrowser.contains("internetexplorer") || strBrowser.contains("ie")){
					captureScreenShot("TestFailure" + GenericKeywords.failureNo);
				}else{
					captureFullScreenShot("TestFailure" + GenericKeywords.failureNo);
				}				
			}	  
			String pathAndFile = GenericKeywords.outputDirectory + "\\Screenshots\\TestFailure" + GenericKeywords.failureNo+ ".png";
			GenericKeywords.child.log(LogStatus.INFO, "Check ScreenShot Below: " + GenericKeywords.child.addScreenCapture(pathAndFile));
			GenericKeywords.child.log(LogStatus.ERROR, "Test Case Failed and Not Continuing the Execution");
			GenericKeywords.parent.appendChild(GenericKeywords.child);
			try{
				GenericKeywords.driver.quit();			
			}catch(Exception ex){
				writeToLogFile("Error", "Unable to Close the browser after failure->"+ex.getMessage());
			}finally{
				GenericKeywords.extent.endTest(GenericKeywords.parent);
				GenericKeywords.extent.flush();
				Assert.fail();
			}						
		}catch(Exception ex){
			writeToLogFile("Error", "Unable to report Failures to Extent Html Report->"+ex.getMessage());
			Assert.fail();
		}

	}

	public static void testStepPassed(String errMessage)
	{
		writeToLogFile("Info", errMessage);
		testReporter("Green", errMessage);
	}

	public static void testStepWarning(String errMessage)
	{
		writeToLogFile("Warn", errMessage);
		testReporter("Orange", errMessage);
	}

	public static void testStepInfo(String errMessage) {
		GenericKeywords.child.log(LogStatus.INFO, errMessage);
		writeToLogFile("Info", errMessage);
		testReporter("Blue", errMessage);
	}

	public static void testStepInfoStart(String testDataSet) {
		String strCategory = "";
		try{
			if(Common.retrieve("Category").equals("")){
				strCategory = "Regression";
			}else{
				strCategory = Common.retrieve("Category");
			}
		}catch(Exception ex){
			strCategory = "Regression";
		}
		GenericKeywords.child = GenericKeywords.extent.startTest(testDataSet);
		GenericKeywords.child.assignCategory(strCategory);
		GenericKeywords.child.log(LogStatus.INFO, "########### Start of Test Case Data Set: "+testDataSet + " ###########");	    
	}


	public static void testStepInfoEnd(String testDataSet) {
		GenericKeywords.child.log(LogStatus.INFO, "########### End of Test Case Data Set: "+testDataSet + " ###########");
		GenericKeywords.parent
		.appendChild(GenericKeywords.child);		
	}

	public static void reportStart(String strName,String testCaseDescription) {
		GenericKeywords.testCaseExecutionStatus = false;
		GenericKeywords.elementLoadWaitTime = Integer.parseInt(getConfigProperty("ElementLoadWaitTime").toString().trim());
		GenericKeywords.textLoadWaitTime = Integer.parseInt(getConfigProperty("TextLoadWaitTime").toString().trim());
		GenericKeywords.pageLoadWaitTime = Integer.parseInt(getConfigProperty("PageLoadWaitTime").toString().trim());
		GenericKeywords.implicitlyWaitTime = Integer.parseInt(getConfigProperty("ImplicitlyWaitTime").toString().trim());
		writeToLogFile("INFO", "Element time out set");
		Common.writeToLogFile("INFO", "##### Start of Test Case : " + testCaseDescription + " #####");        
		for (String testCaseName : GenericKeywords.testCaseNames)
		{
			if (testCaseName.equals(strName))
			{
				writeToLogFile("INFO", "Test Case Name: " + testCaseName);
				updateTestDataSet(testCaseName);
				testCaseexecutionNo += 1;
				break;
			}
			testCaseDataNo += 1;
		}
		GenericKeywords.parent = GenericKeywords.extent.startTest(strName,"<font size=4 color=black>" +testCaseDescription+ "</font><br/>");
	}



	public static void updateTestDataSet(String testCaseName)
	{
		useExcelSheet(getConfigProperty("TestDataFile"), 1);
		Sheet readsheet = DataDriver.w.getSheet(0);
		String testCaseDataSet = null;
		String executionFlag = null;
		Boolean flag = Boolean.valueOf(false);
		for (int caseRow = 0; caseRow < readsheet.getRows(); caseRow++) {
			GenericKeywords.testCaseDataSets.clear();
			if (testCaseName.equals(readsheet.getCell(1, caseRow).getContents()))
			{
				for (int DataRow = caseRow; DataRow < readsheet.getRows(); DataRow++)
				{
					GenericKeywords.testCaseRow = caseRow + 1;
					testCaseDataSet = readsheet.getCell(1, DataRow).getContents();
					writeToLogFile("INFO", "Test Data Set Name: " + testCaseDataSet);
					executionFlag = readsheet.getCell(2, DataRow).getContents();
					writeToLogFile("INFO", "Execution Flag: " + executionFlag);
					if ((testCaseDataSet.startsWith(testCaseName)) && (executionFlag.toUpperCase().equals("YES")))
					{
						GenericKeywords.testCaseDataSets.add(testCaseDataSet);
					} else if (testCaseDataSet.isEmpty())
					{
						flag = Boolean.valueOf(true);
						break;
					}
				}
				if (flag.booleanValue()) {
					break;
				}
			}
		}
	}




	public static void embedScreenshot(String colour, String pathAndFile)
	{   
		GenericKeywords.child.log(LogStatus.INFO, "Manual Verification Point: " + GenericKeywords.child.addScreenCapture(pathAndFile+ ".png"));    
	}


	public static void takeScreenshot()
	{
		GenericKeywords.screenshotNo += 1;     

		String strBrowser = Common.getConfigProperty("Browser").toLowerCase();
		if(strBrowser.contains("internetexplorer") || strBrowser.contains("ie")){
			captureScreenShot("Screenshot" + GenericKeywords.screenshotNo);
		}else{
			captureFullScreenShot("Screenshot" + GenericKeywords.screenshotNo);
		}			
		embedScreenshot("orange", GenericKeywords.outputDirectory + "\\Screenshots" + "\\Screenshot" + GenericKeywords.screenshotNo);
	}


	public static void useExcelSheet(String pathOfExcel, int sheetNumber)
	{
		DataDriver.useExcelSheet(pathOfExcel, sheetNumber);
	}



	public static String retrieve(String Label)
	{
		return DataDriver.retrieve(GenericKeywords.testCaseRow, GenericKeywords.testCaseDataRow, Label);
	}

	public static int returnRowNumber(String Label)
	{
		return DataDriver.returnRowNo(2, Label);
	}

	public static void loadTestCaseData() {
		useExcelSheet(getConfigProperty("TestDataFile"), 1);

		Sheet readsheet = DataDriver.w.getSheet(0);
		for (int i = 0; i < readsheet.getRows(); i++) {
			String testCaseName = readsheet.getCell(1, i).getContents();
			GenericKeywords.testCaseNames.add(testCaseName);
		}
	}


	public static String retrieveExceptionMessage(Integer exceptionNumber)
	{
		String exceptionMessage = null;
		String exceptionNo = exceptionNumber.toString();
		Sheet readsheet = DataDriver.w.getSheet(1);
		for (int i = 0; i < readsheet.getRows(); i++) {
			String testCaseName = readsheet.getCell(0, i).getContents();
			if (testCaseName.equals(exceptionNo))
			{
				exceptionMessage = readsheet.getCell(4, i).getContents();
				writeToLogFile("INFO", "Exception Message: " + exceptionMessage);
				break;
			}
		}
		return exceptionMessage;
	}


	public AdactinHomePage testStart(String strName,String testCaseDescription) {
		GenericKeywords.testFailure = false;
		GenericKeywords.currentStep = 0;
		reportStart(strName,testCaseDescription);
		adactinApplication=new AdactinApplication();
		return adactinApplication.openAdactinApplication();

	}

	public void testEnd() {
		try {
			adactinApplication.close();			
		} catch (Exception e) {
			System.out.println("Expception : " + e.getMessage());
		}finally{
			GenericKeywords.extent.endTest(GenericKeywords.parent);
			GenericKeywords.extent.flush();
			try{
				java.io.File curdir = new java.io.File(".");
				String strPath = curdir.getCanonicalPath()+"\\JenkinsResults\\";
				File srcDir = new File(GenericKeywords.outputDirectory);
				File destDir = new File(strPath);
				FileUtils.copyDirectory(srcDir, destDir);
			}catch(Exception ex){

			}
		}
	}
}
