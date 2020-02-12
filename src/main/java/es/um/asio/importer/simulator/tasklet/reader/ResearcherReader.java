package es.um.asio.importer.simulator.tasklet.reader;

/**
 * Researcher reader.
 */
public class ResearcherReader extends AbstractLinesReader {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFileName() {
        return isInitialDataset() ?  "researcher_initial.csv" : "researcher.csv";
    }

}
