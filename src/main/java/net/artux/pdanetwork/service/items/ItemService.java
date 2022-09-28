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
import net.artux.pdanetwork.entity.items.WeaponEntity;
import net.artux.pdanetwork.entity.items.WearableEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.items.ItemDto;
import net.artux.pdanetwork.models.items.ItemMapper;
import net.artux.pdanetwork.repository.items.ArmorRepository;
import net.artux.pdanetwork.repository.items.ArtifactRepository;
import net.artux.pdanetwork.repository.items.CommonItemRepository;
import net.artux.pdanetwork.repository.items.DetectorRepository;
import net.artux.pdanetwork.repository.items.ItemRepository;
import net.artux.pdanetwork.repository.items.MedicineRepository;
import net.artux.pdanetwork.repository.items.WeaponRepository;
import net.artux.pdanetwork.repository.items.WearableItemRepository;
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

    private final WeaponRepository weaponRepository;
    private final ArmorRepository armorRepository;
    private final ItemRepository itemRepository;
    private final MedicineRepository medicineRepository;
    private final ArtifactRepository artifactRepository;
    private final DetectorRepository detectorRepository;


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
            addAsNotCountable(user, itemEntity, quantity);
        }
    }

    public void deleteItem(UserEntity user, int baseId, int quantity) {
        Optional<ItemEntity> optional = itemRepository.findByOwnerAndBaseId(user, baseId);
        if (optional.isPresent()) {
            ItemEntity t = optional.get();
            t.setQuantity(t.getQuantity() - quantity);
            if (t.getQuantity() > 0)
                itemRepository.save(t);
            else
                itemRepository.delete(t);
        }
    }

    public <T extends ItemEntity> void deleteItem(UserEntity user, ItemType type, UUID id, int quantity) {
        CommonItemRepository<T> repository = getRepository(type);
        Optional<T> optional = repository.findByOwnerAndId(user, id);
        if (optional.isPresent()) {
            T t = optional.get();
            if (type.isCountable()) {
                t.setQuantity(t.getQuantity() - quantity);
                if (t.getQuantity() > 0)
                    repository.save(t);
                else
                    repository.delete(t);
            } else {
                repository.delete(t);
            }
        }
    }

    public void resetAll(UserEntity owner) {
        armorRepository.deleteByOwner(owner);
        weaponRepository.deleteByOwner(owner);
        itemRepository.deleteByOwner(owner);
        medicineRepository.deleteByOwner(owner);
        detectorRepository.deleteByOwner(owner);
        artifactRepository.deleteByOwner(owner);
    }

    @SuppressWarnings("unchecked")
    public Status setWearable(ItemType type, UUID id) {
        UserEntity user = userService.getUserById();
        ItemEntity item = findByOwnerAndId(type, id).orElseThrow();
        if (item instanceof WearableEntity && type.isWearable()) {
            WearableItemRepository<WearableEntity> repository = (WearableItemRepository) getRepository(type);
            Optional<WearableEntity> optionalItem = repository.findByOwnerAndIsEquippedTrue(user);
            if (optionalItem.isPresent()) {
                WearableEntity oldWearable = optionalItem.get();

                oldWearable.setEquipped(false);
                save(oldWearable);
                if (item.getId().equals(oldWearable.getId()))
                    return new Status(true, "Предмет снят.");
            }

            ((WearableEntity) item).setEquipped(true);
            itemRepository.save(item);

            return new Status(true, "Предмет надет.");

        } else return new Status(true, "Невозможно надеть предмет");
    }

    private <T extends ItemEntity> void addAsNotCountable(UserEntity user, T item, int quantity) {
        CommonItemRepository<T> repository = getRepository(item.getBase().getType());
        item.setQuantity(1);
        item.setOwner(user);
        List<T> items = new LinkedList<>();
        for (int i = 0; i < quantity; i++) {
            items.add(item);
        }
        repository.saveAll(items);
        //todo set wearable for weapons and armors if equipment is empty
    }

    private <T extends ItemEntity> void addAsCountable(UserEntity user, T itemEntity) {
        CommonItemRepository<T> repository = getRepository(itemEntity.getBase().getType());
        Optional<T> optionalItem = repository.findByOwnerAndBaseId(userService.getUserById(), itemEntity.getBase().getId());
        if (optionalItem.isPresent()) {
            T item = optionalItem.get();
            item.setQuantity(item.getQuantity() + itemEntity.getQuantity());
            repository.save(item);
        } else {
            itemEntity.setOwner(user);
            repository.save(itemEntity);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends ItemEntity> CommonItemRepository<T> getRepository(ItemType type) {
        return switch (type) {
            case ARMOR -> (CommonItemRepository<T>) armorRepository;
            case PISTOL, RIFLE -> (CommonItemRepository<T>) weaponRepository;
            case ARTIFACT -> (CommonItemRepository<T>) artifactRepository;
            case MEDICINE -> (CommonItemRepository<T>) medicineRepository;
            case DETECTOR -> (CommonItemRepository<T>) detectorRepository;
            default -> (CommonItemRepository<T>) itemRepository;
        };
    }

    public <T extends ItemEntity> Optional<T> findByOwnerAndId(ItemType type, UUID id) {
        return (Optional<T>) getRepository(type).findByOwnerAndId(userService.getUserById(), id);
    }

    public <T extends ItemEntity> T save(T t) {
        return getRepository(t.getBase().getType()).save(t);
    }

    public <T extends ItemEntity> void delete(T t) {
        getRepository(t.getBase().getType()).delete(t);
    }

}