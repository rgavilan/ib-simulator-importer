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
        return "academic_institution.csv";
    }

}
