package PageMethods;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.GenericKeywords;

public class NewUserRegistration extends Page {
	
	
protected static String HOME_PAGE_TITLE = "AdactIn.com - New User Registration";
	
	
	public NewUserRegistration(WebDriver browser) {
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
		until(ExpectedConditions.presenceOfElementLocated(By.xpath(" //*[contains(text(),'Captcha is Empty')]")));				
	}catch(Exception e){
		System.out.println(e.getMessage());			
	}
}




}
