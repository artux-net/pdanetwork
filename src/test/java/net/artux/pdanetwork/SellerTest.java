package net.artux.pdanetwork;

import net.artux.pdanetwork.configuration.ServiceTestConfiguration;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.items.ItemDto;
import net.artux.pdanetwork.models.items.WeaponDto;
import net.artux.pdanetwork.repository.items.ItemRepository;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.service.items.SellerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Order(10)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ServiceTestConfiguration.class)
public class SellerTest {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private ActionService actionService;

    @Autowired
    private ItemRepository itemRepository;

    private final long sellerId = 1;

    @Test
    @Order(0)
    public void getSeller(){
        Assertions.assertNotNull(sellerService.getSeller(sellerId));
    }

    @Test
    @Order(1)
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
    public void addCountableItemsToSeller(){
        sellerService.addSellerItems(sellerId, List.of("29:50", "29:50"));
        ItemDto itemDto = sellerService.getSeller(sellerId).getBullets()
                .stream()
                .filter(dto -> dto.getBaseId() == 29)
                .findFirst().get();
        Assertions.assertEquals(300, itemDto.getQuantity()); // изначально там 200 патрон
    }

    @Test
    @Order(8)
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
    public void addUncountableItemsToSeller(){
        //TODO: unique type chests for uncountable items
    }

    @Test
    @Order(9)
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
    public void testBuy(){
        actionService.applyCommands(Collections.singletonMap("money", List.of("10000")));
        actionService.applyCommands(Collections.emptyMap()).getWeapons().forEach(System.out::println);

        WeaponDto weaponDto = sellerService.getSeller(sellerId).getWeapons().stream().findFirst().get();
        Status status = sellerService.buy(sellerId, weaponDto.getId(), 1);

        actionService.applyCommands(Collections.emptyMap()).getWeapons().forEach(System.out::println);

        Assertions.assertTrue(status.isSuccess());
    }

    @Test
    @Order(10)
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
    void testDeleteSellerItems() {
        sellerService.fixSellersItems();

        WeaponDto weaponDto = sellerService.getSeller(sellerId).getWeapons().stream().filter(itemDto1 -> itemDto1.getBaseId() == 10).findFirst().get();
        ItemDto itemDto = sellerService.getSeller(sellerId).getBullets().stream().filter(itemDto1 -> itemDto1.getBaseId() == 29).findFirst().get();

        List<UUID> itemIds = List.of(itemDto.getId(), weaponDto.getId());
        System.out.println(weaponDto.getBaseId());
        System.out.println(itemDto.getBaseId());
        sellerService.deleteSellerItems(sellerId, itemIds);

        Assertions.assertTrue(sellerService.getSeller(sellerId).getWeapons()
                .stream()
                .filter(item -> item.getId().equals(weaponDto.getId()))
                .findFirst()
                .isEmpty());

        Assertions.assertTrue(sellerService.getSeller(sellerId).getBullets()
                .stream()
                .filter(item -> item.getId().equals(itemDto.getId()))
                .findFirst()
                .isEmpty());

        for (UUID id : itemIds)
            Assertions.assertTrue(itemRepository.findById(id).isEmpty());
    }

    @Test
    @Order(11)
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
    public void testFixSellersItems() {
        sellerService.fixSellersItems();
        Assertions.assertTrue(sellerService.getSeller(sellerId).getBullets().stream().anyMatch(item -> item.getBaseId() == 29));
        Assertions.assertTrue(sellerService.getSeller(sellerId).getWeapons().stream().anyMatch(item -> item.getBaseId() == 10));
        ItemDto dto = sellerService.getSeller(sellerId).getBullets().stream().filter(item -> item.getBaseId() == 29).findFirst().get();
        sellerService.buy(sellerId, dto.getId(), 180);
        sellerService.fixSellersItems();
        dto = sellerService.getSeller(sellerId).getBullets().stream().filter(item -> item.getBaseId() == 29).findFirst().get();
        Assertions.assertEquals(200, dto.getQuantity());
    }

}