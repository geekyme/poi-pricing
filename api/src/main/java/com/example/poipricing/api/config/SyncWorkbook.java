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
  private XSSFWorkbook workbook;
  
  public SyncWorkbook() throws Exception {
    FileInputStream file = new FileInputStream(new File("CountriesDetails.xlsx"));
		this.workbook = new XSSFWorkbook(file);
  }

  synchronized public double calculate(int value) {
    Cell cell;

    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

    XSSFSheet sheet = workbook.getSheet("Country");
    cell = getCell(sheet, "C2");
    System.out.println("Setting cell value: " + value);
    cell.setCellValue(value);


    cell = getCell(sheet, "C7");
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