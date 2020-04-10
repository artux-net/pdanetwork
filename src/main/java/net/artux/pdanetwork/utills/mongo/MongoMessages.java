package net.artux.pdanetwork.utills.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

public class MongoMessages {

    private MongoClient mongoClient;
    private MongoDatabase db;

    public MongoMessages(){
        mongoClient = MongoClients.create("mongodb://admin:f8a9s5t@localhost:27017/");
        db = mongoClient.getDatabase("messagesdb");
    }

    public String getDialogName(int pdaId, int toPdaId) {
        if(pdaId < toPdaId){
            return pdaId + "-to-" + toPdaId;
        } else {
            return toPdaId + "-to-" + pdaId;
        }
    }

    public MongoCollection<Document> getDialogCollection(String dialogName){
        return db.getCollection(dialogName);
    }

    public boolean dialogExists(String dialogName) {
        ArrayList<String> names = db.listCollectionNames().into(new ArrayList<String>());
        return names.contains(dialogName);
    }

    public void createDialog(String dialogName){
        db.createCollection(dialogName);
    }
    
    
}
