package ru.list.surkovr.controllers;


import io.javalin.http.Context;
import ru.list.surkovr.configs.Path;
import ru.list.surkovr.model.entities.Word;
import ru.list.surkovr.model.repositories.WordRepository;
import ru.list.surkovr.util.ViewUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PageControllerImpl implements PageController {

    private WordRepository wordRepository;

    public PageControllerImpl(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

//        model.put("users", userDao.getAllUserNames());
    @Override
    public void serveIndexPage(Context context) {
        Map<String, Object> model = ViewUtil.baseModel(context);
        model.put("words", wordRepository.findAll());
        context.render(Path.Template.INDEX, model);
    }

    public void serveAddWordsPage(Context context) {
        context.render(Path.Template.WORDS_ADD);
    }

    public void serveAllWordsPage(Context context) {
        Map<String, Object> model = ViewUtil.baseModel(context);
        List<Word> words = wordRepository.findAll();
        model.put("words", words);
        context.render(Path.Template.WORDS_ALL, model);
    }

    @Override
    public void servePlayWordsPage(Context context) {
        startGame(context);
    }

    private void startGame(Context context) {
        Map<String, Object> model = ViewUtil.baseModel(context);
        List<Word> words = wordRepository.findAll();
        words.sort((o1, o2) -> o2.getDifficulty() - o1.getDifficulty());
        model.put("words", words);
        context.render(Path.Template.WORDS_GAME, model);
    }
}
