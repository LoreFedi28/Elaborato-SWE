package main.java.domainModel.State;

public class Booked extends State {

    private String patientCF;

    public Booked(String patientCF) {
        this.state = "Booked";
        this.patientCF = patientCF;
    }

    @Override
    public String getState() {
        return this.state;
    }

    @Override
    public String getExtraInfo() {
        return this.patientCF;
    }
}
