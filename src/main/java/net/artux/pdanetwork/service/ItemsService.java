package net.artux.pdanetwork.service;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.repository.items.ArmorRepository;
import net.artux.pdanetwork.repository.items.ArtifactRepository;
import net.artux.pdanetwork.repository.items.BaseItemRepository;
import net.artux.pdanetwork.repository.items.DetectorRepository;
import net.artux.pdanetwork.repository.items.ItemRepository;
import net.artux.pdanetwork.repository.items.MedicineRepository;
import net.artux.pdanetwork.repository.items.WeaponRepository;
import net.artux.pdanetwork.service.files.ItemProvider;
import net.artux.pdanetwork.service.member.UserService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemsService {

    private final ItemProvider itemProvider;

    private final UserService userService;

    private final WeaponRepository weaponRepository;
    private final ArmorRepository armorRepository;
    private final ItemRepository itemRepository;
    private final MedicineRepository medicineRepository;
    private final ArtifactRepository artifactRepository;
    private final DetectorRepository detectorRepository;

    public boolean addItem(int type, int baseId, int quantity) {
        ItemEntity itemEntity = itemProvider.getItem(type, baseId);
        switch (itemEntity.getType()) {
            case ARMOR:
            case RIFLE:
            case PISTOL:
            case DETECTOR:
                addAsNotCountable(itemEntity, quantity);
                break;
            case ITEM:
            case MEDICINE:
            case ARTIFACT:
                itemEntity.setQuantity(quantity);
                addAsCountable(itemEntity);
                break;
            default:
                return false;
        }
        return true;
    }

    public <T extends ItemEntity> void deleteItem(int type, int baseId, int quantity) {
        BaseItemRepository<T> repository = getRepository(ItemType.getByTypeId(type));
        Optional<T> optional = repository.findByOwnerAndBaseId(userService.getMember(), baseId);
        if (optional.isPresent()) {
            T t = optional.get();
            t.setQuantity(t.getQuantity() - quantity);
            if (t.getQuantity() > 0)
                repository.save(t);
            else
                repository.delete(t);
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

    public <T extends ItemEntity> void addAsNotCountable(T item, int quantity) {
        BaseItemRepository<T> repository = getRepository(item.getType());
        item.setQuantity(1);
        item.setOwner(userService.getMember());
        List<T> items = new LinkedList<>();
        for (int i = 0; i < quantity; i++) {
            items.add(item);
        }
        repository.saveAll(items);
    }

    public <T extends ItemEntity> void addAsCountable(T itemEntity) {
        BaseItemRepository<T> repository = getRepository(itemEntity.getType());
        Optional<T> optionalItem = repository.findByOwnerAndBaseId(userService.getMember(), itemEntity.getBaseId());
        if (optionalItem.isPresent()) {
            T item = optionalItem.get();
            item.setQuantity(item.getQuantity() + itemEntity.getQuantity());
            repository.save(item);
        } else {
            itemEntity.setOwner(userService.getMember());
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

    public <T extends ItemEntity> List<T> getAllByUserAndType(UserEntity user, ItemType type) {
        return (List<T>) getRepository(type).findAllByOwnerAndType(user, type);
    }
}
