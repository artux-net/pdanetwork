package net.artux.pdanetwork.communication.utilities;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import net.artux.pdanetwork.communication.model.Conversation;
import net.artux.pdanetwork.communication.model.ConversationRequest;
import net.artux.pdanetwork.communication.model.UserMessage;
import net.artux.pdanetwork.communication.utilities.model.DBMessage;
import net.artux.pdanetwork.service.mongo.MongoUsers;
import net.artux.pdanetwork.service.util.ValuesService;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Service
public class MongoMessages {

    private final MongoClient mongoClient;
    private final MongoDatabase db;
    private final MongoCollection<Conversation> conversations;
    private final ConnectionString connectionString;
    private final MongoUsers mongoUsers;

    public MongoMessages(MongoUsers mongoUsers, ValuesService valuesService) {
        this.mongoUsers = mongoUsers;

        connectionString = new ConnectionString(valuesService.getMongoUri());
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        mongoClient = MongoClients.create(clientSettings);
        db = mongoClient.getDatabase("messages");
        conversations = db.getCollection("conversations", Conversation.class);
        conversations.createIndex(Indexes.ascending("cid"));
    }

    private Conversation addConversation(int ownerId, Conversation conversation) {
        int id = getNewId();
        for (int i : conversation.allMembers()) {
            mongoUsers.addDialog(i, id);
        }

        conversations.insertOne(conversation);
        return conversation;
    }

    public Conversation newConversation(int ownerId, int pdaId){
        return addConversation(ownerId,  new Conversation(getNewId(), ownerId, pdaId));
    }
    public Conversation newConversation(int ownerId, ConversationRequest request){
        return addConversation(ownerId,  new Conversation(getNewId(), ownerId, request));
    }

    private int getNewId(){
        Conversation document = conversations.find().sort(new Document("_id", -1)).first();

        if(document!=null){
            int res = document.cid;
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
        query.put("cid", id);

        Conversation document = conversations.find(query).first();
        if (document != null) {
            List<Integer> ids = document.allMembers();
            return ids.contains(pda);
        } else return false;
    }

    public int getDialogID(int pda1, int pda2){
        List<Integer> members = Collections.singletonList(pda1);
        List<Integer> owners = Collections.singletonList(pda2);
        Document query = new Document();
        query.put("members", members);
        query.put("owners", owners);
        Conversation document = conversations.find(query).first();

        if (document!=null)
            return document.cid;
        else {
            members = Collections.singletonList(pda2);
            owners = Collections.singletonList(pda1);
            query.clear();
            query.put("members", members);
            query.put("owners", owners);
            document = conversations.find(query).first();
            if (document != null)
                return document.cid;
            else
                return 0;
        }
    }

    public List<UserMessage> getLastMessages(int id, int size, int page) {
        ArrayList<UserMessage> messages = new ArrayList<>();
        getConversationCollection(id).find()
                .sort(new Document("_id", -1))
                .skip(page * size)
                .limit(size)
                .forEach(dbMessage -> {
                    messages.add(0, new UserMessage(mongoUsers.getById(dbMessage.pdaId), dbMessage));
                });
        return messages;
    }


    public MongoCollection<DBMessage> getConversationCollection(int id) {
        return db.getCollection(String.valueOf(id), DBMessage.class);
    }

    public List<Integer> getIDs(int id) {
        Document query = new Document();
        query.put("cid", id);

        Conversation result = conversations.find(query).first();

        if(result!=null) {
            return result.allMembers();
        } else return null;
    }

    public boolean updateConversation(ConversationRequest request) {
        Conversation result = conversations.find(eq("cid", request.cid)).first();

        if(result!=null) {
            if (result.title != null && !request.title.equals(""))
                result.title = request.title;
            result.owners.addAll(request.owners);
            result.owners.removeAll(request.deleteOwners);
            result.members.addAll(request.members);
            result.members.removeAll(request.deleteMembers);
            conversations.replaceOne(eq("cid", request.cid), result);
            return true;
        } else return false;
    }

    public void addMessageToConversation(int id, UserMessage userMessage) {
        getConversationCollection(id).insertOne(new DBMessage(userMessage));
        if (userMessage.message.length() > 40)
            userMessage.message = userMessage.message.substring(0, 40).trim() + "..";

        conversations.updateOne(eq("cid", id),
                set("lastMessage", userMessage.senderLogin + ": " + userMessage.message));

    }

    public Conversation getConversation(int id) {
        Document query = new Document();
        query.put("cid", id);

        return conversations.find(query).first();
    }


}
