package com.example.poipricing.api;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
  public XSSFWorkbook getWorkbook() throws Exception {
		FileInputStream file = new FileInputStream(new File("CountriesDetails.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		
    return workbook;
  }
}
