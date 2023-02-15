package net.artux.pdanetwork.service.items;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.*;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.items.ItemDto;
import net.artux.pdanetwork.models.items.ItemMapper;
import net.artux.pdanetwork.models.items.ItemsContainer;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.user.UserService;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private HashMap<Long, ItemEntity> itemsMap;
    private ItemsContainer itemsContainer;

    private final Logger logger;
    private final ObjectMapper objectMapper;
    private final ItemMapper itemMapper;
    private final BaseItemService baseItemService;

    private final UserService userService;
    private final UserRepository userRepository;


    @PostConstruct
    public <T extends ItemEntity> void init() throws IOException {
        itemsMap = new HashMap<>();
        itemsContainer = new ItemsContainer();
        for (ItemType type : ItemType.values()) {
            List<T> items = readType(type);
            for (ItemEntity item : items)
                itemsMap.put(item.getBase().getId(), item);
            itemsContainer.set(itemMapper.anyList(items, type), type);
        }
    }

    public ItemsContainer getItemsContainer() {
        return itemsContainer;
    }

    @SuppressWarnings("unchecked")
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
        return itemsMap.get(baseId);
    }

    public ItemDto getItemDto(long baseId) {
        return itemMapper.any(itemsMap.get(baseId));
    }

    public void addItem(UserEntity user, long baseId, int quantity) {
        ItemEntity itemEntity = getItem(baseId);
        ItemType type = itemEntity.getBase().getType();
        logger.info("Add item {} for {}", itemEntity.getBase().getTitle(), user.getLogin());
        if (type.isCountable()) {
            itemEntity.setQuantity(quantity);
            addAsCountable(user, itemEntity);
        } else {
            addAsNotCountable(user, itemEntity);
        }
    }

    public void deleteItem(UserEntity user, int baseId, int quantity) {
        List<? extends ItemEntity> optional = user.getAllItems().stream()
                .filter(item -> item.getBase().getId() == baseId)
                .toList();
        if (optional.size() == 1) {
            ItemEntity t = optional.get(0);
            t.setQuantity(t.getQuantity() - quantity);
        } else {
            user.getItemsByType(getItem(baseId).getBase().getType())
                    .removeIf((Predicate<ItemEntity>) itemEntity -> itemEntity.getBase().getId() == baseId);
        }
    }

    public Status setWearable(UserEntity user, UUID id) {
        ItemEntity item = user.getAllItems()
                .stream()
                .filter(itemEntity -> itemEntity.getId().equals(id))
                .findFirst()
                .orElseThrow();

        ItemType type = item.getBase().getType();
        if (type.isWearable()) {
            Optional<WearableEntity> optionalItem = user.getWearableItems()
                    .stream()
                    .filter(wearableEntity -> wearableEntity.getId().equals(id) && wearableEntity.isEquipped())
                    .findFirst();

            if (optionalItem.isPresent()) {
                WearableEntity oldWearable = optionalItem.get();

                oldWearable.setEquipped(false);
                if (item.getId().equals(oldWearable.getId()))
                    return new Status(true, "Предмет снят.");
            }

            ((WearableEntity) item).setEquipped(true);

            return new Status(true, "Предмет надет.");
        } else return new Status(false, "Невозможно надеть предмет");
    }

    public Status setWearable(UUID id) {
        UserEntity user = userService.getUserById();
        Status status = setWearable(user, id);
        if (status.isSuccess())
            userRepository.save(user);
        return status;
    }

    private <T extends ItemEntity> void addAsNotCountable(UserEntity user, T item) {
        var type = item.getBase().getType();
        if (type.isWearable()) {
            boolean userWears = user.getWearableItems()
                    .stream()
                    .anyMatch(userItem -> userItem.isEquipped() && userItem.getBase().getType().equals(type));
            ((WearableEntity) item).setEquipped(!userWears);
        }
        item.setQuantity(1);
        addAsIs(user, item);
    }

    private <T extends ItemEntity> void addAsCountable(UserEntity user, T itemEntity) {
        Optional<? extends ItemEntity> optionalItem = user.getAllItems()
                .stream()
                .filter(item -> item.getBase().getId().equals(itemEntity.getBase().getId()))
                .findFirst();
        if (optionalItem.isPresent()) {
            ItemEntity item = optionalItem.get();
            item.setQuantity(item.getQuantity() + itemEntity.getQuantity());
        } else {
            addAsIs(user, itemEntity);
        }
    }

    private <T extends ItemEntity> void addAsIs(UserEntity user, T itemEntity) {
        itemEntity.setOwner(user);
        switch (itemEntity.getBase().getType()) {
            case BULLET -> user.getBullets().add((BulletEntity) itemEntity);
            case ARMOR -> user.getArmors().add((ArmorEntity) itemEntity);
            case PISTOL, RIFLE -> user.getWeapons().add((WeaponEntity) itemEntity);
            case ARTIFACT -> user.getArtifacts().add((ArtifactEntity) itemEntity);
            case DETECTOR -> user.getDetectors().add((DetectorEntity) itemEntity);
            case MEDICINE -> user.getMedicines().add((MedicineEntity) itemEntity);
        }
    }

}