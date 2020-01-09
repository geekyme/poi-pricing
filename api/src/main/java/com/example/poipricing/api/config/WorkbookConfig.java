package com.example.poipricing.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkbookConfig {
	@Bean
  public SyncWorkbook getWorkbook() throws Exception {
		SyncWorkbook syncWorkbook = new SyncWorkbook();
		
    return syncWorkbook;
  }
}