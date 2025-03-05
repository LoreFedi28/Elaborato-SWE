package main.java.dao;

import java.util.List;

public class DAO <T, ID>{

    T get(ID id) throws Exception;

    List<T> getAll() throws Exception;

    void insert(T t) throws Exception;

    void update(T t) throws Exception;

    boolean delete(ID id) throws Exception;
}
