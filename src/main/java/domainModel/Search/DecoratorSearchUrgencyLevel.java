package domainModel.Search;

public class DecoratorSearchUrgencyLevel extends BaseDecoratorSearch {

    private final String urgencyLevel;

    public DecoratorSearchUrgencyLevel(Search decoratedSearch, String urgencyLevel) {
        super(decoratedSearch);
        if (urgencyLevel == null || urgencyLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("Urgency level cannot be null or empty.");
        }
        this.urgencyLevel = urgencyLevel.trim();
    }

    @Override
    public String getSearchQuery() {
        return String.format("%s AND V.idVisit IN (SELECT idVisit FROM visitsTags WHERE tagType = 'UrgencyLevel' AND tag = '%s')",
                super.getSearchQuery(), escapeSql(urgencyLevel));
    }

    /**
     * Metodo per evitare SQL Injection, sostituendo apici singoli con doppio apice.
     */
    private String escapeSql(String value) {
        return value.replace("'", "''");
    }
}