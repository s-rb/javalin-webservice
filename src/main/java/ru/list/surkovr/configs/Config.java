package ru.list.surkovr.configs;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.javalin.core.JavalinConfig;
import io.javalin.core.security.BasicAuthCredentials;
import io.javalin.http.Context;
import lombok.Data;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.eclipse.jetty.http.MimeTypes;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Data
public class Config {
//    private static Map<String, Set<UserRole>> userCredentials = Map.of("admin|password", Set.of(UserRole.ADMIN, UserRole.PUBLIC));

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

        /*
        config.accessManager((handler, ctx, permittedRoles) -> {
            Set<UserRole> roles = getUserRole(ctx);
            Set<Role> intersection = new HashSet<>(permittedRoles);
            intersection.retainAll(roles);
            if (!intersection.isEmpty()) {
                handler.handle(ctx);
            } else {
                ctx.status(401).json("Unauthorized");
            }
        });
        */
    }

//    public static Set<UserRole> getUserRole(Context ctx) {
//        BasicAuthCredentials credentials = ctx.basicAuthCredentials();
//
//        return userCredentials.getOrDefault(credentials.getUsername() + "|" + credentials.getPassword(), Set.of(UserRole.PUBLIC));
//    }
}
