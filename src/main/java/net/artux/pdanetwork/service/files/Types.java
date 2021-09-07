package net.artux.pdanetwork.service.files;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import net.artux.pdanetwork.models.profile.items.*;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Getter
public class Types implements FileService {

    private List<ItemType> itemTypes = new ArrayList<>();
    private static HashMap<Integer, List<? extends Item>> items = new HashMap<>();
    private static HashMap<Integer, List<EncItem>> encItems = new HashMap<>();

    private final ValuesService valuesService;
        /*
        0 - pistols
        1 - rifles
        2 - bullets
        3 - artifacts
        4 - armors
         */

    static Gson gson = new Gson();

    Types(ValuesService valuesService) {
        this.valuesService = valuesService;
        reset();
    }

    public void reset(){
        itemTypes = initTypes();
        items = new HashMap<>();
        encItems = new HashMap<>();
        if (itemTypes!=null)
            for (ItemType type: itemTypes){
                items.put(type.getId(), getType(type.getId()));
                encItems.put(type.getId(), getEncType(type.getId()));
            }
    }

    private List<ItemType> initTypes() {
        Class c = ItemType.class;
        try {
            String commonFile = readFile(valuesService.getWorkingDirectory() + "base/items/types/info.json", StandardCharsets.UTF_8);
            Type itemsListType = TypeToken.getParameterized(ArrayList.class, c).getType();
            return gson.fromJson(commonFile, itemsListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private <T extends Item> List<T> getType(int type) {
        Class<? extends Item> c = getClassByType(type);
        try {
            String commonFile = readFile(valuesService.getWorkingDirectory() + "base/items/types/" + type + ".json", StandardCharsets.UTF_8);
            Type itemsListType = TypeToken.getParameterized(ArrayList.class, c).getType();
            return gson.fromJson(commonFile, itemsListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<EncItem> getEncType(int type) {
        Class c = EncItem.class;
        try {
            String commonFile = readFile(valuesService.getWorkingDirectory() + "base/items/types/" + type + ".json", StandardCharsets.UTF_8);
            Type itemsListType = TypeToken.getParameterized(ArrayList.class, c).getType();
            return gson.fromJson(commonFile, itemsListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class<? extends Item> getClassByType(int type){
        switch (type){
            case 0:
            case 1:
                return Weapon.class;
            case 3:
                return Artifact.class;
            case 4:
                return Armor.class;
            default:
                return Item.class;

        }
    }

    public List<? extends Item> getTypeList(int type){
        return items.get(type);
    }

    public List<EncItem> getEncTypeList(int type){
        return encItems.get(type);
    }

    public Item getItem(int type, int id) {
        for (Item item : getTypeList(type)) {
            if (item.getId() == id) {
                item.setType(type);
                return item;
            }
        }
        return null;
    }

    public List<? extends Item> getItems(int type, List<Integer> ids) {
        List<Item> items = new ArrayList<>();
        for (Item item : getTypeList(type)) {
            if (ids.contains(item.getId())) {
                item.setType(type);
                items.add(item);
            }
        }
        return items;
    }
    public EncItem getEncItem(int type, int id) {
        for (EncItem item : getEncTypeList(type)) {
            if (item.getId() == id) {
                item.setType(type);
                return item;
            }
        }
        return null;
    }
}