package com.github.erf88.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class PropertiesConfig {

	@Value("${batch.path.in}")
	private String pathIn;
	
	@Value("${batch.path.out}")
	private String pathOut;
	
}
