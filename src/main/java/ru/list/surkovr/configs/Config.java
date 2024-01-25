package ru.list.surkovr.configs;

import io.javalin.core.JavalinConfig;
import lombok.Data;
import org.eclipse.jetty.http.MimeTypes;

@Data
public class Config {

    private int port;
    public static final String PATH_TO_DB_BACKUP_DIR = "data/backup_db";
    public static final String FILENAME_DB_BACKUP = "db_backup";
    public static final String FILE_EXT_TXT = ".txt";

    public Config() {
        init();
    }

    public void init() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        // Получаем доступ к переменным окружения, которые задали на сервере (можно менять параметры)
        port = Integer.parseInt(processBuilder.environment().getOrDefault("APP_PORT", "8080"));
    }

    public static void configApp(JavalinConfig config) {
        config.defaultContentType = MimeTypes.Type.APPLICATION_JSON_UTF_8.asString();
    }
}
