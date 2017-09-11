package PageMethods;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.GenericKeywords;


public class ConfirmationPage extends Page {
	protected static String HOME_PAGE_TITLE = "Kaspersky Endpoint Security 10 for Windows";
	protected ConfirmationPage(WebDriver browser) {
		super(browser);
		// TODO Auto-generated constructor stub
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
			until(ExpectedConditions.presenceOfElementLocated(By.xpath(" //*[contains(text(),'Endpoint Security 10 for Windows')]")));				
		}catch(Exception e){
			System.out.println(e.getMessage());			
		}
	}
}
