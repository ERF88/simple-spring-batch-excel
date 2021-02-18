package com.github.erf88.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.erf88.dto.CustomerDTO;
import com.github.erf88.listener.ExcelFileJobListener;
import com.github.erf88.processor.ExcelFileProcessor;
import com.github.erf88.reader.ExcelFileReader;
import com.github.erf88.writer.ExcelFileWriter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ExcelFileJobConfig {
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final ExcelFileJobListener jobListener;
	private final PropertiesConfig properties;
	
    @Bean
    public Job excelFileJob() {
        return jobBuilderFactory.get("excelFileJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobListener)
                .start(excelFileStep())
                .build();
    }
	
    @Bean
    public Step excelFileStep() {
        return stepBuilderFactory.get("excelFileStep")
                .<CustomerDTO, CustomerDTO>chunk(1)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
	
	@Bean
	@StepScope
	public ExcelFileReader reader() {
		return new ExcelFileReader(properties.getPathIn());
	}

	@Bean
	public ExcelFileProcessor processor() {
		return new ExcelFileProcessor();
	}
	
	@Bean
	public ExcelFileWriter writer() {
		return new ExcelFileWriter(properties.getPathOut());
	}
	
}
