package tests;

import net.artux.pdanetwork.communication.utilities.MongoMessages;

public class MainTest {


    public static void main(String[] args)  {
        MongoMessages mongoMessages = new MongoMessages();
        //mongoMessages.newConversation(1, new ArrayList<>(Collections.singletonList(2)));
        System.out.println(mongoMessages.getDialogID(3,2));
    }

}
