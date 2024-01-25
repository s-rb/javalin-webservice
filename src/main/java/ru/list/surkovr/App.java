package ru.list.surkovr;

import io.javalin.Javalin;
import ru.list.surkovr.configs.Config;
import ru.list.surkovr.configs.Path;
import ru.list.surkovr.configs.databases.AbstractDatabase;
import ru.list.surkovr.configs.databases.DatabaseFactory;
import ru.list.surkovr.configs.databases.MongoDB;
import ru.list.surkovr.controllers.PageControllerImpl;
import ru.list.surkovr.controllers.WordController;
import ru.list.surkovr.controllers.WordControllerImpl;
import ru.list.surkovr.model.repositories.WordRepository;
import ru.list.surkovr.model.repositories.WordRepositoryImpl;
import ru.list.surkovr.services.FileService;
import ru.list.surkovr.services.FileServiceImpl;

import static io.javalin.apibuilder.ApiBuilder.*;

public class App {

    private final Config config = new Config();
    private final WordController wordController;
    private final PageControllerImpl pageController;
    private final AbstractDatabase database;

    public App() {
        database = new DatabaseFactory().getDatabase(MongoDB.getInstance());
        database.initDB();
        WordRepository wordRepository = new WordRepositoryImpl(database);
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
            database.close();
            app.stop();
        }));

        app.start(config.getPort());
    }
}