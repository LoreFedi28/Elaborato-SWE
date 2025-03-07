package domainModel.State;

import java.time.LocalDateTime;

public class Completed extends State {

    private final LocalDateTime completedTime;

    public Completed(LocalDateTime completedTime) {
        this.state = "Completed";
        this.completedTime = completedTime;
    }

    @Override
    public String getState(){
        return this.state;
    }

    @Override
    public String getExtraInfo(){
        return this.completedTime.toString();
    }
}
