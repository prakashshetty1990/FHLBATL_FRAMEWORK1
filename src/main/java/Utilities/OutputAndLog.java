package Utilities;


import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

public class OutputAndLog
{
  public static String am_pm;
  public static String min;
  public static String hr;
  public static String sec;
  public static int yr;
  public static String mon;
  public static String day;
  
  public OutputAndLog() {}
  
  public static void createOutputDirectory()
  {
    java.io.File curdir = new java.io.File(".");
    
    Calendar calendar = new java.util.GregorianCalendar();
    

    hr = "0" + calendar.get(10);
    hr = hr.substring(hr.length() - 2);
    
    min = "0" + calendar.get(12);
    min = min.substring(min.length() - 2);
    

    sec = "0" + calendar.get(13);
    sec = sec.substring(sec.length() - 2);
    
    yr = calendar.get(1);
    

    mon = "0" + (calendar.get(2) + 1);
    mon = mon.substring(mon.length() - 2);
    
    day = "0" + calendar.get(5);
    day = day.substring(day.length() - 2);
    
    if (calendar.get(9) == 0) {
      am_pm = "AM";
    } else {
      am_pm = "PM";
    }    
    GenericKeywords.timeStamp = yr + "_" + mon + "_" + day + "_" + hr + "_" + min + "_" + sec + "_" + am_pm;    
    try
    {
      GenericKeywords.outputDirectory = curdir.getCanonicalPath() + "\\TestResults\\" + yr + "_" + mon + "_" + day + "_" + hr + "_" + min + "_" + sec + "_" + am_pm;

    }
    catch (IOException e)
    {
      System.out.println("IO Error while creating Output Directory : " + GenericKeywords.outputDirectory);
    }
    
    createLogFile();
  }
  

  public static void createLogFile()
  {
    Properties props = new Properties();
    String propsFileName = "./Config/log4j.properties";
    
    try
    {
      java.io.FileInputStream configStream = new java.io.FileInputStream(propsFileName);
      props.load(configStream);
      


      String myStr = GenericKeywords.outputDirectory.substring(GenericKeywords.outputDirectory.length() - 22);
      
      myStr = "./TestResults/" + myStr + "/LogFile.log";
      

      props.setProperty("log4j.appender.FA.File", myStr);
      java.io.FileOutputStream output = new java.io.FileOutputStream(propsFileName);
      props.store(output, "Output Directory updated : " + GenericKeywords.outputDirectory);
      

      output.close();
      configStream.close();
      

      org.apache.log4j.PropertyConfigurator.configure(propsFileName);
      
      GenericKeywords.logger = org.apache.log4j.Logger.getLogger(myStr);
      Common.writeToLogFile("INFO", "Startup activites...");
      
      Common.writeToLogFile("INFO", "Test Output Directory creation successful :" + GenericKeywords.outputDirectory);
      Common.writeToLogFile("INFO", "Log File creation successful : LogFile.log");
    }
    catch (IOException ex)
    {
      System.out.println("There was an error creating the log file");
    }
  }
}
