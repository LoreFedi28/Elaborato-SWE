package domainModel;

import java.util.Objects;

public abstract class Person {

    private final String CF;
    private final String name;
    private final String surname;

    public Person(String CF, String name, String surname){
        this.CF = CF;
        this.name = name;
        this.surname = surname;
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
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        Person person = (Person) o;
        return Objects.equals(CF, person.CF);
    }
}
