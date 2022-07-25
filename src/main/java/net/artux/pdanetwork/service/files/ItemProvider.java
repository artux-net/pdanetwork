package net.artux.pdanetwork.service.files;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import net.artux.pdanetwork.entity.items.EncItemEntity;
import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    private final ObjectMapper objectMapper;

    public ItemProvider(ValuesService valuesService, ObjectMapper objectMapper) {
        this.valuesService = valuesService;
        this.objectMapper = objectMapper;

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
        try {
            String commonFile = readFile(valuesService.getConfigUrl() + "base/items/types/info.json");
            return Arrays.asList(objectMapper.readValue(commonFile, ItemEncType[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private <T extends ItemEntity> List<T> getType(int type) {
        Class<? extends ItemEntity> c = ItemType.getByTypeId(type).getTypeClass();
        try {
            String commonFile = readFile(valuesService.getConfigUrl() + "base/items/types/" + type + ".json");
            return Arrays.asList(objectMapper.readValue(commonFile, objectMapper.getTypeFactory().constructCollectionType(List.class, c)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<EncItemEntity> getEncType(int type) {
        try {
            String commonFile = readFile(valuesService.getConfigUrl() + "base/items/types/" + type + ".json");
            return Arrays.asList(objectMapper.readValue(commonFile, EncItemEntity[].class));
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