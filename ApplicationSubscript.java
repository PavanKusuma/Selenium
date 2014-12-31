	package subscripts;

	import eShopObjectRepository.ObjectRepository;
import genericConstants.GenericApplicationConstants;
import genericConstants.VerificationStringConstants;

	import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

	import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.seleniumemulation.SetTimeout;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;

	@SuppressWarnings("deprecation")
public class ApplicationSubscript {
	WebDriver appDriver;
	static DesiredCapabilities capabilities;
	static String appBrowser;
	static Actions interactDriver;
	static String applicationURL;
	

	
	public WebDriver intializeDriver(String sBrowserName, String sVersion, String sAppURL)
	{   		
		appBrowser = sBrowserName;
		applicationURL = sAppURL;			
		
		if(sBrowserName.equalsIgnoreCase("firefox"))		{
			
			/********** Path points to Firefox installation *******/
			System.setProperty("webdriver.firefox.bin",
					"C:\\Program Files\\Mozilla Firefox\\firefox.exe");
			
			//FirefoxProfile fp = new FirefoxProfile(new File(System.getProperty("user.dir")+"\\profile\\d0610p7u.default"));
			//FirefoxProfile fp = new FirefoxProfile(new File("C:\\Users\\pavan_kusuma\\AppDAta\\Roaming\\Mozilla\\Firefox\\Profiles\\t81ona0a.default-1373427097864"));
			FirefoxProfile fp = new FirefoxProfile(new File("D:\\Eclipse IDE\\Automation_FireFox_Profile"));
			//C:\Users\reshmi_pothuri\AppDAta\Roaming\Mozilla\Firefox\Profiles\t81ona0a.default-1373427097864
			// Below code accepts security certificates..
			fp.setPreference("setAcceptUntrustedCertificates", "true");
			fp.setAcceptUntrustedCertificates(true);
			fp.setAssumeUntrustedCertificateIssuer(true);
			appDriver = new FirefoxDriver(fp);
					 
			capabilities = DesiredCapabilities.firefox();
			capabilities.setVersion("24");					
			
		}

		if(sBrowserName.equalsIgnoreCase("ie"))		{		
			File ieFile = new File(System.getProperty("user.dir")+"\\IEDriverServer.exe");
			
			System.setProperty("webdriver.ie.driver", ieFile.getAbsolutePath());
			capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			appDriver = new InternetExplorerDriver(capabilities);
		}
		
		if(sBrowserName.equalsIgnoreCase("chrome"))		{		
			File chromeDriver = new File(System.getProperty("user.dir")+"\\chromedriver.exe");
			//System.setProperty("ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY", chromeDriver.getAbsolutePath());
						
			capabilities = DesiredCapabilities.chrome();
			
			System.setProperty("webdriver.chrome.driver", chromeDriver.getAbsolutePath());
			
			capabilities.setCapability("chrome.switches", Arrays.asList("--allow-running-insecure-content"));
			capabilities.setCapability("chrome.switches", Arrays.asList("--start-maximized"));
			capabilities.setVersion("26");
			try {
			ChromeDriverService chromeService = ChromeDriverService.createDefaultService();
							
				chromeService.start();
				appDriver = new ChromeDriver(capabilities);
			} catch (IOException e) {
				
				e.printStackTrace();
			}			
		}	
		
		interactDriver = new Actions(appDriver);
		return appDriver;
	}
	
	/********************************************************************************************************************
	 * Function Name : captureScreenErros
	 *   Description : Capture the errors on the screen
	 ********************************************************************************************************************/
	public void captureScreenErros(WebDriver augmentdriver, String fileName)
	{
		
	File newFile = new File(System.getProperty("user.dir")
			+ "\\images\\"+fileName+".png");

	File screenShotFile = ((TakesScreenshot) augmentdriver)
			.getScreenshotAs(OutputType.FILE);

	try {
		FileUtils.copyFile(screenShotFile, newFile, true);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	byte[] bFile = new byte[(int) newFile.length()];

	try {

		FileInputStream byteFileInput = new FileInputStream(newFile);

		byteFileInput.read(bFile);

	} catch (Exception e) {
		e.printStackTrace();
	}

	}
	/*
	 * Function to Clear Cookies of Web Application
	 */

	public void clearCache()
	{
		Set<Cookie> siteCookies = appDriver.manage().getCookies();

		for (Cookie siteCookie : siteCookies) {
			
				
			appDriver.manage().deleteCookie(siteCookie);
		}
	}
	
	

	public void validateMDDUpgrade()
	{
	Actions actions = new Actions(appDriver);
	//go to Shop link in MDD
	WebElement menuHoverLink = appDriver.findElement(By.xpath(ObjectRepository.NAVIGATE_TO_MDD_SHOP));
	actions.moveToElement(menuHoverLink);
	//navigate to Upgrade sub link inside MDD
	WebElement subLink = appDriver.findElement(By.xpath(ObjectRepository.NAVIGATE_UPGRADE_LINK));
	actions.moveToElement(subLink);
	//click on Upgrade sub link inside MDD
	actions.click();
	actions.perform();
	}
	

	/*******************************************************************************	 
	 *   Function Name : userLoginToeShop
	 *   Description : Check if user is able to login to eshop as an Upgrade User
	 **********************************************************************************/ 	
	public void userLoginToeShop(String Username)
	{
		appDriver.findElement(By.xpath(ObjectRepository.LOGIN_BUTTON)).click();
		
		appDriver.switchTo().activeElement();		
		

		appDriver.findElement(By.xpath(ObjectRepository.LOGIN_USERNAME_FIELD)).clear();
	    appDriver.findElement(By.xpath(ObjectRepository.LOGIN_USERNAME_FIELD)).sendKeys(Username);
	    appDriver.findElement(By.xpath(ObjectRepository.LOGIN_PAGE_BUTTON)).click();
	    
	
	}
	
	/*******************************************************************************	 
	 *   Function Name : retrieveClassName
	 *   Description : Retrieves that class Name without package Name
	 **********************************************************************************/
	public String retrieveClassName(String name) {

		String className = null;		
		String[] classABSName = name.split("\\.");		

		if(classABSName.length!=0)
		{
			className = classABSName[1];
			
		}			
		return className;
	}
	 /*******************************************************************************	 
	 *   Function Name : clickOnWebElement
	 *   Description : Click on Element
	 **********************************************************************************/  
  public void clickOnWebElement(String elementSelector)
  {
	 
	  WebElement button_Manufacturer_More = appDriver.findElement(By.xpath(elementSelector));       
	  
		if(button_Manufacturer_More.isDisplayed() || button_Manufacturer_More.isEnabled())
		{   
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			button_Manufacturer_More.click();		 
			appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		}else
		{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}
		
  }	
  
  /*******************************************************************************	 
	 *   Function Name : WaitForAjaxToLoad
	 *   Description : Wait for Ajax to Load
	 **********************************************************************************/
	public void waitForAjaxToLoad()
	{
		JavascriptExecutor executor = (JavascriptExecutor) appDriver;		
        boolean ajaxstatus = true;		
        int i=0;
		while(ajaxstatus)
		{
			boolean ajaxFlag = (Boolean) executor.executeScript("return (window.jQuery !=null) && (jQuery.active==0);");
			if(ajaxFlag){
				break;
			}
			i=i+1;			 
		}
	}
	
	/*******************************************************************************	 
	 *   Function Name : waitForElement
	 *   Description : Check if element is visible or wait for the specified time
	 **********************************************************************************/ 
	public void waitForElement(String controlSelector )
	{
		/*Wait for the Ajax call to end and the display of the Phone */
		WebDriverWait appWait = new WebDriverWait(appDriver, 30);
		//appWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(controlSelector))); 	
		//appWait.until(ExpectedConditions.elementToBeClickable(By.xpath(controlSelector))); 
		
		//appWait.until(ExpectedConditions.textToBePresentInElement(By.xpath(controlSelector), "")); 
	}
	 /*******************************************************************************	 
	 *   Function Name : removePackagesPresent
	 *   Description : Remove all Package Items
	 **********************************************************************************/
	
	public void removePackagesPresent()
	{
		List<WebElement> removePackageElements;
		
		removePackageElements = appDriver.findElements(By.xpath(ObjectRepository.REMOVE_PACKAGE));
		if((removePackageElements.size()!=0) && (!removePackageElements.isEmpty()))
		{
			
			//for(WebElement packageElement : removePackageElements)
			for(int i=1; i<=removePackageElements.size(); i++)
			{
				WebElement packageElement = appDriver.findElement(By.xpath(ObjectRepository.REMOVE_PACKAGE+"[" + i +"]"));
				packageElement.click();		
				DriverLoad();	
				
				appDriver.get(appDriver.getCurrentUrl());
				DriverLoad();
				
				if(!appDriver.findElement(By.xpath("//div[@id='content']")).getText().trim().contains(GenericApplicationConstants.BROWSE)){
				removePackageElements = appDriver.findElements(By.xpath(ObjectRepository.REMOVE_PACKAGE));
				
				i=i-1;
				}else{break;}					
			}
			
			clearCache();
			appDriver.quit();						
			
		}	
	}
	
	/*******************************************************************************	 
	 *   Function Name : removePackageAdded
	 *   Description : Check if user is able to remove all added packages
	 **********************************************************************************/ 		
	public void removePackageAdded()
	{
		
		if(appBrowser.equalsIgnoreCase("ie")){
			appDriver.findElement(By.cssSelector("a.dl-head.secondary > span[class*='trolley']")).click();					
		}else{
	     clickOnWebElement( ObjectRepository.BASKET_ICON);
		}
		

	waitForElement( ObjectRepository.EMPTY_BASKET_ALERT);
	
	List<WebElement> miniBasketLink = appDriver.findElements(By.xpath(ObjectRepository.EMPTY_BASKET_ALERT));	
		if(!miniBasketLink.isEmpty())
		{	
			for(WebElement linkElement : miniBasketLink)
			{
				if(!linkElement.getText().trim().contains(GenericApplicationConstants.BROWSE)){				
			
					clickOnWebElement(ObjectRepository.VIEW_BASKET);
					DriverLoad();
					
					removePackagesPresent();			
	           }else{    	   	        	   
	        	   clickOnWebElement( ObjectRepository.EMPTY_BASKET_ALERT);	
	        	   waitForAjaxToLoad();
	        	   appDriver.quit();	
	           }								
				break;
			}
		}	
   
   }
	
	/*******************************************************************************	 
	 *   Function Name : validateUpgradeLandingPage
	 *   Description : Select upgrade landing page
	 **********************************************************************************/
	public void validateUpgradeLandingPage(Map checkoutDetailsMap)	{
			 		 

		  /************************** Navigate to Shop link in MDD ***************************/		
		  
		  /*********************** Click on PAYG Phone Link ***************************/		
		  
			Actions actions = new Actions(appDriver);
			//go to Shop link in MDD
			WebElement menuHoverLink = appDriver.findElement(By.xpath(ObjectRepository.NAVIGATE_TO_MDD_SHOP));
			actions.moveToElement(menuHoverLink);
	
			//navigate to sub link inside MDD
			WebElement subLink = appDriver.findElement(By.xpath(ObjectRepository.NAVIGATE_UPGRADE_LINK));
			actions.moveToElement(subLink);
			//click on sub link inside MDD
			actions.click();
			actions.perform();
	

		/************************ Check Page Title ****************************/
		validatePageTitle( VerificationStringConstants.UPGRADE_PAGE_TITLE);		
		
		/**************** Check for Header Text Match **********************************/
		validateTextOnControl(ObjectRepository.H1, VerificationStringConstants.UPGRADE_PAGE_HEADER);
		
		
	}
	
	 /*******************************************************************************	 
	 *   Function Name : validateLeftHandLinks
	 *   Description : Validate Left hand Links
	 **********************************************************************************/
	public void validateLeftHandLinks()
	{
		if(appDriver.findElement(By.xpath(ObjectRepository.LHF_UPGRADE_PHONE_AND_PLAN)).getText().equalsIgnoreCase(VerificationStringConstants.UPGRADE_PHONE_AND_PLAN))
		{
			if(appDriver.findElement(By.xpath(ObjectRepository.LHF_UPGRADE_PHONE)).getText().equalsIgnoreCase(VerificationStringConstants.UPGRADE_PHONE))
			{
				if(appDriver.findElement(By.xpath(ObjectRepository.LHF_UPGRADE_PLAN)).getText().equalsIgnoreCase(VerificationStringConstants.UPGRADE_PLAN))
				{
					Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
				}
			}
		}
		else
		{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}
		
	
	}
	 /*******************************************************************************	 
	 *   Function Name : validateTextOnElements
	 *   Description : Validate Title
	 **********************************************************************************/
  public boolean validatePageTitle(String valueToCheck)
  {
	 boolean bFlag = false;
	 System.out.println(appDriver.getTitle());
	  if(appDriver.getTitle().equalsIgnoreCase(valueToCheck))
		{
		  bFlag=true;
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
		}else
		{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}
	  
	  waitForAjaxToLoad();
	  
	return bFlag;
  }
  
  /*******************************************************************************	 
	 *   Function Name : validateBasketMobileNumber
	 *   Description : Validate Mobile number displayed in the basket page
	 **********************************************************************************/
  public void validateBasketMobileNumber(String mobileNumber)
  {
	  
	  WebElement MobileNumber = appDriver.findElement(By.xpath(ObjectRepository.BASKET_MOBILE_NUMBER));       
	  
		if(MobileNumber.isDisplayed() || MobileNumber.isEnabled())
		{
			//validate the mobile number
			if(MobileNumber.getText().contains(mobileNumber))
			{   
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			}
			else
			{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
		}
	
  }
  
  /*******************************************************************************	 
	 *   Function Name : validateBasketDates
	 *   Description : Validate new contract end date =  Contract due date + Plan Term
	 **********************************************************************************/
  public void validateBasketDates(String DateXpath, String planTerm)
  {
	  //get the date
	  Date d = new Date(2014, 12, 12);
	  d.getYear();
	  
	  //get the term of plan
	  int term = Integer.valueOf(planTerm.toString().substring(0, 3).toString().trim());
	  
	  term = term/12;
	  
	  WebElement RenwalDate = appDriver.findElement(By.xpath(DateXpath));       
	  
		if(RenwalDate.isDisplayed() || RenwalDate.isEnabled())
		{
			System.out.println(RenwalDate.getText());
			System.out.println(String.valueOf(d.getYear()));
			//validate the due date
			if(RenwalDate.getText().contains(String.valueOf(d.getYear())))
			{   
				//validate the contract new date 
				if(RenwalDate.getText().contains(String.valueOf(d.getYear()+term)))
					Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			}
			else
			{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
			
		}
	
  }
  
  
  /*******************************************************************************	 
	 *   Function Name : validateContractendDate
	 *   Description : Validate new contract end date =  Contract due date + Plan Term
	 **********************************************************************************/
  public void validateContractendDate(String DateXpath, String planTerm)
  {
	  //get the date
	  Date d = new Date(2015, 12, 12);
	  d.getYear();
	  
	  //get the term of plan
	  int term = Integer.valueOf(planTerm.toString().substring(0, 3).toString().trim());
	  
	  term = term/12;
	  
	  WebElement RenwalDate = appDriver.findElement(By.xpath(DateXpath));       
	  
		if(RenwalDate.isDisplayed() || RenwalDate.isEnabled())
		{
			//validate the new contract end date
			if(RenwalDate.getText().contains(String.valueOf(d.getYear()+term)))
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			}
			else
			{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
			
  }
	
  
  /*******************************************************************************	 
		 *   Function Name : validateContractendDate
		 *   Description : Validate new contract end date =  Contract due date + Plan Term
		 **********************************************************************************/
	  public void validateOrderDeliveryAddress(String DateXpath, Map paMap)
	  {
		 WebElement delvieryAddress = appDriver.findElement(By.xpath(DateXpath));       
		  
			if(delvieryAddress.isDisplayed() || delvieryAddress.isEnabled())
			{
				//validate the new contract end date
				if(delvieryAddress.getText().contains(paMap.get(GenericApplicationConstants.USERNAME).toString().trim()))
					if(delvieryAddress.getText().contains(paMap.get(GenericApplicationConstants.FLAT_NUMBER).toString().trim()))
						if(delvieryAddress.getText().contains(paMap.get(GenericApplicationConstants.HOUSE_NUMBER).toString().trim()))
							if(delvieryAddress.getText().contains(paMap.get(GenericApplicationConstants.HOUSE_NAME).toString().trim()))
								if(delvieryAddress.getText().contains(paMap.get(GenericApplicationConstants.TOWN).toString().trim()))
									if(delvieryAddress.getText().contains(paMap.get(GenericApplicationConstants.POSTCODE).toString().trim()))
										Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
				}
				else
				{
					Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
				}
				
	  }
  
  /*******************************************************************************	 
	 *   Function Name : validateTextOnElements
	 *   Description : Validate the Text
	 **********************************************************************************/
   public void validateTextOnControl(String controlSelector, String valueToCheck)
   {
	   
	   
	   WebElement webPageHeader = appDriver.findElement(By.xpath(controlSelector));
	   
	   System.out.println(webPageHeader.getText());
	   System.out.println(valueToCheck);
	   if((webPageHeader.isDisplayed()) && (!webPageHeader.getText().isEmpty()))
	   {
		   if(webPageHeader.getText().contains(valueToCheck.trim()))
			{
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);;
			}else
			{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
	   }else
		{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}
   }
   
   /*******************************************************************************	 
	 *   Function Name : capture
	 *   Description : Capture Issue Images
	 **********************************************************************************/ 
	 public void capture(String sClassName) {

		File screenshot = ((TakesScreenshot) appDriver)
				.getScreenshotAs(OutputType.FILE);
		
		DateFormat dateFormatted = new SimpleDateFormat("ddMMyyyy_HHmmss");
		Date currDate = new Date();
		
		String dateTimeStamp = dateFormatted.format(currDate);
		

		try {
			FileUtils.copyFile(screenshot, new File("screenShots/"
					+ sClassName + "-" + dateTimeStamp + "-" + appBrowser
					+ ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
   
	 /*******************************************************************************	 
		 *   Function Name : selectDesiredPhoneandPlan
		 *   Description : Select the Desired Phone and plan
		 **********************************************************************************/
		public void selectDesiredPhoneandPlan()
		{
			appDriver.switchTo().activeElement();
			boolean bFlag = false;
			
			List<WebElement> select_Phone_Compared;
			int i=0;
			
			appDriver.findElement(By.xpath(ObjectRepository.BUTTON_SELECT_PHONE_AND_PLAN)).click();
			
			/*
	        do
	        {
	        	select_Phone_Compared = appDriver.findElements(By.xpath(ObjectRepository.BUTTON_SELECT_PHONE));	
	        	
	        	System.out.println(select_Phone_Compared.size());
			
			                                        
			if(select_Phone_Compared.size()!=0)
			{
				
				
				for(;i<select_Phone_Compared.size();i++)
				{
				
					System.out.println(sHardwareSKUID);
					System.out.println(select_Phone_Compared.get(i).getAttribute(GenericApplicationConstants.HREF).toString());
					if(select_Phone_Compared.get(i).getAttribute(GenericApplicationConstants.HREF).toString().contains(sHardwareSKUID))
					{
						select_Phone_Compared.get(i).click();
						bFlag = true;
						break;	
					}
				}
	        }
			if(bFlag == false)
		       {
		               if (appDriver.findElement(By.xpath("//a[@id='load-results']")).isDisplayed())
		               {
		            	   appDriver.findElement(By.xpath("//a[@id='load-results']")).click();
		            	   appDriver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		            	   select_Phone_Compared = appDriver.findElements(By.xpath(ObjectRepository.BUTTON_SELECT_PHONE));
		            	   System.out.println(select_Phone_Compared.size());
		            	   
		               }
		           else 
		           {
		               break;
		           }
		       }
			
			
	        }
			while (bFlag == false);
					if(bFlag)
					{
						Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
					}
					else
					{
						Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
					}
	             
			*/
		}
   
		/*******************************************************************************	 
		 *   Function Name : scollPage
		 *   Description : Scroll the Web Page
		 **********************************************************************************/	
		public void scollPage(String yCoordinate)
		{
			JavascriptExecutor jscriptExecutor = (JavascriptExecutor)appDriver;
			jscriptExecutor.executeScript("window.scrollTo(0," + yCoordinate + ")", "");
		}

		/*******************************************************************************	 
		 *   Function Name : validateEditLink
		 *   Description : Validate Edit Link and Success Icon
		 **********************************************************************************/
		public void validateEditLink()
		{
			
			/********************* Validate Edit Link ***************************/
			validateElementDisplayed( ObjectRepository.ABOUT_YOU_EDIT_LINK);
			
			/*************** Validate Success Icon ********************************/		
			validateElementDisplayed( ObjectRepository.ABOUT_YOU_SUCCESS_ICON);		
		}
		
		 /*******************************************************************************	 
		 *   Function Name : elementDisplayed
		 *   Description : Check if Element displayed
		 **********************************************************************************/    
	  public boolean validateElementDisplayed(String elementSelector)
	  {  
		  
			WebElement cardCCVIcon = appDriver.findElement(By.xpath(elementSelector));
			boolean bFlag = false;
			if(cardCCVIcon.isDisplayed())
			{
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
				bFlag = true;
			}else
			{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);			
			}	
			return bFlag;
	  }
	  
	  /*******************************************************************************	 
		 *   Function Name : validateDeliveryOptions
		 *   Description : Validate Delivery Options
		 **********************************************************************************/
		public void validateDeliveryOptions()
		{		
		
			/********************** Validate Delivery Option *************************/
			//validateTextOnControl(ObjectRepository.DELIVERY_OPTION_LABEL, GenericApplicationConstants.DELIVERY_OPTIONS);
			
			//validateElementAttribute( ObjectRepository.DELIVERY_TO_ADDRESS, GenericApplicationConstants.CLASS, VerificationStringConstants.CHECKED);
			
			appDriver.findElement(By.xpath(ObjectRepository.DELIVERY_OPTION_LABEL)).click();
			
			/*********************** Click on Continue Button *************************************/ 
			
			clickOnWebElement( ObjectRepository.CLICK_CONTINUE_BTN_DELIVERY);
			
			/********************** Wait for Page Load *********************************************/
			appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);	
		}
		
		
		 /*******************************************************************************	 
		 *   Function Name : validateDeliverytoStore
		 *   Description : Validate Delivery to Store
		 **********************************************************************************/
		public void validateDeliverytoStore()
		{		
		
			/********************** Validate Delivery Option *************************/
			//validateTextOnControl(ObjectRepository.DELIVERY_OPTION_LABEL, GenericApplicationConstants.DELIVERY_OPTIONS);
			
			//validateElementAttribute( ObjectRepository.DELIVERY_TO_ADDRESS, GenericApplicationConstants.CLASS, VerificationStringConstants.CHECKED);
			
			appDriver.findElement(By.xpath(ObjectRepository.DELIVERY_COLLECT_FROM_STORE)).click();
			
			appDriver.findElement(By.xpath(ObjectRepository.DELIVERY_FINDSTORES_BTN)).click();
			waitForAjaxToLoad();
			/*********************** Click on Continue Button *************************************/ 
			
			clickOnWebElement( ObjectRepository.CLICK_CONTINUE_BTN_DELIVERY);
			
			/********************** Wait for Page Load *********************************************/
			appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);	
		}
		
		/*******************************************************************************	 
		 *   Function Name : selectFromDropdown
		 *   Description : Select Value from dropdown
		 **********************************************************************************/
		public void selectFromDropdown(String elementID, String sSelectValue)
		{
			/* Select Value from Dropdown */		  
			Select valueSelect = new Select(appDriver.findElement(By.id(elementID)));	
			
					
			
				List<WebElement> selectionIter = valueSelect.getOptions();
				for(WebElement selectedOption : selectionIter)
				{
					
					if(selectedOption.getText().contains(sSelectValue))
					{				
					 valueSelect.selectByVisibleText(sSelectValue);
					 //selectedOption.submit();
					
					 break;
					}
			    }  		
			appDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}	
		
		
		/*******************************************************************************	 
		 *   Function Name : validateReviewOrderSection
		 *   Description : Validate Review Order Section
		 **********************************************************************************/
		public void validateReviewOrderSection(Map checkoutDetailsMap)
		{     
			    
			    /************* Click on terms and Conditions Checkbox *************/	
			     //clickOnWebElement( ObjectRepository.CHECK_BOX_TERMS);	
			     appDriver.findElement(By.xpath(ObjectRepository.CHECK_BOX_TERMS)).click();
			     /****************** Scroll to Bottom *********************/
			     //scollPage( "1200");	
			     
			     validateElementDisplayed( ObjectRepository.BTN_RETURN_BASKET);
	          		
				/************* If all the details are correct, click on Place Order *************/				  
				 //clickOnWebElement( ObjectRepository.CHECKOUT_UPGRADE_NOW_BTN);	
				 			 
		}
		
		/*******************************************************************************	 
		 *   Function Name : validateThankYouPage
		 *   Description : Validate Thank you Page
		 **********************************************************************************/

		public void validateThankYouPage()
		{
			 /* Check Page Title */
			validatePageTitle( VerificationStringConstants.UPGRADE_THANK_YOU_PAGE_TITLE);
				
			/* Check for Header Text Match */	
			validateTextOnControl(ObjectRepository.H1, VerificationStringConstants.THANK_YOU_PAGE_HEADER_TITLE);
			
			/* Retrieve Order Reference Number */
			WebElement orderNumberRefLabel = appDriver.findElement(By.xpath(ObjectRepository.ORDER_REF_NUMBER_LABEL));
			if(orderNumberRefLabel.isDisplayed())
			{
				if(orderNumberRefLabel.getText().contains(GenericApplicationConstants.WEB_ORDER))
				{
					String OrderNumber = orderNumberRefLabel.getText().split("-")[1];
					
					Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);;
				}else
				{
					Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
				}
			}
			
		}
		
		
		
		/*******************************************************************************	 
		 *   Function Name : validateFieldAndEnterNumeric
		 *   Description : Enter Value into Text Fields
		 **********************************************************************************/
		public void validateFieldAndEnterNumeric(String sSelectorChoice, String fieldID, String textToEnter, Actions interactions)
		{
			WebElement element = null;
			if(sSelectorChoice.equalsIgnoreCase("ID")){		
			 element = appDriver.findElement(By.id(fieldID));
			}else if(sSelectorChoice.equalsIgnoreCase("Name")){
			 element = appDriver.findElement(By.name(fieldID));
			}
			
			if(element.isEnabled())
			{
				
				element.click();
				/*if(textToEnter.contains("E") && (textToEnter.length() > 20) ){
					textToEnter = (textToEnter.split("E")[0].split("\\.")[0]+textToEnter.split("E")[0].split("\\.")[1]).split("0")[0].trim()+"1";
				}else
				{
				textToEnter = textToEnter.split("E")[0].split("\\.")[0]+textToEnter.split("E")[0].split("\\.")[1];	
				}*/
				
				if(!textToEnter.contains("E")){
				if(textToEnter.length() > 15 ){
					textToEnter = textToEnter.split("0")[0].trim()+"1";
				}}else
				{
					textToEnter = textToEnter.split("E")[0].split("\\.")[0]+textToEnter.split("E")[0].split("\\.")[1];
				}
					
				element.clear();
				
				element.sendKeys(textToEnter);
				//interactions.sendKeys(element, textToEnter);
				
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);			
				appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);			
			}else{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
			
		}
		
		/*******************************************************************************	 
		 *   Function Name : validateFieldAndEnterText
		 *   Description : Enter Value into Text Fields
		 **********************************************************************************/
		public void validateFieldAndEnterText(String sSelectorChoice, String fieldID, String textToEnter, Actions interactions)
		{	
			WebElement element = null;
			if(sSelectorChoice.equalsIgnoreCase("ID")){		
			 element = appDriver.findElement(By.id(fieldID));
			}else if(sSelectorChoice.equalsIgnoreCase("Name")){
			 element = appDriver.findElement(By.name(fieldID));
			}
			else if(sSelectorChoice.equalsIgnoreCase("Xpath")){
				 element = appDriver.findElement(By.xpath(fieldID));
				}
				
				if(element.isEnabled())
				{					
					if(!element.getText().isEmpty()){					
						
					}
					
					
					element.clear();
					element.sendKeys(textToEnter);
				    appDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			}else{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
			
		}
		
		/*******************************************************************************	 
		 *   Function Name : validateElementNotPresent
		 *   Description : Validate whether the web element is present or not by Tag name
		 **********************************************************************************/
	
		public void validateElementNotPresent(String TagName, String value)
		{
			List<WebElement> select_Phone_Compared = appDriver.findElements(By.tagName(TagName));
			Boolean Flag = false;
			for(int i=0;i<select_Phone_Compared.size();i++){
				if(select_Phone_Compared.get(i).getText().toString().contains(value))
					Flag=true;
				else
					Flag=false;
			}
			
			if(Flag==false)
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			else
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}
		
		/*******************************************************************************	 
		 *   Function Name : sortByManufacturer
		 *   Description : Sort List By Manufacturer
		 **********************************************************************************/
		public void sortByManufacturer(String sPhoneManufacturer)
		{		
			/************* Scroll Page *****************/
			scollPage("600");		

			/* Wait for Page Load */
			appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

			/* Click on More Button*/		
			clickOnWebElement(ObjectRepository.BUTTON_MANUFACTURER_MORE);		

			/* Filter phones based on manufacturer */
			List<WebElement> collection_Manufacturer = appDriver.findElements(By.xpath(ObjectRepository.LABEL_MANUFACTURER));

			if(!collection_Manufacturer.isEmpty())	{

				for(WebElement checkElement : collection_Manufacturer){
					if(checkElement.getText().equalsIgnoreCase(sPhoneManufacturer)) {
						checkElement.click();
						break;
					}
				}
			}
			
			waitForAjaxToLoad();
			

			/* Scroll Page */
			scollPage("300");
			
			//appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

			/*Wait for the Ajax call to end and the display of the Phone
			WebDriverWait appWait = new WebDriverWait(appDriver, 20);
			appWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), '"+ sPhoneChecked +"')]"))); */		
		}
		
		/*******************************************************************************	 
		 *   Function Name : selectDesiredPhone
		 *   Description : Select the Desired Phone
		 **********************************************************************************/
		public void selectDesiredItem(String sHardwareSKUID)
		{
			appDriver.switchTo().activeElement();
			boolean bFlag = false;
			
			List<WebElement> select_Phone_Compared;
			int i=0;
			
	        do
	        {
	        	select_Phone_Compared = appDriver.findElements(By.xpath(ObjectRepository.BUTTON_SELECT_PHONE));	
	        	
	        	System.out.println(select_Phone_Compared.size());
			
			/*Check if collection is not Empty*/                                        
			if(select_Phone_Compared.size()!=0)
			{
				
				//for(WebElement selectCompared : select_Phone_Compared )
				for(;i<select_Phone_Compared.size();i++)
				{
				
					System.out.println(sHardwareSKUID);
					System.out.println(select_Phone_Compared.get(i).getAttribute(GenericApplicationConstants.HREF).toString());
					if(select_Phone_Compared.get(i).getAttribute(GenericApplicationConstants.HREF).toString().contains(sHardwareSKUID))
					{
						select_Phone_Compared.get(i).click();
						bFlag = true;
						break;	
					}
				}
	        }
			if(bFlag == false)
		       {
		               if (appDriver.findElement(By.xpath("//a[@id='load-results']")).isDisplayed())
		               {
		            	   appDriver.findElement(By.xpath("//a[@id='load-results']")).click();
		            	   appDriver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		            	   select_Phone_Compared = appDriver.findElements(By.xpath(ObjectRepository.BUTTON_SELECT_PHONE));
		            	   System.out.println(select_Phone_Compared.size());
		            	   
		               }
		           else 
		           {
		               break;
		           }
		       }
			
			
	        }
			while (bFlag == false);
					if(bFlag)
					{
						Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
					}
					else
					{
						Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
					}
	              }
			
		/*******************************************************************************	 
		 *   Function Name : validatePhoneAddedToStepper
		 *   Description : Validate the correct phone was added to the Stepper
		 **********************************************************************************/

		public void validatePhoneAddedToStepper(String sPhoneDisplayName)
		{
			/*Check Phone added to Stepper */
			WebElement ElementsOnStepper = appDriver.findElement(By.xpath(ObjectRepository.ITEM_PHONE_PLAN_ADDED_STEPPER));
	        System.out.println(ObjectRepository.ITEM_PHONE_PLAN_ADDED_STEPPER.toString());
			System.out.println(ElementsOnStepper.findElement(By.xpath(ObjectRepository.STEPPER_BODY)).getText());
			
			if(ElementsOnStepper.findElement(By.xpath(ObjectRepository.STEPPER_HEAD)).getText().contains(GenericApplicationConstants.ITEM_DEVICE) || ElementsOnStepper.findElement(By.xpath(ObjectRepository.STEPPER_BODY)).getText().contains(sPhoneDisplayName) )
			{
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);			
			}else
			{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
		}
		
		/********************************************************************************************************************
		 * Function Name : validatePayMPlansPage
		 *   Description : Validate PayM Plans Page
		 ********************************************************************************************************************/
		public void validatePayMPlansPage()
		{
			/***************** Validate the PayM Plans page *****************************************************/
			validatePageTitle(VerificationStringConstants.UPGRADE_PAGE_TITLE);

			/*******************Check for PayM Plan radio button checked ***************************************/
			//validateElementAttribute( ObjectRepository.PAYM_PLAN_RADIO_BUTTON, GenericApplicationConstants.CLASS, VerificationStringConstants.CLICK+ " " + VerificationStringConstants.CHECKED);
			
			validateTextOnControl(ObjectRepository.H1, VerificationStringConstants.PAYM_PLAN_HEADER_TITLE);
			
			/************************  Validate Stepper has no plans added ***************************************/
			validateTextOnControl(ObjectRepository.PLAN_EMPTY_STEPPER, VerificationStringConstants.EMPTY_PLAN_STEPPER_TEXT);
			
			scollPage("900");
					
			scollPage("400");
		}
		
		/********************************************************************************************************************
		 * Function Name : validatePayMPlanDetailsPage
		 *   Description : Validate PayM Plans Details Page
		 ********************************************************************************************************************/
		public void validatePayMPlanDetailsPage(String planName)
		{
			/***************** Validate the PayM Plans page *****************************************************/
			validatePageTitle(VerificationStringConstants.UPGRADE_PAGE_TITLE);

			/******************* Validate the plan display name ***************************************/
			validateTextOnControl(ObjectRepository.H1, planName);
			
			scollPage("400");
		}
		
		/*******************************************************************************	 
		 *   Function Name : validateLabels
		 *   Description : Validate the C Label Display
		 **********************************************************************************/	
		public String validateLabels(String element)
		{
			String sValue = null;
			WebElement creditCardAccNoCaption = appDriver.findElement(By.xpath(element)); 
			if((creditCardAccNoCaption.isDisplayed()) && (!creditCardAccNoCaption.getText().isEmpty()))
			{
				sValue = creditCardAccNoCaption.getText();
			   Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			}else
			{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
			return sValue;
		}
		
		
		  /*******************************************************************************	 
		 *   Function Name : validateElementAttribute
		 *   Description : Validate Attribute
		 **********************************************************************************/  
	  public void validateElementAttribute(String controlSelector, String attribute, String valueToCheck)
	  {
		  
		  /*Check for Pay M radio button checked */
			WebElement payMRadioBTNElement = appDriver.findElement(By.xpath(controlSelector));
			if(payMRadioBTNElement.isEnabled()){
			
			if(payMRadioBTNElement.getAttribute(attribute).equalsIgnoreCase(valueToCheck))
			{
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			}
			}else
			{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
	  }
	  
	  /*******************************************************************************	 
		 *   Function Name : selectDesiredPlan
		 *   Description : Select the Desired Plan
		 **********************************************************************************/
		public void selectDesiredPlan(String sHardwareSKUID)
		{
			appDriver.switchTo().activeElement();
			boolean bFlag = false;
			
			List<WebElement> select_Phone_Compared;
			int i=0;
			
	        do
	        {
	        	select_Phone_Compared = appDriver.findElements(By.xpath(ObjectRepository.UPGRADE_SELECTPLAN_BUTTON));	
	        	
	        	System.out.println(select_Phone_Compared.size());
			
			/*Check if collection is not Empty*/                                        
			if(select_Phone_Compared.size()!=0)
			{
				
				//for(WebElement selectCompared : select_Phone_Compared )
				for(;i<select_Phone_Compared.size();i++)
				{
				
					System.out.println(sHardwareSKUID);
					System.out.println(select_Phone_Compared.get(i).getAttribute(GenericApplicationConstants.ONCLICK).toString());
					if(select_Phone_Compared.get(i).getAttribute(GenericApplicationConstants.ONCLICK).toString().contains(sHardwareSKUID))
					{
						select_Phone_Compared.get(i).click();
						bFlag = true;
						break;	
					}
				}
	        }
			if(bFlag == false)
		       {
		               if (appDriver.findElement(By.xpath("//a[@id='load-results']")).isDisplayed())
		               {
		            	   appDriver.findElement(By.xpath("//a[@id='load-results']")).click();
		            	   appDriver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		            	   select_Phone_Compared = appDriver.findElements(By.xpath(ObjectRepository.BUTTON_SELECT_PLAN));
		            	   System.out.println(select_Phone_Compared.size());
		            	   
		               }
		           else 
		           {
		               break;
		           }
		       }
			
			
	        }
			while (bFlag == false);
					if(bFlag)
					{
						Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
					}
					else
					{
						Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
					}
	              }

		
		/*******************************************************************************	 
		 *   Function Name : validatePhoneAddedToStepper
		 *   Description : Validate the correct phone was added to the Stepper
		 **********************************************************************************/
		public void validatePlanAddedToStepper(Map planDetailsMap)
		{
			
			/*****Check Phone added to Stepper *****/
			List<WebElement> ElementsOnStepper = appDriver.findElements(By.xpath(ObjectRepository.ITEM_PHONE_PLAN_ADDED_STEPPER));
			
			boolean bFlag = false;

			for(WebElement planStepperElement : ElementsOnStepper)		
			{
				
				
				if(planStepperElement.getText().contains(GenericApplicationConstants.ITEM_PLAN))
				{
					Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);				
					bFlag = true;
						
					
				
				}
			}
			         if(bFlag){
				          Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			         }else{
					      Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			  	     }
		}
		
		
		/*******************************************************************************	 
		 *   Function Name : validateExtrasAndAccessoryPage
		 *   Description : Validate the E&A Page
		 **********************************************************************************/
		public void validateExtrasAndAccessoryPage()
		{
			/*Validate the E&A  page */		
			validatePageTitle( VerificationStringConstants.UPGRADE_PAGE_TITLE);

			/* Check for Header Text Match */
			
			WebElement webPageHeader = appDriver.findElement(By.xpath(ObjectRepository.H1));
			
			if(webPageHeader.isDisplayed()){
			if((webPageHeader.getText().contains(VerificationStringConstants.HEADER_TITLE_EXTRAANDACCESSORIES_UPGRADE)))
			{
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);;
			}else
			{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
			}		

			
		}

		/*******************************************************************************	 
		 *   Function Name : retrieveTextValue
		 *   Description : Retrieves the Text value
		 **********************************************************************************/ 
		public String retrieveTextValue(String selector) {
			String str = null;		
			
			WebElement element = appDriver.findElement(By.xpath(selector));
			
			if(element.isDisplayed()){
				str = element.getText();
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			}else{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}			
			return str;
		}
		
		/*******************************************************************************	 
		 *   Function Name : clickOnQuickView
		 *   Description : Click on QuickView to Quickly view Mobile Device Details
		 **********************************************************************************/
		public void clickOnQuickView(String sPhoneSKU)
		{
			waitForAjaxToLoad();
			Actions interactions = new Actions(appDriver);
			//scollPage( "720");	
			
			
			appDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
//			System.out.println(appDriver.findElement(By.xpath(ObjectRepository.IMG_QUICK_VIEW_START+sPhoneSKU+ObjectRepository.IMG_QUICK_VIEW_END)).getText());
			
			appDriver.findElement(By.xpath(ObjectRepository.IMG_QUICK_VIEW_START+sPhoneSKU+ObjectRepository.IMG_QUICK_VIEW_END)).click();

			
			/************************* Wait for Page Load **************************************************/
			appDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			waitForAjaxToLoad();
		}
		
		/*******************************************************************************	 
		 *   Function Name : validateQuickViewPage
		 *   Description : Validate all the capabilities of Quick View page
		 **********************************************************************************/
		public void validateQuickViewPage(Map backOrderTestCaseMap)
		{
			boolean bFlag = false;
			
			/**********************Validate the name of the phone in quickview *****************************/
			if(appDriver.switchTo().activeElement().findElement(By.xpath(ObjectRepository.HEADER_TEXT_QUICK_VIEW)).getText().contains(backOrderTestCaseMap.get(GenericApplicationConstants.PHONE_DISPLAY_NAME).toString().trim()))
			{
			    bFlag = true;
			}
			else
			{
				bFlag = false;
			}
			
			/********************* Validate QuickView Close icon ********************************************/
			if(appDriver.switchTo().activeElement().findElement(By.xpath(ObjectRepository.CLOSE_DIALOG_LINK)).getText().contains(GenericApplicationConstants.CLOSE))
			{
		        bFlag = true;
		            
			}else
			{
				bFlag = false;
			}
			
					
			/*************************** Wait for Page Load ***************/
			appDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			
			/***************** Validate Image Gallary Slots ************************************************/
			List<WebElement> imageGallary = appDriver.switchTo().activeElement().findElements(By.xpath(ObjectRepository.IMAGE_GALLERY_SLOTS));

			if(!imageGallary.isEmpty())
			{
				for(WebElement imagesinSlot : imageGallary)
				{
					if(imagesinSlot.getAttribute(GenericApplicationConstants.ALT_VALUE).contains(backOrderTestCaseMap.get(GenericApplicationConstants.PHONE_DISPLAY_NAME).toString())){
						bFlag=true;
					}else
					{
						bFlag=false;					
					}
				}
			}
			
			/************************** Validate Device Color ************************************/		
			//System.out.println(appDriver.switchTo().activeElement().findElement(By.className(VerificationStringConstants.CHECKED)).getAttribute("for").toString());
			
			if(appDriver.switchTo().activeElement().findElement(By.className(VerificationStringConstants.CHECKED)).isDisplayed())
			{
				System.out.println("Device color displayed");
				bFlag = true;
			}
			else
			{
				bFlag = false;
			}
			
			if(appDriver.switchTo().activeElement().findElement(By.className(VerificationStringConstants.CHECKED)).getAttribute("for").toString().contains(backOrderTestCaseMap.get(GenericApplicationConstants.PHONE_SKU).toString()))
			{
				System.out.println("Device color icon correct");
				bFlag = true;
			}
			else
			{
				bFlag = false;
			}
			/*
			if(appDriver.switchTo().activeElement().findElement(By.className(VerificationStringConstants.CHECKED)).findElement(By.className("label")).getText().contentEquals(backOrderTestCaseMap.get(GenericApplicationConstants.PHONE_COLOR).toString().trim()))
			{
				System.out.println("Device color verified");
				bFlag = true;
		           
			}else
			{
				bFlag = false;
			}
			*/
			
			
				
			
					
					validateTextOnControl(ObjectRepository.CLOSE_DIALOG_LINK, "Close");
					
					//appDriver.findElement(By.xpath(ObjectRepository.CLOSE_DIALOG_LINK)).click();
		}
		
		
		/*******************************************************************************	 
		 *   Function Name : DriverLoad
		 *   Description : waits for the page to load depending on the browser
		 * @return 
		 **********************************************************************************/
		public void DriverLoad()
		{
			//check for the browser and wait for the page to load 
			if(appBrowser.equalsIgnoreCase("ie"))
				appDriver.manage().timeouts().pageLoadTimeout(GenericApplicationConstants.TIME_DELAY_SECONDS, TimeUnit.SECONDS);
			else
				waitForAjaxToLoad();
			
		}
		
		
}

	
