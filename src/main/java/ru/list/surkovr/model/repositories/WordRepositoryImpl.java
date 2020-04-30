package ru.list.surkovr.model.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import org.bson.types.ObjectId;
import ru.list.surkovr.model.entities.Word;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class WordRepositoryImpl implements WordRepository {

    private static final String COLLECTION_NAME = "words";
    private static final FindOneAndReplaceOptions UPDATE_OPTIONS = new FindOneAndReplaceOptions()
            .returnDocument(ReturnDocument.AFTER);

    private final MongoCollection<Word> words;

    public WordRepositoryImpl(MongoDatabase database) {
        this.words = database.getCollection(COLLECTION_NAME, Word.class);
    }

    @Override
    public void create(Word word) {
        word.setId(new ObjectId());
        words.insertOne(word);
    }

    @Override
    public Word find(String id) {
        return words.find(eq("_id", new ObjectId(id))).first();
    }

    @Override
    public List<Word> findAll() {
        List<Word> result = new LinkedList<>();
        for (Word word : words.find()) {
            result.add(word);
        }
        return result;
    }

    @Override
    public Word update(Word word, String id) {
        return words.findOneAndReplace(new Document("_id", new ObjectId(id)), word, UPDATE_OPTIONS);
    }

    @Override
    public void delete(String id) {
        words.deleteOne(new Document("_id", new ObjectId(id)));
    }

    @Override
    public Word getRandomWord() {
        List<Word> allWords = findAll();
        if (allWords == null || allWords.isEmpty()) return null;
        int random = new Random().nextInt(allWords.size());
        return allWords.get(random);
    }
}
