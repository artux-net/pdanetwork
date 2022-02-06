package net.artux.pdanetwork.service.files;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import net.artux.pdanetwork.models.profile.items.*;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Getter
public class Types implements FileService {

    private List<ItemType> itemTypes = new ArrayList<>();
    private static HashMap<Integer, List<? extends ItemEntity>> items = new HashMap<>();
    private static HashMap<Integer, List<EncItemEntity>> encItems = new HashMap<>();

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
            String commonFile = readFile(valuesService.getConfigUrl() + "base/items/types/info.json");
            Type itemsListType = TypeToken.getParameterized(ArrayList.class, c).getType();
            return gson.fromJson(commonFile, itemsListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private <T extends ItemEntity> List<T> getType(int type) {
        Class<? extends ItemEntity> c = getClassByType(type);
        try {
            String commonFile = readFile(valuesService.getConfigUrl() + "base/items/types/" + type + ".json");
            Type itemsListType = TypeToken.getParameterized(ArrayList.class, c).getType();
            return gson.fromJson(commonFile, itemsListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<EncItemEntity> getEncType(int type) {
        Class c = EncItemEntity.class;
        try {
            String commonFile = readFile(valuesService.getConfigUrl() + "base/items/types/" + type + ".json");
            Type itemsListType = TypeToken.getParameterized(ArrayList.class, c).getType();
            return gson.fromJson(commonFile, itemsListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class<? extends ItemEntity> getClassByType(int type){
        switch (type){
            case 0:
            case 1:
                return WeaponEntity.class;
            case 3:
                return ArtifactEntity.class;
            case 4:
                return ArmorEntity.class;
            default:
                return ItemEntity.class;

        }
    }

    public List<? extends ItemEntity> getTypeList(int type){
        return items.get(type);
    }

    public List<EncItemEntity> getEncTypeList(int type){
        return encItems.get(type);
    }

    public ItemEntity getItem(int type, int id) {
        for (ItemEntity itemEntity : getTypeList(type)) {
            if (itemEntity.getId() == id) {
                itemEntity.setType(type);
                return itemEntity;
            }
        }
        return null;
    }

    public List<? extends ItemEntity> getItems(int type, List<Integer> ids) {
        List<ItemEntity> itemEntities = new ArrayList<>();
        for (ItemEntity itemEntity : getTypeList(type)) {
            if (ids.contains(itemEntity.getId())) {
                itemEntity.setType(type);
                itemEntities.add(itemEntity);
            }
        }
        return itemEntities;
    }
    public EncItemEntity getEncItem(int type, int id) {
        for (EncItemEntity item : getEncTypeList(type)) {
            if (item.getId() == id) {
                item.setType(type);
                return item;
            }
        }
        return null;
    }
}