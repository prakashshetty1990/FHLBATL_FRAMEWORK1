package TestCases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import PageMethods.AdactinApplication;
import PageMethods.AdactinHomePage;
import PageMethods.ConfirmationPage;
import PageMethods.ForgetPassword;
import PageMethods.NewUserRegistration;
import PageMethods.SearchHotel;
import PageMethods.SelectHotel;
import Utilities.Common;
import Utilities.GenericKeywords;

@Listeners({ Utilities.TestListener.class })
public class TC_002 extends Common {

	public static int count = 1;
	private AdactinApplication adactinApplication;
	private AdactinHomePage adactinHomePage;
	private SearchHotel searchHotel;
	private SelectHotel selectHotel;
	private ForgetPassword ForgetPswd;

	private ConfirmationPage confirmpage;
	private NewUserRegistration Registrationpage;


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
	public void TC_002() {
		String strName = new Exception().getStackTrace()[0].getMethodName();		
		adactinHomePage = testStart(strName,"Search a hotel");		

		for (String testDataSet : GenericKeywords.testCaseDataSets) {
			GenericKeywords.testCaseDataRow = returnRowNumber(testDataSet);
			testStepInfoStart(testDataSet );

			searchHotel = adactinHomePage.Login();	
			if(searchHotel.verifyLoginpage())
				selectHotel=searchHotel.BookHotel();

			testStepInfoEnd(testDataSet);
		}
		testEnd();

	}


}
