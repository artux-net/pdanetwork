package net.artux.pdanetwork.service.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.service.util.ValuesService;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Service
public class MongoAdmin {

    private final MongoClient mongoClient;
    private final MongoCollection<UserEntity> table;

    private final ConnectionString connectionString;

    public MongoAdmin(ValuesService valuesService) {
        connectionString = new ConnectionString(valuesService.getMongoUri());
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        mongoClient = MongoClients.create(clientSettings);
        MongoDatabase db = mongoClient.getDatabase("test");

        table = db.getCollection("member", UserEntity.class);
    }

    public long getSize() {
        return table.countDocuments();
    }

    public List<UserEntity> getRating(int from) {
        List<UserEntity> users = new ArrayList<>();
        table.find().sort(new Document("xp", -1)).skip(from).limit(30).forEach(users::add);
        return users;
    }

    public long getOnline() {
        return table.countDocuments(new Document("lastLoginAt", new Document("$gte", getStartOfDay())));
    }

    public long getRegistrations() {
        return table.countDocuments(new Document("registrationDate", new Document("$gte", getStartOfDay())));
    }

    private long getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        System.out.println(calendar.getTimeInMillis());
        return calendar.getTimeInMillis();
    }


    public List<UserEntity> find(String q) {
        List<UserEntity> users = new ArrayList<>();

        table.find(Filters.regex("login", q)).forEach(users::add);
        return users;
    }

}
