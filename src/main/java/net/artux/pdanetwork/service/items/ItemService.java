package net.artux.pdanetwork.service.items;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.models.items.ItemDto;
import net.artux.pdanetwork.models.items.ItemMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemService {

    private HashMap<Long, ItemEntity> items = new HashMap<>();

    private final ObjectMapper objectMapper;
    private final ItemMapper itemMapper;
    private final BaseItemService baseItemService;

    @PostConstruct
    public void init() throws IOException {
        items = new HashMap<>();
        for (ItemType type : ItemType.values()) {
            for (ItemEntity item : readType(type))
                items.put(item.getBase().getId(), item);
        }
    }

    private <T extends ItemEntity> List<T> readType(ItemType type) throws IOException {
        Resource resource = new ClassPathResource("static/base/items/types/" + type.getTypeId() + ".json");
        List<T> list = new LinkedList<>();
        JsonNode node = objectMapper.readTree(resource.getInputStream());

        if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                JsonNode item = node.get(i);

                T t = (T) objectMapper.treeToValue(item, type.getTypeClass());
                long baseId = item.get("baseId").asLong();
                t.setBase(baseItemService.getBaseItem(baseId));
                list.add(t);
            }
        }

        return list;
    }

    public ItemEntity getItem(long baseId) {
        return items.get(baseId);
    }

    public ItemDto getItemDto(long baseId) {
        return itemMapper.any(items.get(baseId));
    }


}