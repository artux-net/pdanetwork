package net.artux.pdanetwork.utills;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import net.artux.pdanetwork.communication.utilities.MongoMessages;
import net.artux.pdanetwork.service.util.ValuesService;
import net.artux.pdanetwork.utills.mongo.MongoFeed;
import net.artux.pdanetwork.utills.mongo.MongoUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

public class ServletContext{

    public static final boolean debug = false;
    //mongodb://mongo-root:XWA47iIgQrPhuaukTryu@35.237.32.236:27017/
    public static String getAddress(){
        return "https://api.artux.net/pda/";
    }

    public static String host = "35.237.32.236";
    {
        System.setProperty("java.rmi.server.hostname","35.237.32.236");
        if (!debug){
            InetAddress localhost;
            try {
                localhost = InetAddress.getLocalHost();

                log("System IP Address : " +
                        (localhost.getHostAddress()).trim());
                URL url_name = new URL("http://bot.whatismyipaddress.com");

                BufferedReader sc =
                        new BufferedReader(new InputStreamReader(url_name.openStream()));
                String systemipaddress = "";
                systemipaddress = sc.readLine().trim();

                log("Public IP Address: " + systemipaddress + "\n");
                host = systemipaddress;
            } catch (Exception e) {
                error("Init error", e);
            }
        }
    }


    //public static MailService mailService = new MailService();
    public static MongoUsers mongoUsers = new MongoUsers();
    public static MongoMessages mongoMessages = new MongoMessages(new ValuesService());
    public static MongoFeed mongoFeed = new MongoFeed(new ValuesService());
    //public static ActionService actionService = new ActionService();

    private static Logger logger;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.setProperty("log4j.configurationFile", getPath() + "config/log.conf");
        System.setProperty("log-path", getPath() + "logs");

        System.setProperty("DEBUG.MONGO", "true");
        System.setProperty("DB.TRACE", "true");

        logger = LogManager.getLogger(ServletContext.class);
        log("Servlets initialized, server started.");
        InetAddress localhost = null;
        try {
            if (debug)
                host = "35.237.32.236";
            else {
                localhost = InetAddress.getLocalHost();
                log("System IP Address : " +
                        (localhost.getHostAddress()).trim());
                URL url_name = new URL("http://bot.whatismyipaddress.com");

                BufferedReader sc =
                        new BufferedReader(new InputStreamReader(url_name.openStream()));
                String systemipaddress = "";
                systemipaddress = sc.readLine().trim();

                log("Public IP Address: " + systemipaddress + "\n");
                host = systemipaddress;
            }

        } catch (Exception e) {
            error("Can not determine IP", e);
        }

        log("Server info: " + servletContextEvent.getServletContext().getServerInfo());
        log("Working Directory:" + System.getProperty("user.dir"));
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.ERROR);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log("Servlet eliminated, please check the status.");
        mongoUsers.close();
    }

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
