package domainModel.State;

import java.time.LocalDateTime;

public class Completed extends State {

    private final LocalDateTime completedTime;

    public Completed(LocalDateTime completedTime) {
        super("Completed");
        if (completedTime == null) {
            throw new IllegalArgumentException("Completed time cannot be null.");
        }
        this.completedTime = completedTime;
    }

    @Override
    public String getExtraInfo() {
        return this.completedTime.toString();
    }
}