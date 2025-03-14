package domainModel.Search;

public class DecoratorSearchZone extends BaseDecoratorSearch {

    private final String zone;

    public DecoratorSearchZone(Search decoratedSearch, String zone) {
        super(decoratedSearch);
        if (zone == null || zone.trim().isEmpty()) {
            throw new IllegalArgumentException("Zone cannot be null or empty.");
        }
        this.zone = zone.trim();
    }

    @Override
    public String getSearchQuery() {
        return String.format("%s AND V.idVisit IN (SELECT idVisit FROM visitsTags WHERE tagType = 'Zone' AND tag = '%s')",
                super.getSearchQuery(), escapeSql(zone));
    }

    /**
     * Metodo per evitare SQL Injection, sostituendo apici singoli con doppio apice.
     */
    private String escapeSql(String value) {
        return value.replace("'", "''");
    }
}