package es.um.asio.importer.simulator.tasklet.reader;

/**
 * Academic institution reader.
 */
public class AcademicInstitutionReader extends AbstractLinesReader {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFileName() {
        return isInitialDataset() ?  "academic_institution_initial.csv" : "academic_institution.csv";
    }

}
