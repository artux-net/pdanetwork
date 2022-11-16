package net.artux.pdanetwork.service.items;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.ArmorEntity;
import net.artux.pdanetwork.entity.items.ArtifactEntity;
import net.artux.pdanetwork.entity.items.BulletEntity;
import net.artux.pdanetwork.entity.items.DetectorEntity;
import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.entity.items.MedicineEntity;
import net.artux.pdanetwork.entity.items.UsualItemEntity;
import net.artux.pdanetwork.entity.items.WeaponEntity;
import net.artux.pdanetwork.entity.items.WearableEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.items.ItemDto;
import net.artux.pdanetwork.models.items.ItemMapper;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.user.UserService;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private HashMap<Long, ItemEntity> items = new HashMap<>();

    private final Logger logger;
    private final ObjectMapper objectMapper;
    private final ItemMapper itemMapper;
    private final BaseItemService baseItemService;

    private final UserService userService;
    private final UserRepository userRepository;


    @PostConstruct
    public void init() throws IOException {
        items = new HashMap<>();
        for (ItemType type : ItemType.values()) {
            for (ItemEntity item : readType(type))
                items.put(item.getBase().getId(), item);
        }
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
        return items.get(baseId);
    }

    public ItemDto getItemDto(long baseId) {
        return any(items.get(baseId));
    }

    private ItemDto any(ItemEntity entity1) {
        return switch (entity1.getBase().getType()) {
            case ARMOR -> itemMapper.armor((ArmorEntity) entity1);
            case PISTOL, RIFLE -> itemMapper.weapon((WeaponEntity) entity1);
            case MEDICINE -> itemMapper.medicine((MedicineEntity) entity1);
            case ARTIFACT -> itemMapper.artifact((ArtifactEntity) entity1);
            case DETECTOR -> itemMapper.detector((DetectorEntity) entity1);
            case BULLET -> itemMapper.item((BulletEntity) entity1);
            case ITEM -> itemMapper.item((UsualItemEntity) entity1);
        };
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

    public Status setWearable(ItemType type, UUID id) {
        UserEntity user = userService.getUserById();
        ItemEntity item = user.getAllItems()
                .stream()
                .filter(itemEntity -> itemEntity.getId().equals(id))
                .findFirst()
                .orElseThrow();

        if (type.isWearable()) {
            Optional<WearableEntity> optionalItem = user.getWearableItems()
                    .stream()
                    .filter(wearableEntity -> wearableEntity.getId().equals(id) && wearableEntity.isEquipped())
                    .findFirst();

            if (optionalItem.isPresent()) {
                WearableEntity oldWearable = optionalItem.get();

                oldWearable.setEquipped(false);
                userRepository.save(user);
                if (item.getId().equals(oldWearable.getId()))
                    return new Status(true, "Предмет снят.");
            }

            ((WearableEntity) item).setEquipped(true);
            userRepository.save(user);

            return new Status(true, "Предмет надет.");

        } else return new Status(true, "Невозможно надеть предмет");
    }

    private <T extends ItemEntity> void addAsNotCountable(UserEntity user, T item) {
        var type = item.getBase().getType();
        if (type.isWearable()) {
            boolean wear = user.getWearableItems()
                    .stream()
                    .filter(userItem -> userItem.isEquipped() && userItem.getBase().getType().equals(type))
                    .toList()
                    .size() > 0;
            if (!wear)
                ((WearableEntity) item).setEquipped(true);
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