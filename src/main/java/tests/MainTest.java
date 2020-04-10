package tests;

import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.utills.Security;
import net.artux.pdanetwork.utills.mongo.MongoUsers;

public class MainTest {

    static int s = 2;

    public static void main(String[] args)  {
        String registerToken = Security.encrypt("Prisoner");
        System.out.println(registerToken);
        registerToken = Security.encrypt("Prisone");
        System.out.println(registerToken);
    }

    private static void testMongoUsers(){
        MongoUsers m = new MongoUsers();
        m.add(new RegisterUser("","", "", "", "2"));
    }

}
