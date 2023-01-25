package net.artux.pdanetwork.controller.rest.enc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.items.ItemsContainer;
import net.artux.pdanetwork.service.items.ItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Предметы")
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemsController {

    private final ItemService itemService;

    @Operation(summary = "Получить все предметы по типам")
    @GetMapping("/all")
    public ItemsContainer getContainer() {
        return itemService.getItemsContainer();
    }

}
