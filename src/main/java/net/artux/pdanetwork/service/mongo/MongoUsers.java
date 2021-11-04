package net.artux.pdanetwork.service.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import net.artux.pdanetwork.models.Member;
import net.artux.pdanetwork.models.RegisterUser;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.UserInfo;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.service.util.ValuesService;
import net.artux.pdanetwork.utills.Security;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Service
public class MongoUsers {

    private final MongoClient mongoClient;
    private final MongoCollection<Member> table;

    // global settings for all users
    private final int daysValidAccount = 90;

    private final ConnectionString connectionString;

    MongoUsers(ValuesService valuesService){
        connectionString = new ConnectionString(valuesService.getMongoUri());
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        mongoClient = MongoClients.create(clientSettings);
        MongoDatabase db = mongoClient.getDatabase("users");

        table = db.getCollection("usersCollection", Member.class);
        //table.createIndex(Indexes.ascending("token", "pdaId", "login"));
    }

    public void close(){
        mongoClient.close();
    }

    public int add(RegisterUser user){
        int pdaId = getPdaID();
        table.insertOne(new Member(user, pdaId, new BCryptPasswordEncoder()));

        table.createIndex(Indexes.ascending("lastLoginAt"),
                new IndexOptions().expireAfter(Integer.toUnsignedLong( daysValidAccount * 24 * 3600), TimeUnit.SECONDS));

        return pdaId;
    }

    private int getPdaID(){
        Member member = table.find().sort(new Document("_id", -1)).first();

        if (member != null) {
            return member.getPdaId() + 1;
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

    public boolean setBlocked(int pdaId, int blocked){
        Member result = getMember(pdaId);

        if(result!=null) {
            result.setBlocked(blocked);
            updateMember(result);
            return true;
        } else return false;
    }
    public Member getById(int pdaId) {
        return getMember(pdaId);
    }

    public Member getByToken(String token){
        Member result = getMember(token);

        if(result!=null) {
            table.updateOne(
                    eq("token", token),
                    combine(set("lastLoginAt", Instant.now().toEpochMilli())));
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

    public void updatePassword(int pdaId, String password) {
        Member member = getMember(pdaId);
        member.hashPassword(password);
        updateMember(member);
    }

    public void deleteByLogin(String login) {
        Document query = new Document();
        query.put("login", login);

        table.findOneAndDelete(query);
    }

    public Status checkUser(RegisterUser user) {
        if (user.getLogin() != null && !user.getLogin().equals("")) {
            if (getMember("login", user.getLogin()) != null) {
                return new Status(false, "Пользователь с таким логином уже существует.");
            }
        } else {
            return new Status(false, "Логин не соответствует требованиям");
        }
        if (user.getEmail() != null && !user.getEmail().equals("")
                && user.getEmail().contains("@") && user.getEmail().contains(".")) {
            if (getMember("email", user.getEmail()) != null) {
                return new Status(false, "Пользователь с таким e-mail уже существует.");
            }
        } else {
            return new Status(false, "E-mail не соответствует требованиям");
        }

        if (user.getName() != null && user.getName().equals(""))
            return new Status(false, "Имя не может быть пустым");

        if (user.getNickname() != null && user.getNickname().equals(""))
            return new Status(false, "Кличка не может быть пустой");

        if (user.getPassword() != null && user.getPassword().length() < 8)
            return new Status(false, "В пароле минимум 8 символов");

        return new Status(true, "Логин и почта свободны.");
    }

    public Status tryLogin(String emailOrLogin, String password){
        Member element = getMember("login", emailOrLogin);

        if(element!=null){
            return checkPassword(element, password, true);
        } else {
            element = getMember("email", emailOrLogin);

            if (element!=null){
                return checkPassword(element, password, true);
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
        if (element.getRole().equals("admin")) {
            return checkPassword(element, password, false);
        } else {
            //ServletContext.log("Not admin tried to use admin panel " + element.getLogin());
            return new Status(false, "You aren't admin");
        }
    }

    private Status checkPassword(Member element, String password, boolean resetToken) {
        if (element.getPassword().equals(String.valueOf(password.hashCode()))) {
            if (resetToken) {
                element.setToken(Security.encrypt(element.getLogin() + ":" + password));
                updateMember(element);
            }
            return new Status();
        } else {
            //ServletContext.log("Wrong password for " + element.getLogin());
            return new Status(false, "Wrong password");
        }
    }

    public void friendRequest(String token, int id){
        Member user = getMember(token);
        Member newFriend = getMember(id);

        /*user.subs.add(newFriend.getPdaId());
        updateMember(user);

        newFriend.requests.add(user.getPdaId());*/
        updateMember(newFriend);
    }

    /*public List<Integer> getFriendRequests(int pdaId) {
        return getMember(pdaId).requests;
    }*/

    public boolean addFriend(String token, Integer id) {
        Member user = getMember(token);
        if (user.requests.contains(id)) {
            user.requests.remove(id);
            if (!user.subs.contains(id)) {
                //user.subs.add(id);

                updateMember(user);
            }
            return true;
        }else
            return false;
    }

    public boolean removeFriend(String token, Integer id) {
        Member user = getMember(token);
        if (user.subs.contains(id)) {
            user.subs.remove(id);
            updateMember(user);

            Member oldFriend = getMember(id);
            if (oldFriend.subs.contains(user.getPdaId())) {
                //user.requests.add(id);
                updateMember(user);
            } else {
                oldFriend.requests.remove(user.getPdaId());
                updateMember(oldFriend);
            }
            return true;
        } else
            return false;
    }

    public void addDialog(int pdaId, int conversation) {
        Member user = getMember(pdaId);

        if (user != null) {
            user.dialogs.add(conversation);
            updateMember(user);
        }
    }

    public void updateDialog(int pdaId, Integer conversation) {
        Member user = getMember(pdaId);
        if (user != null) {
            user.dialogs.remove(conversation);
            user.dialogs.add(0, conversation);
            updateMember(user);
        }
    }

    public Member getMember(int pdaId) {
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
        //table.find().sort(new Document("xp", -1)).skip(from).limit(30).forEach(member -> users.add(new UserInfo(member)));
        return users;
    }

    public UpdateResult resetData(String token) {
        Member member = getMember(token);
        member.setData(new Data());
        return updateMember(member);
    }

    public UpdateResult updateMember(@NotNull Member member) {
        member.setLastModified(Instant.now().toEpochMilli());
        return table.replaceOne(eq("pdaId", member.getPdaId()), member);
    }

    public UpdateResult changeField(Object token, String field, Object newValue) {
        return table.updateOne (eq("token", token), combine(set(field, newValue),set("lastModified", Instant.now().toEpochMilli())));
    }

    public UpdateResult changeFields(Object token, List<Bson> updates) {
        updates.add(set("lastModified", Instant.now().toEpochMilli()));
        return table.updateOne (eq("token", token), combine(updates), new UpdateOptions().upsert(true).bypassDocumentValidation(true));
    }

}
