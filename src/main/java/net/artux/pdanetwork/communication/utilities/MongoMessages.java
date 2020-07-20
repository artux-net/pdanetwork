package net.artux.pdanetwork.communication.utilities;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.artux.pdanetwork.communication.model.Conversation;
import net.artux.pdanetwork.utills.ServletContext;
import org.bson.Document;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class MongoMessages {

    private MongoClient mongoClient;
    private MongoDatabase db;
    private Gson gson = new Gson();
    private MongoCollection<Document> conversations;

    public MongoMessages(){
        mongoClient = MongoClients.create("mongodb://admin:f8a9s5t@localhost:27017/");
        db = mongoClient.getDatabase("messages");
        conversations = db.getCollection("conversations");

    }

    public int newConversation(int ownerId, List<Integer> members){
        int id = getNewId();
        List<Integer> owners = new ArrayList<>(Collections.singletonList(ownerId));
        Conversation conversation = new Conversation(id, owners, members);
        Document document = Document.parse(gson.toJson(conversation));
        for (int i : conversation.getMembers()) {
            ServletContext.mongoUsers.addDialog(i, id);
        }

        conversations.insertOne(document);
        return id;
    }

    public int newConversation(int ownerId, int pdaId){
        return newConversation(ownerId, new ArrayList<>(Collections.singletonList(pdaId)));
    }

    private int getNewId(){
        Document document = conversations.find().sort(new Document("_id",-1)).first();

        if(document!=null){
            int res = document.getInteger("id");
            return res+1;
        } else {
            return 1;
        }
    }

    public boolean hasConversation(int id){
        for (String s : db.listCollectionNames()) {
            if (s.equals(String.valueOf(id))) return true;
        }
        return false;
    }

    public boolean conversationHas(int id, int pda){
        Document query = new Document();
        query.put("id", id);

        Document document = conversations.find(query).first();
        if (document != null) {
            List<Integer> ids = document.get("members", new ArrayList<>());
            if (!ids.contains(pda)) {
                ids = document.get("owners", new ArrayList<>());
                return ids.contains(pda);
            } else return true;
        } else return false;
    }

    public int getDialogID(int pda1, int pda2){
        List<Integer> members = Collections.singletonList(pda1);
        List<Integer> owners = Collections.singletonList(pda2);
        Document query = new Document();
        query.put("members", members);
        query.put("owners", owners);
        Document document = conversations.find(query).first();

        if (document!=null)
            return document.getInteger("id");
        else {
            members = Collections.singletonList(pda2);
            owners = Collections.singletonList(pda1);
            query.clear();
            query.put("members", members);
            query.put("owners", owners);
            document = conversations.find(query).first();
            if (document != null)
                return document.getInteger("id");
            else
                return 0;
        }
    }

    public void sendLastMessages(int id, Session session){
        Block<Document> printBlock = document -> {
            try {
                session.getBasicRemote().sendText(document.toJson());
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        db.getCollection(String.valueOf(id)).find().sort(new BasicDBObject()).limit(30).forEach(printBlock);
    }


    public MongoCollection<Document> getConversationCollection(int id){
        return db.getCollection(String.valueOf(id));
    }

    public List<Integer> getIDs(int id){
        Document query = new Document();
        query.put("id", id);

        Document result = conversations.find(query).first();

        if(result!=null) {
            return new Gson().fromJson(result.toJson(), Conversation.class).getMembers();
        } else return null;
    }

    public void updateConversation(int id, String message){
      conversations.updateOne(eq("id", id), set("lastMessage", message));
    }

    public Conversation getConversation(int id){
        Document query = new Document();
        query.put("id", id);

        Document result = conversations.find(query).first();
        if (result!=null){
            return gson.fromJson(result.toJson(), Conversation.class);
        } else {
            return null;
        }
    }
    
}
