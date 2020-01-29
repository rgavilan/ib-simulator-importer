package es.um.asio.importer.simulator.tasklet.processor;

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
 * Item processor for JSON completion purposes.
 */
public abstract class AbstractCompleteJSONProcessor implements Tasklet, StepExecutionListener {

    /**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(AbstractCompleteJSONProcessor.class);

    /**
     * JSON data array.
     */
    private JsonArray jDataArray;

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
        this.jDataArray = (JsonArray) JsonParser
                .parseString((String) executionContext.get(LinesToJSONProcessor.JSON_DATA_BATCH_CONTEXT_OBJECT));
        this.completedJDataArray = new JsonArray();
        this.logger.debug("Complete JSON Processor initialized.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        JsonObject jData;

        for (int i = 0; i < this.jDataArray.size(); i++) {
            jData = new JsonObject();
            jData.addProperty("id", i);
            jData.addProperty("type", this.getType());
            jData.add("data", this.jDataArray.get(i));

            this.completedJDataArray.add(jData);
        }

        return RepeatStatus.FINISHED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExitStatus afterStep(final StepExecution stepExecution) {
        stepExecution.getJobExecution().getExecutionContext().put(COMPLETED_JSON_DATA_BATCH_CONTEXT_OBJECT,
                this.completedJDataArray.toString());
        this.logger.debug("Complete JSON Processor ended.");
        return ExitStatus.COMPLETED;
    }

    /**
     * Gets item type.
     *
     * @return Item type.
     */
    protected abstract String getType();

    /**
     * JSON data object name in context.
     */
    public static final String COMPLETED_JSON_DATA_BATCH_CONTEXT_OBJECT = "completed-json-data";
}
