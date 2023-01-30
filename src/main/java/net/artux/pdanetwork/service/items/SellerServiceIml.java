package net.artux.pdanetwork.service.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.ArmorEntity;
import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.entity.items.WeaponEntity;
import net.artux.pdanetwork.entity.items.WearableEntity;
import net.artux.pdanetwork.entity.seller.SellerEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.seller.Seller;
import net.artux.pdanetwork.models.seller.SellerDto;
import net.artux.pdanetwork.models.seller.SellerMapper;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.repository.items.ItemRepository;
import net.artux.pdanetwork.repository.items.SellerRepository;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SellerServiceIml implements SellerService {

    private final SellerRepository sellerRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final UserRepository userRepository;

    private final SellerMapper sellerMapper;
    private final UserService userService;
    private final ObjectMapper mapper;

    @PostConstruct
    void init() throws IOException {
        List<SellerEntity> sellers = readSellers();
        for (SellerEntity seller : sellers) {
            if (!sellerRepository.existsById(seller.getId()))
                sellerRepository.save(seller);
        }
    }

    private List<SellerEntity> readSellers() throws IOException {
        Resource resource = new ClassPathResource("static/base/sellers/info.json");

        Seller[] arr = mapper.readValue(resource.getInputStream(), Seller[].class);
        return Arrays.stream(arr)
                .map(sellerMapper::entity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SellerDto getSeller(long id) {
        return sellerRepository.findById(id).map(sellerMapper::dto).orElseThrow();
    }

    @Override
    @Transactional
    public Status add(UUID pdaId, int baseId, int quantity) {
        if (userService.getUserById().getRole() == Role.ADMIN) {
            UserEntity userEntity = userRepository.getById(pdaId);
            itemService.addItem(userEntity, baseId, quantity);
            return new Status(true, "Ok");
        } else return new Status(false, "Not admin");
    }

    @Override
    @Transactional
    public Status buy(long sellerId, UUID id, int quantity) {
        UserEntity userEntity = userService.getUserById();
        SellerEntity sellerEntity = sellerRepository.findById(sellerId).orElseThrow();
        //todo check if seller does not have item
        ItemEntity sellerItem = itemRepository.findById(id).orElseThrow();
        long baseId = sellerItem.getBase().getId();
        ItemEntity userItem = userEntity.findItem(baseId);

        if (!sellerItem.getBase().getType().isCountable())
            quantity = 1;
        if (quantity > 0 && userEntity.canBuy(getPrice(sellerItem, sellerEntity.getBuyCoefficient(), quantity))) {
            if (quantity == sellerItem.getQuantity()) {
                sellerEntity.removeItem(sellerItem);
                if (userItem == null) {
                    sellerItem.setOwner(userEntity);
                    sellerRepository.save(sellerEntity);
                    itemRepository.save(sellerItem);
                } else {
                    userItem.setQuantity(userItem.getQuantity() + quantity);
                    sellerRepository.save(sellerEntity);
                    itemRepository.deleteById(sellerItem.getId());
                }
            } else if (quantity < sellerItem.getQuantity()) {
                sellerItem.setQuantity(sellerItem.getQuantity() - quantity);
                sellerItem = itemRepository.save(sellerItem);

                ItemEntity separatedItem = itemService.getItem(baseId);

                if (userItem == null) {
                    //user does not have an item
                    separatedItem.setOwner(userEntity);
                    separatedItem.setQuantity(quantity);
                    itemRepository.save(separatedItem);
                } else {
                    //user has an item
                    userItem.setQuantity(userItem.getQuantity() + quantity);
                    itemRepository.save(userItem);
                }
            } else
                return new Status(false, "У продавца столько нет.");

            itemRepository.save(sellerItem);
            userRepository.save(userEntity);

            return new Status(true, "Ok.");
        } else
            return new Status(true, "Недостаточно средств.");
    }

    @Override
    @Transactional
    public Status sell(long sellerId, UUID id, int quantity) {
        UserEntity userEntity = userService.getUserById();
        SellerEntity sellerEntity = sellerRepository.findById(sellerId).orElseThrow();
        ItemEntity item = itemRepository.findById(id).orElseThrow();

        if (!item.getBase().getType().isCountable())
            quantity = 1;
        if (item instanceof WearableEntity wearable)
            wearable.setEquipped(false);

        if (item instanceof ArmorEntity && ((ArmorEntity) item).getCondition() < 70
                || item instanceof WeaponEntity && ((WeaponEntity) item).getCondition() < 70)
            itemRepository.delete(item);
        else {
            if (quantity > item.getQuantity() && quantity < 0)
                return new Status(false, "У вас столько нет");
            if (item.getQuantity() > quantity) {
                item.setQuantity(item.getQuantity() - quantity);
                item = itemRepository.save(item);

                long baseId = item.getBase().getId();
                ItemEntity sellerItem = sellerEntity.findItem(baseId);
                if (sellerItem != null)
                    sellerItem.setQuantity(sellerItem.getQuantity() + quantity);
                else {
                    // new item for seller
                    sellerItem = itemService.getItem(baseId);
                    sellerItem.setQuantity(quantity);
                    sellerEntity.addItem(sellerItem);
                }
                sellerRepository.save(sellerEntity);
            } else {
                // if quantities equals
                item.setOwner(null);
                item = itemRepository.save(item);
                sellerEntity.addItem(item);
                sellerRepository.save(sellerEntity);
            }
        }
        userEntity.setMoney(userEntity.getMoney() + getPrice(item, sellerEntity.getSellCoefficient(), quantity));
        userRepository.save(userEntity);
        return new Status(true, "Ok.");
    }

    private int getPrice(ItemEntity item, float sellerCoefficient, int quantity) {
        float additionalCoefficient = 1;
        if (item instanceof WeaponEntity) {
            additionalCoefficient = ((WeaponEntity) item).getCondition() / 100f;
        }
        if (item instanceof ArmorEntity) {
            additionalCoefficient = ((ArmorEntity) item).getCondition() / 100f;
        }
        return (int) (item.getBase().getPrice() * additionalCoefficient * sellerCoefficient) * quantity;
    }

    @Override
    @Transactional
    public Status set(ItemType itemType, UUID id) {
        return itemService.setWearable(itemType, id);//todo remove from here
    }

}
