package domainModel.Search;

import domainModel.Visit;
import java.util.List;

public class DecoratorSearchPrice extends BaseDecoratorSearch{

    private final double maxPrice;

    public DecoratorSearchPrice(Search decoratorSearch, double maxPrice) {
        super(decoratorSearch);
        this.maxPrice = maxPrice;
    }

    @Override
    public String getSearchQuery(){
        return super.getSearchQuery() + " AND price <= '" + maxPrice + "'";
    }
}
