package ru.list.surkovr.configs;

public class Path {

    public static final String INDEX = "/";
    public static final String WORDS = "/words";
    public static final String ID_PARAM = "/id";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String ADD = "/add";
    public static final String EXPORT = "/export";
    public static final String IMPORT = "/import";
    public static final String GAME = "/game";

    public static String formatWordLocation(String id) {
        return String.format("%s/%s", WORDS, id);
    }

    public static class Template {
        public static final String INDEX = "/templates/index.html";
        public static final String WORDS_ADD = "/templates/words/add.html";
        public static final String LOGIN = "/templates/login.html";
        public static final String WORDS_ALL = "/templates/words/all.html";
        public static final String WORDS_ONE = "/templates/words/one.html";
        public static final String NOT_FOUND = "/templates/notFound.html";
        public static final String WORDS_GAME = "/templates/words/game.html";
    }

}