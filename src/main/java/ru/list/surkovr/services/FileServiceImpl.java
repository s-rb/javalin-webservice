package ru.list.surkovr.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.list.surkovr.configs.Config;
import ru.list.surkovr.model.entities.FindableById;
import ru.list.surkovr.model.repositories.WordRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

public class FileServiceImpl implements FileService {

    private WordRepository wordRepository;

    public FileServiceImpl(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    public <T extends FindableById> boolean dumpDataToFile(String pathToBackupDir,
                                                           String pathToBackupFile, Collection<T> itemsToDump) {
        java.nio.file.Path backupFilePath = Paths.get(pathToBackupDir + "/" +
                pathToBackupFile.replaceAll(Config.FILE_EXT_TXT,
                        System.currentTimeMillis() + Config.FILE_EXT_TXT));
        try {
            Files.createDirectories(Paths.get(pathToBackupDir));
            Files.createFile(backupFilePath);
            File backupFile = backupFilePath.toFile();

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();

            for (T item : itemsToDump) {
                rootNode.putPOJO(item.getClass().getName() + "_" + item.getId(), item);
            }
            mapper.writeValue(backupFile, rootNode);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
