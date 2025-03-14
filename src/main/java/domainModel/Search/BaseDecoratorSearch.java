package domainModel.Search;

public abstract class BaseDecoratorSearch implements Search {

    private final Search decoratedSearch;

    public BaseDecoratorSearch(Search decoratedSearch) {
        if (decoratedSearch == null) {
            throw new IllegalArgumentException("Decorated search cannot be null.");
        }
        this.decoratedSearch = decoratedSearch;
    }

    @Override
    public String getSearchQuery() {
        return decoratedSearch.getSearchQuery();
    }
}