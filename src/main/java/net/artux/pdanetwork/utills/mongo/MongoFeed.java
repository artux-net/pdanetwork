package net.artux.pdanetwork.utills.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import net.artux.pdanetwork.servlets.Feed.Models.Article;
import net.artux.pdanetwork.service.util.ValuesService;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Service
public class MongoFeed {

    private final MongoClient mongoClient;
    private final MongoCollection<Article> table;

    private final ConnectionString connectionString;

    public MongoFeed(ValuesService valuesService){
        connectionString = new ConnectionString(valuesService.getMongoUri());
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        mongoClient = MongoClients.create(clientSettings);
        MongoDatabase db = mongoClient.getDatabase("feed");

        table = db.getCollection("articles", Article.class);
        table.createIndex(Indexes.ascending("feedId", "tags"));
    }

    public void close() {
        mongoClient.close();
    }

    public List<Article> getFeed(int skip, int limit) {
        ArrayList<Article> list = new ArrayList<>();
        table.find().sort(new Document("published", -1)).skip(skip).limit(limit).forEach(list::add);
        return list;
    }

    public String addArticle(String title, String desc, String image, List<String> tags, String content) {
        Article article = new Article(title, image,
                tags,
                desc, content);
        System.out.println(article);
        table.insertOne(article);
        return article.getId();
    }

    public long editArticle(String feedId, String title, String desc, String image, String tags, String content) {
        return table.replaceOne(new Document("id", feedId), new Article( title, image,
                Arrays.asList(tags.split(",")),
                desc, content)).getModifiedCount();
    }

    public long removeArticle(int id) {
        return table.deleteOne(new Document("feedId", id)).getDeletedCount();
    }

    public Article getArticle(int feedId){
        return table.find(new Document("feedId", feedId)).first();
    }

    public List<Article> getArticles(int from) {
        return getFeed(from, 15);
    }

}
