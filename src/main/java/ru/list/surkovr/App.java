package ru.list.surkovr;

import com.mongodb.client.MongoClient;
import io.javalin.Javalin;
import ru.list.surkovr.configs.Config;
import ru.list.surkovr.configs.Path;
import ru.list.surkovr.controllers.PageControllerImpl;
import ru.list.surkovr.controllers.WordController;
import ru.list.surkovr.controllers.WordControllerImpl;
import ru.list.surkovr.model.repositories.WordRepository;
import ru.list.surkovr.model.repositories.WordRepositoryImpl;
import ru.list.surkovr.services.FileService;
import ru.list.surkovr.services.FileServiceImpl;

import static io.javalin.apibuilder.ApiBuilder.*;
import static ru.list.surkovr.configs.Config.getMongoClient;

public class App {

    private final Config config = new Config();
    private final MongoClient mongoClient;
    private final WordController wordController;
    private final PageControllerImpl pageController;

    public App() {
        this.mongoClient = getMongoClient(config);
        WordRepository wordRepository = new WordRepositoryImpl(mongoClient.getDatabase(config.getDbName()));
        FileService fileService = new FileServiceImpl(wordRepository);
        this.wordController = new WordControllerImpl(wordRepository, fileService);
        this.pageController = new PageControllerImpl(wordRepository);
    }

    public static void main(String[] args) {
        new App().start();
    }

    private void start() {

        Javalin app = Javalin.create(Config::configApp);

        app.routes(() -> {
            path(Path.INDEX, () ->
                    get(Path.INDEX, pageController::serveIndexPage));
//                            ctx -> ctx.result("Hello!")
            path(Path.WORDS, () -> {
                get(pageController::serveAllWordsPage);
                post(wordController::create);
                path(Path.ID_PARAM, () -> {
                    get(wordController::findOne);
                    put(wordController::updateOne);
                    delete(wordController::deleteOne);
                });
                path(Path.ADD, () -> {
                    get(pageController::serveAddWordsPage);
                });
                path(Path.EXPORT, () -> {
                    get(wordController::exportWords);
                });
                path(Path.IMPORT, () -> {
                    get(wordController::importWords);
                });
                path(Path.GAME, () -> {
                    get(pageController::servePlayWordsPage);
                });
            });
        });


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            mongoClient.close();
            app.stop();
        }));

        app.start(config.getPort());
    }
}