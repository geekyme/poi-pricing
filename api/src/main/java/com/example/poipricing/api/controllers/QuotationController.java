package com.example.poipricing.api.controllers;

import com.example.poipricing.api.config.PoolWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.poipricing.api.config.SyncWorkbook;

/**
 * QuotationController
 */

@RestController
public class QuotationController {
  @Autowired
  SyncWorkbook workbook;
  @Autowired
  PoolWorkbook poolWorkbook;
  
  @GetMapping("/calculate/simple/{value}")
  @ResponseBody
  public double calculateSimple(@PathVariable int value) throws Throwable {
    return workbook.calculateSimple(value);
  }

  @GetMapping("/calculate/advanced")
  @ResponseBody
  public double calculateAdvanced(@RequestParam double medicard, @RequestParam double managedCare, @RequestParam double privateInsurance, @RequestParam double selfPay) throws Throwable {
    return workbook.calculateAdvanced(medicard, managedCare, privateInsurance, selfPay);
  }

  @GetMapping("/calculate/simple/pool/{value}")
  @ResponseBody
  public double calculateSimpleWithPool(@PathVariable int value) throws Throwable {
    return poolWorkbook.calculateSimple(value);
  }

  @GetMapping("/calculate/advanced/pool")
  @ResponseBody
  public double calculateAdvancedWithPool(@RequestParam double medicard, @RequestParam double managedCare, @RequestParam double privateInsurance, @RequestParam double selfPay) throws Throwable {
    return poolWorkbook.calculateAdvanced(medicard, managedCare, privateInsurance, selfPay);
  }
}