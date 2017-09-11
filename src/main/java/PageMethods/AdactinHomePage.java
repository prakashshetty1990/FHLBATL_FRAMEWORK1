package PageMethods;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.Common;
import Utilities.GenericKeywords;

public class AdactinHomePage extends Page {

	@FindBy(xpath="//input[@name='username']")	
	private WebElement weUsername;

	@FindBy(xpath="//input[@name='usname']")
	private WebElement weUsername1;

	@FindBy(xpath="//input[@name='password']")
	private WebElement wePassword;

	@FindBy(xpath="//input[@name='login']")
	private WebElement btnLogin;

	@FindBy(xpath="//a[contains(@href,'Register.php')]")
	private WebElement Registerlink;

	@FindBy(xpath="//a[contains(@href,'ForgotPassword.php')]")
	private WebElement weForgetpswdlink;

	@FindBy(xpath="//*[@id='emailadd_recovery']")
	private WebElement weEmailAddress;

	@FindBy(xpath="//*[@id='Submit']")
	private WebElement weSubmit;

	@FindBy(xpath="//a[contains(@href,'Register.php')]")
	private WebElement weNewRegister;	

	@FindBy(xpath="//*[@id='username']")
	private WebElement weNewusername;

	@FindBy(xpath="//*[@id='password']")
	private WebElement weNewPassword;

	@FindBy(xpath="//*[@id='re_password']")
	private WebElement weRePassword;

	@FindBy(xpath="//*[@id='full_name']")
	private WebElement weFullName;

	@FindBy(xpath="//*[@id='email_add']")
	private WebElement weEmailAdd;

	@FindBy(xpath="//*[@id='tnc_box']")
	private WebElement chkAgree;

	@FindBy(xpath="//*[@id='Submit']")
	private WebElement btnRegister;

	protected static String HOME_PAGE_TITLE = "AdactIn.com - Hotel Reservation System";

	public AdactinHomePage(WebDriver browser) {
		super(browser);		
	}

	@Override
	protected boolean isValidPage() {
		if (browser.getTitle().trim().contains(HOME_PAGE_TITLE)) {
			return true;
		}
		return false;
	}

	@Override
	protected void waitForPageLoad() {
		try{
			new WebDriverWait(browser,GenericKeywords.pageLoadWaitTime).
			until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='login']")));				
		}catch(Exception e){
			Common.writeToLogFile("ERROR", "Home Page is not loaded Successfully");					
		}
	}



	public SearchHotel Login()
	{	
		try{	
			if(enterText(weUsername,Common.retrieve("UserName"))) 	
				Common.testStepPassed("Entered Username ->"+ Common.retrieve("UserName"));
			if(enterText(wePassword,Common.retrieve("Password")))
				Common.testStepPassed("Entered Password ->"+ Common.retrieve("Password"));
			Common.takeScreenshot();
			clickOn(btnLogin,"Login");
		}catch(Exception ex){
			Common.testStepFailed("Exception Caught->"+ex.getMessage());
		}
		return new SearchHotel(browser);
	}


	public ForgetPassword forgetpassword()
	{
		clickOn(weForgetpswdlink,"ForgetPassword");
		enterText(weEmailAddress,Common.retrieve("EmailAddress")); 
		clickOn(weSubmit,"Submit");
		return new ForgetPassword(browser);		
	}


	public NewUserRegistration UserRegistration()
	{
		clickOn(weNewRegister,"New Registration");
		Common.testStepPassed("Clicked on link->New Registration");
		enterText(weNewusername,Common.retrieve("UserName"));
		Common.testStepPassed("Entered UserName->"+Common.retrieve("UserName"));
		enterText(weNewPassword,Common.retrieve("Password"));
		Common.testStepPassed("Entered Password->"+Common.retrieve("Password"));
		enterText(weRePassword,Common.retrieve("RePassword"));
		Common.testStepPassed("Re Entered Password->"+Common.retrieve("RePassword"));
		enterText(weFullName,Common.retrieve("FullName"));
		Common.testStepPassed("Entered Full Name->"+Common.retrieve("FullName"));
		enterText(weEmailAdd,Common.retrieve("EmailAddress"));
		Common.testStepPassed("Entered Email Address->"+Common.retrieve("EmailAddress"));
		clickOn(chkAgree,"Agree");
		Common.testStepPassed("Clicked on Agree checkbox");
		Common.takeScreenshot();
		clickOn(btnRegister,"Register");
		Common.testStepPassed("Clicked on Register Button");		
		return new NewUserRegistration(browser);
	}


}
