package PageMethods;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.Common;
import Utilities.GenericKeywords;

public class SearchHotel extends Page {

	@FindBy(xpath="//*[@id='location']")
	private WebElement weLocation;

	@FindBy(xpath="//*[@id='hotels']")
	private WebElement weHotels;

	@FindBy(xpath="//*[@id='room_type']")
	private WebElement weRoomtype;

	@FindBy(xpath="//*[@id='room_nos']")
	private WebElement weRoomnos;

	@FindBy(xpath="//input[@name='datepick_in']")
	private WebElement Checkindate;

	@FindBy(xpath="//input[@name='datepick_out']")
	private WebElement Checkoutdate;

	@FindBy(xpath="//*[@id='adult_room']")
	private WebElement weAdults;

	@FindBy(xpath="//*[@id='child_room']")
	private WebElement weChildren;

	@FindBy(xpath="//input[@name='Submit']")
	private WebElement btnSearch;

	@FindBy(xpath="//input[@name='Reset']")
	private WebElement Reset;

	@FindBy(xpath="//a[text()='Logout']")
	private WebElement lnkLogout;

	@FindBy(xpath="//a[text()='Click here to login again']")
	private WebElement lnkLoginAgain;


	protected static String HOME_PAGE_TITLE = "AdactIn.com - Search Hotel";

	public SearchHotel(WebDriver browser) {
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
			until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='Reset']")));				
		}catch(Exception e){
			System.out.println(e.getMessage());		
		}
	}

	public SelectHotel BookHotel(){
		try{
			selectByVisisbleText(weLocation,"Location",Common.retrieve("Location"));
			selectByVisisbleText(weHotels,"Hotel",Common.retrieve("Hotel"));
			selectByVisisbleText(weRoomtype,"RoomType",Common.retrieve("RoomType"));
			selectByVisisbleText(weRoomnos,"NumRooms",Common.retrieve("RoomNos"));

			enterText(Checkindate,Common.retrieve("CheckInDate")); 
			Common.testStepPassed("Selected Check In Date as ->"+Common.retrieve("CheckInDate") );
			enterText(Checkoutdate,Common.retrieve("CheckOutDate")); 
			Common.testStepPassed("Selected Check Out Date as ->"+Common.retrieve("CheckOutDate") );
			selectByVisisbleText(weAdults,"Adults",Common.retrieve("Adults"));
			selectByVisisbleText(weChildren,"Children",Common.retrieve("Children"));
			Common.takeScreenshot();
			clickOn(btnSearch,"Search");
			Common.testStepPassed("Clicked on Seach button");
			Common.takeScreenshot();
		}catch(Exception ex){
			Common.testStepFailed("Exception Caught, Message is->"+ex.getMessage());
		}
		return new SelectHotel(browser);
	}

	public boolean verifyLoginpage(){
		try{
			if (isElementPresent(By.xpath("//input[@name='Reset']"))) {
				Common.testStepPassed("Successfully logged in to the application" );
				Common.takeScreenshot();
				return true;
			}else{
				//Common.testAssertFail("Unable to login to application, Check Username and password" );
				Common.testStepFailed("Unable to login to application, Check Username and password" );
				return false;
			}
		}catch(Exception ex){
			Common.testStepFailed("Exception Caught, Message is->"+ex.getMessage());
			return false;
		}
	}

	public AdactinHomePage logoutFromApp(){
		try{
			clickOn(lnkLogout, "Logout Link");
			clickOn(lnkLoginAgain, "Login Again Link");
		}catch(Exception ex){
			Common.testStepFailed("Exception Caught, Message is->"+ex.getMessage());			
		}
		return new AdactinHomePage(browser);
	}

}

