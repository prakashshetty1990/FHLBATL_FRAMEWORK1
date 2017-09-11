package Utilities;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;



public class DataDriver
{
  public static int rowCount = 0; public static int colCount = 0; public static int maxRows = 100;
  
  public static String wkbook = "";
  public static int currentExcelSheetNo;
  public static File nf;
  public static Workbook w;
  
  public DataDriver() {}
  
  public static void useExcelSheet(String wkbook, int dsheet) { currentExcelBook = wkbook;
    currentExcelSheetNo = dsheet;
    nf = new File(currentExcelBook);
    if (nf.exists()) {
      try {
        w = Workbook.getWorkbook(nf);
      }
      catch (BiffException e) {
        e.printStackTrace();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      if (w.getNumberOfSheets() >= dsheet) {
        s = w.getSheet(dsheet - 1);
        updateRowCount();
        updateColCount();
      }
    }
  }
  
  public static String getData(int row, int col)
  {
    Cell c = s.getCell(col - 1, row - 1);
    return c.getContents();
  }
  
  public static Sheet s;
  public static String currentExcelBook;
  public static String currentExcelSheet;
  public static String retrieve(String Label) { return retrieve(GenericKeywords.testCaseDataRow, Label); }
  


  public static String retrieve(int datasetNo, String colLabel)
  {
    return getData(datasetNo + 1, returnColNo(datasetNo, colLabel));
  }
  

  public static String retrieve(int intLabelRow, int intDataRow, String colLabel) { return getData(intDataRow, returnColNo(intLabelRow, colLabel)); }
  
  public static int returnColNo(int datasetNo, String colLabel) {
    boolean flag = true;
    int temp = 0;
    while (flag)
    {
      try
      {

        temp++;
        if (getData(datasetNo, temp).trim().equalsIgnoreCase(colLabel.trim()))
        {
          flag = false;
          return temp;
        }
      }
      catch (Exception e)
      {
        Common.testStepFailed("'" + colLabel + "' label not found" + " row no-->" + datasetNo + " column no-->" + temp);
        Common.testFailed();
      }
    }
    
    return 0;
  }
  
  public static int returnRowNo(int colIndex, String rowLabel) { boolean flag = true;
    int temp = 0;
    while (flag)
    {
      try
      {

        temp++;
        if (getData(temp, colIndex).trim().equalsIgnoreCase(rowLabel.trim()))
        {
          flag = false;
          return temp;
        }
      }
      catch (Exception e)
      {
        Common.testStepFailed("'" + rowLabel + "' label not found" + " row no-->" + colIndex + " column no-->" + temp);
        Common.testFailed();
      }
    }
    
    return 0;
  }
  
  public static void updateRowCount() {
    boolean flag = true;
    int temp = 0;
    while (flag) {
      temp++;
      try {
        if (getData(temp, 1).trim().length() == 0) {
          flag = false;
        }
      } catch (Exception e) {
        GenericKeywords.rowCount = temp - 2;
        break;
      }
    }
  }
  
  public static void updateColCount()
  {
    boolean flag = true;
    int temp = 0;
    while (flag) {
      temp++;
      try {
        if (getData(1, temp).trim().length() == 0) {
          flag = false;
        }
      } catch (Exception e) {
        GenericKeywords.colCount = temp - 1;
        break;
      }
    }
  }
  
  public static void putData(int row, int col, String data) {
    String wkbook;
    if (GenericKeywords.currentExcelBook.length() != 0) {
      wkbook = GenericKeywords.currentExcelBook;
    } else {
      wkbook = "";
      Common.writeToLogFile("ERROR", 
        "Excel Book - Not initialized/defined earlier"); }    
    int dsheet; if (GenericKeywords.currentExcelSheet != 0) {
      dsheet = GenericKeywords.currentExcelSheet;
    } else
      dsheet = 0;
    Common.writeToLogFile("ERROR", 
      "Excel Sheet - Not initialized/defined earlier");
    
    File nf = new File(wkbook);
    WritableWorkbook w = null;
    WritableSheet s = null;
    WritableCell c = null;
    try {
      if (nf.exists()) {
        Workbook wb = Workbook.getWorkbook(nf);
        w = Workbook.createWorkbook(new File(wkbook), wb);
        
        if (w.getNumberOfSheets() >= dsheet) {
          s = w.getSheet(dsheet - 1);
          try
          {
            c = s.getWritableCell(col - 1, row - 1);
            Common.writeToLogFile(
              "INFO", 
              "Value of cell before modification : " + 
              c.getContents());
            Label l = new Label(col - 1, row - 1, data);
            s.addCell(l);
            w.write();
            Cell c1 = s.getCell(col - 1, row - 1);
            Common.writeToLogFile(
              "INFO", 
              "Value of cell after modification : " + 
              c1.getContents());
          }
          catch (Exception e) {
            Common.writeToLogFile("ERROR", 
              "Data cannot be modified in the cell | Row:" + 
              row + ", Col:" + col);
          }
        } else {
          Common.writeToLogFile("ERROR", 
            "Invalid sheet number :" + dsheet);
        }
        
        w.close();
      } else {
        Common.writeToLogFile("ERROR", 
          "Specified File/Path does not exist :" + wkbook + 
          ". Please check.");
      }
    } catch (Exception e) {
      Common.writeToLogFile("ERROR", 
        "Data cannot be modified in the cell | Row:" + row + 
        ", Col:" + col);
    }
  }
  
  public static void closeExcelSheet() {
    currentExcelBook = "";
    currentExcelSheetNo = 0;
    w.close();
    nf = null;
  }
}
