package es.um.asio.importer.simulator.tasklet.processor;

import java.util.concurrent.ThreadLocalRandom;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Item processor for Randon data.
 */
public class AddRandomElementProcessor implements Tasklet, StepExecutionListener {

    /**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(AddRandomElementProcessor.class);

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
        this.logger.debug("Add random data Processor initialized.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        JsonObject jData;

        final int min = 1;
        final int max = 100;

        for (int i = 0; i < this.completedJDataArray.size(); i++) {
            jData = (JsonObject) this.completedJDataArray.get(i);

            ((JsonObject) jData.get("data")).addProperty("Random:P6:string", ThreadLocalRandom.current().nextInt(min, max + 1));
        }

        return RepeatStatus.FINISHED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExitStatus afterStep(final StepExecution stepExecution) {
        stepExecution.getJobExecution().getExecutionContext().put(
                AbstractCompleteJSONProcessor.COMPLETED_JSON_DATA_BATCH_CONTEXT_OBJECT,
                this.completedJDataArray.toString());
        this.logger.debug("Add Random Data Processor ended.");
        return ExitStatus.COMPLETED;
    }

}
