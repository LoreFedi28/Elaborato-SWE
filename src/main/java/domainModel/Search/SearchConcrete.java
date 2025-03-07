package domainModel.Search;

public class SearchConcrete implements Search {

    private final String query;

    public SearchConcrete() {
        this.query = "SELECT * FROM visits AS V WHERE V.state = 'Available'";
    }

    @Override
    public String getSearchQuery() {
        return query;
    }

}
