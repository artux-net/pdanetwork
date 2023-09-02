package net.artux.pdanetwork.service.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
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
import net.artux.pdanetwork.models.seller.SellerAdminDto;
import net.artux.pdanetwork.models.seller.SellerDto;
import net.artux.pdanetwork.models.seller.SellerMapper;
import net.artux.pdanetwork.models.story.StoryMapper;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.repository.items.ItemRepository;
import net.artux.pdanetwork.repository.items.SellerRepository;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.utills.security.ModeratorAccess;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class SellerServiceIml implements SellerService {

    private final SellerRepository sellerRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final UserRepository userRepository;

    private final StoryMapper storyMapper;
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
    @Transactional(readOnly = true)
    public SellerDto getSeller(long id) {
        return sellerRepository.findById(id).map(sellerMapper::dto).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SellerAdminDto> getSellers() {
        return sellerRepository.findAll().stream().map(sellerMapper::adminDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Status buy(long sellerId, UUID id, int quantity) {
        UserEntity userEntity = userService.getUserById();
        SellerEntity sellerEntity = sellerRepository.findById(sellerId).orElseThrow();
        //todo check if seller does not have item
        ItemEntity sellerItem = itemRepository.findById(id).orElseThrow();
        long baseId = sellerItem.getBasedId();
        ItemType type = sellerItem.getBasedType();

        Optional<? extends ItemEntity> optionalUserItem = userEntity.findItem(baseId);

        if (!sellerItem.getBase().getType().isCountable())
            quantity = 1;
        if (quantity > 0 && userEntity.canBuy(getPrice(sellerItem, sellerEntity.getBuyCoefficient(), quantity))) {
            if (quantity == sellerItem.getQuantity()) {
                sellerEntity.removeItem(sellerItem);
                if (optionalUserItem.isEmpty() || !type.isCountable()) {
                    sellerItem.setOwner(userEntity);
                    sellerRepository.save(sellerEntity);
                    itemRepository.save(sellerItem);
                } else {
                    ItemEntity userItem = optionalUserItem.get();
                    userItem.setQuantity(userItem.getQuantity() + quantity);
                    itemRepository.deleteById(sellerItem.getId());
                }
            } else if (quantity < sellerItem.getQuantity()) {
                sellerItem.setQuantity(sellerItem.getQuantity() - quantity);
                itemRepository.save(sellerItem);

                ItemEntity separatedItem = itemService.getItem(baseId);

                if (optionalUserItem.isEmpty()) {
                    //user does not have an item
                    separatedItem.setOwner(userEntity);
                    separatedItem.setQuantity(quantity);
                    itemRepository.save(separatedItem);
                } else {
                    //user has an item
                    ItemEntity userItem = optionalUserItem.get();
                    userItem.setQuantity(userItem.getQuantity() + quantity);
                    itemRepository.save(userItem);
                }
            } else
                return new Status(false, "У продавца столько нет.");

            StoryData storyData = storyMapper.storyData(userRepository.save(userEntity));

            return new Status(true, "Ok.", storyData);
        } else
            return new Status(false, "Недостаточно средств.");
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

        StoryData storyData = storyMapper.storyData(userRepository.save(userEntity));
        return new Status(true, "Ok.", storyData);
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

    @ModeratorAccess
    public SellerDto createSeller(SellerAdminDto dto) {
        SellerEntity sellerEntity = new SellerEntity();
        sellerEntity.setId(dto.getId());
        sellerEntity.setName(dto.getName());
        sellerEntity.setIcon(dto.getName());
        sellerEntity.setImage(dto.getImage());
        sellerEntity.setBuyCoefficient(dto.getBuyCoefficient());
        sellerEntity.setSellCoefficient(dto.getSellCoefficient());

        return sellerMapper.dto(sellerRepository.save(sellerEntity));
    }

    @ModeratorAccess
    public SellerDto updateSeller(Long id, SellerAdminDto dto) {
        SellerEntity sellerEntity = sellerRepository.findById(id).orElseThrow();
        sellerEntity.setName(dto.getName());
        sellerEntity.setIcon(dto.getIcon());
        sellerEntity.setImage(dto.getImage());
        sellerEntity.setBuyCoefficient(dto.getBuyCoefficient());
        sellerEntity.setSellCoefficient(dto.getSellCoefficient());

        return sellerMapper.dto(sellerRepository.save(sellerEntity));
    }

    @ModeratorAccess
    public SellerDto addSellerItems(Long sellerId, List<String> s) {
        SellerEntity sellerEntity = sellerRepository.findById(sellerId).orElseThrow();
        for (String couple : s) {
            if (!couple.contains(":"))
                continue;

            String[] parts = couple.split(":");
            try {
                int id = Integer.parseInt(parts[0]);
                int quantity = Integer.parseInt(parts[1]);

                ItemEntity item = itemService.getItem(id);
                item.setQuantity(quantity);
                sellerEntity.addItem(itemRepository.save(item));
            } catch (Exception ignored) {
            }
        }

        return sellerMapper.dto(sellerRepository.save(sellerEntity));
    }

    @ModeratorAccess
    public SellerDto deleteSellerItems(Long sellerId, List<UUID> ids) {
        SellerEntity sellerEntity = sellerRepository.findById(sellerId).orElseThrow();
        List<UUID> sellerItemIds = sellerEntity.getAllItems()
                .stream()
                .map(ItemEntity::getId)
                .collect(Collectors.toList());
        ids.removeIf(id -> !sellerItemIds.contains(id));

        for (UUID itemId : ids) {
            itemRepository.deleteById(itemId);
        }
        return sellerMapper.dto(sellerEntity);
    }

    @ModeratorAccess
    public void deleteSeller(Long id) {
        sellerRepository.deleteById(id);
    }

}
