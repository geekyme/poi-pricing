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
  private FormulaEvaluator simpleFormula;
  private FormulaEvaluator advancedFormula;
  
  public SyncWorkbook() throws Exception {
    FileInputStream simple = new FileInputStream(new File("SimpleCalculation.xlsx"));
    FileInputStream advanced = new FileInputStream(new File("AdvancedCalculation.xlsx"));
    this.simpleWorkbook = new XSSFWorkbook(simple);
    this.advancedWorkbook = new XSSFWorkbook(advanced);
    this.simpleFormula = this.simpleWorkbook.getCreationHelper().createFormulaEvaluator();
    this.advancedFormula = this.advancedWorkbook.getCreationHelper().createFormulaEvaluator();
  }

  synchronized public double calculateSimple(int value) {
    Cell cell;

    XSSFSheet sheet = simpleWorkbook.getSheet("Country");
    cell = getCell(sheet, "C2");
    System.out.println("Setting cell value: " + value);
    cell.setCellValue(value);
    simpleFormula.notifyUpdateCell(cell);


    cell = getCell(sheet, "C7");
    CellValue calculated = simpleFormula.evaluate(cell);

    return calculated.getNumberValue();
  }

  synchronized public double calculateAdvanced(double medicard, double managedCare, double privateInsurance, double selfPay) {
    Cell cell;
    XSSFSheet inputSheet = advancedWorkbook.getSheet("2A- Data Entry Worksheet");
    XSSFSheet outputSheet = advancedWorkbook.getSheet("2B- Est. Rev. Proj. Wksheet");

    System.out.println("Setting medicard: " + medicard);
    cell = getCell(inputSheet, "B6");
    cell.setCellValue(medicard);
    advancedFormula.notifyUpdateCell(cell);

    System.out.println("Setting managedCare: " + managedCare);
    cell = getCell(inputSheet, "B7");
    cell.setCellValue(managedCare);
    advancedFormula.notifyUpdateCell(cell);

    System.out.println("Setting privateInsurance: " + privateInsurance);
    cell = getCell(inputSheet, "B8");
    cell.setCellValue(privateInsurance);
    advancedFormula.notifyUpdateCell(cell);

    System.out.println("Setting selfPay: " + selfPay);
    cell = getCell(inputSheet, "B9");
    cell.setCellValue(selfPay);
    advancedFormula.notifyUpdateCell(cell);
    
    // result
    cell = getCell(outputSheet, "L80");
    CellValue calculated = advancedFormula.evaluate(cell);

    return calculated.getNumberValue();
  }

  private Cell getCell(XSSFSheet sheet, String addr) {
    CellReference cellReference = new CellReference(addr);
    Row row = sheet.getRow(cellReference.getRow());
    Cell cell = row.getCell(cellReference.getCol()); 

    return cell;
  }
}