package net.artux.pdanetwork.service;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.repository.items.*;
import net.artux.pdanetwork.service.files.ItemProvider;
import net.artux.pdanetwork.service.member.UserService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommonItemsRepository {

    private final ItemProvider itemProvider;

    private final UserService userService;

    private final WeaponRepository weaponRepository;
    private final ArmorRepository armorRepository;
    private final ItemRepository itemRepository;
    private final MedicineRepository medicineRepository;
    private final ArtifactRepository artifactRepository;
    private final DetectorRepository detectorRepository;

    private boolean isCountable(ItemType type) {
        switch (type) {
            case ITEM:
            case MEDICINE:
            case ARTIFACT:
                return true;
            default:
                return false;
        }
    }

    public boolean addItem(UserEntity user, int type, int baseId, int quantity) {
        ItemEntity itemEntity = itemProvider.getItem(type, baseId);
        if (isCountable(itemEntity.getType())) {
            itemEntity.setQuantity(quantity);
            addAsCountable(user, itemEntity);
        } else {
            addAsNotCountable(user, itemEntity, quantity);
        }
        return true;
    }

    public <T extends ItemEntity> void deleteItem(UserEntity user, int type, int baseId, int quantity) {
        BaseItemRepository<T> repository = getRepository(ItemType.getByTypeId(type));
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
        BaseItemRepository<T> repository = getRepository(type);
        Optional<T> optional = repository.findByOwnerAndId(user, id);
        if (optional.isPresent()) {
            T t = optional.get();
            if (isCountable(type)) {
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

    private <T extends ItemEntity> void addAsNotCountable(UserEntity user, T item, int quantity) {
        BaseItemRepository<T> repository = getRepository(item.getType());
        item.setQuantity(1);
        item.setOwner(user);
        List<T> items = new LinkedList<>();
        for (int i = 0; i < quantity; i++) {
            items.add(item);
        }
        repository.saveAll(items);
    }

    private <T extends ItemEntity> void addAsCountable(UserEntity user, T itemEntity) {
        BaseItemRepository<T> repository = getRepository(itemEntity.getType());
        Optional<T> optionalItem = repository.findByOwnerAndBaseId(userService.getMember(), itemEntity.getBaseId());
        if (optionalItem.isPresent()) {
            T item = optionalItem.get();
            item.setQuantity(item.getQuantity() + itemEntity.getQuantity());
            repository.save(item);
        } else {
            itemEntity.setOwner(user);
            repository.save(itemEntity);
        }
    }

    private <T extends ItemEntity> BaseItemRepository<T> getRepository(ItemType type) {
        switch (type) {
            case ARMOR:
                return (BaseItemRepository<T>) armorRepository;
            case PISTOL:
            case RIFLE:
                return (BaseItemRepository<T>) weaponRepository;
            case ARTIFACT:
                return (BaseItemRepository<T>) artifactRepository;
            case MEDICINE:
                return (BaseItemRepository<T>) medicineRepository;
            case DETECTOR:
                return (BaseItemRepository<T>) detectorRepository;
            default:
                return (BaseItemRepository<T>) itemRepository;
        }
    }

    public <T extends ItemEntity> List<T> findAllByUserAndType(UserEntity user, ItemType type) {
        return (List<T>) getRepository(type).findAllByOwnerAndType(user, type);
    }
}
