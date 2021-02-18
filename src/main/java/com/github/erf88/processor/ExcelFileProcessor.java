package com.github.erf88.processor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.item.ItemProcessor;

import com.github.erf88.dto.CustomerDTO;

public class ExcelFileProcessor implements ItemProcessor<CustomerDTO, CustomerDTO>{

	@Override
	public CustomerDTO process(CustomerDTO item) throws Exception {

        String processedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		item.setProcessedAt(processedAt);
		item.setStatus("OK");
		
		return item;
	}

}
