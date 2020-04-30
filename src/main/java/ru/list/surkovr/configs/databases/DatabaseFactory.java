package ru.list.surkovr.configs.databases;

public class DatabaseFactory {

    public AbstractDatabase getDatabase(AbstractDatabase abstractDatabase) {
        AbstractDatabase database = null;
        if (abstractDatabase instanceof MongoDB) {
            database = MongoDB.getInstance();
        }
        return database;
    }
}