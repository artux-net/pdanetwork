package net.artux.pdanetwork.utills.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import net.artux.pdanetwork.servlets.Feed.Models.Article;
import net.artux.pdanetwork.utills.ServletContext;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoFeed {

    private static final MongoClient mongoClient;
    private static final MongoCollection<Article> table;

    // global settings for all users
    private final int daysValidAccount = 90;

    private static final ConnectionString connectionString;

    static {
        if (ServletContext.debug)
            connectionString = new ConnectionString("mongodb://mongo-root:XWA47iIgQrPhuaukTryu@35.237.32.236:27017/");
        else
            connectionString = new ConnectionString("mongodb://mongo-root:XWA47iIgQrPhuaukTryu@localhost:27017/");
    }

    static {

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
        table.find()
                .sort(new Document("_id", -1))
                .skip(skip)
                .limit(limit)
                .forEach(list::add);
        return list;
    }

    public int addArticle(String title, String desc, String image, String tags, String content) {
        Article article = new Article(getFeedId(), title, image,
                Arrays.asList(tags.split(",")),
                desc, content);
        table.insertOne(article);
        return article.getFeedId();
    }

    public long editArticle(int feedId, String title, String desc, String image, String tags, String content) {
        return table.replaceOne(new Document("feedId", feedId), new Article(feedId, title, image,
                Arrays.asList(tags.split(",")),
                desc, content)).getModifiedCount();


    }

    public long removeArticle(int id) {
        return table.deleteOne(new Document("feedId", id)).getDeletedCount();
    }

    public int getFeedId() {
        Article article = table.find().sort(new Document("_id", -1)).first();

        if (article != null) {
            return article.getFeedId() + 1;
        } else {
            return 1;
        }
    }

    public List<Article> getArticles(int from) {
        ArrayList<Article> articles = new ArrayList<>();
        table.find().sort(new Document("published", -1)).skip(from).limit(15).forEach(articles::add);
        return articles;
    }

    public void editArticle(Article article) {
        table.replaceOne(new Document("feedId", article.getFeedId()), article);
    }

    public void deleteArticle(int feedId) {
        table.deleteOne(new Document("feedId", feedId));
    }
}
