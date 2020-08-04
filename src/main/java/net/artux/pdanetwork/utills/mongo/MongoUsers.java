package net.artux.pdanetwork.utills.mongo;

import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoConfigurationException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.UserInfo;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class MongoUsers {

    private MongoClient mongoClient;
    private MongoCollection<Member> table;

    // global settings for all users
    private int daysValidAccount = 90;

    public MongoUsers() throws MongoConfigurationException {

        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://mongo-users:slVtKwrvFE2Er3JRTFxO@localhost:27017/"))
                .codecRegistry(codecRegistry)
                .build();

        mongoClient = MongoClients.create(clientSettings);
        MongoDatabase db = mongoClient.getDatabase("users");

        table = db.getCollection("usersCollection", Member.class);
    }

    public void close(){
        mongoClient.close();
    }

    public int add(RegisterUser user){
        int pdaId = getPdaID();
        table.insertOne(new Member(user, pdaId));

        table.createIndex(Indexes.ascending("lastLoginAt"),
                new IndexOptions().expireAfter(Integer.toUnsignedLong( daysValidAccount * 24 * 3600), TimeUnit.SECONDS));

        return pdaId;
    }

    private int getPdaID(){
        Member member = table.find().sort(new Document("_id", -1)).first();

        if (member != null) {
            int res = member.getPdaId();
            return res+1;
        } else {
            return 1;
        }
    }

    public boolean isBlocked(String token){
        Member result = getMember(token);

        if(result!=null) {
            return result.getBlocked() > 0;
        } else return false;
    }

    public Member getByToken(String token){
        Member result = getMember(token);

        if(result!=null) {
            table.updateOne(
                    eq("token", token),
                    combine(set("lastLoginAt", new Date())));
            return result;
        } else return null;
    }

    public int getPdaIdByToken(String token){
        Member result = getMember(token);
        //TODO if 0 then... What?
        if (result!=null)
            return result.getPdaId();
        else
            return 0;
    }

    public Member getEmailUser(String loginOrEmail){
        Member element = getMember("login", loginOrEmail);
        if (element!=null) {
            return element;
        }

        element = getMember("email", loginOrEmail);

        return element;
    }

    public Profile getProfileByPdaId(int pdaId){
        Member element = getMember(pdaId);

        if (element!=null) {
            return new Profile(element);
        }
        return null;
    }

    public Profile getProfileByPdaId(String token, int pdaId) {
        Member user = getMember(token);
        Member element = getMember(pdaId);

        if (element != null) {
            return new Profile(element, user);
        }
        return null;
    }

    public boolean userExists(int pdaId){
        return getMember(pdaId) != null;
    }

    public void updateByLogin(String login, String newLogin){
        Document newData = new Document();

        // задаем новый логин
        newData.put("login", newLogin);

        // указываем обновляемое поле и текущее его значение
        Document searchQuery = new Document().append("login", login);

        // обновляем запись
        table.findOneAndUpdate(searchQuery, newData);
    }

    public void deleteByLogin(String login){
        Document query = new Document();
        query.put("login", login);

        table.findOneAndDelete(query);
    }

    public Status checkUser(String login, String email){

        if (login!=null) {
            if (getMember("login", login) != null) {
                return new Status(false, "Пользователь с таким логином уже существует.");
            }
        }

        if (email!=null) {
            if (getMember("email", email) != null) {
                return new Status(false,"Пользователь с таким e-mail уже существует.");
            }
        }

        return new Status(true,"Логин и почта свободны.");
    }

    public Status tryLogin(String emailOrLogin, String password){
        Member element = getMember("login", emailOrLogin);

        if(element!=null){
          return checkPassword(element,password);
        } else {
            element = getMember("email", emailOrLogin);

            if (element!=null){
                return checkPassword(element,password);
            }else {
                return new Status(false,"Wrong login or email");
            }
        }
    }

    public Status tryAdminLogin(String emailOrLogin, String password){
        Member element = getMember("login", emailOrLogin);

        if(element!=null){
            return getLoginAdminStatus(password, element);
        } else {
            element = getMember("email", emailOrLogin);

            if (element!=null){
                return getLoginAdminStatus(password, element);
            }else {
                return new Status(false, "Wrong email or password");
            }
        }
    }

    private Status getLoginAdminStatus(String password, Member element) {
        if (element.getAdmin() > 0) {
            return checkPassword(element, password);
        } else {
            return new Status(false, "You aren't admin");
        }
    }

    private Status checkPassword(Member element, String password) {
        if (element.getPassword().equals(String.valueOf(password.hashCode()))) {
            return new Status(String.valueOf(element.getToken()));
        } else {
            return new Status(false, "Wrong password");
        }
    }

    public void friendRequest(String token, int id){
        Member user = getMember(token);
        Member newFriend = getMember(id);

        user.friends.add(newFriend.getPdaId());
        updateMember(user);

        newFriend.friendRequests.add(user.getPdaId());
        updateMember(newFriend);
    }

    public List<Integer> getFriends(int pdaId) {
        return getMember(pdaId).friends;
    }

    public List<Integer> getFriendRequests(int pdaId) {
        return getMember(pdaId).friendRequests;
    }

    public boolean addFriend(String token, Integer id) {
        Member user = getMember(token);
        if (user.friendRequests.contains(id)) {
            user.friendRequests.remove(id);
            if (!user.friends.contains(id)) {
                user.friends.add(id);

                updateMember(user);
            }
            return true;
        }else
            return false;
    }

    public boolean removeFriend(String token, Integer id) {
        Member user = getMember(token);
        if (user.friends.contains(id)) {
            user.friends.remove(id);
            updateMember(user);

            Member oldFriend = getMember(id);
            if (oldFriend.friends.contains(user.getPdaId())) {
                user.friendRequests.add(id);
                updateMember(user);
            } else {
                oldFriend.friendRequests.remove(user.getPdaId());
                updateMember(oldFriend);
            }
            return true;
        }else
            return false;
    }

    public void addDialog(int pdaId, int conversation) {
        Member user = getMember(pdaId);
        if (user != null) {
            user.dialogs.add(conversation);
            updateMember(user);
        }
    }

    private Member getMember(int pdaId) {
        Document query = new Document();
        query.put("pdaId", pdaId);

        return table.find(query).first();
    }

    private Member getMember(String token) {
        Document query = new Document();
        query.put("token", token);

        return table.find(query).first();
    }

    private Member getMember(String key, Object value) {
        Document query = new Document();
        query.put(key, value);

        return table.find(query).first();
    }

    public List<UserInfo> sort(int from) {
        List<UserInfo> users = new ArrayList<>();
        table.find().sort(new Document("xp", -1)).skip(from).limit(30).forEach((Block<Member>) member -> {
            users.add(new UserInfo(member));
        });
        return users;
    }

    public UpdateResult updateMember(Member member) {
        member.setLastModified(new Date());
        return table.replaceOne(eq("token", member.getToken()), member);
    }

    public UpdateResult changeField(Object token, String field, Object newValue) {
        return table.updateOne (eq("token", token), combine(set(field, newValue),set("lastModified", new Date().toString())));
    }

    public UpdateResult changeFields(Object token, List<Bson> updates) {
        updates.add(set("lastModified", new Date().toString()));
        return table.updateOne (eq("token", token), combine(updates), new UpdateOptions().upsert(true).bypassDocumentValidation(true));
    }

}
