package Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;

import jxl.Sheet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class PropertiesFile
  extends DataDriver
{
  public static File nf;
  
  public PropertiesFile() {}
  
  public static final ArrayList<String> testCases = new ArrayList<String>();
  
  public static void properties() {
    Properties prop = new Properties();
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = dbf.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element rootElement = doc.createElement("suite");
      doc.appendChild(rootElement);
      Element test = doc.createElement("test");
      Element classes = doc.createElement("classes");
      Element classs = doc.createElement("class");
      Element methods = doc.createElement("methods");
      rootElement.appendChild(test);
      test.appendChild(classes);
      classes.appendChild(classs);
      classes.appendChild(methods);
      
      rootElement.setAttribute("name", "Suite");
      rootElement.setAttribute("parallel", "none");
      rootElement.setAttribute("configfailurepolicy", "continue");
      test.setAttribute("name", "Test");
      test.setAttribute("preserve-order", "true");
      classs.setAttribute("name", "TestCases.TestCases");
      TransformerFactory tff = TransformerFactory.newInstance();
      DataDriver.useExcelSheet("./src/test/resources/TestConfiguration.xls", 1);
      Sheet readsheet = w.getSheet(0);
      
      for (int i = 1; i < readsheet.getRows(); i++) {
        String Keyword = readsheet.getCell(1, i).getContents();
        String value = readsheet.getCell(2, i).getContents();
        prop.setProperty(Keyword, value);
      }
      

      prop.store(new FileOutputStream(
        "./config/TestConfiguration.properties"), null);
      w.close();      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static String Keyword;
}
