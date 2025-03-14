package domainModel.State;

import java.time.LocalDateTime;

public class Cancelled extends State {

    private final LocalDateTime cancelledTime;

    public Cancelled(LocalDateTime cancelledTime) {
        super("Cancelled");
        if (cancelledTime == null) {
            throw new IllegalArgumentException("Cancelled time cannot be null.");
        }
        this.cancelledTime = cancelledTime;
    }

    @Override
    public String getExtraInfo() {
        return this.cancelledTime.toString();
    }
}