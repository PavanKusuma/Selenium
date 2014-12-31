Selenium
========

Create a project
Configuring libraries / buildpath
Structure of the Selenium automation Project
Creating xml to run the test scripts using TestNG
Tracking the test execution and reporting

Create a Project

1.	Ensure you are in “Java” perspective in eclipse. 
 
2.	Navigate to File Menu and Click on ‘Java new Project’

 

3.	New Java Project window will be opened asking for inputs needed to create a project
 

4.	Provide a project name and then click on ‘next’ button.  Java settings window will be shown where basic predefined configurations for the projects is visible
a.	Initial source folder for the project
 
b.	Initial Java library for the project

 	

5.	Proceed and click on ‘Finish’ to create a project. Project will be created with the specified name and will be shown in ‘Package Explorer’ which is present in specified workspace.
 

Configuring Libraries / Buildpath

In order write the scripts and execute them using the predefined classes or Libraries, we need to configure the required libraries and JAR files to the project. Follow the below steps.

1.	Right-click on the project in the ‘Package Explorer’ and select ‘Properties’
Or
Select the Project and click ‘Alt+Enter’ to open properties of the project
 
2.	Select the ‘Java Build Path’ from the left hand panel of properties window.
3.	To Add external Jars to the project, click on ‘Add External JARs…’ button.
 
4.	Ensure that you place all the JAR required for the project at one location. Browse the location and select all required JARs and click on ‘open’ button and then click on ‘ok’ button of properties window.
5.	Main JARs among the configured JARs are
a.	selenium-java-2.31.0.jar
b.	selenium-server-standalone-2.31.0
c.	testng-6.5.1.jar
d.	poi-3.7-20101029.jar
e.	saxon-8.7.jar
f.	SaxonLiaison.jar
g.	tools.jar

6.	Selected JARs will be added to the project under ‘Referenced Libraries’ inside the project folder as shown below.
 
7.	After completing the JAR files configuration for the project. TestNG eclipse plugin should be available to the workspace for executing our test scripts from the workspace projects.

8.	Navigate to the ‘Plugins’ folder present inside your eclipse folder, place the TestNG folder inside it.
Or
This plugin can be installed through Eclipse as follows:
a.	Launch Eclipse
b.	Go to Help-Install New Software. “Install” window will be opened.
c.	Click on “Add” button.
d.	Enter the “Name” as “TestNG”.
e.	Enter the “Location” as “http://beust.com/eclipse”.
f.	Click on “OK” button.
g.	Select the Checkbox named “TestNG” and click “Next” button.
Complete the installation.




Structure of the Selenium automation Project

We classify the project structure based on the process that we follow in executing test case manually.
We organize our project in the following mentioned groups
1.	Generic Constants
2.	Subscripts
3.	Test Data
4.	Utilities
5.	Object Repository
6.	Test suite

 
Generic constants:
Here we declare all the constants that we use for cross checking with the data shown in shop. For example, to check the H1 text of heading present in a page, we need to know with what text you are going to cross check. That value is assigned to a variable in a class and place in this package. Hence it is accessible across the project and is unique.
Subscripts:
A part of flow in execution which is same in many scripts can be placed at one location. The code for that part of flow is written in methods of class file and is place in subscripts package. In Simple terms, we call this as reusable code. Hence it is easy to call this code from any script across the package.
Test Data:
The data that we use for input in the form and which is independent to particular test script is placed in excel file under this Test Data source folder. It can be updated based on the specified test script that we need.
Utilities:
In order to fetch the values from Test Data we write the code in classes present in utilities package. From any script, this can be accessed and data can be extracted for the needed.
Object Repository:
For automating a particular flow in html page, we need to gather the information of the html elements that we are going to access.  Html element can be accessed using its XPath.  Initially, we capture the XPaths of the elements that we are going to access and assign them to the variables in a class. That class is placed under this Package which is accessible across the project.
Test suite:
Main automated test script developed for every functional test case is written in class inside package present in Test suite. These all set of classes are considered as Test Suite. We use access Test Data using Utilities and navigate with the flow by getting elements from Object Repository. Reusable code is called using objects from the classes inside Subscripts package.
Structure of a test script

A test script is nothing but a java class. In the class you write the code inside methods. These methods are classified as follows:
a.	BeforeTest  - @BeforeTest()
b.	Test  -  @Test()
c.	AfterTest  -  @AfterTest()
TestNG will pick the method present in the class based on the above mentioned classification. Those will be placed accordingly above the method that you want to classify for execution.
For example, initially we need to open a browser by configuring it. That code is written under method that comes under BeforeTest.
Creating xml to run the test scripts using TestNG

To execute a test script, we need to run an xml file using ‘TestNG Suite’. Create a xml file under the project. It should contain the <suite> tag under which you provide a script name to execute as shown below.
<suite name="Suite"  preserve-order="true" parallel="false">  
<parameters>   
 <parameter name="browser" value="firefox" />
 <parameter name="version" value="17" />   
 <parameter name="applicationURL" value="http://XX.XXX.XX.XXX/index.html" />
</parameters>    
  
<test name="Test1">  
<classes>      
       <class name="Package.TestScriptName"/>         
    </classes>
</test> 
  
<suite> tag will contain <test> tag where in you provide the class name(test script) to pick and execute the methods inside is it. You can also pass the parameters from this xml to the methods. Declare the parameters under <suite> tag and add the below code above the method
@Parameters( {"browser", "version", "applicationURL" })

The parameters present inside the parameters section will be passed to the method.

Tracking the test execution and reporting

