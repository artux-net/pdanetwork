package net.artux.pdanetwork.service.items;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.artux.pdanetwork.entity.items.ArmorEntity;
import net.artux.pdanetwork.entity.items.ArtifactEntity;
import net.artux.pdanetwork.entity.items.BulletEntity;
import net.artux.pdanetwork.entity.items.DetectorEntity;
import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.entity.items.MedicineEntity;
import net.artux.pdanetwork.entity.items.WeaponEntity;
import net.artux.pdanetwork.entity.items.WearableEntity;
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

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ItemService {

    private final HashMap<Long, ItemEntity> itemsMap;
    private ItemsContainer itemsContainer;

    private final Logger logger;
    private final ObjectMapper objectMapper;
    private final ItemMapper itemMapper;
    private final BaseItemService baseItemService;

    private final UserService userService;
    private final UserRepository userRepository;

    public <T extends ItemEntity> ItemService(Logger logger, ObjectMapper objectMapper, ItemMapper itemMapper,
                                              BaseItemService baseItemService,
                                              UserService userService, UserRepository userRepository) {
        this.logger = logger;
        this.objectMapper = objectMapper;
        this.itemMapper = itemMapper;
        this.baseItemService = baseItemService;
        this.userService = userService;
        this.userRepository = userRepository;

        itemsMap = new HashMap<>();
        itemsContainer = new ItemsContainer();
        try {
            for (ItemType type : ItemType.values()) {
                List<T> items = readType(type);
                for (ItemEntity item : items)
                    itemsMap.put(item.getBasedId(), item);
                itemsContainer.set(itemMapper.anyList(items, type), type);
            }
            logger.info("Total read {} items", itemsMap.size());
        } catch (IOException e) {
            logger.error("Failed to read items", e.getMessage());
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
        logger.info("Read {} items of type {}", list.size(), type.name());

        return list;
    }

    public ItemEntity getItem(long baseId) {
        return itemsMap.get(baseId);
    }

    public ItemDto getItemDto(long baseId) {
        return itemMapper.any(getItem(baseId));
    }

    public void addItem(UserEntity user, long baseId, int quantity) {
        final ItemEntity itemEntity = getItem(baseId);
        ItemType type = itemEntity.getBasedType();
        logger.info("Add item {} for {}", itemEntity.getBase().getTitle(), user.getLogin());
        if (type.isCountable()) {
            itemEntity.setQuantity(quantity);
            addAsCountable(user, itemEntity);
        } else {
            addAsNotCountable(user, itemEntity);
        }
    }

    public void deleteItem(UserEntity user, int baseId, int quantity) {
        ItemType type = getItem(baseId).getBasedType();
        List<? extends ItemEntity> optional = user.getItemsByType(type)
                .stream()
                .filter(item -> item.getBasedId() == baseId)
                .toList();

        if (optional.size() == 1) {
            ItemEntity item = optional.get(0);
            item.setQuantity(item.getQuantity() - quantity);
            if (item.getQuantity() <= 0)
                user.getItemsByType(type).remove(item);
        } else {
            final int q[] = {quantity};
            user.getItemsByType(type)
                    .removeIf(itemEntity -> {
                        boolean toRemove = itemEntity.getBasedId() == baseId;
                        if (toRemove)
                            q[0]--;
                        if (q[0] < 0)
                            return false;
                        return toRemove;
                    });
        }
    }

    public Status setWearableItem(UserEntity user, WearableEntity item) {
        ItemType type = item.getBasedType();
        if (!type.isWearable())
            return new Status(false, "Невозможно надеть предмет");

        WearableEntity wearableItem = item;
        boolean isEquippedNow = !wearableItem.isEquipped();
        wearableItem.setEquipped(isEquippedNow);

        if (isEquippedNow) {
            user.getItemsByType(type)
                    .forEach(wearableEntity -> ((WearableEntity) wearableEntity).setEquipped(false));
            return new Status(true, "Предмет надет.");
        }

        return new Status(true, "Предмет снят.");
    }

    public Status setWearableItemById(UserEntity user, UUID id) {
        ItemEntity item = user.getAllItems()
                .stream()
                .filter(itemEntity -> itemEntity.getId().equals(id))
                .findFirst()
                .orElseThrow();

        return setWearableItem(user, (WearableEntity) item);
    }

    public Status setWearableItemById(UserEntity user, Integer id) {
        ItemEntity item = user.getAllItems()
                .stream()
                .filter(itemEntity -> itemEntity.getBasedId() == id)
                .sorted((Comparator<ItemEntity>) (o1, o2) -> o1.getId() == null ? -1 : 0)
                .findFirst()
                .orElseThrow();

        return setWearableItem(user, (WearableEntity) item);
    }

    public Status setWearableItemById(UUID id) {
        UserEntity user = userService.getUserById();
        Status status = setWearableItemById(user, id);
        if (status.isSuccess())
            userRepository.save(user);
        return status;
    }

    private <T extends ItemEntity> void addAsNotCountable(UserEntity user, T item) {
        var type = item.getBase().getType();
        if (type.isWearable()) {
            if (type.getTypeClass() != ArtifactEntity.class) {
                boolean userWears = user
                        .getItemsByType(type)
                        .stream()
                        .anyMatch(userItem -> ((WearableEntity) userItem).isEquipped());
                ((WearableEntity) item).setEquipped(!userWears);
                logger.info("User {} wears {} - ", user.getLogin(), item.getBase().getTitle(), !userWears);
            }
        }
        item.setQuantity(1);
        addAsIs(user, item);
    }

    private <T extends ItemEntity> void addAsCountable(UserEntity user, T itemEntity) {
        Optional<? extends ItemEntity> optionalItem = user.getItemsByType(itemEntity.getBasedType())
                .stream()
                .filter(item -> item.getBasedId() == itemEntity.getBasedId())
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
        switch (itemEntity.getBasedType()) {
            case BULLET -> user.getBullets().add((BulletEntity) itemEntity);
            case ARMOR -> user.getArmors().add((ArmorEntity) itemEntity);
            case PISTOL, RIFLE -> user.getWeapons().add((WeaponEntity) itemEntity);
            case ARTIFACT -> user.getArtifacts().add((ArtifactEntity) itemEntity);
            case DETECTOR -> user.getDetectors().add((DetectorEntity) itemEntity);
            case MEDICINE -> user.getMedicines().add((MedicineEntity) itemEntity);
        }
    }

}