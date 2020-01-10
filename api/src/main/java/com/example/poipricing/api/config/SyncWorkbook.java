package com.example.poipricing.api.config;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class SyncWorkbook {
  private XSSFWorkbook simpleWorkbook;
  private XSSFWorkbook advancedWorkbook;
  
  public SyncWorkbook() throws Exception {
    FileInputStream simple = new FileInputStream(new File("SimpleCalculation.xlsx"));
    FileInputStream advanced = new FileInputStream(new File("AdvancedCalculation.xlsx"));
    this.simpleWorkbook = new XSSFWorkbook(simple);
    this.advancedWorkbook = new XSSFWorkbook(advanced);
  }

  synchronized public double calculateSimple(int value) {
    Cell cell;

    FormulaEvaluator evaluator = simpleWorkbook.getCreationHelper().createFormulaEvaluator();

    XSSFSheet sheet = simpleWorkbook.getSheet("Country");
    cell = getCell(sheet, "C2");
    System.out.println("Setting cell value: " + value);
    cell.setCellValue(value);


    cell = getCell(sheet, "C7");
    CellValue calculated = evaluator.evaluate(cell);

    return calculated.getNumberValue();
  }

  synchronized public double calculateAdvanced(double medicard, double managedCare, double privateInsurance, double selfPay) {
    FormulaEvaluator evaluator = advancedWorkbook.getCreationHelper().createFormulaEvaluator();

    XSSFSheet inputSheet = advancedWorkbook.getSheet("2A- Data Entry Worksheet");
    XSSFSheet outputSheet = advancedWorkbook.getSheet("2B- Est. Rev. Proj. Wksheet");
    System.out.println("Setting medicard: " + medicard);
    getCell(inputSheet, "B6").setCellValue(medicard);
    System.out.println("Setting managedCare: " + managedCare);
    getCell(inputSheet, "B7").setCellValue(managedCare);
    System.out.println("Setting privateInsurance: " + privateInsurance);
    getCell(inputSheet, "B8").setCellValue(privateInsurance);
    System.out.println("Setting selfPay: " + selfPay);
    getCell(inputSheet, "B9").setCellValue(selfPay);
    
    // result
    Cell cell = getCell(outputSheet, "L80");
    CellValue calculated = evaluator.evaluate(cell);

    return calculated.getNumberValue();
  }

  private Cell getCell(XSSFSheet sheet, String addr) {
    CellReference cellReference = new CellReference(addr);
    Row row = sheet.getRow(cellReference.getRow());
    Cell cell = row.getCell(cellReference.getCol()); 

    return cell;
  }
}