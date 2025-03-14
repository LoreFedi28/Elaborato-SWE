package domainModel.Search;

public class DecoratorSearchSpecialty extends BaseDecoratorSearch {

    private final String specialty;

    public DecoratorSearchSpecialty(Search decoratedSearch, String specialty) {
        super(decoratedSearch);
        if (specialty == null || specialty.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialty cannot be null or empty.");
        }
        this.specialty = specialty.trim();
    }

    @Override
    public String getSearchQuery() {
        return String.format("%s AND v.idVisit IN (SELECT idVisit FROM visitsTags WHERE tagType = 'Specialty' AND tag = '%s')",
                super.getSearchQuery(), escapeSql(specialty));
    }

    /**
     * Metodo per evitare SQL Injection, sostituendo apici singoli con doppio apice.
     */
    private String escapeSql(String value) {
        return value.replace("'", "''");
    }
}