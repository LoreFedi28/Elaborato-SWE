package domainModel.Search;

public class DecoratorSearchSpecialty extends BaseDecoratorSearch{

    private final String specialty;

    public DecoratorSearchSpecialty(Search decoratedSearch, String specialty) {
        super(decoratedSearch);
        this.specialty = specialty;
    }

    @Override
    public String getSearchQuery(){
        return super.getSearchQuery() + " AND v.idVisit IN (SELECT idVisit FROM visitsTags WHERE tagType = 'Specialty' AND tag = ' " + specialty + "')";
    }
}
