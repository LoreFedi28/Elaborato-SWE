package domainModel.Search;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DecoratorSearchStartTime extends BaseDecoratorSearch {

    private final LocalDateTime minStartTime;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DecoratorSearchStartTime(Search decoratedSearch, LocalDateTime minStartTime) {
        super(decoratedSearch);
        if (minStartTime == null) {
            throw new IllegalArgumentException("Start time cannot be null.");
        }
        this.minStartTime = minStartTime;
    }

    @Override
    public String getSearchQuery() {
        return String.format("%s AND startTime >= '%s'", super.getSearchQuery(), minStartTime.format(FORMATTER));
    }
}