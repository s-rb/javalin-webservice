package ru.list.surkovr.model.repositories;

import ru.list.surkovr.model.entities.Word;

import java.util.List;

public interface WordRepository {

    void create(Word word);

    Word find(String id);

    List<Word> findAll();

    Word update(Word word, String id);

    void delete(String id);

    Word getRandomWord();
}
