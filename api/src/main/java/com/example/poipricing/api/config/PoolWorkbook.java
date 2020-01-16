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
  private ConcurrentHashMap<String, ConcurrentLinkedQueue<XSSFWorkbook>> poolMap = new ConcurrentHashMap<>();
  private ConcurrentHashMap<XSSFWorkbook, FormulaEvaluator> workbookFormulaEvaluatorConcurrentHashMap = new ConcurrentHashMap<>();
  private int count = 0;

  public double calculateSimple(int value) throws IOException {
    String filePath = "SimpleCalculation.xlsx";
    XSSFWorkbook workbook = getWorkbook(filePath);
    FormulaEvaluator formula = workbookFormulaEvaluatorConcurrentHashMap.get(workbook);

    Cell cell;

    XSSFSheet sheet = workbook.getSheet("Country");
    cell = getCell(sheet, "C2");
    cell.setCellValue(value);
    formula.notifyUpdateCell(cell);


    cell = getCell(sheet, "C7");
    CellValue calculated = formula.evaluate(cell);

    releaseWorkbook(filePath, workbook);

    return calculated.getNumberValue();
  }

  public double calculateAdvanced(double medicard, double managedCare, double privateInsurance, double selfPay) throws IOException {
    String filePath = "AdvancedCalculation.xlsx";
    XSSFWorkbook workbook = getWorkbook(filePath);
    FormulaEvaluator formula = workbookFormulaEvaluatorConcurrentHashMap.get(workbook);

    Cell cell;
    XSSFSheet inputSheet = workbook.getSheet("2A- Data Entry Worksheet");
    XSSFSheet outputSheet = workbook.getSheet("2B- Est. Rev. Proj. Wksheet");

    System.out.println("Setting medicard: " + medicard);
    cell = getCell(inputSheet, "B6");
    cell.setCellValue(medicard);
    formula.notifyUpdateCell(cell);

    System.out.println("Setting managedCare: " + managedCare);
    cell = getCell(inputSheet, "B7");
    cell.setCellValue(managedCare);
    formula.notifyUpdateCell(cell);

    System.out.println("Setting privateInsurance: " + privateInsurance);
    cell = getCell(inputSheet, "B8");
    cell.setCellValue(privateInsurance);
    formula.notifyUpdateCell(cell);

    System.out.println("Setting selfPay: " + selfPay);
    cell = getCell(inputSheet, "B9");
    cell.setCellValue(selfPay);
    formula.notifyUpdateCell(cell);

    // result
    cell = getCell(outputSheet, "L80");
    CellValue calculated = formula.evaluate(cell);

    releaseWorkbook(filePath, workbook);

    return calculated.getNumberValue();
  }

  private Cell getCell(XSSFSheet sheet, String addr) {
    CellReference cellReference = new CellReference(addr);
    Row row = sheet.getRow(cellReference.getRow());
    Cell cell = row.getCell(cellReference.getCol()); 

    return cell;
  }

  private XSSFWorkbook getWorkbook(String filePath) throws IOException {
    ConcurrentLinkedQueue<XSSFWorkbook> workbookPool = poolMap.get(filePath);
    if (workbookPool == null) {
      poolMap.put(filePath, new ConcurrentLinkedQueue<>());
      workbookPool = poolMap.get(filePath);
    }

    XSSFWorkbook workbook = workbookPool.poll();

    if (workbook == null) {
      System.out.println(++count);
      FileInputStream fileInputStream = new FileInputStream(new File(filePath));
      workbook = new XSSFWorkbook(fileInputStream);
      workbookFormulaEvaluatorConcurrentHashMap.put(workbook, workbook.getCreationHelper().createFormulaEvaluator());
    }

    return workbook;
  }

  private void releaseWorkbook(String filePath, XSSFWorkbook xssfWorkbook) throws IOException {
    ConcurrentLinkedQueue<XSSFWorkbook> workbookPool = poolMap.get(filePath);
    workbookPool.add(xssfWorkbook);
  }
}