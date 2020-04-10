package net.artux.pdanetwork.utills;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
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

    public static MongoUsers mongoUsers = new MongoUsers();
    public static String host;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log("Servlets initialized, server started.");

        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("google.com", 80));
            host = socket.getLocalAddress().getHostAddress();
            System.out.println("Host Address: " + host);
        } catch (IOException e) {
            System.out.println("Cannot define host, start local mode");
            host = "127.0.0.1";
        }
        log("Server info: " + servletContextEvent.getServletContext().getServerInfo());
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.ERROR);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Servlet eliminated, please check the status.");
    }

    void log(String msg){
        System.out.println(new Date() +" Debug: "+msg);
    }
}
