package net.artux.pdanetwork;

import net.artux.pdanetwork.configuration.ServiceTestConfiguration;
import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.command.ServerCommand;
import net.artux.pdanetwork.models.items.ItemDto;
import net.artux.pdanetwork.models.items.WeaponDto;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.service.items.SellerService;
import net.artux.pdanetwork.service.user.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ServiceTestConfiguration.class)
public class SellerTest {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private ActionService actionService;

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
    public void addItemsToSeller(){
        long sellerId = 1;
        sellerService.addSellerItems(sellerId, List.of("29:50", "29:50"));
        ItemDto itemDto = sellerService.getSeller(sellerId).getBullets()
                .stream()
                .filter(dto -> dto.getBaseId() == 29)
                .findFirst().get();
        Assertions.assertEquals(100, itemDto.getQuantity());
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
    public void testBuy(){
        actionService.applyCommands(Collections.singletonMap("money", List.of("10000")));
        actionService.applyCommands(Collections.emptyMap()).getWeapons().forEach(weapon -> {
            System.out.println(weapon);
        });
        long sellerId = 1;
        WeaponDto weaponDto = sellerService.getSeller(sellerId).getWeapons().stream().findFirst().get();
        Status status = sellerService.buy(sellerId, weaponDto.getId(), 1);

        actionService.applyCommands(Collections.emptyMap()).getWeapons().forEach(weapon -> {
            System.out.println(weapon);
        });

        Assertions.assertTrue(status.isSuccess());
    }

}