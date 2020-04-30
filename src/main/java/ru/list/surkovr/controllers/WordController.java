package ru.list.surkovr.controllers;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public interface WordController {

    void create(@NotNull Context context);

    void findOne(@NotNull Context context);

//    void findAll(@NotNull Context context);

    void updateOne(@NotNull Context context);

    void deleteOne(@NotNull Context context);

    void exportWords(Context context);

    void importWords(Context context);
}