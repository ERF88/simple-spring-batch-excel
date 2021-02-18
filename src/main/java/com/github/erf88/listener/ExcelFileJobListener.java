package com.github.erf88.listener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class ExcelFileJobListener extends JobExecutionListenerSupport {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("Job start: ".concat(jobExecution.getJobInstance().getJobName() + " "
				.concat(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))));
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info("Job end: ".concat(jobExecution.getJobInstance().getJobName() + " "
				.concat(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))));
		
		log.info("Job run time: "
				.concat(Duration.between(jobExecution.getStartTime().toInstant(), jobExecution.getEndTime().toInstant()).toMillis() + " ms"));
		
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("Job successfully executed!");
		}
		
		if(jobExecution.getStatus() == BatchStatus.FAILED) {
			log.error("Job failed with the following exceptions: ");
			jobExecution.getAllFailureExceptions().forEach(ex -> log.error("Exceptions: ".concat(ex.getMessage())));
			System.exit(1);
		}
	}
}
