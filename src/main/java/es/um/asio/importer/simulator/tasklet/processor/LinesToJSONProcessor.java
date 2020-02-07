package es.um.asio.importer.simulator.tasklet.processor;

import java.util.List;

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

import es.um.asio.importer.simulator.tasklet.reader.AbstractLinesReader;

/**
 * Items processor.
 */
public class LinesToJSONProcessor implements Tasklet, StepExecutionListener {
    /**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(LinesToJSONProcessor.class);

    /**
     * File lines.
     */
    private List<String> lines;

    /**
     * JSON data array.
     */
    private JsonArray jDataArray;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void beforeStep(final StepExecution stepExecution) {
        final ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        this.lines = (List<String>) executionContext.get(AbstractLinesReader.LINES_BATCH_CONTEXT_OBJECT);
        this.jDataArray = new JsonArray();
        this.logger.debug("Lines Processor initialized.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        String[] headers = null;
        String[] fields;

        for (int i = 0; i < this.lines.size(); i++) {
            fields = this.lines.get(i).split(DELIMITER);

            if (i == 0) {
                headers = fields;
            } else {
                this.jDataArray.add(this.generateJSON(fields, headers));
            }
        }

        return RepeatStatus.FINISHED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExitStatus afterStep(final StepExecution stepExecution) {
        stepExecution.getJobExecution().getExecutionContext().put(JSON_DATA_BATCH_CONTEXT_OBJECT,
                this.jDataArray.toString());
        this.logger.debug("Lines Processor ended.");
        return ExitStatus.COMPLETED;
    }

    /**
     * Generate JSON object from an array of fields.
     *
     * @param fields
     *            Array of fields
     * @param headers
     *            Headers
     * @return {@link JsonObject}
     */
    private JsonObject generateJSON(final String[] fields, final String[] headers) {
        final JsonObject jData = new JsonObject();

        for (int i = 0; i < fields.length; i++) {
            jData.addProperty(this.getHeader(headers, i), fields[i]);
        }

        return jData;
    }

    /**
     * Gets header at provided position
     *
     * @param headers
     *            Array of headers.
     * @param position
     *            Position
     * @return Header at position, {@code null instead}
     */
    private String getHeader(final String[] headers, final int position) {
        String header = null;

        if ((headers != null) && (position < headers.length)) {
            header = headers[position];
        } else if (this.logger.isWarnEnabled()) {
            this.logger.warn(String.format("Is not posible to recover the header with position %s", position));
        }

        return header;
    }

    /**
     * JSON data object name in context.
     */
    public static final String JSON_DATA_BATCH_CONTEXT_OBJECT = "json-data";

    private static final String DELIMITER = ",";
}
