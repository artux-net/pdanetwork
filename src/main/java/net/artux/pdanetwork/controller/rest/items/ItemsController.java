package net.artux.pdanetwork.controller.rest.items;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.items.ItemsContainer;
import net.artux.pdanetwork.service.items.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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


    @Operation(summary = "Экипировать предмет")
    @PostMapping("/set/{itemId}")
    public Status setItem(@PathVariable UUID itemId) {
        return itemService.setWearable(itemId);
    }

}
