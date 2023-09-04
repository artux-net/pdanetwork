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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
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
    private final Logger logger = LoggerFactory.getLogger(SellerService.class);

    private List<SellerAdminDto> initialSellers;

    @PostConstruct
    void init() throws IOException {
        initialSellers = readSellers();
        logger.info("Прочитан список продавцов, продавцы:");
        for (SellerAdminDto seller : initialSellers)
            logger.info("{}, покупка: {}, продажа: {}", seller.getName(), seller.getBuyCoefficient(), seller.getSellCoefficient());

        for (SellerAdminDto seller : initialSellers)
            if (!sellerRepository.existsById(seller.getId()))
                sellerRepository.save(sellerMapper.entity(seller));
    }

    private List<SellerAdminDto> readSellers() throws IOException {
        Resource resource = new ClassPathResource("static/base/sellers/info.json");
        logger.info("Чтение списка продавцов");
        SellerAdminDto[] arr = mapper.readValue(resource.getInputStream(), SellerAdminDto[].class);
        return Arrays.stream(arr)
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 5 * * * *")
    @Transactional
    public void fixSellersItems() {
        List<SellerEntity> sellerEntities = sellerRepository.findAll();
        logger.info("Обновление ассортимента продавцов", sellerEntities.size());
        sellerEntities.forEach(seller -> {
            Optional<SellerEntity> optional = initialSellers.stream()
                    .filter(initialSeller -> initialSeller.getId() == seller.getId())
                    .map(sellerMapper::entity)
                    .findFirst();
            if (optional.isEmpty())
                return;

            SellerEntity initialSeller = optional.get();
            initialSeller.getAllItems().forEach(initialItem -> {
                long basedId = initialItem.getBasedId();
                ItemEntity sellerItem = seller.findItem(basedId);
                if (sellerItem == null) {
                    seller.addItem(initialItem);
                    logger.info("{}: отсутствует предмет {} ({}), добавление.",
                            seller.getName(), initialItem.getBase().getTitle(), basedId);
                } else {
                    int basedQuantity = initialItem.getQuantity();
                    if (sellerItem.getQuantity() < basedQuantity) {
                        sellerItem.setQuantity(basedQuantity);
                        logger.info("{}: обновление количества {} ({})",
                                seller.getName(), initialItem.getBase().getTitle(), basedId);
                    }
                }
            });
        });
        sellerRepository.saveAll(sellerEntities);

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
                sellerRepository.save(sellerEntity);
                if (optionalUserItem.isEmpty() || !type.isCountable()) {
                    sellerItem.setOwner(userEntity);
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
        UserEntity userEntity = userService.getUserById();
        SellerEntity sellerEntity = new SellerEntity();
        sellerEntity.setId(dto.getId());
        sellerEntity.setName(dto.getName());
        sellerEntity.setIcon(dto.getName());
        sellerEntity.setImage(dto.getImage());
        sellerEntity.setBuyCoefficient(dto.getBuyCoefficient());
        sellerEntity.setSellCoefficient(dto.getSellCoefficient());
        sellerEntity = sellerRepository.save(sellerEntity);
        initialSellers.add(sellerMapper.adminDto(sellerEntity));
        logger.info("Создание продавца {} ({}) пользователем {}", sellerEntity.getName(), sellerEntity.getId(), userEntity.getLogin());

        return sellerMapper.dto(sellerEntity);
    }

    @ModeratorAccess
    public SellerDto updateSeller(Long id, SellerAdminDto dto) {
        dto.setId(id);
        initialSellers.removeIf(s -> s.getId() == id);
        initialSellers.add(dto);

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
                /*
                TODO надо чтобы там всегда 1 была, другие сервисы ожидают что там 1,
                соответственно надо навый объект делать, а там свои проблемы..
                 */
                ItemEntity stackTarget = null;
                if (item.getBase().getType().isCountable()) {
                    List<ItemEntity> sellerItems = sellerEntity.getAllItems();
                    Optional<ItemEntity> sellerItem = sellerItems
                            .stream()
                            .filter(itemEntity -> itemEntity.getBasedId() == id)
                            .findAny();
                    if (sellerItem.isPresent()) {
                        stackTarget = sellerItem.get();
                    }
                }

                if (stackTarget != null) {
                    stackTarget.setQuantity(stackTarget.getQuantity() + quantity);
                } else {
                    sellerEntity.addItem(itemRepository.save(item));
                }

                item.setQuantity(1); //TODO самый настоящий костыль
            } catch (Exception ignored) {
            }
        }

        return sellerMapper.dto(sellerRepository.save(sellerEntity));
    }

    @ModeratorAccess
    @Transactional
    public SellerDto deleteSellerItems(Long sellerId, List<UUID> ids) {
        List<UUID> finalIds = new LinkedList<>(ids);

        SellerEntity sellerEntity = sellerRepository.findById(sellerId).orElseThrow();
        List<UUID> sellerItemIds = new LinkedList<>();
        sellerEntity.getAllItems()
                .stream()
                .filter(entity -> finalIds.contains(entity.getId()))
                .forEach(item -> {
                    sellerItemIds.add(item.getId());
                    sellerEntity.removeItem(item);
                });

        finalIds.removeIf(id -> !sellerItemIds.contains(id));
        SellerDto dto = sellerMapper.dto(sellerRepository.save(sellerEntity));
        itemRepository.deleteAllById(finalIds);

        return dto;
    }

    @ModeratorAccess
    public void deleteSeller(Long id) {
        sellerRepository.deleteById(id);
    }

}
