package domainModel.State;

public class Booked extends State {

    private final String patientCF;

    public Booked(String patientCF) {
        super("Booked");
        if (patientCF == null || patientCF.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient CF cannot be null or empty.");
        }
        this.patientCF = patientCF.trim();
    }

    @Override
    public String getExtraInfo() {
        return this.patientCF;
    }
}