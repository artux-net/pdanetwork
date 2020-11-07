package net.artux.pdanetwork.utills;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import net.artux.pdanetwork.authentication.UserManager;
import net.artux.pdanetwork.communication.utilities.MongoMessages;
import net.artux.pdanetwork.utills.mail.MailService;
import net.artux.pdanetwork.utills.mongo.MongoUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@WebListener
public class ServletContext implements javax.servlet.ServletContextListener {

    public static MailService mailService = new MailService();
    public static MongoUsers mongoUsers = new MongoUsers();
    public static MongoMessages mongoMessages = new MongoMessages();
    public static UserManager userManager = new UserManager();
    private static final boolean debug = false;

    private static Logger logger;

    public static String host;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        System.setProperty("log4j.configurationFile", getPath() + "config/log.conf");
        System.setProperty("log-path", getPath() + "logs");
        logger = LogManager.getLogger(ServletContext.class);
        log("Servlets initialized, server started.");
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("google.com", 80));
            if (debug)
                host = "192.168.0.200";
            else
                host = "35.204.191.66";
            System.out.println("Host Address: " + host);
        } catch (IOException e) {
            System.out.println("Cannot define host, start local mode");
            host = "127.0.0.1";
        }
        log("Server info: " + servletContextEvent.getServletContext().getServerInfo());
        log("Working Directory:" + System.getProperty("user.dir"));
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.ERROR);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log("Servlet eliminated, please check the status.");
        mongoUsers.close();
    }

    public static void log(String msg) {
        logger.info(msg);
    }

    public static void error(String msg, Throwable thr) {
        logger.error(msg, thr);
    }

    public static Logger getLogger() {
        return logger;
    }

    public static String getPath() {
        if (debug)
            return "data/pdanetwork/";
        else
            return "/data/pdanetwork/";
    }
}
