package ru.list.surkovr.model.repositories;

import org.bson.types.ObjectId;
import ru.list.surkovr.configs.databases.AbstractDatabase;
import ru.list.surkovr.model.entities.Word;

import java.util.List;

public class WordRepositoryImpl implements WordRepository {

    private final AbstractDatabase database;

    public WordRepositoryImpl(AbstractDatabase database) {
        this.database = database;
    }

    @Override
    public void create(Word word) {
        word.setId(new ObjectId());
        database.insertObject(word);
    }

    @Override
    public Word find(String id) {
        return database.find(id, Word.class);
    }

    @Override
    public List<Word> findAll() {
        return database.findAll(Word.class);
    }

    @Override
    public Word update(Word word, String id) {
        return database.update(word, id);
    }

    @Override
    public void delete(String id) {
        database.delete(id, Word.class);
    }

    @Override
    public Word getRandomWord() {
        return database.getRandomWord();
    }
}
