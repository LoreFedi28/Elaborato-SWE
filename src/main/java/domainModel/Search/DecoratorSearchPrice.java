package domainModel.Search;

import java.util.Locale;

public class DecoratorSearchPrice extends BaseDecoratorSearch {

    private final double maxPrice;

    public DecoratorSearchPrice(Search decoratorSearch, double maxPrice) {
        super(decoratorSearch);
        if (maxPrice < 0) {
            throw new IllegalArgumentException("Max price cannot be negative.");
        }
        this.maxPrice = maxPrice;
    }

    @Override
    public String getSearchQuery() {
        return String.format(Locale.US, "%s AND price <= %.2f", super.getSearchQuery(), maxPrice);
    }
}