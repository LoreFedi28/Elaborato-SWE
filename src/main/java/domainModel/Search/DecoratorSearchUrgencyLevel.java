package domainModel.Search;

public class DecoratorSearchUrgencyLevel extends BaseDecoratorSearch{
    private final String urgencyLevel;

    public DecoratorSearchUrgencyLevel(Search decoratedSearch, String urgencyLevel) {
        super(decoratedSearch);
        this.urgencyLevel = urgencyLevel;
    }

    @Override
    public String getSearchQuery() {
        return super.getSearchQuery() + " AND V.idVisit IN (SELECT idVisit FROM visitsTags WHERE tagType = 'UrgencyLevel' AND tag = '" + urgencyLevel + "' )";
    }
}
