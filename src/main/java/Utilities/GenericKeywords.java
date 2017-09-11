package Utilities;


import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class GenericKeywords
  extends Common
{
  public static WebDriver driver;
  public static ExtentReports extent;
  public static ExtentTest parent;
  public static ExtentTest child;
  public static String outputDirectory;
  public static String currentExcelBook;
  public static Logger logger;
  public static int currentTestCaseNumber;
  public static int currentExcelSheet;
  public static int currentStep;
  public static int failureNo;
  public static int screenshotNo; public static int rowCount; public static int colCount;  public static boolean testFailure = false;
  public static boolean loadFailure = false;
  public static int temp = 1;
  public static String testStatus = "";
  public static int testCaseDataRow; public static int textLoadWaitTime; public static int elementLoadWaitTime; public static int implicitlyWaitTime; public static int pageLoadWaitTime = 0;
  public static int testCaseRow;
  public static final ArrayList<String> testCaseNames = new ArrayList<String>();
  public static ArrayList<String> testCaseDataSets = new ArrayList<String>();
  public static boolean windowreadyStateStatus = true;
  public static int testSuccessCount = 0;
  public static int testFailureCount = 0;
  public static int testSkippedCount = 0;
  public static String timeStamp = "";
  public static boolean testCaseExecutionStatus = false;
  


  public GenericKeywords() {}
  

}
