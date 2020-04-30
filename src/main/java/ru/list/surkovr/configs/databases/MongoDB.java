package ru.list.surkovr.configs.databases;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import ru.list.surkovr.model.entities.Word;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDB extends AbstractDatabase {

    private static volatile MongoDB instance;
    private int mongoPort;
    private String mongoHost;
    private String dbName;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Word> words;

    private static final String WORDS_COLLECTION_NAME = "words";
    private static final FindOneAndReplaceOptions UPDATE_OPTIONS = new FindOneAndReplaceOptions()
            .returnDocument(ReturnDocument.AFTER);

    private MongoDB() {
    }

    public static MongoDB getInstance() {

        if (instance == null) {
            synchronized (MongoDB.class) {
                if (instance == null) {
                    instance = new MongoDB();
                }
            }
        }
        return instance;
    }

    @Override
    public void initDB() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        // Получаем доступ к переменным окружения, которые задали на сервере (можно менять параметры)
        mongoPort = Integer.parseInt(processBuilder.environment().getOrDefault("MONGO_PORT", "27017"));
        mongoHost = processBuilder.environment().getOrDefault("MONGO_HOST", "localhost");
        dbName = processBuilder.environment().getOrDefault("DB_NAME", "wordsdb");

        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        mongoClient = MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(List.of(new ServerAddress(mongoHost, mongoPort))))
                .codecRegistry(codecRegistry)
                .build());
        database = mongoClient.getDatabase(dbName);
        words = database.getCollection(WORDS_COLLECTION_NAME, Word.class);
    }

    @Override
    public void insertObject(Object o) {
        if (o instanceof Word) {
            words.insertOne((Word) o);
        }
    }

    @Override
    public <T> T find(String id, Class<T> tClass) {
        if (tClass.equals(Word.class)) {
            return (T) words.find(eq("_id", new ObjectId(id))).first();
        }
        return null;
    }

    @Override
    public <T> List<T> findAll(Class<T> tClass) {
        if (tClass.equals(Word.class)) {
            List<Word> result = new LinkedList<>();
            for (Word word : words.find()) {
                result.add(word);
            }
            return (List<T>) result;
        }
        return null;
    }

    @Override
    public <T> T update(T object, String id) {
        if (object.getClass().equals(Word.class)) {
        return (T) words.findOneAndReplace(
                new Document("_id", new ObjectId(id)), (Word) object, UPDATE_OPTIONS); }
        return null;
    }

    @Override
    public <T> void delete(String id, Class<T> tClass) {
        if (tClass.equals(Word.class)) {
        words.deleteOne(new Document("_id", new ObjectId(id))); }
    }

    @Override
    public Word getRandomWord() {
        List<Word> allWords = findAll(Word.class);
        if (allWords == null || allWords.isEmpty()) return null;
        int random = new Random().nextInt(allWords.size());
        return allWords.get(random);
    }

    @Override
    public void close() {
        mongoClient.close();
    }
}
