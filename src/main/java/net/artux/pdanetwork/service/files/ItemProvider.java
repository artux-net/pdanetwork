package net.artux.pdanetwork.service.files;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import net.artux.pdanetwork.entity.items.EncItemEntity;
import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Getter
public class ItemProvider implements FileService {

    private List<ItemEncType> itemEncTypes = new ArrayList<>();
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

    public ItemProvider(ValuesService valuesService) {
        this.valuesService = valuesService;
        reset();
    }

    public void reset() {
        itemEncTypes = initTypes();
        items = new HashMap<>();
        encItems = new HashMap<>();
        if (itemEncTypes != null)
            for (ItemEncType type : itemEncTypes) {
                items.put(type.getId(), getType(type.getId()));
                encItems.put(type.getId(), getEncType(type.getId()));
            }
    }

    private List<ItemEncType> initTypes() {
        Class c = ItemEncType.class;
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
        Class<? extends ItemEntity> c = ItemType.getByTypeId(type).getTypeClass();
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

    private List<? extends ItemEntity> getTypeList(int type) {
        return items.get(type);
    }

    public ItemEntity getItem(int type, int id) {
        for (ItemEntity itemEntity : getTypeList(type)) {
            if (itemEntity.getBaseId() == id) {
                itemEntity.setType(ItemType.getByTypeId(type));
                return itemEntity;
            }
        }
        return null;
    }

    public List<? extends ItemEntity> getItems(int type, List<Integer> ids) {
        List<ItemEntity> itemEntities = new ArrayList<>();
        for (ItemEntity itemEntity : getTypeList(type)) {
            if (ids.contains(itemEntity.getBaseId())) {
                itemEntity.setType(ItemType.getByTypeId(type));
                itemEntities.add(itemEntity);
            }
        }
        return itemEntities;
    }

    public List<EncItemEntity> getEncTypeList(int type) {
        return encItems.get(type);
    }

    public EncItemEntity getEncItem(int type, int id) {
        for (EncItemEntity item : getEncTypeList(type)) {
            if (item.getBaseId() == id) {
                item.setType(ItemType.getByTypeId(type));
                return item;
            }
        }
        return null;
    }
}