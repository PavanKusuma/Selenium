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
			
			// firefox path
			System.setProperty("webdriver.firefox.bin", "C:\\Program Files\\Mozilla Firefox\\firefox.exe");
			
			// firefox profile
			FirefoxProfile fp = new FirefoxProfile(new File("D:\\Eclipse IDE\\Automation_FireFox_Profile"));
			
			// Below code accepts security certificates..
			fp.setPreference("setAcceptUntrustedCertificates", "true");
			fp.setAcceptUntrustedCertificates(true);
			fp.setAssumeUntrustedCertificateIssuer(true);
			appDriver = new FirefoxDriver(fp);
					 
			capabilities = DesiredCapabilities.firefox();
			capabilities.setVersion("24");					
			
		}

		if(sBrowserName.equalsIgnoreCase("ie"))		{		
			// set the ie diver path
			File ieFile = new File(System.getProperty("user.dir")+"\\IEDriverServer.exe");
			
			System.setProperty("webdriver.ie.driver", ieFile.getAbsolutePath());
			capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			appDriver = new InternetExplorerDriver(capabilities);
		}
		
		if(sBrowserName.equalsIgnoreCase("chrome"))		{		
			// set the chrome driver path
			File chromeDriver = new File(System.getProperty("user.dir")+"\\chromedriver.exe");
						
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

	public void clearCookies()
	{
		Set<Cookie> siteCookies = appDriver.manage().getCookies();

		for (Cookie siteCookie : siteCookies) {
			
				
			appDriver.manage().deleteCookie(siteCookie);
		}
	}
	
	/********************************************************************************************************************
	 * Function Name : validatePayMPhonesPage
	 *   Description : Validate PayM Phones Page
	 ********************************************************************************************************************/
	public void validatePayMPhonesPage()
	{
		
		/* Check Page Title */
		validatePageTitle(VerificationStringConstants.PAYM_PHONE_HOME_PAGE_TITLE);
		
		/*Check for Pay M radio button checked */		
		//validateElementAttribute( ObjectRepository.PAYM_RADIO_BUTTON, GenericApplicationConstants.CLASS, VerificationStringConstants.CHECKED + " " +VerificationStringConstants.CLICK);
		
		/* Check for Header Text Match */
		validateTextOnControl(ObjectRepository.H1, VerificationStringConstants.PAYM_PHONE_HEADER_TITLE);
		
		/*Check for Text on Empty Stepper */
		validateTextOnControl( ObjectRepository.PHONE_EMPTY_STEPPER, VerificationStringConstants.EMPTY_PHONE_STEPPER_TEXT);				
	}
	
	/********************************************************************************************************************
	 * Function Name : validatePayMPlansPage
	 *   Description : Validate PayM Plans Page
	 ********************************************************************************************************************/
	public void validatePayMPlansPage()
	{
		/***************** Validate the PayM Plans page *****************************************************/
		validatePageTitle(VerificationStringConstants.PAYM_PLANS_HOME_PAGE_TITLE);

		/*******************Check for PayM Plan radio button checked ***************************************/
		//validateElementAttribute( ObjectRepository.PAYM_PLAN_RADIO_BUTTON, GenericApplicationConstants.CLASS, VerificationStringConstants.CLICK+ " " + VerificationStringConstants.CHECKED);
		
		validateTextOnControl(ObjectRepository.H1, VerificationStringConstants.PAYM_PLAN_HEADER_TITLE);
		
		/************************  Validate Stepper has no plans added ***************************************/
		validateTextOnControl(ObjectRepository.PLAN_EMPTY_STEPPER, VerificationStringConstants.EMPTY_PLAN_STEPPER_TEXT);
		
		scollPage("900");
		/********************* Validate Personal Use is default selected ********************************/ 		
		validateElementAttribute(ObjectRepository.LABEL_CONSUMER_USE, GenericApplicationConstants.CLASS, VerificationStringConstants.CLICK+ " " + VerificationStringConstants.CHECKED);
				
		scollPage("400");
	}

	/********************************************************************************************************************
	 * Function Name : validatePayGPlansPage
	 *   Description : Validate PayG Plans Page
	 ********************************************************************************************************************/
	public void validatePayGPlansPage()
	{
		/*Validate the PayM Plans page */
		validatePageTitle( VerificationStringConstants.PAYG_PLANS_HOME_PAGE_TITLE);		
		
		/******************  Check for Header Text Match ***********************************************/
		validateTextOnControl( ObjectRepository.H1, VerificationStringConstants.PAYG_PLAN_HEADER_TITLE);
		
		/*********************** Validate Stepper has no plans added ****************************/
		validateTextOnControl( ObjectRepository.PLAN_EMPTY_STEPPER, VerificationStringConstants.EMPTY_PLAN_STEPPER_TEXT);
	}

	/********************************************************************************************************************
	 * Function Name : validateMBBPage
	 *   Description : Validate MBB Page
	 ********************************************************************************************************************/
	public void validateMBBPage()
	{
		 /************************** Navigate to Shop link in MDD ***************************/		
		  
		  /*********************** Click on MBB Link ***************************/		
		  
			Actions actions = new Actions(appDriver);
			//go to Shop link in MDD
			WebElement menuHoverLink = appDriver.findElement(By.xpath(ObjectRepository.NAVIGATE_TO_MDD_SHOP));
			actions.moveToElement(menuHoverLink);
	
			//navigate to sub link inside MDD
			WebElement subLink = appDriver.findElement(By.xpath(ObjectRepository.NAVIGATE_MBB_DEVICES_LINK));
			actions.moveToElement(subLink);
			//click on sub link inside MDD
			actions.click();
			actions.perform();
			
		/* Check Page Title */
		validatePageTitle(VerificationStringConstants.MBB_DEVICE_TITLE);
		
		/*Check for Pay M radio button checked */		
		//validateElementAttribute( ObjectRepository.PAYM_RADIO_BUTTON, GenericApplicationConstants.CLASS, VerificationStringConstants.CHECKED + " " +VerificationStringConstants.CLICK);
		
		/* Check for Header Text Match */
		validateTextOnControl(ObjectRepository.H1, VerificationStringConstants.MBB_HEADER_TITLE);
		
		/*Check for Text on Empty Stepper */
		validateTextOnControl( ObjectRepository.PHONE_EMPTY_STEPPER, VerificationStringConstants.EMPTY_PHONE_STEPPER_TEXT);				
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
	 *   Function Name : validatePriceFilter
	 *   Description : Validate the price filter from the left hand filters
	 **********************************************************************************/
	public void validatePriceFilter()
	{
		appDriver.switchTo().activeElement();
		boolean bFlag = false;
		int count =0;
		
		List<WebElement> select_Phone_Compared;
		int i=0;
		
		
		/* Click on More Button*/		
		clickOnWebElement(ObjectRepository.BUTTON_PRICE_FILTER_MORE);
		
		//appDriver.findElement(By.xpath(ObjectRepository.PRICE_FILTER)).click();
		
		/* Filter phones based on manufacturer */
		List<WebElement> collection_PriceFilter = appDriver.findElements(By.xpath(ObjectRepository.LABEL_PRICE_FILTER));

		if(!collection_PriceFilter.isEmpty())	{

			for(WebElement checkElement : collection_PriceFilter){
				if(checkElement.getText().trim().equalsIgnoreCase("£10 - £20")) {
					checkElement.click();
					appDriver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
					break;
				}
			}
		}
		
	}
	
	/*******************************************************************************	 
	 *   Function Name : validateProceFilterForPhones
	 *   Description : Validate the prices of the displayed phones after 
	 *   				applying price filter
	 **********************************************************************************/
	public void validatePriceFilterForPhones()
	{
		appDriver.switchTo().activeElement();
		boolean bFlag = false;
		int count =0;
		
		List<WebElement> select_Phone_Pricing;
		int i=0;
	
        do
        {
        	
        	//get the price statement of every phone
        	select_Phone_Pricing = appDriver.findElements(By.xpath(ObjectRepository.PRICE_SECTION_OF_PHONE));	
        	
        	System.out.println(select_Phone_Pricing.size());
		
		/*Check if collection is not Empty*/                                        
		if(select_Phone_Pricing.size()!=0)
		{
			
			//for(WebElement selectCompared : select_Phone_Compared )
			for(;i<select_Phone_Pricing.size();i++)
			{
			
				
				//System.out.println(select_Phone_Compared.get(i).getText());
				System.out.println(select_Phone_Pricing.get(i).getText().substring(select_Phone_Pricing.get(i).getText().lastIndexOf(" ")+2).toString());
				if(Double.valueOf(select_Phone_Pricing.get(i).getText().substring(select_Phone_Pricing.get(i).getText().lastIndexOf(" ")+2))<=20)
				{
					//System.out.println("yes");
					//select_Phone_Compared.get(i).click();
					//bFlag = true;
					count++;
					bFlag = true;
					if(count>select_Phone_Pricing.size()){
						bFlag = false;
						break;
					}
							
				}
			}
        }
		if(bFlag == false)
	       {
	               if (appDriver.findElement(By.xpath("//a[@id='load-results']")).isDisplayed())
	               {
	            	   appDriver.findElement(By.xpath("//a[@id='load-results']")).click();
	            	   appDriver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
	            	   select_Phone_Pricing = appDriver.findElements(By.xpath(ObjectRepository.BUTTON_SELECT_PHONE));
	            	   System.out.println(select_Phone_Pricing.size());
	            	   
	               }
	           else 
	           {
	               break;
	           }
	       }
		
		
        }
		while (count>select_Phone_Pricing.size());
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
			waitForAjaxToLoad();
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
        	select_Phone_Compared = appDriver.findElements(By.xpath(ObjectRepository.BUTTON_SELECT_PLAN));	
        	
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
			waitForAjaxToLoad();
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
	/*
    public void selectDesiredItem(String sHardwareSKUID)
    {
    int test =0;
    List<WebElement> select_Phone_Compared = appDriver.findElements(By.xpath(ObjectRepository.BUTTON_SELECT_PLAN));
           
    do
    {

    	appDriver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		                                       
		if(!select_Phone_Compared.isEmpty())
		{
		      for(WebElement selectCompared : select_Phone_Compared )
		      {
		            System.out.println("-----" + selectCompared.getAttribute("onclick"));
		             if(selectCompared.getAttribute("onclick").contains(sHardwareSKUID))
		            {
		                 test++;
		                 System.out.println("Done");
		                  selectCompared.click();
		                   break;
		            }                             
		      }
		
		if(test == 0)
		{
		       if (appDriver.findElement(By.xpath("//a[@id='load-results']")).isDisplayed())
		       {
		      appDriver.findElement(By.xpath("//a[@id='load-results']")).click();
		appDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			  select_Phone_Compared = appDriver.findElements(By.xpath(ObjectRepository.BUTTON_SELECT_PLAN));
		
		       }
		   else 
		   {
		       break;
		   }
		}
} }while (test== 0);
appDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
}
*/

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
	 *   Function Name : addItemsToCompare
	 *   Description : Add Items to Comparison page
	 **********************************************************************************
	 */
	public void addItemsToCompare(String sAttribute, String[] skuIDS )
	{
		List<WebElement> collection_Link_AddToCompare= appDriver.findElements(By.xpath(ObjectRepository.LINK_ADD_TO_COMPARE)); 
		//int iNoOfPhonesFiltered = collection_Link_AddToCompare.size();
		

		/*************** Check filtered result count ************************************************/			
		//String labelText = validateLabels(ObjectRepository.LABEL_COUNT_OF_FILTERED_ITEMS);
		

		/*Check if this is not an blank Label 
		if(!labelText.equalsIgnoreCase(""))	{
			
				if(labelText.split(" ")[1].contains(String.valueOf(iNoOfPhonesFiltered))){
					Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
				}else{
					Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
				}				
		}*/

		/*Check if collection is not Empty */					  
		if(!collection_Link_AddToCompare.isEmpty())	
		{
			for(WebElement linkElement : collection_Link_AddToCompare){
				
				for(String skuIDAttribute :skuIDS)
				{
					if(linkElement.getAttribute(sAttribute).equalsIgnoreCase(skuIDAttribute))
					{
						linkElement.click();	
						waitForAjaxToLoad();
					}
				}}
		}
	}

	/******************************************************************************************
	 *  Check for values Added to Comparison page
	 ******************************************************************************************/
	public void validateItemsAddedForComparison(String[] sItemValue)
	{
		/*Check Item added to Compare Section */
		List<WebElement> collection_Item_Added_Comparison = appDriver.findElements(By.xpath(ObjectRepository.ITEM_ADDED_COMPARISON));
		boolean bFlag = false;
		int iCount = 0;
		/*Check if collection is not Empty */
		if(!collection_Item_Added_Comparison.isEmpty())
		{
			
			for(WebElement comparedItem : collection_Item_Added_Comparison)
			{
				
				for(String skuAttribute :sItemValue)
				{
					if(comparedItem.getAttribute(GenericApplicationConstants.ALT_VALUE).contains(skuAttribute))
					{
						iCount = iCount+1;
						bFlag = true;
						break;
					}
				}
			}
		}

		

		if(bFlag = true){				
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);			
		}else{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}
	}

	/* *******************************************************************************
	 *   Click on Compare Now button  
	 ************************************************************************************/
	public void clickCompareNow(String sButtonAttribute)
	{
		/************ scroll to top of Page ************************/		
		scollPage("200");

		/**************** Click on Compare Now Button **************/               
		WebElement compareButtonElem = appDriver.findElement(By.xpath(sButtonAttribute));
		
		if(compareButtonElem.isEnabled())
		{
			Assert.assertTrue(true, GenericApplicationConstants.OBJECT_EXISTS);

			compareButtonElem.click();            	   
		}else
		{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}

		/*************************** Wait for Page Load ***************/
		appDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	/***************************************************************************************
	 *  Check for values with Comparison page
	 ***************************************************************************************/
	public void validateComparePage( )
	{
		//validatePageTitle( VerificationStringConstants.PAGE_TITLE_COMPARE_PHONES);		

		/*Check for Title on Comparison page */    
		validateTextOnControl(ObjectRepository.H1, VerificationStringConstants.HEADER_TITLE_COMPARE_PHONES);
	
		/* Check for Back to previous page  Link */
		 validateElementDisplayed( ObjectRepository.LINK_BACK_PREVIOUS_PAGE);		
	}
	
	/*******************************************************************************	 
	 *   Function Name : validatePackageAddedForComparison
	 *   Description : Validate Package added
	 **********************************************************************************/
	public void validatePackageAddedForComparison(String sBundleDisplayName)
	{
		/* validate package name added to Comparelist */
		List<WebElement> packageComparedCollection = appDriver.findElements(By.xpath(ObjectRepository.PACKAGE_ADDED_FOR_COMPARISON));
		if(!packageComparedCollection.isEmpty())
		{
			for(WebElement packageadded : packageComparedCollection)
			{
				
				
				
				if(packageadded.getText().equalsIgnoreCase(sBundleDisplayName.trim()))
				{
					Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
				}else
				{
					Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
				}
			}
		}

		validateElementDisplayed( ObjectRepository.PACKAGE_ALT+sBundleDisplayName+ObjectRepository.COMPLETE_XPATH_SELECTOR);
		
		/*WebElement imgElement = appDriver.findElement(By.xpath(ObjectRepository.PACKAGE_ALT+sBundleDisplayName+ObjectRepository.COMPLETE_XPATH_SELECTOR));
		if(imgElement.isDisplayed())
		{
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
		}else
		{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}*/
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
						
			   
/*
				if(planStepperElement.findElement(By.xpath(ObjectRepository.STEPPER_BODY+ObjectRepository.UL+ObjectRepository.SPAN)).getText().contains(planDetailsMap.get(GenericApplicationConstants.PLAN_TERM).toString()))
			    {
					Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);	
				}else
				{
					Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
				}
			
				
			    
				
	
				if(planStepperElement.findElement(By.xpath(ObjectRepository.STEPPER_BODY+ObjectRepository.UL+ObjectRepository.LI)).getText().contains(GenericApplicationConstants.COMMA + planDetailsMap.get(GenericApplicationConstants.PLAN_TALKTIME).toString() +GenericApplicationConstants.COMMA + planDetailsMap.get(GenericApplicationConstants.PLAN_TEXT).toString() + GenericApplicationConstants.COMMA + planDetailsMap.get(GenericApplicationConstants.PLAN_DATA).toString()))
				{
					Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);	
				}else
				{
					Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
				}
				
		*/		
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
		validatePageTitle( VerificationStringConstants.PAGE_TITLE_EXTRAANDACCESSORIES);

		/* Check for Header Text Match */
		
		WebElement webPageHeader = appDriver.findElement(By.xpath(ObjectRepository.H1));
		if(webPageHeader.isDisplayed()){
		if((webPageHeader.getText().contains(VerificationStringConstants.HEADER_TITLE_EXTRAANDACCESSORIES))||(webPageHeader.getText().contains(VerificationStringConstants.HEADER_TITLE_FREEBEE_EXTRAANDACCESSORIES)))
		{
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);;
		}else
		{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}
		}		

		/*Validate Stepper has no Extras added */
		validateTextOnControl(ObjectRepository.EXTRAANDACC_EMPTY_STEPPER, VerificationStringConstants.EMPTY_EXTRAS_STEPPER_TEXT);
	}

	/*******************************************************************************	 
	 *   Function Name : validateExtrasDisplayed
	 *   Description : Validate the correctness of the Extra Displayed
	 **********************************************************************************/
	public void validateExtrasDisplayed(Map phoneDetailsMap)
	{		
		int i=0;
		/*Check Phone added to Stepper */
		List<WebElement> extrasGroupDisplayed = appDriver.findElements(By.xpath(ObjectRepository.EXTRA_GROUP_HEADER_NAME));

		for(WebElement extraElement : extrasGroupDisplayed){			
			
			if(extraElement.getText().contains(phoneDetailsMap.get("Extra Group").toString()))
			{		
				validateTextOnControl(ObjectRepository.DISPLAY_EXTRA_DESC+i+ObjectRepository.COMPLETE_XPATH_SELECTOR+ObjectRepository.EXTRA_DISPLAY_NAME, phoneDetailsMap.get("Extra DisplayName").toString());
				
				validateTextOnControl(ObjectRepository.DISPLAY_EXTRA_DESC+i+ObjectRepository.COMPLETE_XPATH_SELECTOR+ObjectRepository.EXTRA_DISPLAY_DESCRIPTION, phoneDetailsMap.get("Extra Description").toString());						
				
				validateTextOnControl(ObjectRepository.DISPLAY_EXTRA_DESC+i+ObjectRepository.COMPLETE_XPATH_SELECTOR+ObjectRepository.EXTRA_PRICE_DISPLAYED, phoneDetailsMap.get("Extra_Price").toString());
			}
			
			if(extraElement.getText().contains(phoneDetailsMap.get("Topup Group").toString()))
			{		
				validateTextOnControl(ObjectRepository.DISPLAY_EXTRA_DESC+i+ObjectRepository.COMPLETE_XPATH_SELECTOR+ObjectRepository.TOPUP_DISPLAY_NAME, phoneDetailsMap.get("Topup DisplayName").toString());
				
				validateTextOnControl(ObjectRepository.DISPLAY_EXTRA_DESC+i+ObjectRepository.COMPLETE_XPATH_SELECTOR+ObjectRepository.EXTRA_DISPLAY_DESCRIPTION, phoneDetailsMap.get("Topup Description").toString());						
			}
			i=i+1;		
		}
	}

	/*******************************************************************************	 
	 *   Function Name : addIntendedExtra
	 *   Description : Add the Intended Extra 
	 **********************************************************************************/
	public void addIntendedExtra(String sExtraSKUID)
	{
		List<WebElement> add_Extra_Button = appDriver.findElements(By.xpath(ObjectRepository.ADD_EXTRA_BUTTON));
		
		boolean bFlag = false;

		/*Check if collection is not Empty */                                       
		if(!add_Extra_Button.isEmpty())
		{
			for(WebElement extraDisplayed : add_Extra_Button )
			{
				

				if(extraDisplayed.getAttribute(GenericApplicationConstants.ID).contains(sExtraSKUID))
				{
					
					extraDisplayed.click();
					bFlag=true;
					break;
				}
			}
		}
		
		if(bFlag)
		{
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
		}else{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}		

		WebElement removeExtraElement = appDriver.findElement(By.xpath(ObjectRepository.LINK_REMOVE_EXTRA));
		if(removeExtraElement.isDisplayed())
		{
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);

		}else
		{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}
	}

	/*******************************************************************************	 
	 *   Function Name : validateExtraAddedToStepper
	 *   Description : Validate the correct Extra was added to the Stepper
	 **********************************************************************************/
	public void validateExtraAddedToStepper(String[] sExtraName)
	{
		/*Check Phone added to Stepper */
		List<WebElement> extrasOnStepper = appDriver.findElements(By.xpath(ObjectRepository.EXTRAANDACC_EMPTY_STEPPER+ObjectRepository.PARENT_ELEMENT + ObjectRepository.STEPPER_BODY + ObjectRepository.LI));
						
		boolean bFlag = false;		
		for(WebElement extras : extrasOnStepper)		
		{ 	
			
			for(int i=0; i<sExtraName.length; i++)
			{	
				
				if(extras.getText().contains(sExtraName[i]))
				{
					bFlag=true;
				}else
				{
					bFlag=false;
				}
		    }
		}
		if(bFlag)
		{
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
		}else
		{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}
	}

	/*******************************************************************************	 
	 *   Function Name : validateTotalCostOnStepper
	 *   Description : Validate the correct cost of items added is displayed on the Stepper
	 **********************************************************************************/
	public void validateTotalCostOnStepper(Map<Object, String> packageComponentAttributes)
	{
		List<WebElement> costOnStepperElement = appDriver.findElements(By.xpath(ObjectRepository.COST_OF_ITEMS_DISPLAYED_ON_STEPPER));

		for(WebElement oneOffMonthlyCost : costOnStepperElement)
		{
			if(!oneOffMonthlyCost.getText().isEmpty() || !oneOffMonthlyCost.getText().equals(" "))			 

			{
				if(oneOffMonthlyCost.getText().contains(GenericApplicationConstants.CONTRACT_MONTHLY))
				{
					String[] sMontlyComponent = oneOffMonthlyCost.getText().split("£");

					int planPrice = Integer.valueOf(String.valueOf(packageComponentAttributes.get("planMonthlyCost")));
					int extrasPrice = Integer.valueOf(String.valueOf(packageComponentAttributes.get("extraMonthlyCost")));

					if(sMontlyComponent[1].trim().equalsIgnoreCase(String.valueOf(planPrice+extrasPrice)))
					{

					}
				}
			}
		}
	}

	/*******************************************************************************	 
	 *   Function Name : clickOnQuickView
	 *   Description : Click on QuickView to Quickly view Mobile Device Details
	 **********************************************************************************/
	public void clickOnQuickView(String sDeviceDisplayName)
	{
		waitForAjaxToLoad();
		Actions interactions = new Actions(appDriver);
		//scollPage( "720");	
		
		List<WebElement> quickViewBtns = appDriver.findElements(By.xpath(ObjectRepository.IMG_QUICK_VIEW));

		if(!quickViewBtns.isEmpty())
		{
			for(WebElement quickView : quickViewBtns)
			{
				
				if(quickView.getAttribute(GenericApplicationConstants.ALT_VALUE).contains(sDeviceDisplayName))
				{	
					appDriver.switchTo().activeElement();
					
					interactions.click(quickView).build().perform();
					
					//interactions.moveToElement(quickView).build().perform();
				//	interactions.clickAndHold(quickView).build().perform();
					
					//interactions.sendKeys(Keys.ENTER);
					
					//WebElement quickViewButton = appDriver.findElement(By.xpath(ObjectRepository.BTN_QUICK_VIEW));
					//appDriver.switchTo().activeElement();

					//quickViewButton.click();
					
				}
			}
		}
		/************************* Wait for Page Load **************************************************/
		appDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		waitForAjaxToLoad();
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
		
		if(appDriver.switchTo().activeElement().findElement(By.className(VerificationStringConstants.CHECKED)).findElement(By.className("label")).getText().contentEquals(backOrderTestCaseMap.get(GenericApplicationConstants.PHONE_COLOR).toString().trim()))
		{
			System.out.println("Device color verified");
			bFlag = true;
	           
		}else
		{
			bFlag = false;
		}
		
		
	
		/******************************** Validate Full Details Button *************************************************/				
		validateTextOnControl(ObjectRepository.QUICKVIEW_FULL_DETAILS, GenericApplicationConstants.FULL_DETAILS);
		
		validateTextOnControl(ObjectRepository.QUICKVIEW_FULL_DETAILS+ObjectRepository.SPAN, backOrderTestCaseMap.get("Phone Display Name").toString().trim());
		
		WebElement fulldetailsBtn = appDriver.findElement(By.xpath(ObjectRepository.QUICKVIEW_FULL_DETAILS+ObjectRepository.SPAN));
		
			if(fulldetailsBtn.getText().contains(backOrderTestCaseMap.get("Phone Display Name").toString()))
			{
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			}else
			{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);					
			}
			
			/******************************** Validate Select Phone Button *************************************************/				
			//validateTextOnControl(ObjectRepository.QUICKVIEW_SELECT_BUTTON, GenericApplicationConstants.FULL_DETAILS);
			
			validateTextOnControl(ObjectRepository.QUICKVIEW_SELECT_BUTTON+ObjectRepository.SPAN, backOrderTestCaseMap.get("Phone Display Name").toString().trim());
			System.out.println("yes");
			WebElement fulldetailsBtn1 = appDriver.findElement(By.xpath(ObjectRepository.QUICKVIEW_SELECT_BUTTON+ObjectRepository.SPAN));
			
				if(fulldetailsBtn1.getText().contains(backOrderTestCaseMap.get("Phone Display Name").toString()))
				{
					Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
				}else
				{
					Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);					
				}	
			
				
				validateTextOnControl(ObjectRepository.CLOSE_DIALOG_LINK, "Close");
				
				appDriver.findElement(By.xpath(ObjectRepository.CLOSE_DIALOG_LINK)).click();
	}
	
	
	/*******************************************************************************	 
	 *   Function Name : validate View as section
	 *   Description : Validate all the View as section : Grid view and list view
	 **********************************************************************************/
	public void validateViewAsSection()
	{
		
		/**********************Validate the name of the phone in quickview *****************************/
		if(appDriver.switchTo().activeElement().findElement(By.xpath(ObjectRepository.VIEW_AS_SECTION_LIST)).isDisplayed())
		{
			System.out.println("List view button present");
		}
		if(appDriver.switchTo().activeElement().findElement(By.xpath(ObjectRepository.VIEW_AS_SECTION_GRID)).isDisplayed())
		{
			System.out.println("Grid view button present");
		}
		
		/********************** Click on list view button ****************************/
		appDriver.switchTo().activeElement().findElement(By.xpath(ObjectRepository.VIEW_AS_SECTION_LIST)).click();
		/*************************** Wait for Page Load ***************/
		appDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		
		/********************** Click on grid view button ****************************/
		appDriver.switchTo().activeElement().findElement(By.xpath(ObjectRepository.VIEW_AS_SECTION_GRID)).click();
		/*************************** Wait for Page Load ***************/
		appDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		
		
	}
	
	/*******************************************************************************	 
	 *   Function Name : validateCostOnStepper
	 *   Description : Validate all the Cost on the Final Stepper
	 **********************************************************************************/
	public ArrayList<String> validateCostOnStepper()
	{
		ArrayList costPriceOnStepper = new ArrayList();

		List<WebElement> priceOnStepper = appDriver.findElements(By.xpath(ObjectRepository.PRICE_TOTAL_STEPPER));
		for(WebElement priceElement : priceOnStepper)
		{
			if(priceElement.getText().contains(GenericApplicationConstants.MONTHLY))
			{
				WebElement monthlyCost = priceElement.findElement(By.xpath(ObjectRepository.COST_ON_STEPPER));			

				costPriceOnStepper.add(1, monthlyCost.getText());
			}

			if(priceElement.getText().contains(GenericApplicationConstants.ONE_OFF))
			{
				WebElement oneOffCost = priceElement.findElement(By.xpath(ObjectRepository.COST_ON_STEPPER));				

				costPriceOnStepper.add(2, oneOffCost.getText());
			}
		}
		
		return costPriceOnStepper;		
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
				System.out.println(element.getText());
				if(!element.getText().isEmpty()){					
					
				}
			    waitForAjaxToLoad();
				//appDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
				element.clear();
				element.sendKeys(textToEnter);
				
			    appDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		}else{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
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
		System.out.println(element.getText());
		if(element.isEnabled())
		{
			
			//element.click();
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
	 *   Function Name : Validate v
	 *   Description : Enter Value into Text Fields
	 **********************************************************************************/
	public void validateTextFieldValues(String fieldID, String valueToCheck)
	{
			
		WebElement element = appDriver.findElement(By.id(fieldID));
		if(element.isDisplayed())
		{
			if(element.getAttribute(GenericApplicationConstants.VALUE).contains(valueToCheck))
			{
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			}else
			{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
		}
	}
	
	
	/*******************************************************************************	 
	 *   Function Name : Validate v
	 *   Description : Enter Value into Text Fields
	 **********************************************************************************/
	public void validateNumericFieldValues(String fieldID, String valueToCheck)
	{
		WebElement element = appDriver.findElement(By.id(fieldID));
		if(element.isDisplayed()){			
			
			valueToCheck = valueToCheck.split("E")[0].split("\\.")[0]+valueToCheck.split("E")[0].split("\\.")[1];
			
			
			
			if(element.getAttribute(GenericApplicationConstants.VALUE).contains(valueToCheck))
			{
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			}else
			{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
		}
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
	 *   Function Name : selectFromDropdown using xpath
	 *   Description : Select Value from drop down
	 **********************************************************************************/
	public void selectFromDropdownUsingXpath(String xpath)
	{
		/* Select Value from Drop down */		  
		Select valueSelect = new Select(appDriver.findElement(By.xpath(xpath)));	
		
		/* Select all the values present in the drop down */
			List<WebElement> selectionIter = valueSelect.getOptions();
			
			for(int i=0;i<selectionIter.size();i++){
				System.out.println(i);
				valueSelect.selectByIndex(i);
				appDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			}
			/*
			for(WebElement selectedOption : selectionIter)
			{
				if(selectedOption.getText().contains(sSelectValue))
				{				
				 valueSelect.selectByVisibleText(sSelectValue);
				 //selectedOption.submit();
				
				 break;
				}
		    }  		*/
		appDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	
	/*******************************************************************************	 
	 *   Function Name : selectFromDropdown
	 *   Description : selectFromDropdown using xpath and select value
	 **********************************************************************************/
	public void selectFromDropdownXpathandValue(String ddxpath, String textToSelect)
	{	/* Select Value from Dropdown */		  
		Select valueSelect = new Select(appDriver.findElement(By.xpath(ddxpath)));	
		
		List<WebElement> selectionIter = valueSelect.getOptions();
		
		System.out.println(selectionIter.size());
		for(WebElement selectedOption : selectionIter)
		{
			
			if(selectedOption.getText().contains(textToSelect))
			{				
			 valueSelect.selectByVisibleText(textToSelect);
			 selectedOption.click();
			 //selectedOption.submit();
			
			 break;
			}
	    }  		
		appDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	
	}
	
	/*******************************************************************************	 
	 *   Function Name : validatePayGPhonesPage
	 *   Description : Select Value from dropdown
	 **********************************************************************************/
	public void validatePayGPhonesPage(Map checkoutDetailsMap)	{
			 		 

		  /************************** Navigate to Shop link in MDD ***************************/		
		  
		  /*********************** Click on PAYG Phone Link ***************************/		
		  
			Actions actions = new Actions(appDriver);
			//go to Shop link in MDD
			WebElement menuHoverLink = appDriver.findElement(By.xpath(ObjectRepository.NAVIGATE_TO_MDD_SHOP));
			actions.moveToElement(menuHoverLink);
	
			//navigate to sub link inside MDD
			WebElement subLink = appDriver.findElement(By.xpath(ObjectRepository.NAVIGATE_TO_PAYG_LINK_MDD));
			actions.moveToElement(subLink);
			//click on sub link inside MDD
			actions.click();
			actions.perform();
	

		/************************ Check Page Title ****************************/
		validatePageTitle( VerificationStringConstants.PAYG_PHONE_HOME_PAGE_TITLE);		
		
		/**************** Check for Header Text Match **********************************/
		validateTextOnControl(ObjectRepository.H1, VerificationStringConstants.PAYG_PHONE_HEADER_TITLE);
		
		/*********Check for Text on Empty Stepper *********************/
		validateTextOnControl(ObjectRepository.PHONE_EMPTY_STEPPER, VerificationStringConstants.EMPTY_PHONE_STEPPER_TEXT);
	}

	/*******************************************************************************	 
	 *   Function Name : validateFreebeeAddedToStepper
	 *   Description : Validate the correct Freebee was added to the Stepper
	 **********************************************************************************/
	public void validateFreebeeAddedToStepper(String freeBeePlanName){
		
		/*****Check Phone added to Stepper ****/
		List<WebElement> ElementsOnStepper = appDriver.findElements(By.xpath(ObjectRepository.ITEM_PHONE_PLAN_ADDED_STEPPER));
          
		if(!ElementsOnStepper.isEmpty()){
		for(WebElement planStepperElement : ElementsOnStepper)		
		{
			
			if(planStepperElement.getText().contains(GenericApplicationConstants.ITEM_PLAN))			{	
				

			if(planStepperElement.findElement(By.xpath(ObjectRepository.STEPPER_BODY+ObjectRepository.UL+ObjectRepository.LI)).getText().contains(freeBeePlanName))
		    {
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);	
			}else
			{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
			}
		}
	   }
	}

	/*******************************************************************************	 
	 *   Function Name : validateAndSelectTopUp
	 *   Description : Validate the correct Topup is displayed
	 **********************************************************************************/
	public void validateAndSelectTopUp(Map payGDetails)
	{
		int topUpCount;
		WebElement topUpElement = appDriver.findElement(By.xpath(ObjectRepository.FREEBEE_TOPUP_DISPLAYED));
		if(topUpElement.isDisplayed())
		{
			String topupText = topUpElement.getText();
			topUpCount= Integer.parseInt(topupText.split("//( ")[1].split("//)")[0]);
			
		}

		/* Validate Topup name */
		WebElement topUpDisplayed = appDriver.findElement(By.xpath(ObjectRepository.FREEBEE_TOPUP_DISPLAY_NAME));
		if(topUpDisplayed.isDisplayed())
		{
			String topupName = topUpDisplayed.getText();
			if(topupName.contains(payGDetails.get("TopUpName").toString()))
			{			   
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			}else
			{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
		}

		/* Validate Topup name */
		WebElement topUpDescDisplayed = appDriver.findElement(By.xpath(ObjectRepository.FREEBEE_TOPUP_DISPLAY_TEXT));
		if(topUpDescDisplayed.isDisplayed())
		{
			String topupName = topUpDescDisplayed.getText();
			if(topupName.contains(payGDetails.get("TopUpDesc").toString()))
			{			   
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			}else
			{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
		}		  

		/*Select Topup */		   
		selectFromDropdown(GenericApplicationConstants.SELECT_TOPUP,payGDetails.get("TopUpCost").toString());

		/*Click on Add Topup Button */		   
		WebElement addTopUpDisplayed = appDriver.findElement(By.xpath(ObjectRepository.ADD_TOPUP_DISPLAYED));
		if(topUpDescDisplayed.isDisplayed())
		{
			topUpDescDisplayed.click();
		}
	}

	/*******************************************************************************	 
	 *   Function Name : clickOnContinueToBasketOnStepper
	 *   Description : Click on Continue To Basket Button on Stepper
	 **********************************************************************************/	
	public void clickOnContinueToBasketOnStepper()
	{
		/*Continue to Basket */
		clickOnWebElement( ObjectRepository.CONT_BASKET_BUTTON_STEPPER);
		
		/* Wait for Page Load */
		appDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	/*******************************************************************************	 
	 *   Function Name : validatePrepayAddressDetailsOnCheckout
	 *   Description : Validate Prepay User Address Details
	 **********************************************************************************/		
	public void validatePrepayAddressDetailsOnCheckout(Map checkoutMap)
	{		
		//validateTextOnControl(ObjectRepository.PREPAY_BILLING_ADDRESS_LABEL, GenericApplicationConstants.BILLING_ADDRESS_DETAILS);
		
		//validateTextOnControl(ObjectRepository.PREPAY_BILLING_ADDRESS_TEXT, GenericApplicationConstants.BILLING_ADDRESS);
				
		validateElementAttribute( ObjectRepository.PREPAY_BILLING_ADDRESS_POST_CODE, GenericApplicationConstants.VALUE, checkoutMap.get(GenericApplicationConstants.POSTCODE).toString().trim());
		
		//validateElementDisplayed( ObjectRepository.FIND_ADDRESS_BUTTON);		

		validateElementAttribute( ObjectRepository.PREPAY_BILLING_ADDRESS_FLAT_NO, GenericApplicationConstants.VALUE, checkoutMap.get(GenericApplicationConstants.FLAT_NUMBER).toString().trim());
			
		validateElementAttribute( ObjectRepository.PREPAY_BILLING_ADDRESS_HOUSE_NO, GenericApplicationConstants.VALUE, checkoutMap.get(GenericApplicationConstants.HOUSE_NUMBER).toString().trim());

		validateElementAttribute( ObjectRepository.PREPAY_BILLING_ADDRESS_HOUSE_NAME, GenericApplicationConstants.VALUE, checkoutMap.get(GenericApplicationConstants.HOUSE_NAME).toString().trim());
				
		//validateTextOnControl(ObjectRepository.PREPAY_BILLING_ADDRESS_STREET, checkoutMap.get(GenericApplicationConstants.STREET).toString());

		validateElementAttribute( ObjectRepository.PREPAY_BILLING_ADDRESS_TOWN, GenericApplicationConstants.VALUE, checkoutMap.get(GenericApplicationConstants.TOWN).toString().trim());
		
		//validateTextOnControl(ObjectRepository.PREPAY_BILLING_ADDRESS_COUNTY, checkoutMap.get(GenericApplicationConstants.COUNTY).toString());
		
		
	}

	/*******************************************************************************	 
	 *   Function Name : validateCheckoutPage
	 *   Description : Validate Checkout Page details
	 **********************************************************************************/	

	public void validateCheckoutPage()
	{    
		validatePageTitle( VerificationStringConstants.CHECKOUT_PAGE_TITLE);
		
		/*********************  Check for Header Text Match ******************************************/
         validateTextOnControl(ObjectRepository.H1, VerificationStringConstants.CHECKOUT_PAGE_HEADER_TITLE);
		
		/************************* Validate BreadCrumb *************************************************/
        // validateTextOnControl(ObjectRepository.BREAD_CRUMB_TEXT, GenericApplicationConstants.LOCATION);
		
		/*Validate BreadCrumb Navigation */
		List<WebElement> breadCrumbNavigation = appDriver.findElements(By.xpath(ObjectRepository.BREAD_CRUMB_NAVIGATION_PATH_TEXT));
		for(WebElement navigationElement : breadCrumbNavigation)
		{
			if(navigationElement.getText().contains(GenericApplicationConstants.HOME) || navigationElement.getText().contains(GenericApplicationConstants.CHECKOUT))
			{
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			}else
			{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}
		}	  
	}
	/*******************************************************************************	 
	 *   Function Name : validateCheckoutPage
	 *   Description : Validate Checkout Page details
	 **********************************************************************************/	
	

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
	 *   Function Name : clickOnContinueButton
	 *   Description : Click on Continue Button
	 **********************************************************************************/
	public void clickOnContinueButton(String additionalParam)
	{  
		List<WebElement> continueButtons = appDriver.findElements(By.xpath(ObjectRepository.CLICK_CONTINUE_BTN));
		if(!continueButtons.isEmpty()){
		if(continueButtons.size() ==1){
			clickOnWebElement( ObjectRepository.CLICK_CONTINUE_BTN);
		}else{
			clickOnWebElement( additionalParam+ObjectRepository.CLICK_CONTINUE_BTN);
		}}
				
						
		/* Wait for Page Load */
		appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);		   
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
	 *   Function Name : validatePaymentDetails
	 *   Description : Validate Payment Options
	 **********************************************************************************/
	public void validatePaymentDetails(Map checkOutDetails)
	{
		
		validateTextOnControl(ObjectRepository.PAYMENT_DETAILS_LABEL, GenericApplicationConstants.PAYMENT_DETAILS);
		
		/****************** validate Card Expiry **********************************************************/
		validateElementAttribute(ObjectRepository.PAYMENT_CARD_OPTION, GenericApplicationConstants.CLASS, VerificationStringConstants.CHECKED);
			
		scollPage( "900");
		
		/********************** Wait for Page Load *********************************************/
		appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);	
		
		
		/*********************** Pay with new Card ********************************************/	
			
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
			 clickOnWebElement( ObjectRepository.PLACE_ORDER_BTN);	
			 			 
	}
	
	/*******************************************************************************	 
	 *   Function Name : validatePackageInCart
	 *   Description : Validate Package details in Review Order Section
	 **********************************************************************************/
	
public void validatePackageInCart(Map checkoutDetailsMap)
{	
	boolean bFlag = false;
	
	List<WebElement> packageDetails = appDriver.findElements(By.xpath(ObjectRepository.ORDER_PACKAGE_DETAILS));    
    if(!packageDetails.isEmpty())
    {
    	for(WebElement elementInPackage :packageDetails)
    	{    		
	    		if(elementInPackage.getText().contains(checkoutDetailsMap.get(GenericApplicationConstants.PHONE_DISPLAY_NAME).toString().trim()))     				
	    		{
	    			bFlag=true;
	    			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
	    		}else if((checkoutDetailsMap.get(GenericApplicationConstants.TOPUP_NAME).toString().trim())!=null && (!checkoutDetailsMap.get(GenericApplicationConstants.TOPUP_NAME).toString().trim().isEmpty()))
	    		{
	    			if(elementInPackage.getText().contains(checkoutDetailsMap.get(GenericApplicationConstants.TOPUP_NAME).toString()))
		    			{
	    				bFlag=true;
		    			  Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
		    			}
	    		}else if((checkoutDetailsMap.get(GenericApplicationConstants.EXTRA_NAME).toString().trim())!=null && (!checkoutDetailsMap.get(GenericApplicationConstants.EXTRA_NAME).toString().trim().isEmpty())){   			
	    		
	    			if(elementInPackage.getText().contains(checkoutDetailsMap.get(GenericApplicationConstants.EXTRA_NAME).toString()))
						{
	    				bFlag=true;
					     Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
						}
	    			}
	    	
    		if(checkoutDetailsMap.get(GenericApplicationConstants.PLAN_DISPLAY_NAME)!=null){
		    	if(checkoutDetailsMap.get(GenericApplicationConstants.PLAN_TERM)!=null){
		    		if(checkoutDetailsMap.get(GenericApplicationConstants.SIM_ONLY_TYPE)!=null){
		    			if(elementInPackage.getText().contains(checkoutDetailsMap.get(GenericApplicationConstants.PLAN_DISPLAY_NAME).toString())){
		    				bFlag=true;
							Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
		    			}
					      }else if(elementInPackage.getText().contains(checkoutDetailsMap.get(GenericApplicationConstants.PLAN_TERM).toString())) {
								bFlag=true;
								Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);					    	  
					      }
				}else if(elementInPackage.getText().contains(checkoutDetailsMap.get(GenericApplicationConstants.PLAN_DISPLAY_NAME).toString())) {
						bFlag=true;
						Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
				}    	    			
    	   }
       }
   }        
	    if(bFlag){
	    	Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
	    }else{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}	
    }

/*******************************************************************************	 
 *   Function Name : validateDeliveryAddressOnThankYou
 *   Description : Validate Delivery Address on Thank you
 **********************************************************************************/

public void validateDeliveryAddressOnThankYou(Map checkoutDetailsMap)
{
	boolean bFlag = false;
	
	
	/* Order Delivery Address */
	List<WebElement> OrderDeliveryAddress = appDriver.findElements(By.xpath(ObjectRepository.ORDER_DELIVERY_ADDRESS));
	for(WebElement orderAddress : OrderDeliveryAddress)
	{
		
		if(orderAddress.getText().contains(checkoutDetailsMap.get(GenericApplicationConstants.USERNAME).toString())){		
				
		if(orderAddress.getText().contains(checkoutDetailsMap.get("flatNo").toString().trim()+", "+ checkoutDetailsMap.get("houseNo").toString().trim()+", " +checkoutDetailsMap.get("houseName").toString().trim()+","))	
		{
		
		if(orderAddress.getText().contains(checkoutDetailsMap.get("streetName").toString())){			
		
		if(orderAddress.getText().contains(checkoutDetailsMap.get("townName").toString())){			
		
		if(orderAddress.getText().contains(checkoutDetailsMap.get("countyName").toString())){	
			
		
		if(orderAddress.getText().contains(checkoutDetailsMap.get(GenericApplicationConstants.POSTCODE).toString())){
			bFlag = true;
		}	
		}
		}
		}
		}
		}
	}
		if(bFlag)
		{
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
		}
		/*else
		{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}	*/
}

/*******************************************************************************	 
 *   Function Name : validateThankYouPage
 *   Description : Validate Thank you Page
 **********************************************************************************/

public void validateThankYouPage(String className)
{
	 /* Check Page Title */
	validatePageTitle( VerificationStringConstants.THANK_YOU_PAGE_TITLE);
		
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
	
	/*************** Validate Success Icon ********************************/		
	validateElementDisplayed( ObjectRepository.THANK_YOU_MESSAGE_ICON);
	
	//extract the order number(last word) from the line
	
	String testString = appDriver.findElement(By.xpath(ObjectRepository.ORDER_REFERENCE_NUMBER)).getText().toString();
	String[] parts = testString.split(" ");
	String lastWord = parts[parts.length - 1];
	System.out.println(lastWord);
	
	//printing order number to file
	//printOrderNumber(className, lastWord);
	
}
	
	/*******************************************************************************	 
	 *   Function Name : printOrderNumber
	 *   Description : Print order number
	 **********************************************************************************/
	public String printOrderNumber(String className){
		
		String testString = appDriver.findElement(By.xpath(ObjectRepository.ORDER_REFERENCE_NUMBER)).getText().toString();
		String[] parts = testString.split(" ");
		String lastWord = parts[parts.length - 1];
		System.out.println(lastWord);
		
		
		System.out.println(className);
		
		
		File file;
		
		try {
        	file = new File("src//testListener//OrderNumber.txt");
        	file.setReadable(true);
        	
        	    if(!file.exists()){
        	        System.out.println("We had to make a new file.");
        	        file.createNewFile();
        	    }
		
					/*FileWriter fileWriter = new FileWriter(file, true);
					
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					
					
				    Date d= new Date();

				  //get the last line of the file to print the total time
					//read the entire file and delete the last line and write it back
							List<String> lines = new ArrayList<String>();
		
						    // read the file into lines
						    BufferedReader r = new BufferedReader(new FileReader(file));
						    String in;
						    while ((in = r.readLine()) != null)
						        lines.add(in);
						    r.close();
						    
						    
						    
						    // write it back
						    PrintWriter w = new PrintWriter(fileWriter);
						    for (String line : lines)
						        w.println(line);
						    w.close();
						    		
						    bufferedWriter.write(orderNumber+"\t"+className+"\t\t"+d.toString()+"\n");
			        			        
				        w.append(orderNumber+"\t"+className+"\t\t"+d.toString()+"\n");
				        w.close();
					
					//bufferedWriter.write(orderNumber+"\t"+className+"\n");
					
					*/
					
         	   
        		}
        		catch (IOException e) {
        			e.printStackTrace();
        		}
		
		return String.valueOf(lastWord);
	}

	/*******************************************************************************	 
	 *   Function Name : validatePreAndBackOrder
	 *   Description : Select the Desired Phone
	 **********************************************************************************/
	public void validatePreOrderDevice(String sHardwareSKUID)
	{   		
		boolean bFlag = false;
		List<WebElement> select_Phone_Compared = appDriver.findElements(By.xpath(ObjectRepository.BUTTON_PRE_ORDER_PHONE));		
        
		/*Check if collection is not Empty */                                       
		if(!select_Phone_Compared.isEmpty())
		{
			for(WebElement selectCompared : select_Phone_Compared )
			{
				if(selectCompared.getAttribute(GenericApplicationConstants.HREF).contains(sHardwareSKUID))
				   {	
					bFlag=true;						
					break;
				   }  		        	  
			}
		}
		
		if(bFlag){
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
		}else{
					Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		     }
	}
	
	/*******************************************************************************	 
	 *   Function Name : validateAddressCompletionForPrepay
	 *   Description : Validate the completion of Adddress section
	 **********************************************************************************/
	public void validateAddressCompletionForPrepay(Map checkOutDetailsMap)
	{
		
	    List<WebElement> addressDetailsDisplayed = appDriver.findElements(By.xpath(ObjectRepository.ADDRESS_SECTION_COMPLETE));
		boolean bFlag = false;
	    for(WebElement addressDisplayed : addressDetailsDisplayed){
	    	if(addressDisplayed.isDisplayed()){
		     if(addressDisplayed.getText().contains(checkOutDetailsMap.get(GenericApplicationConstants.FLAT_NUMBER).toString()+ ", "+checkOutDetailsMap.get(GenericApplicationConstants.HOUSE_NUMBER).toString()+", "+ checkOutDetailsMap.get(GenericApplicationConstants.HOUSE_NAME).toString()))
		     {
				bFlag=true;
		     }
		     if(addressDisplayed.getText().contains(checkOutDetailsMap.get(GenericApplicationConstants.STREET).toString()+ ", "+checkOutDetailsMap.get(GenericApplicationConstants.TOWN).toString()+", "+ checkOutDetailsMap.get(GenericApplicationConstants.COUNTY).toString()+", "+ checkOutDetailsMap.get(GenericApplicationConstants.COUNTRY).toString()))
		     {
				bFlag=true;
		     }
		     if(addressDisplayed.getText().contains(checkOutDetailsMap.get(GenericApplicationConstants.POSTCODE).toString()))
		     {
				bFlag=true;
		     }
		   }else{
			   bFlag=false;
		   }
	    }
	     
	     if(bFlag){
	    	 Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);	
			}else
			{
				Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
			}	     
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
	return bFlag;
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
	 *   Function Name : clickOnWebElement
	 *   Description : Click on Element
	 **********************************************************************************/  
  public void clickOnWebElement(String elementSelector)
  {
	 
	  WebElement button_Manufacturer_More = appDriver.findElement(By.xpath(elementSelector));       
	  
		if(button_Manufacturer_More.isDisplayed() || button_Manufacturer_More.isEnabled())
		{   
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
			appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			button_Manufacturer_More.click();
			appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		}else
		{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}
		
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
	 *   Function Name : validatePreOrderMessage
	 *   Description : Check if Pre Order Message is displayed
	 **********************************************************************************/    
public void validatePreOrderMessage(Map payGDetailsMap)
{
	boolean bFlag = false;

	System.out.println(appDriver.findElement(By.xpath(ObjectRepository.BASKET_DELIVERY_MESSAGE)));
	
  WebElement orderDeliveryBasketMessage = appDriver.findElement(By.xpath(ObjectRepository.BASKET_DELIVERY_MESSAGE));
	if(orderDeliveryBasketMessage.isDisplayed())
	{
		System.out.println(orderDeliveryBasketMessage.getText().toString());
		if(orderDeliveryBasketMessage.getText().contains(GenericApplicationConstants.PRE_ORDER))
		{				
			
			String[] orderDeliveryMsg = orderDeliveryBasketMessage.getText().split(" ");
			String orderDeliveryMonth = orderDeliveryMsg[orderDeliveryMsg.length -1];
			
			String orderDeliveryDate = orderDeliveryMsg[orderDeliveryMsg.length -2];
			
			
			
			if(orderDeliveryMonth.equalsIgnoreCase(payGDetailsMap.get(GenericApplicationConstants.AVAILABILITY_MONTH).toString()))
			 {
						 if(orderDeliveryDate.startsWith("0"))
						  {
							if(orderDeliveryDate.split("0")[1].equalsIgnoreCase(payGDetailsMap.get(GenericApplicationConstants.AVAILABILITY_DATE).toString().trim()))
							{
					             bFlag=true;
							}else{
								 bFlag=false;
							}
							
						  }else if(orderDeliveryDate.equalsIgnoreCase(payGDetailsMap.get(GenericApplicationConstants.AVAILABILITY_DATE).toString().trim()))
						  {
					         bFlag=true;
						  }else{
							  bFlag=false;
						  }			
			  }else{
				 bFlag=false;				
			  }
		}else{
			 bFlag=false;				
		}
			
	}
	if(bFlag)
	{
		Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
	}else
	{
		Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
	}	
}


/*******************************************************************************	 
	 *   Function Name : validateBackOrderMessage
	 *   Description : Check if Back Order Message is displayed
	 **********************************************************************************/    
public void validateBackOrderMessage(Map payGDetailsMap)
{
	boolean bFlag = false;

   WebElement orderDeliveryBasketMessage = appDriver.findElement(By.xpath(ObjectRepository.BASKET_DELIVERY_MESSAGE));
		if(orderDeliveryBasketMessage.isDisplayed())
		{
					if(orderDeliveryBasketMessage.getText().contains(GenericApplicationConstants.BACK_ORDER))
					{	
						String[] orderDeliveryMsg = orderDeliveryBasketMessage.getText().split(" ");
						String orderDeliveryDuration = orderDeliveryMsg[orderDeliveryMsg.length -1].trim();
						
						String orderDeliveryMinDate = orderDeliveryMsg[orderDeliveryMsg.length -4].trim();
						
						String orderDeliveryMaxDate = orderDeliveryMsg[orderDeliveryMsg.length -2].trim();
						
						
						
							   if(orderDeliveryDuration.contains(payGDetailsMap.get(GenericApplicationConstants.AVAIL_MAX_DAY).toString().trim().split(" ")[1])){	  
								
												if(orderDeliveryMinDate.equalsIgnoreCase(payGDetailsMap.get(GenericApplicationConstants.AVAIL_MIN_DAY).toString().trim().split(" ")[0]))
												 {	
													bFlag=true;
												 }else{
													 bFlag=false;				
												  }
								
												if(orderDeliveryMaxDate.equalsIgnoreCase(payGDetailsMap.get(GenericApplicationConstants.AVAIL_MAX_DAY).toString().trim().split(" ")[0]))
												 {	
													bFlag=true;
												 }else{
													 bFlag=false;				
												  }
										 }else{
											 bFlag=false;				
										  }
							}else{
								 bFlag=false;				
							  }
		}else{
			 bFlag=false;				
		  }
	
		
	if(bFlag)	{
		Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
	}else{
		Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
	}	
}
/*******************************************************************************	 
	 *   Function Name : validateAccessoryDisplayed
	 *   Description : Check if intended Accessory is displayed
	 **********************************************************************************/ 
public void validateAccessoryDisplayed(String accessoryName, String accessoryPrice)
{
	List<WebElement> accessoryDisplayBlock = appDriver.findElements(By.xpath(ObjectRepository.ACCESSORY_DISPLAY_BLOCK));
	for(WebElement accessories : accessoryDisplayBlock)
	{
		if(accessories.findElement(By.xpath(ObjectRepository.ACCESSORY_DISPLAY_NAME)).getText().contains(accessoryName))
		{
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);;
		}else
		{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}	
		
		if(accessories.findElement(By.xpath(ObjectRepository.ACCESSORY_DISPLAY_PRICE)).getText().contains(accessoryPrice))
		{
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);;
		}else
		{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}
	}
}

	/***************************************************************************************
	 *  Check for values with Comparison page
	 ***************************************************************************************/
	public void validateCompareBundlePage( )
	{
		validatePageTitle( VerificationStringConstants.PAGE_TITLE_COMPARE_PACKAGE);		

		/*Check for Title on Comparison page */    
		validateTextOnControl(ObjectRepository.H1, VerificationStringConstants.HEADER_TITLE_COMPARE_PACKAGE);
	
		/* Check for Back to previous page  Link */
		 validateElementDisplayed( ObjectRepository.LINK_BACK_PREVIOUS_PAGE);		
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
	 *   Function Name : userLoginToeShop
	 *   Description : Check if user is able to login to eshop
	 **********************************************************************************/ 	
	public void userLoginToeShop(String Username)
	{		
		appDriver.findElement(By.xpath(ObjectRepository.LOGIN_BUTTON)).click();
		
		appDriver.switchTo().activeElement();		
		
		
		
	    //appDriver.findElement(By.linkText(VerificationStringConstants.LOGIN_BUTTON)).click();
		
	    //interactDriver.click(appDriver.findElement(By.xpath("//a[contains(@href, 'ssoLoginRedirect') and contains(text(), 'Log')]"))).build().perform();
			
	    
		//waitForElement( "//a[contains(@href, 'ssoLoginRedirect') and contains(text(), 'Log')]");
		
		//appDriver.findElement(By.xpath("//a[contains(@href, 'ssoLoginRedirect') and contains(text(), 'Log')]")).click();
	   
	    
	     
	    
	    appDriver.findElement(By.xpath(ObjectRepository.LOGIN_USERNAME_FIELD)).clear();
	    appDriver.findElement(By.xpath(ObjectRepository.LOGIN_USERNAME_FIELD)).sendKeys(Username);
	    appDriver.findElement(By.xpath(ObjectRepository.LOGIN_PAGE_BUTTON)).click();
	    
	   // appDriver.findElement(By.xpath(ObjectRepository.LOGIN_USER_TITLE)).getText().contains("Hi Remo");
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
				if(!linkElement.getText().trim().contains(GenericApplicationConstants.CONTINUE)){				
			
					clickOnWebElement(ObjectRepository.VIEW_BASKET);
					appDriver.manage().timeouts().pageLoadTimeout(GenericApplicationConstants.TIME_DELAY_SECONDS, TimeUnit.SECONDS);
					
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
	 *   Function Name : validateNumericOnControl
	 *   Description : Validate the Numeric value
	 **********************************************************************************/ 	
	 public void validateNumericOnControl(String controlSelector, String valueToCheck)
	   {
		   WebElement webPageHeader = appDriver.findElement(By.xpath(controlSelector));
		   System.out.println(webPageHeader.getText());
		   if((webPageHeader.isDisplayed()) && (!webPageHeader.getText().isEmpty()))
		   {			   		   
			  // valueToCheck = valueToCheck.split("E")[0].split("\\.")[0]+valueToCheck.split("E")[0].split("\\.")[1];
				if(webPageHeader.getText().contains(valueToCheck))
				{
					Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
				}else
				{
					Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
				}
		   }
	   }

	/*******************************************************************************	 
	 *   Function Name : validateNumericOnControl
	 *   Description : Validate the Numeric value
	 **********************************************************************************/ 
	public String retrieveElementAttribute(String controlSelector,
			String attribute) {
		
		String sAttributeValue = null;
		boolean bFlag = false;
		
		WebElement elements = appDriver.findElement(By.xpath(controlSelector));
		
		if((elements.isDisplayed()) || (elements.isEnabled()))
		{
			 if(!elements.getAttribute(attribute).trim().isEmpty())
			{
				sAttributeValue = elements.getAttribute(attribute).trim();
				bFlag=true;
			}
		}
		if(bFlag){
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
		}else{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}
		return sAttributeValue;				
	}
	
	/*******************************************************************************	 
	 *   Function Name : validateNumericOnControl
	 *   Description : Validate the Numeric value
	 **********************************************************************************/ 
	public void  validateAccessoryPage(String eshopUserLogin){
		
		  /************************** Navigate to Shop link in MDD ***************************/		
		  
		  /*********************** Click on Accessory Link ***************************/		
		  
			Actions actions = new Actions(appDriver);
			//go to Shop link in MDD
			WebElement menuHoverLink = appDriver.findElement(By.xpath(ObjectRepository.NAVIGATE_TO_MDD_SHOP));
			actions.moveToElement(menuHoverLink);
	
			//navigate to sub link inside MDD
			WebElement subLink = appDriver.findElement(By.xpath(ObjectRepository.NAVIGATE_ACCESSORY_LINK));
			actions.moveToElement(subLink);
			//click on sub link inside MDD
			actions.click();
			actions.perform();
		
		  
		  /******************************* Validate Accessory page **********************************/
		  /*********************** Check Page Title **************************************/
		  validatePageTitle( VerificationStringConstants.ACCESSORY_PAGE_TITLE);
			
		 /************* Check for Header Text Match **********************/
		  validateTextOnControl(ObjectRepository.H1, VerificationStringConstants.ACCESSORY_HEADER_TITLE);
	  }
	
	/*******************************************************************************	 
	 *   Function Name : validateNumericOnControl
	 *   Description : Validate the Numeric value
	 **********************************************************************************/ 
	public void  validateUnauthAccessoryPage(){
		
		  /************************** Navigate to Shop link in MDD ***************************/		
		  
		  /*********************** Click on Accessory Link ***************************/		
		  
			Actions actions = new Actions(appDriver);
			//go to Shop link in MDD
			WebElement menuHoverLink = appDriver.findElement(By.xpath(ObjectRepository.NAVIGATE_TO_MDD_SHOP));
			actions.moveToElement(menuHoverLink);
	
			//navigate to sub link inside MDD
			WebElement subLink = appDriver.findElement(By.xpath(ObjectRepository.NAVIGATE_ACCESSORY_LINK));
			actions.moveToElement(subLink);
			//click on sub link inside MDD
			actions.click();
			actions.perform();
		
		  
		  /******************************* Validate Accessory page **********************************/
		  /*********************** Check Page Title **************************************/
		  validatePageTitle( VerificationStringConstants.ACCESSORY_PAGE_TITLE);
			
		 /************* Check for Header Text Match **********************/
		  validateTextOnControl(ObjectRepository.H1, VerificationStringConstants.ACCESSORY_HEADER_TITLE);
	  }
	
	
	/*******************************************************************************	 
	 *   Function Name : paymentUsingNewCard
	 *   Description : Validate the Payment made using New Card
	 **********************************************************************************/ 
	public void  paymentUsingNewCard(Map checkoutDetailsMap, Actions interactionBuilder){		
			
	
	clickOnWebElement( ObjectRepository.NEW_CARD_ORG+checkoutDetailsMap.get(GenericApplicationConstants.CARD_ORG)+ObjectRepository.COMPLETE_CONTAINS_XPATH_SELECTOR);
	
	validateFieldAndEnterNumeric( "ID", GenericApplicationConstants.CREDIT_CARD_NUMBER, checkoutDetailsMap.get(GenericApplicationConstants.CREDIT_CARD_NUMBER).toString().trim(), interactionBuilder);
	
	selectFromDropdown( GenericApplicationConstants.CC_EXPIRY_MONTH, checkoutDetailsMap.get(GenericApplicationConstants.EXPIRY_MONTH).toString().trim());
	
	selectFromDropdown( GenericApplicationConstants.CC_EXPIRY_YEAR, checkoutDetailsMap.get(GenericApplicationConstants.EXPIRY_YEAR).toString().trim());
	validateFieldAndEnterText( "ID", GenericApplicationConstants.CREDIT_CARD_ACC_HOLDER, checkoutDetailsMap.get(GenericApplicationConstants.CREDIT_CARD_ACC_HOLDER).toString().trim(), interactionBuilder);
	
	validateFieldAndEnterText( "ID", GenericApplicationConstants.CC_NEW_CCV, checkoutDetailsMap.get(GenericApplicationConstants.CCV).toString().trim(), interactionBuilder);

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
	 *   Function Name : sortByManufacturer
	 *   Description : Sort List By Manufacturer
	 **********************************************************************************/
	public void sortByTerm(String sTerm)
	{
		
		
		/************* Scroll Page *****************/
		scollPage("400");		

		/* Wait for Page Load */
		appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		/* Click on More Button*/		
		clickOnWebElement( ObjectRepository.BUTTON_TERM_MORE);		

		/* Filter phones based on manufacturer */
		List<WebElement> collection_Term = appDriver.findElements(By.xpath(ObjectRepository.PLAN_TERM_CHECKBOXES));

		if(!collection_Term.isEmpty())	{

			for(WebElement checkElement : collection_Term){
				if(checkElement.getText().equalsIgnoreCase(sTerm)) {
					checkElement.click();
					break;
				}
			}
		}
		
		waitForAjaxToLoad();
		
		/********** Scroll Page ***************/
		scollPage("200");
		
		//appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		/*Wait for the Ajax call to end and the display of the Phone
		WebDriverWait appWait = new WebDriverWait(appDriver, 20);
		appWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), '"+ sPhoneChecked +"')]"))); */		
	}
	
	/*******************************************************************************	 
	 *   Function Name : sortByTermSIMO
	 *   Description : Sort List By SIMO Term
	 **********************************************************************************/
	public void sortByTermSIMO(String sTerm)
	{
		
		
		/************* Scroll Page *****************/
		scollPage("950");
		
		/* Wait for Page Load */
		appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		/* Click on More Button*/		
		clickOnWebElement( ObjectRepository.BUTTON_SIMO_TERM_MORE);		

		/* Filter phones based on manufacturer */
		List<WebElement> collection_Term = appDriver.findElements(By.xpath(ObjectRepository.PLAN_TERM_CHECKBOXES));

		if(!collection_Term.isEmpty())	{

			for(WebElement checkElement : collection_Term){
				if(checkElement.getText().equalsIgnoreCase(sTerm)) {
					checkElement.click();
					break;
				}
			}
		}
		
		waitForAjaxToLoad();
		
		
		//appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		/*Wait for the Ajax call to end and the display of the Phone
		WebDriverWait appWait = new WebDriverWait(appDriver, 20);
		appWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), '"+ sPhoneChecked +"')]"))); */		
	}
	
	
	/*******************************************************************************	 
	 *   Function Name : sortByManufacturer
	 *   Description : Sort List By Manufacturer
	 **********************************************************************************/
	public void sortByData(String iData)
	{
		
		
		/* Wait for Page Load */
		appDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		
		//fieldset[contains(@class, 'filters')]//span[contains(text(), 'Minutes Filter')]/ancestor::fieldset[contains(@class, 'filters')]//input[@name='minutes']
		
			
		/************* Scroll Page *****************/
		scollPage("950");		

		/* Wait for Page Load */
		appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		/* Click on More Button*/		
		clickOnWebElement( ObjectRepository.BUTTON_DATA_MORE);		

		clickOnIntendedCheckBoxFilter(ObjectRepository.PLAN_DATA_FILTER_SECTION + ObjectRepository.ANSCESTORS+ObjectRepository.PLAN_FILTER_SECTION + ObjectRepository.START_OF_SECTOR+ObjectRepository.PLAN_DATA_CHECKBOXES+ObjectRepository.FOLLOWING_SIBLING + ObjectRepository.LABEL, iData);
		
		/*appDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		waitForElement( ObjectRepository.LINK_SHOW_ALL);*/
		
		waitForAjaxToLoad();	

		/* Scroll Page */
		scollPage("400");
		
		//appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		/*Wait for the Ajax call to end and the display of the Phone
		WebDriverWait appWait = new WebDriverWait(appDriver, 20);
		appWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), '"+ sPhoneChecked +"')]"))); */		
	}
		
	
	/*******************************************************************************	 
	 *   Function Name : sortByMinutes
	 *   Description : Sort List By Minutes
	 **********************************************************************************/
	public void sortByMinutes(String iMinutes)
	{
		
		
		/************* Scroll Page *****************/
		scollPage("850");		

		/* Wait for Page Load */
		appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		/* Click on More Button*/		
		clickOnWebElement( ObjectRepository.BUTTON_MINUTES_MORE);	
		waitForAjaxToLoad();

		clickOnIntendedCheckBoxFilter(ObjectRepository.START_OF_SECTOR+ObjectRepository.PLAN_FILTER_SECTION+ObjectRepository.PLAN_MINUTES_FILTER_SECTION + ObjectRepository.ANSCESTORS+ObjectRepository.PLAN_FILTER_SECTION + ObjectRepository.START_OF_SECTOR+ ObjectRepository.PLAN_MINUTES_CHECKBOXES+ObjectRepository.FOLLOWING_SIBLING + ObjectRepository.LABEL, iMinutes);
		
		/*appDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		waitForElement( ObjectRepository.LINK_SHOW_ALL);*/
		
		waitForAjaxToLoad();	

		/* Scroll Page */
		scollPage("400");
		
		//appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		/*Wait for the Ajax call to end and the display of the Phone
		WebDriverWait appWait = new WebDriverWait(appDriver, 20);
		appWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), '"+ sPhoneChecked +"')]"))); */		
	}
	
	
	
	/*******************************************************************************	 
	 *   Function Name : sortByManufacturer
	 *   Description : Sort List By Manufacturer
	 **********************************************************************************/
	public void sortByText(String iText)
	{
		
		
		/************* Scroll Page *****************/
		scollPage("550");		

		/* Wait for Page Load */
		appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			

		/* Click on More Button*/		
		clickOnWebElement( ObjectRepository.BUTTON_TEXT_MORE);	
		
		clickOnIntendedCheckBoxFilter(ObjectRepository.START_OF_SECTOR+ObjectRepository.PLAN_FILTER_SECTION+ObjectRepository.PLAN_TEXT_FILTER_SECTION + ObjectRepository.ANSCESTORS+ObjectRepository.PLAN_FILTER_SECTION + ObjectRepository.START_OF_SECTOR+ ObjectRepository.PLAN_TEXTS_CHECKBOXES+ObjectRepository.FOLLOWING_SIBLING + ObjectRepository.LABEL, iText);
		
		waitForAjaxToLoad();
		
		/*waitForElement( ObjectRepository.LINK_SHOW_ALL);
		
		appDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);*/

		/* Scroll Page */
		scollPage("400");
		
		//appDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		/*Wait for the Ajax call to end and the display of the Phone
		WebDriverWait appWait = new WebDriverWait(appDriver, 20);
		appWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), '"+ sPhoneChecked +"')]"))); */		
	}
	
	/*******************************************************************************	 
	 *   Function Name : clickOnIntendedCheckBoxFilter
	 *   Description : Check Intended Filter Option
	 **********************************************************************************/
	public void clickOnIntendedCheckBoxFilter(String selector, String iData)
	{
		
      List<WebElement> elemOptions = appDriver.findElements(By.xpath(selector));
		
		if(!elemOptions.isEmpty())
		{
			for(WebElement checkBox : elemOptions)
			{
				String chekBoxValue = checkBox.getText();	
				
				
				if((Double.parseDouble(iData.trim().split(" ")[0].trim())) >= (Double.parseDouble(chekBoxValue.trim().split("-")[0].trim().split(" ")[0])))
				{
					if((Double.parseDouble(iData.trim().split(" ")[0].trim())) <= (Double.parseDouble(chekBoxValue.trim().split("-")[1].trim().split(" ")[0])))
					{
						checkBox.click();
						Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
						break;
					}
				}
				
			}		
	}else{
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
		 *   Function Name : elementDisplayed
		 *   Description : Check if Element displayed
		 **********************************************************************************/    
	  public boolean validateElementNotDisplayed(String elementSelector)
	  {  
		    
			WebElement cardCCVIcon = appDriver.findElement(By.xpath(elementSelector));
			boolean bFlag = false;
			if(!cardCCVIcon.isDisplayed())
			{  
				bFlag = true;
				Assert.assertTrue(true, GenericApplicationConstants.ITEMS_DONOT_MATCH);			
			}	
			return bFlag;
	  }
	
	/*##################################################################################################*/
	public  void ExtraSelection(String ExtraSku, String ExtraName) throws InterruptedException {
		   appDriver.findElement(By.id(ObjectRepository.ADD_EXTRA.replace("skuid", ExtraSku))).click();
		   Assert.assertTrue(appDriver.findElement(By.xpath(ObjectRepository.EXTRA_STEP_TITLE)).getText().matches("Extra"));
		   Assert.assertTrue(appDriver.findElement(By.xpath(ObjectRepository.EXTRA_STEP_BODY)).getText().matches(ExtraName));		
	}
	
	public  void changeLink(String productName)
	{
		List<WebElement> packageContents = appDriver.findElements(By.xpath(ObjectRepository.BASKET_TABLE+ObjectRepository.BASKET_PRODUCT_NAME));
		List<WebElement> changeLink = appDriver.findElements(By.xpath(ObjectRepository.BASKET_TABLE+"//a[@class='fr']"));
		
		 int checkProductFound = 0;
		for(int a=0; a<packageContents.size(); a++)
		{
			
			if (packageContents.get(a).getText().contains(productName))
			{				
				checkProductFound ++;
				changeLink.get(a).click();
				appDriver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);				  
				
			break;
			}
		}
		Assert.assertEquals(1, checkProductFound);
	}

	 /*******************************************************************************	 
		 *   Function Name : removePackage
		 *   Description : Check if Element displayed
		 **********************************************************************************/    
	
	public Map removePackage(int packageNumToRemove, Map packageMaps)
	{
			
		List<WebElement> packages = appDriver.findElements(By.xpath(ObjectRepository.BASKET_PACKAGES));
		
		for (int i=0; i<packages.size(); i++)
		{
			
			if(packages.get(i).getText().contains(VerificationStringConstants.PACKAGE_TITLE+" "+String.valueOf(packageNumToRemove)))
			{
				appDriver.findElement(By.xpath(ObjectRepository.BASKET_TABLE+"["+ String.valueOf(i+1)+"]"+ObjectRepository.BASKET_REMOVE_PACKAGE)).click();
				break;
			}
	}	
	    return packageMaps;
	}
	
	/*******************************************************************************	 
	 *   Function Name : duplicatePackage
	 *   Description : Check if Element displayed
	 **********************************************************************************/    
	public Map duplicatePackage(int packageNumber, Map packageMaps)
	{
		Map tempMap = null;
		List<WebElement> packages = appDriver.findElements(By.xpath(ObjectRepository.BASKET_PACKAGES));
		
		for (int i=0; i<packages.size(); i++)
		{
			
			if(packages.get(i).getText().contains(VerificationStringConstants.PACKAGE_TITLE+" "+String.valueOf(packageNumber)))
			{
				appDriver.findElement(By.xpath(ObjectRepository.BASKET_TABLE+"["+ String.valueOf(i+1)+"]"+ObjectRepository.BASKET_DUPLICATE_PACKAGE)).click();
				break;
			}
		}    
	    return packageMaps;
	}
	
	/*******************************************************************************	 
	 *   Function Name : applyCoupon
	 *   Description : Check if Element displayed
	 **********************************************************************************/  
	public void applyCoupon(String couponName, String discountedPrice, Actions interactions)
	{
		scollPage( "700");
		//Assert.assertTrue(appDriver.findElement(By.xpath(ObjectRepository.BASKET_PROMO_ONE_OFF)).getText().contains("£ 0"));
		
		validateFieldAndEnterText( "ID", GenericApplicationConstants.PROMOTION_CODE, couponName, interactions);
		
		//appDriver.findElement(By.xpath(ObjectRepository.BASKET_PROMO_TEXT_BOX)).sendKeys(couponName);
		
		clickOnWebElement( ObjectRepository.BASKET_PROMO_BUTTON);
		
		//appDriver.findElement(By.xpath(ObjectRepository.BASKET_PROMO_BUTTON)).click();
		
		String promoText = retrieveTextValue(ObjectRepository.BASKET_PROMO_APPLIED_TEXT);
		
		
		//validateTextOnControl(ObjectRepository.BASKET_PROMO_APPLIED_TEXT, "£ "+Double.parseDouble(discountedPrice));
		
		/* Check for Remove Promotion code  Link */
		 validateElementDisplayed( ObjectRepository.BASKET_PROMO_REMOVE_LINK);
			System.out.println(appDriver.findElement(By.xpath(ObjectRepository.BASKET_PROMO_ONE_OFF)).getText());
		Assert.assertTrue(appDriver.findElement(By.xpath(ObjectRepository.BASKET_PROMO_ONE_OFF)).getText().replace("£", "").trim().contains(discountedPrice));
		Assert.assertTrue(appDriver.findElement(By.xpath(ObjectRepository.BASKET_YOU_SAVE_ONE_OFF)).getText().replace("£", "").replace("One-off", "").trim().contains(discountedPrice));
		Assert.assertTrue(appDriver.findElement(By.xpath(ObjectRepository.BASKET_TOTAL_ONE_OFF_COST)).getText().replace("£", "").replace("One-off", "").trim().contains(discountedPrice));
		Assert.assertTrue(appDriver.findElement(By.xpath(ObjectRepository.BASKET_YOU_SAVE_MONTHLY)).getText().contains("£0.00"));

	}
	
	/*******************************************************************************	 
	 *   Function Name : emailBasket
	 *   Description : Check if Element displayed
	 **********************************************************************************/  
	public void emailBasket()
	{
		   appDriver.findElement(By.linkText(ObjectRepository.BASKET_EMAIL_LINK)).click();
		   appDriver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		   appDriver.switchTo().activeElement();
		    appDriver.findElement(By.xpath(ObjectRepository.BASKET_EMAIL_YOUR_NAME)).sendKeys("pavani");
		    appDriver.findElement(By.xpath(ObjectRepository.BASKET_EMAIL_ID)).sendKeys("fgds@b.com");
		    appDriver.findElement(By.xpath(ObjectRepository.BASKET_SEND_EMAIL_BUTTON)).click();
		    appDriver.findElement(By.xpath(ObjectRepository.BASKET_EMAIL_THANQ_TEXT)).getText().concat("fgds@b.com");
		    appDriver.findElement(By.xpath(ObjectRepository.BASKET_EMAIL_CLOSE)).click();
	}
	
	/*******************************************************************************	 
	 *   Function Name : packageValidations
	 *   Description : Check if Element displayed
	 **********************************************************************************/ 		
		public void packageValidations(Map packageMaps){
			
			Map packageMap = null;			

			/**
			 ***************************************************
			 Validate no.of packages present in the page with the count passed to subscript 
			 *************************************************** 
			 */
			List<WebElement> packages = appDriver.findElements(By.xpath(ObjectRepository.BASKET_PACKAGES));
			Assert.assertEquals(packages.size(), packageMaps.size());
						
			/**
			 ***************************************************
			 Repeat the loop for package count times to validate the package details and links against package
			 *************************************************** 
			 */
			for (int i=0; i<packages.size(); i++)
			{
				
				packageMap = ((HashMap)packageMaps.get(String.valueOf(i+1)));   
				Assert.assertTrue(packages.get(i).getText().contains(VerificationStringConstants.PACKAGE_TITLE+" "+String.valueOf(i+1)));
				/**
				 ***************************************************
				 Get the package details such as Phone name /Plan name, One-off cost, monthly cost,
				 Remove package, Duplicate Package for each package present in the webpage
				 *************************************************** 
				 */
				List<WebElement> packageContents = appDriver.findElements(By.xpath(ObjectRepository.BASKET_TABLE+"["+ String.valueOf(i+1)+ "]"+ObjectRepository.BASKET_PRODUCT_NAME));
				List <WebElement> OneOffCost = appDriver.findElements(By.xpath(ObjectRepository.BASKET_TABLE+"["+ String.valueOf(i+1)+"]"+ObjectRepository.BASKET_ONE_OFF_COST));
				List <WebElement> monthlyCost = appDriver.findElements(By.xpath(ObjectRepository.BASKET_TABLE+"["+ String.valueOf(i+1)+"]"+ObjectRepository.BASKET_MONTHLY_COST));
			//	WebElement removeLink = appDriver.findElement(By.xpath(ObjectRepository.BASKET_TABLE+"["+ String.valueOf(i+1)+"]"+ObjectRepository.BASKET_REMOVE_PACKAGE));
			//	WebElement duplicateLink = appDriver.findElement(By.xpath(ObjectRepository.BASKET_TABLE+"["+ String.valueOf(i+1)+"]"+ObjectRepository.BASKET_DUPLICATE_PACKAGE));
			
			//	Need to enable assert statements for checking the links
			// Check for mini basket and basket page is written combinedly, need to write seperate scripts for both	
				
			/**
			 ***************************************************
			 Repeats the loop for no.of Element times each package contains
			 *************************************************** 
			 */		
			
			for(int a=0; a<packageContents.size(); a++)
			{				
				System.out.println(packageContents.get(a).getText().toString());
				System.out.println(OneOffCost.get(a).getText().toString());
				System.out.println(packageMap.get(GenericApplicationConstants.PHONE_PRICE).toString());
				if (((packageMap.get(GenericApplicationConstants.PHONE_DISPLAY_NAME).toString().trim())!=null) && (!packageMap.get(GenericApplicationConstants.PHONE_DISPLAY_NAME).toString().trim().isEmpty()) &&
					(packageContents.get(a).getText().contains(packageMap.get(GenericApplicationConstants.PHONE_DISPLAY_NAME).toString().trim()))){
					
					Assert.assertTrue(packageContents.get(a).getText().contains(packageMap.get(GenericApplicationConstants.PHONE_DISPLAY_NAME).toString()));
					Assert.assertTrue(OneOffCost.get(a).getText().contains(packageMap.get(GenericApplicationConstants.PHONE_PRICE).toString()));
					Assert.assertEquals(true, monthlyCost.get(a).getText().isEmpty());
				}
				else if (packageMap.get(GenericApplicationConstants.PLAN_TERM)!= null && (!packageMap.get(GenericApplicationConstants.PLAN_TERM).toString().trim().isEmpty())
						&& packageContents.get(a).getText().contains(packageMap.get(GenericApplicationConstants.PLAN_TERM).toString())) 
				{
					
					Assert.assertTrue(packageContents.get(a).getText().contains(packageMap.get(GenericApplicationConstants.PLAN_TERM).toString()));
					Assert.assertTrue(packageContents.get(a).getText().contains(packageMap.get(GenericApplicationConstants.PLAN_DATA).toString()));
					Assert.assertTrue(packageContents.get(a).getText().contains(packageMap.get(GenericApplicationConstants.PLAN_TEXT).toString())); 
					Assert.assertTrue(packageContents.get(a).getText().contains(packageMap.get(GenericApplicationConstants.PLAN_TALKTIME).toString().split(" ")[0].trim())); 
					Assert.assertTrue(OneOffCost.get(a).getText().isEmpty());
				}
				else if((packageMap.get(GenericApplicationConstants.EXTRA_NAME).toString().trim()!=null) && (!packageMap.get(GenericApplicationConstants.EXTRA_NAME).toString().trim().isEmpty()) &&
						packageContents.get(a).getText().trim().contains(packageMap.get(GenericApplicationConstants.EXTRA_NAME).toString().trim()))
					{
					
					Assert.assertTrue(packageContents.get(a).getText().contains(packageMap.get(GenericApplicationConstants.EXTRA_NAME).toString().trim()));
					if(Integer.parseInt(packageMap.get(GenericApplicationConstants.EXTRA_ONE_OFF).toString().trim())>0.0)
					{			
							Assert.assertTrue(OneOffCost.get(a).getText().contains(packageMap.get(GenericApplicationConstants.EXTRA_ONE_OFF).toString().trim()));
					}
					
					if(packageMap.get(GenericApplicationConstants.EXTRA_PRICE).toString().trim()!=null)
					{
						Assert.assertTrue(monthlyCost.get(a).getText().contains(packageMap.get(GenericApplicationConstants.EXTRA_PRICE).toString().trim()));
					}
				}
				else if(!packageMap.get(GenericApplicationConstants.PLAN_DISPLAY_NAME).toString().trim().isEmpty()){
				if (packageMap.get(GenericApplicationConstants.PLAN_TERM).toString().trim()== null && 
						packageContents.get(a).getText().trim().contains(packageMap.get(GenericApplicationConstants.PLAN_DISPLAY_NAME).toString().trim()))
				{
					
					Assert.assertTrue(OneOffCost.get(a).getText().isEmpty());
					Assert.assertTrue(monthlyCost.get(a).getText().trim().isEmpty());
				}
				}
				else if(packageMap.get(GenericApplicationConstants.SIM_ONLY_TYPE)!= null &&(!packageMap.get(GenericApplicationConstants.SIM_ONLY_TYPE).toString().trim().isEmpty())){
						if (packageMap.get(GenericApplicationConstants.PLAN_TERM).toString().trim()!= null){
						if(packageContents.get(a).getText().contains(packageMap.get(GenericApplicationConstants.SIM_ONLY_TYPE).toString())) {
					
					Assert.assertTrue(packageContents.get(a).getText().contains(packageMap.get("SIM Type").toString()));
				}}}
				else{
					Assert.assertTrue(false);
				}
			}
			
		//	Assert.assertTrue(duplicateLink.isDisplayed());
			
		//	Assert.assertTrue(removeLink.isDisplayed());			
		}
		}
		
		/*******************************************************************************	 
		 *   Function Name : totalCostValidations
		 *   Description : Check if Element displayed
		 **********************************************************************************/ 	

		public void totalCostValidations(Map packageMaps){
			/****************************************************
			SET the total One-off and Monthly Costs to Zero
			*************************************************** 
				 */
				double totalOneOffCost=0.00;
				double totalMonthlyCost =0.00;
				
				Map packageMap = null;
				for (int i=0; i<packageMaps.size(); i++)
				{
					
					packageMap = ((HashMap)packageMaps.get(String.valueOf(i+1)));   
				   if(packageMap.get(GenericApplicationConstants.PHONE_PRICE).toString().trim() != null && (!packageMap.get(GenericApplicationConstants.PHONE_PRICE).toString().trim().isEmpty())
						   &&  (packageMap.get(GenericApplicationConstants.PHONE_PRICE).toString().trim() != "FREE")
						)
					{
						totalOneOffCost=totalOneOffCost+Double.parseDouble(packageMap.get(GenericApplicationConstants.PHONE_PRICE).toString().trim());
					}
					
					
					if(packageMap.get(GenericApplicationConstants.PLAN_PRICE).toString().trim()!= null && (!packageMap.get(GenericApplicationConstants.PLAN_PRICE).toString().trim().isEmpty()))
					{
						totalMonthlyCost=totalMonthlyCost+Double.parseDouble(packageMap.get(GenericApplicationConstants.PLAN_PRICE).toString().trim());
					}
					
					
					if((!packageMap.get(GenericApplicationConstants.EXTRA_ONE_OFF).toString().trim().isEmpty()) && Integer.parseInt(packageMap.get(GenericApplicationConstants.EXTRA_ONE_OFF).toString().trim())>0.0)
					{
						totalOneOffCost = totalOneOffCost + Double.parseDouble(packageMap.get(GenericApplicationConstants.EXTRA_ONE_OFF).toString().trim());
					}
					
					
					if((!packageMap.get(GenericApplicationConstants.EXTRA_PRICE).toString().trim().isEmpty()) && packageMap.get(GenericApplicationConstants.EXTRA_PRICE).toString().trim() !=null)
					{
						totalMonthlyCost=totalMonthlyCost+Double.parseDouble(packageMap.get(GenericApplicationConstants.EXTRA_PRICE).toString().trim());
					}
					
					
					if((!packageMap.get(GenericApplicationConstants.ACCESSORY_PRICE).toString().trim().isEmpty()) && packageMap.get(GenericApplicationConstants.ACCESSORY_PRICE).toString().trim() !=null)
					{
						totalOneOffCost=totalOneOffCost+Double.parseDouble(packageMap.get(GenericApplicationConstants.ACCESSORY_PRICE).toString().trim());
					}			
					}			
				
			//	Assert.assertEquals(Double.parseDouble(appDriver.findElement(By.xpath(ObjectRepository.BASKET_TOTAL_ONE_OFF_COST)).getText().replace("£", "").replace("One-off", "").trim()), totalOneOffCost, 0);
			//	Assert.assertEquals(Double.parseDouble(appDriver.findElement(By.xpath(ObjectRepository.BASKET_TOTAL_MONTHLY_COST)).getText().replace("£", "").trim()),totalMonthlyCost,0);
		}
		
		/*******************************************************************************	 
		 *   Function Name : accessoryValidations
		 *   Description : Check if Element displayed
		 **********************************************************************************/ 	
		
    public  void accessoryValidations(Map packageMap){
			
			
			/**
			 ***************************************************
			 Validate Accessories Title, Accessory Name and prices
			 *************************************************** 
			 */
			String accessoryTitle = appDriver.findElement(By.xpath(ObjectRepository.BASKET_ACESSORY_TITLE)).getText();
			Assert.assertTrue(accessoryTitle.matches (VerificationStringConstants.ACCESSORY_TITLE_SHOPPING_BASKET));

			Assert.assertTrue(appDriver.findElement(By.xpath(ObjectRepository.BASKET_ACCESSORY_NAME)).getText().contains(packageMap.get("Accessory Name").toString().trim()));
			Assert.assertTrue(appDriver.findElement(By.xpath(ObjectRepository.BASKET_ACCESSORY_ONEOFF)).getText().contains(packageMap.get("Accessory Price").toString().trim()));
			Assert.assertTrue(appDriver.findElement(By.xpath(ObjectRepository.BASKET_ACCESSORY_MONTHLY)).getText().isEmpty());
		}

	public void selectCompatibleItem(String elementSelector) {
		
		
	
		WebElement buttonControl = appDriver.findElement(By.id(elementSelector));
		boolean bFlag = false;
		if(buttonControl.isDisplayed() && buttonControl.isEnabled())
		{  
			bFlag = true;
			buttonControl.click();
					
		}	
		if(bFlag)
		{
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_DONOT_MATCH);	
		}else{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}		
		
	}
	
	/*******************************************************************************	 
	 *   Function Name : validateExtraRetainedOnStepper
	 *   Description : Check if Element are retained on Stepper
	 **********************************************************************************/ 

	public void validateExtraRetainedOnStepper(String[] sExtraName) {
		
		 /*Check Accessory Retained added to Stepper */
		List<WebElement> eNaItemsOnStepper = appDriver.findElements(By.xpath(ObjectRepository.EXTRAANDACC_DISABLED_STEPPER));
		boolean bFlag = false;		
		for(WebElement extras : eNaItemsOnStepper)		
		{ 			
			for(int i=0; i<sExtraName.length; i++)
			{	
				
				if(extras.getText().contains(sExtraName[i]))
				{
					bFlag=true;
				}else
				{
					bFlag=false;
				}
		    }
		}
		if(bFlag)
		{
			Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
		}else
		{
			Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
		}
		
	}
	
	/*******************************************************************************	 
	 *   Function Name : removeAddedExtrasAndAccessories
	 *   Description : Remove Added Extras and Accessories
	 **********************************************************************************/ 	
	public void removeAddedExtrasAndAccessories(String[] itemsToBeRemoved, Actions interactionBuilder)
	{
		scollPage( "600");
		List<WebElement> removeLinks = appDriver.findElements(By.xpath(ObjectRepository.LINK_REMOVE_ADDED_EXTRAANDACC));
		boolean bFlag = false;
		if(!removeLinks.isEmpty()){
			for(WebElement removeExtraAcc : removeLinks)
			{
				for(int j=0; j<itemsToBeRemoved.length; j++)
				{
				if(removeExtraAcc.getText().contains(itemsToBeRemoved[j])){					
					
					interactionBuilder.moveToElement(removeExtraAcc).build().perform();
					interactionBuilder.click(removeExtraAcc).build().perform();
					interactionBuilder.doubleClick(removeExtraAcc).build().perform();
					interactionBuilder.contextClick(removeExtraAcc);
					
					bFlag = true;
					
				}else{
					bFlag = false;
				}				
			}
				
		}
	}
	
	}
	
	/*******************************************************************************	 
	 *   Function Name : validateSIMOnlyPlansPage
	 *   Description : Validate SIM Only Plan Page
	 **********************************************************************************/ 	

	public void validateSIMOnlyPlansPage() {
		
		  /************************** Navigate to Shop link in MDD ***************************/		
		  
		  /*********************** Click on SIM Only plans Link ***************************/		
		  
			Actions actions = new Actions(appDriver);
			//go to Shop link in MDD
			WebElement menuHoverLink = appDriver.findElement(By.xpath(ObjectRepository.NAVIGATE_TO_MDD_SHOP));
			actions.moveToElement(menuHoverLink);
	
			//navigate to sub link inside MDD
			WebElement subLink = appDriver.findElement(By.xpath(ObjectRepository.NAVIGATE_SIM_ONLY_PLANS_LINK));
			actions.moveToElement(subLink);
			//click on sub link inside MDD
			actions.click();
			actions.perform();
		/***************** Validate the PayM Plans page *****************************************************/
		validatePageTitle(VerificationStringConstants.SIM_ONLY_PLANS_HOME_PAGE_TITLE_NEW);

		/*******************Check for PayM Plan radio button checked ***************************************/
		validateElementAttribute( ObjectRepository.SIM_ONLY_PLAN_RADIO_BUTTON, GenericApplicationConstants.CLASS, VerificationStringConstants.CLICK+ " " + VerificationStringConstants.CHECKED);
		
		validateTextOnControl(ObjectRepository.H1, VerificationStringConstants.SIM_ONLY_PLAN_HEADER_TITLE);
		
		/************************  Validate Stepper has no plans added ***************************************/
		validateTextOnControl(ObjectRepository.PLAN_EMPTY_STEPPER, VerificationStringConstants.EMPTY_PLAN_STEPPER_TEXT);
		
		scollPage("900");
		/********************* Validate Personal Use is default selected ********************************/ 		
		validateElementAttribute( ObjectRepository.LABEL_CONSUMER_USE, GenericApplicationConstants.CLASS, VerificationStringConstants.CLICK+ " " + VerificationStringConstants.CHECKED);
				
		scollPage("400");		
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
		 *   Function Name : selectDesiredItemBasedOnSelector
		 *   Description : Select the Desired Items
		 **********************************************************************************/
		public void selectDesiredItemBasedOnSelector(String sHardwareSKUID, String xpathSelector)
		{
			
			List<WebElement> select_Phone_Compared = appDriver.findElements(By.xpath(xpathSelector));	
			System.out.println(select_Phone_Compared.size());
			/*Check if collection is not Empty */                                       
			if(select_Phone_Compared.size()!=0)
			{
				System.out.println(select_Phone_Compared.size());
				boolean bFlag = false;
				for(WebElement selectCompared : select_Phone_Compared )
				{
					if(selectCompared.getAttribute(GenericApplicationConstants.HREF).contains(sHardwareSKUID))
					{						
						selectCompared.click();
						bFlag = true;
						break;
					}else {
						bFlag = false;
					}          	  
				}
					if(bFlag){
						Assert.assertTrue(true, GenericApplicationConstants.ITEMS_MATCH);
					}else{
						Assert.assertTrue(false, GenericApplicationConstants.ITEMS_DONOT_MATCH);
					}
			}
			
			waitForAjaxToLoad();
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
					appDriver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);	
					
					appDriver.get(appDriver.getCurrentUrl());
					appDriver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
					
					if(!appDriver.findElement(By.xpath("//div[@id='content']")).getText().trim().contains(GenericApplicationConstants.BROWSE)){
					removePackageElements = appDriver.findElements(By.xpath(ObjectRepository.REMOVE_PACKAGE));
					
					i=i-1;
					}else{break;}					
				}
				
				clearCookies();
				appDriver.quit();						
				
			}	
		}
		

		public WebElement createElement(String selector) {
			WebElement seleniumElement = appDriver.findElement(By.xpath(selector));
			return seleniumElement;
		}
		
		/*******************************************************************************	 
		 *   Function Name : retrieveExtraAddedToStepper
		 *   Description : Retrieve  Extra  added to the Stepper
		 * @return 
		 **********************************************************************************/
		public ArrayList retrieveExtraAddedToStepper()
		{			
			ArrayList<String> extrasnaccessories = new ArrayList<String>();
			
			/*Check Phone added to Stepper */
			List<WebElement> extrasOnStepper = appDriver.findElements(By.xpath(ObjectRepository.EXTRAANDACC_EMPTY_STEPPER+ObjectRepository.PARENT_ELEMENT + ObjectRepository.STEPPER_BODY + ObjectRepository.LI));
			
			int i=0;
			boolean bFlag = false;		
			for(WebElement extras : extrasOnStepper)		
			{ 	
				if(!extras.getText().isEmpty()){
				extrasnaccessories.add(i, extras.getText());
				i=i+1;
			    }
			}
			
			return extrasnaccessories;			
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
	

