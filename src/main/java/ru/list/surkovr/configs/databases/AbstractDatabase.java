package ru.list.surkovr.configs.databases;

import ru.list.surkovr.model.entities.Word;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public abstract class AbstractDatabase {

    public abstract void initDB();

    public abstract void insertObject(Object o);

    public abstract <T> T find(String id, Class<T> tClass);

    public abstract <T> List<T> findAll(Class<T> tClass);

    public abstract <T> T update(T object, String id);

    public abstract <T> void delete(String id, Class<T> tClass);

    public abstract Word getRandomWord();

    public abstract void close();
}