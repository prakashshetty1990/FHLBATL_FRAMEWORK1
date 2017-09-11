package TestCases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import PageMethods.AdactinHomePage;
import PageMethods.SearchHotel;
import Utilities.Common;
import Utilities.GenericKeywords;


@Listeners({ Utilities.TestListener.class })
public class TC_001 extends Common {

	public static int count = 1;
	private AdactinHomePage adactinHomePage;
	private SearchHotel searchHotel;	


	@BeforeClass
	public void start(){
		try{
			if(GenericKeywords.outputDirectory == null){
				startup();									
			}
		}catch(Exception ex){
			Common.writeToLogFile("ERROR","Exception Caught in startup activities, Message is ->"+ex.getMessage());
		}
	}


	@Test
	public void TC_001() {
		String strName = new Exception().getStackTrace()[0].getMethodName();		
		adactinHomePage = testStart(strName,"Login to Adactin Application");		
		for (String testDataSet : GenericKeywords.testCaseDataSets) {
			GenericKeywords.testCaseDataRow = returnRowNumber(testDataSet);
			testStepInfoStart(testDataSet);
			
			searchHotel = adactinHomePage.Login();			
			if(searchHotel.verifyLoginpage())
				searchHotel.logoutFromApp();
			
			testStepInfoEnd(testDataSet);
		}		
		testEnd();		

	}

}
