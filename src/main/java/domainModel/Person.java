package domainModel;

import java.util.Objects;

public abstract class Person {

    private final String CF;
    private final String name;
    private final String surname;

    public Person(String CF, String name, String surname) {
        if (CF == null || CF.trim().isEmpty()) {
            throw new IllegalArgumentException("Codice fiscale (CF) cannot be null or empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (surname == null || surname.trim().isEmpty()) {
            throw new IllegalArgumentException("Surname cannot be null or empty.");
        }

        this.CF = CF.trim();
        this.name = name.trim();
        this.surname = surname.trim();
    }

    public String getCF() {
        return CF;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return CF.equals(person.CF) &&
                name.equals(person.name) &&
                surname.equals(person.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(CF, name, surname);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "CF='" + CF + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}