package net.artux.pdanetwork.utills;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.artux.pdanetwork.models.profile.items.Item;
import net.artux.pdanetwork.models.profile.items.Type0;
import net.artux.pdanetwork.models.profile.items.Type1;
import org.json.JSONArray;

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
            commonFile = readFile("base/items/common", StandardCharsets.UTF_8);
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
        int index = 0;
        for (Item item : itemsList) {
            if(item.id == id){
                JSONArray jsArray = new JSONArray(commonFile);
                switch (item.type){
                    case 0:
                        return gson.fromJson(jsArray.getJSONObject(index).toString(), Type0.class);
                    case 1:
                        return gson.fromJson(jsArray.getJSONObject(index).toString(), Type1.class);
                    case 2:
                        //return gson.fromJson(jsArray.getJSONObject(index).toString(), Type2.class);
                    default:
                        return item;
                }
            }
            index++;
        }
        return null;
    }

    public static class Icons{

        public static File getIcon(String name){
            return new File("base/items/icons/" + name);
        }

    }

    public static class Articles{

        public static String getArticle(String id) throws IOException {
            return readFile("base/enc/" + id + ".html", StandardCharsets.UTF_8);
        }

    }

}
