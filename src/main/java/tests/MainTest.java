package tests;

import com.pdanetwork.authentication.UserManager;
import com.pdanetwork.models.Login.RegisterUser;
import com.pdanetwork.models.Status;
import com.pdanetwork.utills.mongo.MongoUsers;

public class MainTest {

    public static void main(String[] args)  {
        testMongoUsers();
    }

    private static void testUserManager(){
        System.out.println(System.getProperty("user.home"));
        RegisterUser registerUser = new RegisterUser("Prissoner",
                "maxim","hhdshfsjkhfjsadsasksdhfjksdhf131231432543543654343341234578798h@gmailk.com", "123455", "1");
        Status status = new UserManager().checkNewUser(registerUser, "ru");
        System.out.println(status.toString());
    }

    private static void testMongoUsers(){
        MongoUsers m = new MongoUsers();
        m.add(new RegisterUser("","", "", "", "2"));
    }

}
