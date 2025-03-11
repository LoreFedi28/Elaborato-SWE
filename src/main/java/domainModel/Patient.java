package domainModel;

public class Patient extends Person {

    private String urgencyLevel;

    public Patient(String patientCF, String name, String surname, String urgencyLevel){
        super(patientCF, name, surname);
        this.urgencyLevel = urgencyLevel;
    }

    public String getUrgencyLevel(){
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel){
        this.urgencyLevel = urgencyLevel;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "fiscalCode='" + getCF() + '\'' +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", urgencyLevel=" + urgencyLevel +
                '}';
    }
}
