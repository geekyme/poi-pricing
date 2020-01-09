package com.example.poipricing.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.example.poipricing.api.config.SyncWorkbook;

/**
 * QuotationController
 */

@RestController
public class QuotationController {
  @Autowired
  SyncWorkbook workbook;
  
  @GetMapping("/calculate/{value}")
  @ResponseBody
  public double calculate(@PathVariable int value) throws Throwable {
    return workbook.calculate(value);
  }

  @GetMapping("/generate")
  public void generate() throws Throwable {
    //create blank workbook
    XSSFWorkbook workbook = new XSSFWorkbook(); 
  
    //Create a blank sheet
    XSSFSheet sheet = workbook.createSheet("Country");
  
    ArrayList<Object[]> data=new ArrayList<Object[]>();
    data.add(new String[]{"Country","Capital","Population"});
    data.add(new Object[]{"India","Delhi",10000});
    data.add(new Object[]{"France","Paris",40000});
    data.add(new Object[]{"Germany","Berlin",20000});
    data.add(new Object[]{"England","London",30000});
  
    //Iterate over data and write to sheet
    int rownum = 0;
    for (Object[] countries : data) {
      Row row = sheet.createRow(rownum++);
  
      int cellnum = 0;
      for (Object obj : countries) {
        Cell cell = row.createCell(cellnum++);
        if (obj instanceof String) {
          cell.setCellValue((String)obj);
        } else if (obj instanceof Double) {
          cell.setCellValue((Double)obj);
        } else if(obj instanceof Integer) {
          cell.setCellValue((Integer)obj);
        }
      }
    }
    Row rowGap = sheet.createRow(rownum++);
    Row row = sheet.createRow(rownum++);
    Cell cellTotal = row.createCell(0);
    cellTotal.setCellValue("Total Population");
    
    // Setting cell formula and cell type
    Cell cell = row.createCell(2);
    cell.setCellFormula("SUM(C2:C5)");
    cell.setCellType(Cell.CELL_TYPE_FORMULA);
    try {
      //Write the workbook to the file system
      FileOutputStream out = new FileOutputStream(new File("CountriesDetails.xlsx"));
      workbook.write(out);
      out.close();
      System.out.println("CountriesDetails.xlsx has been created successfully");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // workbook.close();
    }
  }
}