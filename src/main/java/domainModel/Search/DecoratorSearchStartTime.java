package domainModel.Search;

import java.time.LocalDateTime;

public class DecoratorSearchStartTime extends BaseDecoratorSearch {

    private final LocalDateTime minStartTime;

    public DecoratorSearchStartTime(Search decoratedSearch, LocalDateTime minStartTime) {
        super(decoratedSearch);
        this.minStartTime = minStartTime;
    }

    @Override
    public String getSearchQuery(){
        return super.getSearchQuery() + " AND startTime >= '" + minStartTime.toString() + "'";
    }
}
