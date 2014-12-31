package testListener;
 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.ITestContext;
import org.testng.ITestResult;


public class TestMonitor implements org.testng.ITestListener {
	 
    public Logger log = Logger.getLogger(this.getClass().getName());
 
    public String methodName = null;
    public String status = null;
    public int count = 0;
    File file;
    String startTime;
    String endTime;
    String totalTime;
    
    String executionStartTime;
    String executionEndTime;
    String executionTotalTime;
    String executionActualTime;
    
    int exetotalMins = 0;
    int exetotalSecs = 0;

    int countTotal = 0;
    int countPass = 0;
    int countFail = 0;
    
    public TestMonitor() {
		super();
		// TODO Auto-generated constructor stub
		try {
        	file = new File("src//testListener//Status.txt");
        	file.setReadable(true);
        	
        	    if(!file.exists()){
        	        System.out.println("We had to make a new file.");
        	        file.createNewFile();
        	    }
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
    public void onFinish(ITestContext arg0) {
        log.info("Test " + arg0.getName() + " Ends");
        log.info("-----------------------------------------------------------\n");
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss.SSS");
        Date d= new Date();
        endTime = sdfDate.format(d);
        
        
        // Write the status of the execution into a file
        
        	    FileWriter fileWriter;
				try {
					//calculate the time
					Date d1=sdfDate.parse(startTime);
					Date d2=sdfDate.parse(endTime);
					
					long diff = d2.getTime() - d1.getTime();
					long diffSeconds = diff / 1000 % 60;
					long diffMinutes = diff / (60 * 1000) % 60;
					
					
					totalTime = String.valueOf(diffMinutes)+"Mins "+String.valueOf(diffSeconds)+"Secs";
					
					exetotalMins = (int) (exetotalMins + diffMinutes);
					exetotalSecs = (int) (exetotalSecs + diffSeconds);
					
					
					exetotalMins=exetotalMins+(exetotalSecs/60);
					
					exetotalSecs = exetotalSecs%60;
					
					executionActualTime = String.valueOf(exetotalMins)+"Mins "+String.valueOf(exetotalSecs)+"Secs";
					
					fileWriter = new FileWriter(file, true);

					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					
					//get the last line of the file to print the total time
					//read the entire file and delete the last line and write it back
							List<String> lines = new ArrayList<String>();
		
						    // read the file into lines
						    BufferedReader r = new BufferedReader(new FileReader(file));
						    String in;
						    while ((in = r.readLine()) != null)
						        lines.add(in);
						    r.close();
		
						    // delete the last lines to print the updated new line
						    lines=lines.subList(0, lines.size()-3);
						    
						    // write it back
						    PrintWriter w = new PrintWriter(new FileWriter(file));
						    for (String line : lines)
						        w.println(line);
						    w.close();
					
					
	        	    //print individual method execution status line
	        	    bufferedWriter.write(String.format("%-35s %-25s %-25s %-25s %-25s%n", methodName,status,startTime,endTime,totalTime)+"\n");
	        	    
	        	    //increment the counter for countTotal
	        	    countTotal++;
	        	    
	        	    //end line of complete execution
	        	    /*
	        	    //calculate total execution time
	        	    SimpleDateFormat sdfDatee = new SimpleDateFormat("HH:mm:ss.SSS");
	                Date de= new Date();
	                executionEndTime = de.toString();
	                
	        					Date de1=sdfDatee.parse(executionStartTime);
	        					Date de2=sdfDatee.parse(executionEndTime);
	        	    
	        					long diff1 = de2.getTime() - de1.getTime();
	        					long diffSeconds1 = diff1 / 1000 % 60;
	        					long diffMinutes1 = diff1 / (60 * 1000) % 60;
	        					
	        					
	        					executionTotalTime = String.valueOf(diffMinutes1)+"Mins "+String.valueOf(diffSeconds1)+"Secs";
	        		*/	
	        	    //execution end time
	        	    Date de= new Date();
	                executionEndTime = de.toString();
	                
	        	    bufferedWriter.write("-----------------------------------------------------------------------------------------------------------------------------\n");
	        	    bufferedWriter.write(String.format("%-120s %-120s\n", "Test Execution Ended : "+executionEndTime,executionActualTime));
	        	    bufferedWriter.write("-----------------------------------------------------------------------------------------------------------------------------\n");
	        	    
	        	    //bufferedWriter.write("Total Execution Time: "+executionTotalTime);
	        	    //bufferedWriter.write("Actual Execution Time: "+executionActualTime+"\n");
	        	    //bufferedWriter.write("Total: "+countTotal+"\n");
	        	    //bufferedWriter.write("Pass: "+countPass+"\n");
	        	    //bufferedWriter.write("Fail: "+countFail+"\n");
	        	    
	        	    bufferedWriter.close();

	        	    System.out.println("Done");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

        	    
        
			
		
    }
 
    @Override
    public void onStart(ITestContext arg0) {
        PropertyConfigurator.configure("src/log4j.properties");
        log.info("Test " + arg0.getName() + " Starts");
        //assigning method name to a variable
        
        methodName=arg0.getName();
        Date d= new Date();
        executionStartTime = d.toString();
        
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss.SSS");
        Date methodStartTime= new Date();
        startTime = sdfDate.format(methodStartTime);
        
        if(count==0){
        	count++;
        	
        	FileWriter fileWriter;
			try {
				fileWriter = new FileWriter(file, true);
				
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        	    bufferedWriter.write("Test Execution Started : "+executionStartTime.toString()+"\n");
        	    bufferedWriter.write("-----------------------------------------------------------------------------------------------------------------------------\n");
        	    //bufferedWriter.write("Test Script Name \t\t\t\t\t\t\t Status\n");
        	    bufferedWriter.write(String.format("%-35s %-25s %-25s %-25s %-25s%n", "Test Script Name","Status","StartTime","EndTime","TotalTime"));
        	    bufferedWriter.write("-----------------------------------------------------------------------------------------------------------------------------\n\n\n\n");
        	    bufferedWriter.close();

        	    System.out.println("Done");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        
    }
 
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
        log.info("Test failed within success Percentage");
    }
 
    @Override
    public void onTestFailure(ITestResult arg0) {
        log.info("Test run has failed");
        log.error(arg0.getThrowable().getMessage());
 
        StringWriter sw = new StringWriter();
        arg0.getThrowable().printStackTrace(new PrintWriter(sw));
        String stacktrace = sw.toString();
        log.error(stacktrace.trim());
        // assigning the result of execution
        status = "Fail";
        
        //increment the countFail
        countFail++;
        
    }
 
    @Override
    public void onTestSkipped(ITestResult arg0) {
        log.info("Test skipped");
    }
 
    @Override
    public void onTestStart(ITestResult arg0) {
        log.info("Test Method " + arg0.getMethod().getMethodName()
                + " executing...");
    }
 
    @Override
    public void onTestSuccess(ITestResult arg0) {
        log.info("Test run is successful");
        // assigning the result of execution
        status = "Pass";
        
        //increment the pass count
        countPass++;
    }
 
}
