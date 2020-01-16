package com.example.poipricing.api.config;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PoolWorkbook {
  private ConcurrentLinkedQueue<XSSFWorkbook> pool = new ConcurrentLinkedQueue<>();
  private ConcurrentHashMap<XSSFWorkbook, FormulaEvaluator> workbookFormulaEvaluatorConcurrentHashMap = new ConcurrentHashMap<>();
  private int count = 0;

  public double calculate(int value) throws IOException {
    XSSFWorkbook workbook = getWorkbook();
    FormulaEvaluator formula = workbookFormulaEvaluatorConcurrentHashMap.get(workbook);

    Cell cell;

    XSSFSheet sheet = workbook.getSheet("Country");
    cell = getCell(sheet, "C2");
    cell.setCellValue(value);
    formula.notifyUpdateCell(cell);


    cell = getCell(sheet, "C7");
    CellValue calculated = formula.evaluate(cell);

    releaseWorkbook(workbook);

    return calculated.getNumberValue();
  }

  private Cell getCell(XSSFSheet sheet, String addr) {
    CellReference cellReference = new CellReference(addr);
    Row row = sheet.getRow(cellReference.getRow());
    Cell cell = row.getCell(cellReference.getCol()); 

    return cell;
  }

  private XSSFWorkbook getWorkbook() throws IOException {
    XSSFWorkbook workbook = pool.poll();

    if (workbook == null) {
      FileInputStream fileInputStream = new FileInputStream(new File("SimpleCalculation.xlsx"));
      workbook = new XSSFWorkbook(fileInputStream);
      workbookFormulaEvaluatorConcurrentHashMap.put(workbook, workbook.getCreationHelper().createFormulaEvaluator());
    }

    return workbook;
  }

  private void releaseWorkbook(XSSFWorkbook xssfWorkbook) throws IOException {
    pool.add(xssfWorkbook);
  }
}