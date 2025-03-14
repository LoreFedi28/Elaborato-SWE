package domainModel.Search;

public class DecoratorSearchIsOnline extends BaseDecoratorSearch {

    private final String online;

    public DecoratorSearchIsOnline(Search decoratedSearch, String online) {
        super(decoratedSearch);
        if (online == null || online.trim().isEmpty()) {
            throw new IllegalArgumentException("Online value cannot be null or empty.");
        }
        this.online = online.trim();
    }

    @Override
    public String getSearchQuery() {
        return String.format("%s AND V.idVisit IN (SELECT idVisit FROM visitsTags WHERE tagType = 'Online' AND tag = '%s')",
                super.getSearchQuery(), escapeSql(online));
    }

    /**
     * Metodo per evitare SQL Injection, sostituendo apici singoli con doppio apice.
     */
    private String escapeSql(String value) {
        return value.replace("'", "''");
    }
}