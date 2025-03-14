package domainModel;

import java.util.Objects;

public class Patient extends Person {
    private String urgencyLevel;

    public Patient(String patientCF, String name, String surname, String urgencyLevel) {
        super(patientCF, name, surname);
        setUrgencyLevel(urgencyLevel); // Usa il setter per validare il dato
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        if (urgencyLevel == null || urgencyLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("Urgency level cannot be null or empty.");
        }
        this.urgencyLevel = urgencyLevel.trim();
    }

    @Override
    public String toString() {
        return "Patient{" +
                "fiscalCode='" + getCF() + '\'' +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", urgencyLevel='" + urgencyLevel + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Patient patient)) return false;
        return Objects.equals(getCF(), patient.getCF()) &&
                Objects.equals(getName(), patient.getName()) &&
                Objects.equals(getSurname(), patient.getSurname()) &&
                Objects.equals(urgencyLevel, patient.urgencyLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCF(), getName(), getSurname(), urgencyLevel);
    }
}