package net.artux.pdanetwork.utills;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.artux.pdanetwork.models.profile.items.Item;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileGenerator {

    private List<Item> itemsList;
    private Gson gson = new Gson();
    private String commonFile;

    public FileGenerator() {
        try {
            commonFile = readFile("/data/pdanetwork/base/items/common", StandardCharsets.UTF_8);
            Type itemsListType = new TypeToken<List<Item>>() {}.getType();
            itemsList = gson.fromJson(commonFile, itemsListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public Item getItem(int id){
        for (Item item : itemsList) {
            if (item.id==id)
                return item;
        }
        return null;
    }

    public static class Icons{

        public static File getIcon(String name){
            return new File("/data/pdanetwork/base/items/icons/" + name);
        }

    }

    public static class Articles{

        public static String getArticle(String id) throws IOException {
            return readFile("/data/pdanetwork/base/enc/" + id + ".html", StandardCharsets.UTF_8);
        }

    }

}
