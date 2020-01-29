package es.um.asio.importer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

/**
 * Job completion notification listener. 
 */
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
    
    /**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("!!! JOB FINISHED!");
        } else {
            logger.info("Job did not finish. Current status is {}", jobExecution.getStatus());
        }

    }

}
