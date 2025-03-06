package main.java.domainModel.State;

public class Available extends State{

    public Available(){
        this.state = "Available";
    }

    @Override
    public String getExtraInfo(){
        return null;
    }


}
