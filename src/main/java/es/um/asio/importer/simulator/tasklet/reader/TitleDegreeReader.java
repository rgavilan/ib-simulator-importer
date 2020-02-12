package es.um.asio.importer.simulator.tasklet.reader;

/**
 * Title - Degree reader.
 */
public class TitleDegreeReader extends AbstractLinesReader {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFileName() {
        return isInitialDataset() ?  "title_degree_initial.csv" : "title_degree.csv";
    }

}
