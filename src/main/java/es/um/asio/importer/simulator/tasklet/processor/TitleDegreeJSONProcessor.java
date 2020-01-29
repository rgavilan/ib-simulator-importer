package es.um.asio.importer.simulator.tasklet.processor;

/**
 * Item processor for JSON completion purposes (title_degree).
 */
public class TitleDegreeJSONProcessor extends AbstractCompleteJSONProcessor {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getType() {
        return "title_degree";
    }

}
