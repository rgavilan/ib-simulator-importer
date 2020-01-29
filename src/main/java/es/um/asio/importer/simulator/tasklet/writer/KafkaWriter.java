package es.um.asio.importer.simulator.tasklet.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import es.um.asio.importer.simulator.tasklet.processor.AbstractCompleteJSONProcessor;

/**
 * Kafka writer.
 */
public class KafkaWriter implements Tasklet, StepExecutionListener {

    /**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(KafkaWriter.class);
   
    /**
     * Kafka template.
     */
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Value("${app.kafka.input-topic-name}")
    private String topicName;
   
    /**
     * CompletedJSON data array.
     */
    private JsonArray completedJDataArray;

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeStep(final StepExecution stepExecution) {
        final ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        this.completedJDataArray = (JsonArray) JsonParser.parseString(
                (String) executionContext.get(AbstractCompleteJSONProcessor.COMPLETED_JSON_DATA_BATCH_CONTEXT_OBJECT));
        this.logger.debug("Lines Writer initialized.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        for (int i = 0; i < this.completedJDataArray.size(); i++) {
            kafkaTemplate.send(topicName, completedJDataArray.get(i).toString());
        }

        return RepeatStatus.FINISHED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExitStatus afterStep(final StepExecution stepExecution) {
        this.logger.debug("Lines Writer ended.");
        return ExitStatus.COMPLETED;
    }
}
