package es.um.asio.importer.simulator.tasklet.reader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

/**
 * CSV file lines reader.
 */
public abstract class AbstractLinesReader implements Tasklet, StepExecutionListener {

    /**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(AbstractLinesReader.class);

    /**
     * File lines.
     */
    private List<String> lines;

    /**
     * Data directory path
     */
    @Value("${app.data.path}")
    private String dataPath;

    /**
     * Loads initial dataset
     */
    @Value("${app.data.initial}")
    private boolean initialDataset;

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeStep(final StepExecution stepExecution) {
        this.logger.debug("Lines Reader initialized.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {

        this.lines = FileUtils.readLines(this.getFile(), Charset.defaultCharset());

        return RepeatStatus.FINISHED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExitStatus afterStep(final StepExecution stepExecution) {
        stepExecution.getJobExecution().getExecutionContext().put(LINES_BATCH_CONTEXT_OBJECT, this.lines);
        this.logger.debug("Lines Reader ended.");
        return ExitStatus.COMPLETED;
    }

    /**
     * Gets the file.
     * 
     * @return
     * @throws IOException
     */
    private File getFile() throws IOException {
        final String fileName = this.getFileName();
        File file;

        if (StringUtils.isBlank(this.dataPath)) {
            file = new ClassPathResource(fileName).getFile();
        } else {
            file = new File(this.dataPath, fileName);
        }

        return file;
    }

    /**
     * Gets data file name.
     *
     * @return Data file name.
     */
    protected abstract String getFileName();

    /**
     * Gets the initialDataSet.
     * 
     * @return
     */
    protected boolean isInitialDataset() {
        return this.initialDataset;
    }

    /**
     * Lines object name in context.
     */
    public static final String LINES_BATCH_CONTEXT_OBJECT = "lines";

}
