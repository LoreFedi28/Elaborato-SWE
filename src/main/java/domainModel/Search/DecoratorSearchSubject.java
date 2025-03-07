package domainModel.Search;

public class DecoratorSearchSubject extends BaseDecoratorSearch{

    private final String subject;

    public DecoratorSearchSubject(Search decoratedSearch, String subject) {
        super(decoratedSearch);
        this.subject = subject;
    }

    @Override
    public String getSearchQuery(){
        return super.getSearchQuery() + " AND v.idVisit IN (SELECT idVisit FROM visitsTags WHERE tagType = 'Subject' AND tag = ' " + subject + "')";
    }
}
