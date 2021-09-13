package net.artux.pdanetwork.utills;

import net.artux.pdanetwork.communication.utilities.MongoMessages;
import net.artux.pdanetwork.service.util.ValuesService;
import net.artux.pdanetwork.service.mongo.MongoUsers;
import org.apache.logging.log4j.Logger;

public class ServletContext{

    public static MongoUsers mongoUsers = new MongoUsers();
    public static MongoMessages mongoMessages = new MongoMessages(new ValuesService());

    private static Logger logger;

    public static void log(String msg) {
        if (logger!=null)
            logger.info(msg);
        else {
            System.out.println(msg);
        }
    }

    public static void error(String msg, Throwable thr) {
        if (logger!=null)
            logger.error(msg, thr);
        else {
            System.out.println(msg + " ->");
            thr.printStackTrace();
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static String getPath() {
            return "/data/pdanetwork/";
    }
}
