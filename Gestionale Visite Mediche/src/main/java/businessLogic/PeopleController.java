package main.java.businessLogic;

import dao.DAO;
import domainModel.Person;

import java.util.List;
import static java.util.Collections.unmodifiableList;

public abstract class PeopleController <T extends Person> {
    private final DAO<T, String> dao;

    PeopleController(DAO<T, String> dao){
        this.dao = dao;
    }

    protected String addPerson(T newPerson) throws Exception{
        this.dao.insert(newPerson);
        return newPerson.getCF();
    }

    public boolean removePerson(String cf) throws Exception{
        return this.dao.delete(cf);
    }

    public T getPerson(String cf) throws Exception {
        return this.dao.get(cf);
    }

    public List<T> getAll() throws Exception {
        return unmodifiableList(this.dao.getAll());
    }
}
