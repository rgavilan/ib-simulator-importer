package es.um.asio.importer.simulator.tasklet.processor;

/**
 * Item processor for JSON completion purposes (academic_institution).
 */
public class AcademicInstitutionJSONProcessor extends AbstractCompleteJSONProcessor {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getType() {
        return "academic_institution";
    }

}
