package com.batch.BatchSQLExample.listener;

import com.batch.BatchSQLExample.model.BatchParameters;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Log4j2
public class StopJobListener extends JobExecutionListenerSupport {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        ///log.info("JOB: {} with Status: {} is finishing.", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());

        //BatchParameters batch = jdbcTemplate.queryForObject("SELECT * FROM batch_parameters where name = ?", new Object[]{new Object[]{"enabled"}}, BatchParameters.class);
        // if(BooleanUtils.toBooleanObject(batch.getPropertyValue())) {

        //}

        try {
            jobExecution.getExitStatus().getExitCode();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("JOB: {} END.", jobExecution.getJobInstance().getJobName());
    }
}
