package net.artux.pdanetwork.utills;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import net.artux.pdanetwork.authentication.UserManager;
import net.artux.pdanetwork.communication.utilities.MongoMessages;
import net.artux.pdanetwork.utills.mail.MailService;
import net.artux.pdanetwork.utills.mongo.MongoUsers;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;

@WebListener
public class ServletContext implements javax.servlet.ServletContextListener {

    public static MailService mailService = new MailService();
    public static MongoUsers mongoUsers = new MongoUsers();
    public static MongoMessages mongoMessages = new MongoMessages();
    public static UserManager userManager = new UserManager();
    private static boolean debug = false;

    public static String host;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
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
        System.out.println("Servlet eliminated, please check the status.");
        mongoUsers.close();
    }

    private void log(String msg){
        System.out.println(new Date() +" "+msg);
    }

    public static String getPath() {
        if (debug)
            return "data/pdanetwork/";
        else
            return "/data/pdanetwork/";
    }
}
