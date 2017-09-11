package PageMethods;


import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.Common;
import Utilities.GenericKeywords;


/**
 * Base class for all the pages.
 *
 */
public abstract class Page {
	protected WebDriver browser;	
	protected abstract boolean isValidPage();

	protected abstract void waitForPageLoad();


	/**
	 * Constructor for Page class 
	 * @param browser
	 * @param report
	 */
	protected Page(WebDriver browser) {
		this.browser=browser;		
		PageFactory.initElements(browser, this);
		waitForPageLoad();
		verifyApplicationInCorrectPage();
	}


	/**
	 * Verify Application in Correct Page. 
	 * @param Nil 
	 * @return Nil
	 */	
	private void verifyApplicationInCorrectPage() {
		if (!isValidPage()) {			
			String message="The application is not in the expected page , current page: " + 
					browser.getTitle() +" Page.";
			Common.writeToLogFile("Info", message);	
		}
	}


	/**
	 * Check if the element is present in the page
	 * @param element WebElement need to check
	 * @return True/False
	 */
	protected boolean isElementPresent(WebElement element){
		try{
			new WebDriverWait(browser, 2).until(ExpectedConditions
					.elementToBeClickable(element));
			if(element.isDisplayed()){
				return true;
			}else{
				return false;
			}
		}catch(Exception ex){
			return false;		
		}
	}


	/**
	 * Check if the element is present in the page
	 * @param Element locator of type By
	 * @return True/False
	 */
	public boolean isElementPresent(By by){
		try{
			new WebDriverWait(browser, 2).until(ExpectedConditions
					.elementToBeClickable(by));
			if(browser.findElement(by).isDisplayed()){
				return true;
			}else{
				return false;
			}
		}catch(Exception ex){
			return false;		
		}
	}


	/**
	 * Check if the element is present in the page and report
	 * @param element
	 * @param Name of the Element
	 * @param Name of the page
	 */
	protected void isElementPresentReport(WebElement element,String elemName,String pageName) {
		waitForIsClickable(element);
		if(isElementPresent(element)){
			Common.testStepPassed(elemName + "Element is displayed in "+pageName+" page" );
		}else{
			Common.testStepFailed(elemName + "Element is not displayed in "+pageName+" page" );
		}
	}


	/***
	 * Method to switch to child window
	 * @param : parentWindow
	 ***/
	public void navigateToWindowWithPageTitle(String pageTitle,int expectedNumberOfWindows) {
		boolean blnNavigate=false;
		try{				
			Set<String> handles = browser.getWindowHandles();			
			if(waitForNewWindow(expectedNumberOfWindows)){
				for (String windowHandle : handles) {					
					String strActTitle = browser.switchTo().window(windowHandle).getTitle();
					if(strActTitle.contains(pageTitle)){
						blnNavigate = true;
						browser.manage().window().maximize();
						sleep(5000);				
						Common.testStepPassed("Navigated to the page -"+pageTitle+"- successfully");	
						break;
					}					
				}
				if(!blnNavigate){
					Common.testStepFailed("Unable to Navigate to the page -"+pageTitle);
				}
			}else{
				Common.testStepFailed("New window the with page Title "+pageTitle+" is not loaded");
			}
		}
		catch(RuntimeException ex){
			Common.testStepFailed("Unable to Navigate to the page -"+pageTitle+" Exception is->"+ex.getMessage());
		}
	}

	/***
	 * Method to switch to parent window
	 * @param : parentWindow
	 ***/
	public void navigatoToParentWindow(String parentWindow) {
		try{
			browser.switchTo().window(parentWindow);
		}catch(Exception ex){
			Common.testStepFailed("Unable to Navigate to Parent Window");
		}
	}

	public void jsmoveToElement(WebElement elem){
		try {
			String str = elem.toString();
			if(isElementPresent(elem)){
				String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
				JavascriptExecutor js = (JavascriptExecutor) browser;
				js.executeScript(mouseOverScript, elem);
			}else{
				Common.testStepFailed("Element is not displayed to mousehover ->"+str);
			}
		}catch(Exception ex){
			Common.testStepFailed("Unable to mouse hover on the element,Exception is->"+ex.getMessage());
		}
	}

	/***
	 * Method to close a webpage
	 * @return      : 
	 ***/
	public void closeCurrentPage(){
		String str=null;
		try {
			browser.getTitle();
			browser.close();
			sleep(1000);			
			Set<String> windows=browser.getWindowHandles();
			for(String window:windows){
				browser.switchTo().window(window);
			}
			sleep(5000);
			Common.testStepPassed("Closed the current page with title->"+str);
		} catch (Exception e) {
			Common.testStepFailed("Unable to Close the current page with title->"+str);
		}
	}


	//*****************************************************************************************************************//
	//Start Alert pop ups
	//*****************************************************************************************************************//


	/***
	 * Method to accept and close alert and return the text within the alert
	 * @return :alert message
	 ***/
	public String closeAlertAndReturnText(){
		String alertMessage=null;
		try{		
			if(waitForAlert()){
				Alert alert = browser.switchTo().alert();
				alertMessage=alert.getText();			
				alert.accept();
				Common.testStepPassed("Closed the alert successfully with text->"+alertMessage);
			}
		}catch(Exception Ex){
			Common.testStepFailed("Exception Caught while accepting the alert, Message is->"+Ex.getMessage());
		}
		return alertMessage;
	}


	/***
	 * Method to check for an alert for 20 seconds
	 * @param       : Element Name
	 * @return      : 
	 * Modified By  :  
	 ***/

	public boolean isAlertPresent(){
		try{
			WebDriverWait wait = new WebDriverWait(browser, GenericKeywords.elementLoadWaitTime);
			wait.until(ExpectedConditions.alertIsPresent());
			return true;
		}catch(Exception e){			
			return false;
		}
	}

	//*****************************************************************************************************************//
	//End Alert pop ups
	//*****************************************************************************************************************//


	//*****************************************************************************************************************//
	//Start wait
	//*****************************************************************************************************************//


	/**
	 * Method to wait for element to load in the page
	 * @param WebElement
	 */
	protected Boolean waitForIsClickable(WebElement we) {
		String str = null;
		try {
			str = we.toString();
			new WebDriverWait(browser, GenericKeywords.elementLoadWaitTime).until(ExpectedConditions
					.elementToBeClickable(we));			
			if(isElementPresent(we)){
				return true;
			}else{
				Common.testStepFailed("Element is not visible after waiting for "+GenericKeywords.elementLoadWaitTime +" Seconds");
				return false;
			}			
		} catch (Exception ex) {
			Common.testStepFailed("Element is not visible after waiting for "+GenericKeywords.elementLoadWaitTime +" Seconds, : "+str);			
			return false;
		}    	
	}


	/**
	 * Method to wait for element to load in the page
	 * @param WebElement
	 */
	protected Boolean waitForElementPresent(WebElement we) {
		String str = null;
		try {
			str = we.toString();
			new WebDriverWait(browser, GenericKeywords.elementLoadWaitTime).until(ExpectedConditions
					.visibilityOf(we));			
			if(isElementPresent(we)){
				return true;
			}else{
				Common.testStepFailed("Element is not visible after waiting for "+GenericKeywords.elementLoadWaitTime +" Seconds");
				return false;
			}			
		} catch (Exception ex) {
			Common.testStepFailed("Element is not visible after waiting for "+GenericKeywords.elementLoadWaitTime +" Seconds, : "+str);			
			return false;
		}    	
	}


	/**
	 * Method to wait for element to load in the page
	 * @param WebElement
	 */
	protected Boolean waitForElementPresent(By by) {
		String str = null;
		try {
			str = by.toString();
			new WebDriverWait(browser, GenericKeywords.elementLoadWaitTime).until(ExpectedConditions
					.visibilityOfElementLocated(by));			
			if(isElementPresent(by)){
				return true;
			}else{
				Common.testStepFailed("Element is not visible after waiting for "+GenericKeywords.elementLoadWaitTime +" Seconds");
				return false;
			}			
		} catch (Exception ex) {
			Common.testStepFailed("Element is not visible after waiting for "+GenericKeywords.elementLoadWaitTime +" Seconds, : "+str);			
			return false;
		}    	
	}



	/**
	 * Method to wait for element to load in the page
	 * @param WebElement
	 */
	protected Boolean waitForIsClickable(By by) {
		String str = null;
		try {
			str = by.toString();
			new WebDriverWait(browser, GenericKeywords.elementLoadWaitTime).until(ExpectedConditions
					.elementToBeClickable(by));			
			if(isElementPresent(by)){
				return true;
			}else{
				Common.testStepFailed("Element is not visible after waiting for "+GenericKeywords.elementLoadWaitTime +" Seconds");
				return false;
			}			
		} catch (Exception ex) {
			Common.testStepFailed("Element is not visible after waiting for "+GenericKeywords.elementLoadWaitTime +" Seconds, : "+str);			
			return false;
		}    	
	}



	/**
	 * Method to wait for element to load in the page
	 * @param by
	 */
	protected Boolean waitAndSwitchToFrame(By by) {
		String str = null;
		try {
			str = by.toString();
			new WebDriverWait(browser, GenericKeywords.elementLoadWaitTime).until(ExpectedConditions
					.frameToBeAvailableAndSwitchToIt(by));
			return true;
		} catch (Exception ex) {
			Common.testStepFailed("Frame is not displayed after waiting for "+GenericKeywords.elementLoadWaitTime +" Seconds, : "+str);			
			return false;
		}    	
	}


	/**
	 * Method to wait for element to load in the page
	 * @param Frame Index
	 */
	protected Boolean waitAndSwitchToFrame(int intFrameNum) {		
		try {			
			new WebDriverWait(browser, GenericKeywords.elementLoadWaitTime).until(ExpectedConditions
					.frameToBeAvailableAndSwitchToIt(intFrameNum));
			return true;
		} catch (Exception ex) {
			Common.testStepFailed("Frame is not displayed after waiting for "+GenericKeywords.elementLoadWaitTime +" Seconds, : Frame Index->"+intFrameNum);			
			return false;
		}    	
	}


	/**
	 * Method to wait for element to load in the page
	 * @param by
	 */
	protected Boolean waitAndSwitchToFrame(String strFrameName) {		
		try {			
			new WebDriverWait(browser, GenericKeywords.elementLoadWaitTime).until(ExpectedConditions
					.frameToBeAvailableAndSwitchToIt(strFrameName));
			return true;
		} catch (Exception ex) {
			Common.testStepFailed("Frame is not displayed after waiting for "+GenericKeywords.elementLoadWaitTime +" Seconds, : Frame Name->"+strFrameName);			
			return false;
		}    	
	}

	/**
	 * Method to wait for element to load in the page
	 * @param WebElement
	 */
	protected Boolean waitAndSwitchToFrame(WebElement frame) {
		String str = null;
		try {
			str = frame.toString();
			new WebDriverWait(browser, GenericKeywords.elementLoadWaitTime).until(ExpectedConditions
					.frameToBeAvailableAndSwitchToIt(frame));
			return true;
		} catch (Exception ex) {
			Common.testStepFailed("Frame is not displayed after waiting for "+GenericKeywords.elementLoadWaitTime +" Seconds, : "+str);			
			return false;
		}    	
	}


	/***
	 * Method to wait for the list of elements to be displayed
	 * @param       : List<WebElement>
	 * @return      : 
	 * Modified By  :  
	 ***/
	public boolean waitForElementList(List<WebElement> elems){
		try{
			WebDriverWait wait = new WebDriverWait(browser, GenericKeywords.elementLoadWaitTime);
			wait.until(ExpectedConditions.visibilityOfAllElements(elems));			
			return true;
		}catch(Exception Ex){
			Common.testStepFailed("Element List is not visible after waiting for "+GenericKeywords.elementLoadWaitTime +" Seconds");
			return false;
		}
	}


	/**
	 * method to make a thread sleep for customized time in milliseconds
	 * @param milliseconds
	 */
	protected void sleep(int milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to wait for Alert present in the page
	 * @param 
	 */
	protected Boolean waitForAlert(){
		try{
			new WebDriverWait(browser, GenericKeywords.elementLoadWaitTime).until(ExpectedConditions.alertIsPresent());
			return true;
		}catch(Exception Ex){
			Common.testStepFailed("Alert is not displayed after waiting for "+GenericKeywords.elementLoadWaitTime +" Seconds");
			return false;
		}
	}


	/**
	 * Method to wait for Alert present in the page
	 * @param 
	 */
	protected Boolean waitForNewWindow(int expectedNumberOfWindows){
		try{
			new WebDriverWait(browser, GenericKeywords.pageLoadWaitTime).until(ExpectedConditions.numberOfWindowsToBe(expectedNumberOfWindows));
			return true;
		}catch(Exception Ex){
			Common.testStepFailed("New "+expectedNumberOfWindows+"th Window is not displayed after waiting for "+GenericKeywords.pageLoadWaitTime +" Seconds");
			return false;
		}
	}

	/***
	 * Method to wait till the page contains expected text
	 * @param       : txt
	 * @return      : 
	 * Modified By  :  
	 ***/
	public void waitForText(String txt)
	{
		waitForText(txt, GenericKeywords.textLoadWaitTime);
	}


	/***
	 * Method to wait till the page contains expected text
	 * @param       : txt,timeout
	 * @return      : 
	 * Modified By  :  
	 ***/
	public void waitForText(String txt, int timeout){
		for (int second = 0; second < timeout; second++){
			if (second == timeout - 1) {
				Common.testStepFailed("The text '" + txt + "' is not found within " + GenericKeywords.textLoadWaitTime + " seconds timeout");
				break;
			}
			try{
				if (browser.getPageSource().contains(txt)) {
					Common.testStepPassed("Text: '" + txt + "' is present");
				}
			}
			catch (Exception localException){
				sleep(1000);
			}
		}
	}


	public boolean waitForDropDown(WebElement weDropDown){
		try{
			String str= weDropDown.toString();
			if(waitForIsClickable(weDropDown)){		
				for (int second = 0;; second++) {
					if (second >= 20){
						Common.testStepFailed("Values in dropdown are not loaded after waiting for 20 seconds");
						return false;
					}
					try { 
						Select droplist = new Select(weDropDown);
						if(!droplist.getOptions().isEmpty()){
							return true;						
						}
					} catch (Exception e) {
						Common.testStepFailed("Exception Caught while waiting for dropdown loading,Message is->"+e.getMessage());
						return false;
					}
					sleep(1000);
				}
			}else{
				Common.testStepFailed("Dropdown Element is not visible, Expected Property of DropDown is->"+str);
				return false;
			}
		}catch(Exception ex){
			Common.testStepFailed("Exception Caught while waiting for dropdown loading,Message is->"+ex.getMessage());
			return false;
		}
	}

	//*****************************************************************************************************************//
	//End wait
	//*****************************************************************************************************************//


	//*****************************************************************************************************************//
	//Start Click 
	//*****************************************************************************************************************//

	/***
	 * Method to click on a link(WebElement button)
	 * @param : WebElement
	 * @param : Element Name
	 ***/
	public void clickOn(WebElement we,String elemName) {		
		try{
			waitForIsClickable(we);
			String strProp = we.toString();
			if (isElementPresent(we)){
				we.click();				
				Common.testStepPassed("Clicked on WebElement-"+ elemName );	
			}else{
				Common.testStepFailed("Unable to click on Element "+elemName+", Element with following property is not displayed->"+strProp);
			}
		}catch (Exception ex) {
			Common.testStepFailed("Uanble to click on Element-"+ elemName +", Exception is->"+ex.getMessage());
		} 
	}


	/**
	 * Method to click on a link(WebElement link)
	 * @param : WebElement
	 * @param : Element Name
	 */
	protected void jsClick(WebElement we,String elemName) {		
		try{			
			((JavascriptExecutor) browser).executeScript("arguments[0].click();",we);
			Common.testStepPassed("Clicked on -"+ elemName +"- Element");			
		}catch (RuntimeException ex) {
			Common.testStepFailed("Uanble to click on Element-"+ elemName +", Exception is->"+ex.getMessage());
		} 
	}

	/***
	 * Method to enter text in a textbox
	 * @param : WebElement - Textbox
	 * @param : Text to be entered
	 * @return :
	 ***/
	public boolean enterText(WebElement we,String text){
		try{
			waitForIsClickable(we);
			if(isElementPresent(we)){
				we.clear();
				we.sendKeys(text);
				return true;
			}else{
				Common.testStepFailed("Element is not displayed, Unable to enter text->"+ text);
				return false;
			}
		}
		catch (RuntimeException ex) {			
			Common.testStepFailed("Unable to enter text in the text field->"+ text);
			return false;
		} 
	}

	/***
	 * Method to clear text in a textbox
	 * 
	 * @param : Element Name
	 * @return :
	 ***/
	public boolean clearText(WebElement we){
		try{
			waitForIsClickable(we);
			if(isElementPresent(we)){
				we.clear();			
				return true;
			}else{
				Common.testStepFailed("Element is not displayed, Unable to Clear text->");
				return false;
			}
		}catch(RuntimeException ex){
			Common.testStepFailed("Unable to clear text in the text field");
			return false;
		}
	}


	/***
	 * Method to select the checkbox
	 * @param       : cbElement
	 * @return      : 
	 * Modified By  : 
	 ***/
	public boolean selectCheckBox(WebElement cbElement){
		waitForIsClickable(cbElement);
		if (isElementPresent(cbElement)){
			try{
				if (!cbElement.isSelected()){
					cbElement.click();
				}
				Common.testStepPassed("Selected the Checkbox Successfully");
				return true;
			}catch (Exception e){
				Common.testStepFailed("Unable to Select the checkbox->"+e.getMessage());
				return false;
			}
		}else{
			Common.testStepFailed("Unable to Select the checkbox(Element is not displayed)");
			return false;
		}
	}


	/***
	 * Method to UnSelect the checkbox
	 * @param       : cbElement
	 * @return      : 
	 * Modified By  : 
	 ***/
	public boolean unSelectCheckBox(WebElement cbElement)
	{
		waitForIsClickable(cbElement);
		if (isElementPresent(cbElement)) {
			try{
				if (cbElement.isSelected()){
					cbElement.click();
				}
				Common.testStepPassed("Unchecked the checkbox");
				return true;
			}catch (Exception e){
				Common.testStepFailed("Unable to check the checkbox->"+e.getMessage());
				return false;
			}
		}else{
			Common.testStepFailed("Unable to UnSelect the checkbox(Element is not displayed)");
			return false;
		}
	}

	/***
	 * Method to hover over an element
	 * @param       : weMainMenuElement,weSubMenuElement
	 * @return      : 
	 * Modified By  :  
	 ***/
	public void clickOnSubMenu(WebElement weMain,WebElement weSub ){		
		try{
			String strMain = weMain.toString();
			if(isElementPresent(weMain)){
				Actions action = new Actions(browser);
				action.moveToElement(weMain).click().perform();			
				Common.testStepPassed("Hover over the Main menu item successfully");
			}else{
				Common.testStepFailed("Unabel to hover Main menu(Element is not displayed), Expected Property of element is->"+strMain);
			}
		}catch(Exception Ex){
			Common.testStepFailed("Exception Caught while hoverOver the main menu Item,Message is->"+Ex.getMessage());
		}
		try{
			String strSub = weSub.toString();
			waitForIsClickable(weSub);
			if(isElementPresent(weSub)){				
				weSub.click();
				Common.testStepPassed("Clicked on the Sub menu item successfully");
			}else{
				Common.testStepFailed("Sub Menu Element is not displayed, Expected Property of element is->"+strSub);
			}
		}catch(Exception ex){
			Common.testStepFailed("Unable to Click on Sub menu Item,Exception is->"+ex.getMessage());
		}		
	}


	/***
	 * Method to hover over an element
	 * @param       : WebElement we
	 * @return      : 
	 * Modified By  :  
	 ***/
	public boolean moveToElement(WebElement we){				
		try {
			String strMain = we.toString();
			if(isElementPresent(we)){
				Actions action = new Actions(browser);
				action.moveToElement(we).build().perform();
				return true;
			}else{
				Common.testStepFailed("Unable to move to element as element is not displayed, Expected Property of element is->"+strMain);
				return false;
			}
		} catch (Exception e) {
			Common.testStepFailed("Error Occurred while Move to Element --> "+e.getMessage());
			return false;
		}
	}

	/***
	 * Method to drag and drop from source element to destination element
	 * @param       : weSource,weDestination
	 * @return      : 
	 * Modified By  :  
	 ***/
	public void dragAndDrop(WebElement weSource, WebElement weDestination)
	{	
		String strSource = weSource.toString();
		String strDest = weDestination.toString();
		if(!isElementPresent(weSource)){
			Common.testStepFailed("Unable to perform DragAndDrop(Source element is not displayed), Expected Property of element is->"+strSource);
			return;
		}
		if(!isElementPresent(weDestination)){
			Common.testStepFailed("Unable to perform DragAndDrop(Destination element is not displayed), Expected Property of element is->"+strSource);
			return;
		}
		try{	     
			new Actions(browser).dragAndDrop(weSource, weDestination).perform();			
			Common.testStepPassed("Draged Source element and droped on Destination Element Successfully");
		}catch (Exception e){			
			Common.testStepFailed("Exception Caught while performing DragAndDrop, Mesage is->"+e.getMessage());
		}
	}

	//*****************************************************************************************************************//
	//End Click 
	//*****************************************************************************************************************//


	/***
	 * Method to get current time in minutes
	 * @param : Element Name
	 * @return :
	 * Modified By :
	 ***/
	public int getTimeInMin (String time) {
		//String time=new SimpleDateFormat("HH:mm").format(new Date());
		String[] splitTime=time.split(":");
		int hr=Integer.parseInt(splitTime[0]);
		int mn=Integer.parseInt(splitTime[1].substring(0,2));
		if(hr>12){
			hr=hr-12;
		}
		int timStamp=(hr*60)+mn;
		return timStamp;
	}


	/***
	 * Method to switch to default content
	 * @param       : 
	 * @return      : 
	 * Modified By  : 
	 ***/
	public void UnSelectFrame()
	{
		try{
			Common.writeToLogFile("Info", "Switching to default content frame ");
			browser.switchTo().defaultContent();
			Common.testStepPassed("Switched Back from frame to default page successfully");
		}catch (Exception e){
			Common.testStepFailed("Exception caught while Switching back to default page from Frame, Message is->"+e.getMessage());
		}
	}


	/***
	 * Method to Select value from dropdown by visible text
	 * @param       : we,strElemName,strVisibleText
	 * @return      : 
	 * @author      : Prakash Shetty
	 * Modified By  :  
	 ***/

	public void selectByVisisbleText(WebElement we,String strElemName,String strVisibleText){
		try{
			if(waitForDropDown(we)){
				Select sel = new Select(we);
				sel.selectByVisibleText(strVisibleText);
				Common.testStepPassed("Selected value -"+strVisibleText +" from dropdown->"+strElemName);
			}
		}catch(Exception Ex){
			Common.testStepFailed("Unable to select value from the dropdown "+Ex.getMessage());
		}
	}

	/***
	 * Method to Select value from dropdown by index
	 * @param       : we,strElemName,index
	 * @return      : 
	 * Modified By  :  
	 ***/

	public void selectByIndex(WebElement we,String strElemName,int index){
		try{
			if(waitForDropDown(we)){
				Select sel = new Select( we);
				sel.selectByIndex(index);
				Common.testStepPassed("Selected "+index +"option from dropdown->"+strElemName);
			}
		}catch(Exception Ex){
			Common.testStepFailed("Unable to select value from the dropdown "+Ex.getMessage());
		}
	}



	public void selectByVisisbleValue(WebElement we,String strElemName,String strVisibleValue){
		try{
			if(waitForDropDown(we)){
				Select sel = new Select( we);
				sel.selectByValue(strVisibleValue);
				Common.testStepPassed("Selected value -"+strVisibleValue +" from dropdown->"+strElemName);
			}
		}catch(Exception Ex){
			Common.testStepFailed("Unable to select value from the dropdown "+Ex.getMessage());
		}
	}

}
