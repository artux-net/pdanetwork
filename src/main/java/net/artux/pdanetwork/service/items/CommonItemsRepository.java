package net.artux.pdanetwork.service.items;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.entity.items.WearableEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.repository.items.*;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommonItemsRepository {

    private final ItemService itemService;
    private final UserService userService;

    private final WeaponRepository weaponRepository;
    private final ArmorRepository armorRepository;
    private final ItemRepository itemRepository;
    private final MedicineRepository medicineRepository;
    private final ArtifactRepository artifactRepository;
    private final DetectorRepository detectorRepository;

    public boolean addItem(UserEntity user, long baseId, int quantity) {
        ItemEntity itemEntity = itemService.getItem(baseId);
        ItemType type = itemEntity.getBase().getType();
        if (type.isCountable()) {
            itemEntity.setQuantity(quantity);
            addAsCountable(user, itemEntity);
        } else {
            addAsNotCountable(user, itemEntity, quantity);
        }
        return true;
    }

    public <T extends ItemEntity> void deleteItem(UserEntity user, int type, int baseId, int quantity) {
        CommonItemRepository<T> repository = getRepository(ItemType.getByTypeId(type));
        Optional<T> optional = repository.findByOwnerAndBaseId(user, baseId);
        if (optional.isPresent()) {
            T t = optional.get();
            t.setQuantity(t.getQuantity() - quantity);
            if (t.getQuantity() > 0)
                repository.save(t);
            else
                repository.delete(t);
        }
    }

    public <T extends ItemEntity> void deleteItem(UserEntity user, ItemType type, long id, int quantity) {
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

    public Status setWearable(ItemType type, long id) {
        UserEntity user = userService.getMember();
        ItemEntity item = findById(type, id).orElseThrow();
        if (item instanceof WearableEntity) {
            WearableItemRepository repository = (WearableItemRepository) getRepository(type);
            Optional<WearableEntity> optionalItem = repository.findByOwnerAndIsEquippedTrue(user);
            if (optionalItem.isPresent()) {
                WearableEntity oldWearable = optionalItem.get();
                oldWearable.setEquipped(false);
                save(oldWearable);
            }
            ((WearableEntity) item).setEquipped(true);
            save(item);
            return new Status(false, "Предмет надет.");
        } else return new Status(false, "Невозможно надеть предмет");
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
        Optional<T> optionalItem = repository.findByOwnerAndBaseId(userService.getMember(), itemEntity.getBase().getId());
        if (optionalItem.isPresent()) {
            T item = optionalItem.get();
            item.setQuantity(item.getQuantity() + itemEntity.getQuantity());
            repository.save(item);
        } else {
            itemEntity.setOwner(user);
            repository.save(itemEntity);
        }
    }

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

    public <T extends ItemEntity> List<T> findAllByUserAndType(UserEntity user, ItemType type) {
        return (List<T>) getRepository(type).findAllByOwner(user);
    }

    public <T extends ItemEntity> Optional<T> findById(ItemType type, long id) {
        return (Optional<T>) getRepository(type).findByOwnerAndId(userService.getMember(), id);
    }

    public <T extends ItemEntity> T save(T t) {
        return getRepository(t.getBase().getType()).save(t);
    }

    public <T extends ItemEntity> void delete(T t) {
        getRepository(t.getBase().getType()).delete(t);
    }

}
