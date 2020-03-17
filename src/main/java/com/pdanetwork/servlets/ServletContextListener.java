package com.pdanetwork.servlets;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.util.Date;

@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log("Servlets initialized, server started.");
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
