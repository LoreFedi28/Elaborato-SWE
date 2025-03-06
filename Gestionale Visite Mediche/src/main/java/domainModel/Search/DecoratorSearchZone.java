package main.java.domainModel.Search;

public class DecoratorSearchZone extends BaseDecoratorSearch{

    private final String zone;

    public DecoratorSearchZone(Search decoratedSearch, String zone){
        super(decoratedSearch);
        this.zone = zone;
    }

    @Override
    public String getSearchQuery(){
        return super.getSearchQuery() + " AND V.idVisit IN (SELECT idVisit from visitsTags WHERE tagType = 'Zone' AND tag = '" + zone + "')";
    }

}
