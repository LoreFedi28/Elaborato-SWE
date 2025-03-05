package main.java.domainModel.Search;

import dao.VisitDAO;
import domainModel.Visit;

import java.util.ArrayList;
import java.util.List;

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
