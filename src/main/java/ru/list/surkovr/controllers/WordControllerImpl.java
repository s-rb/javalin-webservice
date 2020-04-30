package ru.list.surkovr.controllers;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import ru.list.surkovr.configs.Config;
import ru.list.surkovr.configs.Path;
import ru.list.surkovr.model.entities.Word;
import ru.list.surkovr.model.repositories.WordRepository;
import ru.list.surkovr.services.FileService;

import java.util.List;

public class WordControllerImpl implements WordController {

    private static final String ID = "id";
    private WordRepository wordRepository;
    private FileService fileService;

    public WordControllerImpl(WordRepository wordRepository,
                              FileService fileService) {
        this.wordRepository = wordRepository;
        this.fileService = fileService;
    }

    @Override
    public void create(@NotNull Context context) {
        var word = new Word();
        word.setEnglish(context.formParam("english"));
        word.setRussian(context.formParam("russian"));
        word.setDifficulty(context.formParam("difficulty") != null
                ? Integer.parseInt(context.formParam("difficulty")) : 1);
//                context.bodyAsClass(Word.class);
        if (word.getId() != null) {
            throw new BadRequestResponse(String.format("Unable to create a new word with existing id: %s", word));
        }
        wordRepository.create(word);
        context.status(HttpStatus.CREATED_201)
                .header(HttpHeader.LOCATION.name(), Path.formatWordLocation(word.getId().toString()))
                .redirect(Path.WORDS + Path.ADD);
    }

    @Override
    public void findOne(@NotNull Context context) {
        String id = context.pathParam(ID);
        Word word = wordRepository.find(id);
        if (word == null) {
            throw new NotFoundResponse(String.format("A word with id '%s' is not found", id));
        }
        context.json(word);
    }

    public void findAll(@NotNull Context context) {
        context.json(wordRepository.findAll());
    }

    @Override
    public void updateOne(@NotNull Context context) {
        var word = context.bodyAsClass(Word.class);
        var id = context.pathParam(ID);
        // Javalin выводит ошибку 404 с сообщением
        if (word.getId() != null && !word.getId().toString().equals(id)) {
            throw new BadRequestResponse("Id update is not allowed");
        }
        wordRepository.update(word, id);
    }

    @Override
    public void deleteOne(@NotNull Context context) {
        wordRepository.delete(context.pathParam(ID));
    }

    @Override
    public void exportWords(Context context) {
        List<Word> wordList = wordRepository.findAll();
        boolean isDumped = fileService.dumpDataToFile(Config.PATH_TO_DB_BACKUP_DIR,
                Config.FILENAME_DB_BACKUP + Config.FILE_EXT_TXT, wordList);
        if (isDumped) {
            context.status(200).redirect(Path.INDEX);
        } else {
            context.status(500).redirect(Path.INDEX);
        }
    }

    @Override
    public void importWords(Context context) {

    }
}
