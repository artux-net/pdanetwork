package net.artux.pdanetwork.servlets;

public class Data {

    String baseurl;
    String[] messages = new String[0];
    String[] files;
    boolean[] isImages;
    int code;

    Data(String[] files, String[] messages) {
        baseurl = "https://api.artux.net/pda/uploads/articles/";
        code = 220;
        this.files = files;
        //this.messages = messages;
        isImages = new boolean[files.length];
        for (int i = 0; i < files.length; i++) {
            if (files[i] != null)
                isImages[i] = files[i].contains(".png") || files[i].contains(".jpg")
                        || files[i].contains(".jpeg") || files[i].contains(".svg")
                        || files[i].contains(".bmp") || files[i].contains(".gif");
        }

    }
}
