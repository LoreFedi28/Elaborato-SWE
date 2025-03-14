package domainModel.State;

public abstract class State {

    private final String state;

    protected State(String state) {
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException("State cannot be null or empty.");
        }
        this.state = state.trim();
    }

    public String getState() {
        return this.state;
    }

    public abstract String getExtraInfo();
}