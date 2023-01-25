package net.artux.pdanetwork.controller.rest.enc;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.items.ItemsContainer;
import net.artux.pdanetwork.service.items.BaseItemService;
import net.artux.pdanetwork.service.items.ItemService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class EncController {

    private final ItemService itemService;

    @GetMapping("/all")
    public ItemsContainer getContainer() {
        return itemService.getItemsContainer();
    }

}
