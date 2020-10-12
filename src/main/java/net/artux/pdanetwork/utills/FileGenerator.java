package net.artux.pdanetwork.utills;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static net.artux.pdanetwork.utills.ServletContext.getPath;

public class FileGenerator {

    public static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static class Icons{
        public static File getIcon(String name){
            return new File(getPath() + "base/items/icons/" + name);
        }
    }

    public static class Articles{
        public static String getArticle(String id) throws IOException {
            return readFile(getPath() + "base/enc/" + id + ".html", StandardCharsets.UTF_8);
        }
    }

}
