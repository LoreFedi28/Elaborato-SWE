package domainModel.Search;

/**
 * Implementazione concreta dell'interfaccia Search.
 * Definisce una query di base per ottenere solo le visite disponibili.
 */
public class SearchConcrete implements Search {

    private final String query;

    /**
     * Costruttore predefinito che inizializza la query di base.
     */
    public SearchConcrete() {
        this.query = "SELECT * FROM visits AS V WHERE V.state = 'Available'";
    }

    /**
     * Costruttore che permette di specificare una query personalizzata.
     *
     * @param customQuery La query SQL personalizzata.
     */
    public SearchConcrete(String customQuery) {
        if (customQuery == null || customQuery.trim().isEmpty()) {
            throw new IllegalArgumentException("Query cannot be null or empty.");
        }
        this.query = customQuery.trim();
    }

    @Override
    public String getSearchQuery() {
        return query;
    }
}