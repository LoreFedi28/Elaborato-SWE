package domainModel;

import java.util.Objects;

public class Doctor extends Person {
    private String iban;

    public Doctor(String doctorCF, String name, String surname, String iban) {
        super(doctorCF, name, surname);
        setIban(iban); // Usa il setter per validare il dato
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        if (iban == null || iban.trim().isEmpty()) {
            throw new IllegalArgumentException("IBAN cannot be null or empty.");
        }
        this.iban = iban.trim();
    }

    @Override
    public String toString() {
        return super.toString().replace("Person", "Doctor") + ", iban='" + iban + "'}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Doctor doctor)) return false;
        return Objects.equals(getCF(), doctor.getCF()) &&
                Objects.equals(getName(), doctor.getName()) &&
                Objects.equals(getSurname(), doctor.getSurname()) &&
                Objects.equals(iban, doctor.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCF(), getName(), getSurname(), iban);
    }
}