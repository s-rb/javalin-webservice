package ru.list.surkovr.controllers;

import io.javalin.http.Context;

public interface PageController {

    void serveIndexPage(Context context);

    void serveAddWordsPage(Context context);

    void serveAllWordsPage(Context context);

    void servePlayWordsPage(Context context);
}
