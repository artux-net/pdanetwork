package net.artux.pdanetwork.controller.items;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.seller.SellerDto;
import net.artux.pdanetwork.service.items.SellerService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Продавцы")
@RequestMapping("/sellers")
public class ItemsController {

    private final SellerService sellerService;

    @Operation(summary = "Получить продавца")
    @GetMapping("/{id}")
    public SellerDto getSeller(@PathVariable long id) {
        return sellerService.getSeller(id);
    }

    @Operation(summary = "Операция с предметом")
    @PostMapping("/{sellerId}/{itemId}")
    public Status actionWithItem(OperationType operationType, @PathVariable long sellerId,
                                 @PathVariable UUID itemId, int quantity) {
        return switch (operationType) {
            case BUY -> sellerService.buy(sellerId, itemId, quantity);
            case SELL -> sellerService.sell(sellerId, itemId, quantity);
        };
    }

    @Operation(summary = "Сделать предмет по умолчанию")
    @PostMapping("/set/{itemId}")
    public Status setItem(@PathVariable UUID itemId, ItemType type) {
        return sellerService.set(type, itemId);
    }

    @Operation(summary = "Добавить предмет (только для админа)")
    @PostMapping("/add")
    public Status addItem(UUID pdaId, int id, int quantity) {
        return sellerService.add(pdaId, id, quantity);
    }

    enum OperationType {
        BUY,
        SELL
    }

}