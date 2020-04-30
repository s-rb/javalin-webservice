package ru.list.surkovr.services;

import ru.list.surkovr.model.entities.FindableById;

import java.util.Collection;

public interface FileService {

    public <T extends FindableById> boolean dumpDataToFile(String pathToBackupDir,
                                                           String pathToBackupFile, Collection<T> itemsToDump);
}
