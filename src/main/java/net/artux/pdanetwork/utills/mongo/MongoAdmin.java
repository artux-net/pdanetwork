package net.artux.pdanetwork.utills.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.utills.ServletContext;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoAdmin {

    private final MongoClient mongoClient;
    private final MongoCollection<Member> table;

    private static final ConnectionString connectionString;

    static {
        if (ServletContext.debug)
            connectionString = new ConnectionString("mongodb://mongo-users:slVtKwrvFE2Er3JRTFxO@35.237.32.236:27017/");
        else
            connectionString = new ConnectionString("mongodb://mongo-users:slVtKwrvFE2Er3JRTFxO@localhost:27017/");
    }

    public MongoAdmin() {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        mongoClient = MongoClients.create(clientSettings);
        MongoDatabase db = mongoClient.getDatabase("users");

        table = db.getCollection("usersCollection", Member.class);
    }

    public long getSize() {
        return table.countDocuments();
    }

    public List<Member> getRating(int from) {
        List<Member> users = new ArrayList<>();
        table.find().sort(new Document("xp", -1)).skip(from).limit(30).forEach(users::add);
        return users;
    }

    public List<Member> find(String q) {
        List<Member> users = new ArrayList<>();

        table.find(Filters.regex("login", q)).forEach(users::add);
        return users;
    }

}
