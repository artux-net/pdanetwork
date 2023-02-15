package net.artux.pdanetwork.controller.rest.items;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.seller.SellerDto;
import net.artux.pdanetwork.service.items.SellerService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Продавцы")
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;

    @Operation(summary = "Получить продавца")
    @GetMapping("/{id}")
    public SellerDto getSeller(@PathVariable long id) {
        return sellerService.getSeller(id);
    }

    @Operation(summary = "Операция с предметом")
    @PostMapping("/{sellerId}/{type}/{itemId}")
    public Status actionWithItem(@PathVariable OperationType type, @PathVariable long sellerId,
                                 @PathVariable UUID itemId, int quantity) {
        return switch (type) {
            case BUY -> sellerService.buy(sellerId, itemId, quantity);
            case SELL -> sellerService.sell(sellerId, itemId, quantity);
        };
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