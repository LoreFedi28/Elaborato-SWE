package main.java.domainModel;

public class Doctor extends Person{

    private String iban;

    public Doctor(String doctorCF, String name, String surname, String iban){
        super(doctorCF, name, surname);
        this.iban = iban;
    }

    public String getIban(){
        return iban;
    }

    public void setIban(String iban){
        this.iban = iban;
    }

    @Override
    public String toString(){
        return "Doctor{" +
                " codice fiscale =' " + getCF() + '\'' +
                ", name = '" + getName() + '\'' +
                ", surname = '" + getSurname() + '\'' +
                ", iban = " + iban +
                '}';
    }

}
