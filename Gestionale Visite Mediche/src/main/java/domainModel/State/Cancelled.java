package main.java.domainModel.State;

import java.time.LocalDateTime;

public class Cancelled extends State {

    private final LocalDateTime cancelledTime;

    public Cancelled(LocalDateTime cancelledTime) {
        this.state = "Cancelled";
        this.cancelledTime = cancelledTime;
    }

    @Override
    public String getState() {
        return this.state;
    }

    @Override
    public String getExtraInfo() {
        return this.cancelledTime.toString();
    }
}
