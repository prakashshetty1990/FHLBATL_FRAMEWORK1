package Utilities;


import org.testng.ITestResult;
import org.testng.TestListenerAdapter;



public class TestListener
  extends TestListenerAdapter
{
  public TestListener() {}
  
  @Override
public void onTestFailure(ITestResult result)
  {
    GenericKeywords.testFailure = true;
    GenericKeywords.testFailureCount += 1;
  }
  
  @Override
public void onTestSuccess(ITestResult result)
  {
    GenericKeywords.testSuccessCount += 1;
  }
  

  @Override
public void onTestSkipped(ITestResult result)
  {
    GenericKeywords.testSkippedCount += 1;
  }
}
