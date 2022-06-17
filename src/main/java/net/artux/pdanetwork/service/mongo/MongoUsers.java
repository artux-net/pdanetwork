package net.artux.pdanetwork.service.mongo;

public class MongoUsers {
/*

    private final MongoClient mongoClient;
    private final MongoCollection<UserEntity> table;

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
        MongoDatabase db = mongoClient.getDatabase("test");

        table = db.getCollection("member", UserEntity.class);
        //table.createIndex(Indexes.ascending("token", "pdaId", "login"));
    }

    public void close(){
        mongoClient.close();
    }

    public int add(RegisterUser user){
        int pdaId = getPdaID();
        table.insertOne(new UserEntity(user, pdaId, new BCryptPasswordEncoder()));

        table.createIndex(Indexes.ascending("lastLoginAt"),
                new IndexOptions().expireAfter(Integer.toUnsignedLong( daysValidAccount * 24 * 3600), TimeUnit.SECONDS));

        return pdaId;
    }

    private int getPdaID(){
        UserEntity userEntity = table.find().sort(new Document("_id", -1)).first();

        if (userEntity != null) {
            return userEntity.getPdaId() + 1;
        } else {
            return 1;
        }
    }

    public boolean isBlocked(String token){
         return false;
    }

    public boolean setBlocked(int pdaId, int blocked){
        return false;
    }
    public UserEntity getById(int pdaId) {
        return getMember(pdaId);
    }

    public UserEntity getByToken(String token){
        UserEntity result = getMember(token);

        if(result!=null) {
            table.updateOne(
                    eq("token", token),
                    combine(set("lastLoginAt", Instant.now().toEpochMilli())));
            return result;
        } else return null;
    }

    public int getPdaIdByToken(String token){
        UserEntity result = getMember(token);
        //TODO if 0 then... What?
        if (result!=null)
            return result.getPdaId();
        else
            return 0;
    }

    public UserEntity getEmailUser(String loginOrEmail){
        UserEntity element = getMember("login", loginOrEmail);
        if (element!=null) {
            return element;
        }

        element = getMember("email", loginOrEmail);

        return element;
    }

    public Profile getProfileByPdaId(int pdaId){
        UserEntity element = getMember(pdaId);

        if (element!=null) {
            return new Profile(element);
        }
        return null;
    }

    public Profile getProfileByPdaId(String token, int pdaId) {
        UserEntity user = getMember(token);
        UserEntity element = getMember(pdaId);

        if (element != null) {
            return new Profile(element, user);
        }
        return null;
    }

    public boolean userExists(int pdaId){
        return getMember(pdaId) != null;
    }

    public void updatePassword(int pdaId, String password) {
        UserEntity userEntity = getMember(pdaId);
        userEntity.hashPassword(password);
        updateMember(userEntity);
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
        UserEntity element = getMember("login", emailOrLogin);

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
        UserEntity element = getMember("login", emailOrLogin);

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

    private Status getLoginAdminStatus(String password, UserEntity element) {
        if (element.getRole().equals("admin")) {
            return checkPassword(element, password, false);
        } else {
            //ServletContext.log("Not admin tried to use admin panel " + element.getLogin());
            return new Status(false, "You aren't admin");
        }
    }

    private Status checkPassword(UserEntity element, String password, boolean resetToken) {
        if (element.getPassword().equals(String.valueOf(password.hashCode()))) {
            if (resetToken) {
                //element.setToken(Security.encrypt(element.getLogin() + ":" + password));
                updateMember(element);
            }
            return new Status();
        } else {
            //ServletContext.log("Wrong password for " + element.getLogin());
            return new Status(false, "Wrong password");
        }
    }

    public void friendRequest(String token, int id){
        UserEntity user = getMember(token);
        UserEntity newFriend = getMember(id);

        */
/*user.subs.add(newFriend.getPdaId());
        updateMember(user);

        newFriend.requests.add(user.getPdaId());*//*

        updateMember(newFriend);
    }

    */
/*public List<Integer> getFriendRequests(int pdaId) {
        return getMember(pdaId).requests;
    }*//*


    public boolean addFriend(String token, Integer id) {

            return false;
    }

    public boolean removeFriend(String token, Integer id) {

            return false;
    }

    public void addDialog(int pdaId, int conversation) {
        */
/*UserEntity user = getMember(pdaId);

        if (user != null) {
            user.dialogs.add(conversation);
            updateMember(user);
        }*//*

    }

    public void updateDialog(int pdaId, Integer conversation) {
        */
/*UserEntity user = getMember(pdaId);
        if (user != null) {
            user.dialogs.remove(conversation);
            user.dialogs.add(0, conversation);
            updateMember(user);
        }*//*

    }

    public UserEntity getMember(int pdaId) {
        Document query = new Document();
        query.put("pdaId", pdaId);

        return table.find(query).first();
    }

    private UserEntity getMember(String token) {
        Document query = new Document();
        query.put("token", token);

        return table.find(query).first();
    }

    private UserEntity getMember(String key, Object value) {
        Document query = new Document();
        query.put(key, value);

        return table.find(query).first();
    }

    public List<UserInfo> sort(int from) {
        List<UserInfo> users = new ArrayList<>();
        //table.find().sort(new Document("xp", -1)).skip(from).limit(30).forEach(member -> users.add(new UserInfo(member)));
        return users;
    }

    public UpdateResult updateMember(@NotNull UserEntity userEntity) {
        //userEntity.setLastModified(Instant.now().toEpochMilli());
        return table.replaceOne(eq("pdaId", userEntity.getPdaId()), userEntity);
    }

    public UpdateResult changeField(Object token, String field, Object newValue) {
        return table.updateOne (eq("token", token), combine(set(field, newValue),set("lastModified", Instant.now().toEpochMilli())));
    }

    public UpdateResult changeFields(Object token, List<Bson> updates) {
        updates.add(set("lastModified", Instant.now().toEpochMilli()));
        return table.updateOne (eq("token", token), combine(updates), new UpdateOptions().upsert(true).bypassDocumentValidation(true));
    }
*/

}
