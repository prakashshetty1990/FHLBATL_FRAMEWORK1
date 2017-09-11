package PageMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.Common;
import Utilities.GenericKeywords;

public class SelectHotel  extends Page{

	protected static String HOME_PAGE_TITLE = "AdactIn.com - Select Hotel";
	protected SelectHotel(WebDriver browser) {
		super(browser);
		// TODO Auto-generated constructor stub
	}
	@FindBy(xpath="//*[@id='radiobutton_0']")
	private WebElement rbSelect;

	@FindBy(xpath="//input[@name='continue']")
	private WebElement btnContinue;
	@FindBy(xpath="//input[@name='cancel']")
	private WebElement btnCancel;
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
			until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='continue']")));				
		}catch(Exception e){
			System.out.println(e.getMessage());
			//report.reportFailEvent("Check Illinois Life Insurance Home Page","Home Page is not displayed");
		}
	}

	public void Selecthotel()
	{
		clickOn(rbSelect,"Selected");
		Common.testStepPassed("Selected radio button");
		//	System.out.println(Common.retrieve("rbSelect")+ "radio");
		//selectByVisisbleValue(rbSelect,"Select",Common.retrieve("rbSelect"));
		clickOn(btnContinue,"Continue");
		Common.testStepPassed("Clicked on Continue Button");
	}


}
