package domainModel.State;

public class Available extends State {

    public Available() {
        super("Available");
    }

    @Override
    public String getExtraInfo() {
        return null;
    }
}