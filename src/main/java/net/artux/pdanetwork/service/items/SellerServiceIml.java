package net.artux.pdanetwork.service.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.ArmorEntity;
import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.entity.items.WeaponEntity;
import net.artux.pdanetwork.entity.seller.SellerEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.seller.Seller;
import net.artux.pdanetwork.models.seller.SellerDto;
import net.artux.pdanetwork.models.seller.SellerMapper;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.repository.items.SellerRepository;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SellerServiceIml implements SellerService {

    private final SellerRepository sellerRepository;
    private final CommonItemsRepository itemRepository;
    private final UserRepository userRepository;

    private final SellerMapper sellerMapper;
    private final UserService userService;
    private final ObjectMapper mapper;

    @PostConstruct
    void init() throws IOException {
        List<SellerEntity> sellers = readSellers();
        for (SellerEntity seller : sellers){
            if (!sellerRepository.existsById(seller.getId()))
                sellerRepository.save(seller);
        }
    }

    private List<SellerEntity> readSellers() throws IOException {
        Resource resource = new ClassPathResource("static/base/sellers/info.json");

        Seller[] arr = mapper.readValue(resource.getFile(), Seller[].class);
        return Arrays.stream(arr)
                .map(sellerMapper::entity)
                .collect(Collectors.toList());
    }

    @Override
    public SellerDto getSeller(long id) {
        return sellerRepository.findById(id).map(sellerMapper::dto).orElseThrow();
    }

    @Override
    public Status add(Long pdaId, int baseId, int quantity) {
        if (userService.getMember().getRole() == Role.ADMIN) {
            UserEntity userEntity = userService.getMemberByPdaId(pdaId);
            itemRepository.addItem(userEntity, baseId, quantity);
            return new Status(true, "Ok");
        } else return new Status(false, "Not admin");
    }

    @Override
    public Status buy(long sellerId, ItemType itemType, long id, int quantity) {
        UserEntity userEntity = userService.getMember();
        SellerEntity sellerEntity = sellerRepository.findById(sellerId).orElseThrow();
        //todo check if seller does not have item
        ItemEntity item = itemRepository.findById(itemType, id).orElseThrow();

        if (!itemType.isCountable())
            quantity = 1;
        if (quantity > 0 && userEntity.buy(getPrice(item, sellerEntity.getBuyCoefficient(), quantity))) {
            if (quantity == item.getQuantity()) {
                sellerEntity.removeItem(item);
                sellerRepository.save(sellerEntity);
            } else if (quantity < item.getQuantity()) {
                item.setQuantity(item.getQuantity() - quantity);
                item = itemRepository.save(item);
                item.setId(null);
                item.setQuantity(quantity);
            } else
                return new Status(false, "У проодавца столько нет.");
            item.setOwner(userEntity);
            itemRepository.save(item);
            userRepository.save(userEntity);

            return new Status(true, "Ok.");
        } else
            return new Status(true, "Недостаточно средств.");
    }

    @Override
    public Status sell(long sellerId, ItemType itemType, long id, int quantity) {
        UserEntity userEntity = userService.getMember();
        SellerEntity sellerEntity = sellerRepository.findById(sellerId).orElseThrow();
        ItemEntity item = itemRepository.findById(itemType, id).orElseThrow();

        if (!itemType.isCountable())
            quantity = 1;

        if (item instanceof ArmorEntity && ((ArmorEntity) item).getCondition() < 70
                || item instanceof WeaponEntity && ((WeaponEntity) item).getCondition() < 70)
            itemRepository.delete(item);
        else {
            if (quantity > item.getQuantity() && quantity < 0)
                return new Status(false, "У вас столько нет");
            if (item.getQuantity() > quantity) {
                item.setQuantity(item.getQuantity() - quantity);
                item = itemRepository.save(item);
                item.setId(null);
            }

            item.setOwner(null);
            item = itemRepository.save(item);
            sellerEntity.addItem(item);
            sellerRepository.save(sellerEntity);
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
    public Status set(ItemType itemType, long id) {
        return itemRepository.setWearable(itemType, id);
    }

}
