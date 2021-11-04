package net.artux.pdanetwork.utills;

import org.apache.logging.log4j.Logger;

public class ServletContext{


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
