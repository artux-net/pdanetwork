package net.artux.pdanetwork.servlets;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class Upload {

    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    boolean success;
    String time = "2021-04-05 12:20:46";
    Data data;
    String elapsedTime;

    public Upload(String[] files, String[] messages) {
        success = true;
        time = dateFormat.format(Date.from(Instant.now()));
        data = new Data(files, messages);
        elapsedTime = "null";
    }

}
