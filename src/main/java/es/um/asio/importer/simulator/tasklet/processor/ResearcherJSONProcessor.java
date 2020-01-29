package es.um.asio.importer.simulator.tasklet.processor;

/**
 * Item processor for JSON completion purposes (researcher).
 */
public class ResearcherJSONProcessor extends AbstractCompleteJSONProcessor {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getType() {
        return "researcher";
    }

}
