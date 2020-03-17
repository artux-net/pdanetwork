package com.pdanetwork.utills.mongo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoConfigurationException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.pdanetwork.utills.Security;
import com.pdanetwork.models.Chat.Dialog;
import com.pdanetwork.models.Login.LoginStatus;
import com.pdanetwork.models.Login.Member;
import com.pdanetwork.models.Login.RegisterUser;
import com.pdanetwork.models.Login.UpdateData;
import com.pdanetwork.models.Profile;
import com.pdanetwork.models.Status;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;


public class MongoUsers {

    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Document> table;

    private Gson gson = new Gson();




    // global settings for all users
    private int daysValidAccount = 90;

    public MongoUsers() throws MongoConfigurationException {
        mongoClient = MongoClients.create("mongodb://localhost:27017/db");
        db = mongoClient.getDatabase("usersdb");

        table = db.getCollection("usersCollection");
    }

    public void closeClient(){
        mongoClient.close();
    }

    public int add(RegisterUser user){
        Document document = Document.parse(gson.toJson(user));

        int pdaId = getPdaID();
        document.put("token", Security.encrypt(user.getLogin()+":"+user.getPassword()));
        document.put("pdaId", pdaId);
        document.put("admin", 0);
        document.put("blocked", 0);
        document.put("group", 0);
        document.put("xp", 0);
        document.put("location","Ч-4");
        document.put("data", "");
        document.put("dialogs", "");
        document.put("lastModified", new Date().toString());
        document.put("registrationDate", new SimpleDateFormat("dd MM yyyy", Locale.US).format(new Date()));
        document.put("lastLoginAt", new Date());
        table.insertOne(document);

        table.createIndex(Indexes.ascending("lastLoginAt"),
                new IndexOptions().expireAfter(Integer.toUnsignedLong( daysValidAccount * 24 * 3600), TimeUnit.SECONDS));

        return pdaId;
    }

    private int getPdaID(){
        Document document = table.find().sort(new Document("_id",-1)).first();

        if(document!=null){
            int res = document.getInteger("pdaId");
            return res+1;
        } else {
            return 1;
        }
    }

    public boolean isBlocked(String token){
        Document query = new Document();

        query.put("token", token);

        Document result = table.find(query).first();

        if(result!=null) {
            int blocked = result.getInteger("blocked");
            return blocked == 1;
        } else return false;
    }

    public Member getByToken(String token){
        Document query = new Document();
        query.put("token", token);

        Document result = table.find(query).first();

        if(result!=null) {
            Member user = gson.fromJson(result.toJson(), Member.class);

            table.updateOne(
                    eq("token", token),
                    combine(set("lastLoginAt", new Date().toString())));

            return user;
        } else return null;
    }

    public int getPdaIdByToken(String token){
        Document query = new Document();
        query.put("token", token);

        Document result = table.find(query).first();

        return result.getInteger("pdaId");
    }

    public Member getEmailUser(String loginOrEmail){
        Document query = new Document();

        // задаем поле и значение поля по которому будем искать
        query.put("login", loginOrEmail);

        // осуществляем поиск
        Document element = table.find(query).first();

        if (element!=null) {
            return gson.fromJson(element.toJson(), Member.class);
        }

        query.clear();
        query.put("email", loginOrEmail);

        // осуществляем поиск
        element = table.find(query).first();
        if (element!=null){
            return gson.fromJson(element.toJson(), Member.class);
        }

        return null;
    }

    public Profile getProfileByPdaId(int pdaId){
        Document query = new Document();

        query.put("pdaId", pdaId);

        Document element = table.find(query).first();

        if (element!=null) {
            return gson.fromJson(element.toJson(), Member.class).getProfile();
        }

        return null;
    }

    public boolean userExists(int pdaId){
        Document query = new Document();
        query.put("pdaId", pdaId);

        Document result = table.find(query).first();

        return result != null;
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

        // указываем какую запись будем удалять с коллекции
        // задав поле и его текущее значение
        query.put("login", login);

        // удаляем запись с коллекции/таблицы
        table.findOneAndDelete(query);
    }

    public Status existsUserStatus(String login, String email){
        Status status = new Status();

        Document query = new Document();

        if (login!=null) {
            query.put("login", login);

            Document element = table.find(query).first();

            if (element == null) {
                status.setSuccess(true);
                status.setCode(200);
                status.setDescription("OK");
            } else {
                status.setSuccess(false);
                status.setCode(400);
                status.setDescription("Данный логин уже занят.");
                return status;
            }
        }

        query.clear();
        if (email!=null) {
            query.put("email", email);

            Document element = table.find(query).first();
            if (element == null) {
                status.setSuccess(true);
                status.setCode(200);
                status.setDescription("OK");
            } else {
                status.setSuccess(false);
                status.setCode(400);
                status.setDescription("Данный e-mail уже занят. Попробуйте войти.");
            }
        }
        return status;
    }

    public boolean existsUserBool(String login, String email){
        boolean exists = false;

        Document query = new Document();
        Document element;

        if (login!=null) {
            query.put("login", login);

            element = table.find(query).first();

            if (element == null) {
                exists = false;
            } else {
                exists = true;
                return exists;
            }
        }

        query.clear();
        if (email!=null) {
            query.put("email", email);

            element = table.find(query).first();
            if (element == null) {
                exists = false;
            } else {
                exists = true;
            }
        }
        return exists;
    }



    public LoginStatus tryLogin(String emailOrLogin, String password){
        LoginStatus status = new LoginStatus();

        Document query = new Document();

        query.put("login", emailOrLogin);

        Document element = table.find(query).first();

        if(element!=null){
          status = checkPassword(element,password);
        } else {
            query.clear();
            query.put("email",emailOrLogin);

            element = table.find(query).first();

            if (element!=null){
                status = checkPassword(element,password);
            }else {
                status.setSuccess(false);
                status.setCode(1);
                status.setDescription("Wrong login or email");
            }
        }

        return status;
    }

    public LoginStatus tryAdminLogin(String emailOrLogin, String password){
        LoginStatus status = new LoginStatus();

        Document query = new Document();

        query.put("login", emailOrLogin);

        Document element = table.find(query).first();

        if(element!=null){
            status = getLoginAdminStatus(password, status, element);
        } else {
            query.clear();
            query.put("email",emailOrLogin);

            element = table.find(query).first();

            if (element!=null){
                status = getLoginAdminStatus(password, status, element);
            }else {
                status.setSuccess(false);
                status.setCode(1);
                status.setDescription("Wrong login or email");
            }
        }

        return status;
    }

    private LoginStatus getLoginAdminStatus(String password, LoginStatus status, Document element) {
        if((Integer) element.get("admin")==1){
            status = checkPassword(element, password);
        } else {
            status.setSuccess(false);
            status.setCode(400);
            status.setDescription("Вы не администратор");
            status.setToken(null);
        }
        return status;
    }

    LoginStatus checkPassword(Document element, String password){
        LoginStatus status = new LoginStatus();
        Member user = new Member();
        user.setId((ObjectId) element.get("_id"));
        user.setLogin(String.valueOf(element.get("login")));
        user.setName(String.valueOf(element.get("name")));
        user.setPassword(String.valueOf(element.get("password")));
        user.setEmail(String.valueOf(element.get("email")));
        user.setToken(String.valueOf(element.get("token")));

        if (user.getPassword().equals(password)){
            status.setSuccess(true);
            status.setCode(0);
            status.setToken(user.getToken());
        } else {
            status.setSuccess(false);
            status.setCode(2);
            status.setDescription("Wrong password");
        }
        return status;
    }

    public void addDialogByToken(String token, int toPdaId, String dialogName, String firstMessage){
        // находим собеседника по id
        Document query = new Document();
        query.put("pdaId", toPdaId);
        Document result = table.find(query).first();
        //создаем по нему диалог
        Member member = gson.fromJson(result.toJson(), Member.class);
        Dialog dialog = new Dialog(dialogName, toPdaId,member.getLogin(), member.getAvatar(), firstMessage);

        //добавляем оригинальному юзеру дмиалог в топ
        query = new Document();
        ObjectId id = new ObjectId(token);
        query.put("_id", id);
        result = table.find(query).first();

        member = gson.fromJson(result.toJson(), Member.class);

        List<Dialog> dialogs = new ArrayList<>();

        Type listType = new TypeToken<List<Dialog>>(){}.getType();
        if(gson.fromJson(member.getDialogs(), listType)!=null){
            dialogs = gson.fromJson(member.getDialogs(), listType);
        }
        dialogs.add(0, dialog);

        // обновляем запись
        table.updateOne(eq("_id", id),
                set("dialogs",gson.toJson(dialogs)));

        dialog = new Dialog(dialogName, member.getPdaId(), member.getLogin(), member.getAvatar(), firstMessage);

        // второму юзеру по Id
        query = new Document();
        query.put("pdaId", toPdaId);
        result = table.find(query).first();

        member = gson.fromJson(result.toJson(), Member.class);

        dialogs = new ArrayList<>();

        listType = new TypeToken<List<Dialog>>(){}.getType();
        if(gson.fromJson(member.getDialogs(), listType)!=null){
            dialogs = gson.fromJson(member.getDialogs(), listType);
        }
        dialogs.add(0, dialog);

        // обновляем запись
        table.updateOne(eq("pdaId", toPdaId),
                set("dialogs",gson.toJson(dialogs)));

    }

    public void upDialog(String token, int toPdaId, String dialogName, String lastMessage){
        // находим собеседника по id
        Document query = new Document();
        query.put("pdaId", toPdaId);
        Document result = table.find(query).first();
        //получаем по нему диалог
        Member member = gson.fromJson(result.toJson(), Member.class);
        Dialog dialog = new Dialog(dialogName, toPdaId, member.getLogin(), member.getAvatar(), lastMessage);

        //добавляем оригинальному юзеру диалог в топ
        query = new Document();
        ObjectId id = new ObjectId(token);
        query.put("_id", id);
        result = table.find(query).first();

        Member member1 = gson.fromJson(result.toJson(), Member.class);

        updateDialogs(member1, dialog);

        // второму юзеру по id
        query = new Document();
        query.put("pdaId", toPdaId);
        result = table.find(query).first();

        member = gson.fromJson(result.toJson(), Member.class);

        dialog = new Dialog(dialogName, member1.getPdaId(), member1.getLogin(), member1.getAvatar(), lastMessage);

        updateDialogs(member, dialog);
    }

    private void updateDialogs(Member member, Dialog dialog) {
        Type listType = new TypeToken<List<Dialog>>(){}.getType();
        if(gson.fromJson(member.getDialogs(), listType)!=null){
            List<Dialog> dialogs = gson.fromJson(member.getDialogs(), listType);
            for (int i=0; i<dialogs.size(); i++){
                if(dialogs.get(i).name.equals(dialog.name)) dialogs.remove(i);
            }
            dialogs.add(0, dialog);
            // обновляем запись
            table.updateOne(eq("pdaId", member.getPdaId()),
                    set("dialogs",gson.toJson(dialogs)));
        } else {
            List<Dialog> dialogs = new ArrayList<>();
            dialogs.add(0, dialog);
            // обновляем запись
            table.updateOne(eq("pdaId", member.getPdaId()),
                    set("dialogs",gson.toJson(dialogs)));
        }
    }

    public UpdateResult changeField(String token, String field, String newValue){
        return table.updateOne (eq("token", token), combine(set(field, newValue),set("lastModified", new Date().toString())));
    }

    public UpdateResult changeFields(String token, List<Bson> updates){
        return table.updateOne (eq("token", token), combine(updates), new UpdateOptions().upsert(true).bypassDocumentValidation(true));
    }

    public Status updateMember(String token, UpdateData updateData){
        Status status = new Status();

        List<Bson> listSets = new ArrayList<>();

        for (String key : updateData.values.keySet()) {
            listSets.add(set(key, updateData.values.get(key)));
        }
        listSets.add(set("lastModified", new Date().toString()));
        UpdateResult updateResult = changeFields(token, listSets);

        return status;
    }
}
