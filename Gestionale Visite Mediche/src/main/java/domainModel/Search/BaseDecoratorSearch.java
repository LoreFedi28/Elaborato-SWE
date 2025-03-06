package main.java.domainModel.Search;

import dao.VisitDAO;

public abstract class BaseDecoratorSearch implements Search{

    private Search decoratedSearch;

    public BaseDecoratorSearch(Search decoratedSearch) {
        this.decoratedSearch = decoratedSearch;
    }

    @Override
    public String getSearchQuery() {
        return decoratedSearch.getSearchQuery();
    }
}