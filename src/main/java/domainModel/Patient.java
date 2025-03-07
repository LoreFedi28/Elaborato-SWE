package domainModel;

public class Patient extends Person {

    private String level;

    public Patient(String patientCF, String name, String surname, String level){
        super(patientCF, name, surname);
        this.level = level;
    }

    public String getLevel(){
        return level;
    }

    public void setLevel(String level){
        this.level = level;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "fiscalCode='" + getCF() + '\'' +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", level=" + level +
                '}';
    }
}
