package com.batch.BatchSQLExample.writer;

import com.batch.BatchSQLExample.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Slf4j
public class LogginItemWriter extends ItemListenerSupport implements StepExecutionListener, ItemWriter<User> {
    @Autowired
    public Job jdbcPaginationJob;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private StepExecution stepExecution;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        stepExecution.setTerminateOnly();
        return ExitStatus.STOPPED;
    }


    @Override
    public void write(List<? extends User> list) throws Exception {
        log.debug("JOB: {} with Status: {} is finishing.", stepExecution.getJobExecution().getJobInstance().getJobName(), stepExecution.getJobExecution().getStatus());

        try {
            Boolean batch = jdbcTemplate.queryForObject("SELECT property_value FROM batch_parameters WHERE name = 'enabled'", Boolean.class);
            if (batch) {
                stepExecution.setTerminateOnly();
                jdbcTemplate.execute("UPDATE batch_parameters set property_value ='false' WHERE name = 'enabled'");
                log.info("JOB: {} END.", stepExecution.getJobExecution().getJobInstance().getJobName());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("Writting users: {}", list);
    }
}
