package net.artux.pdanetwork.controller.rest.items;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.seller.SellerAdminDto;
import net.artux.pdanetwork.models.seller.SellerDto;
import net.artux.pdanetwork.service.items.SellerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Продавцы")
@RequestMapping("/api/v1/seller")
public class SellerController {

    private final SellerService sellerService;

    @Operation(summary = "Получить продавца")
    @GetMapping("/{id}")
    public SellerDto getSeller(@PathVariable long id) {
        return sellerService.getSeller(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить всех продавцов")
    public List<SellerAdminDto> getSellers() {
        return sellerService.getSellers();
    }

    @Operation(summary = "Продать или купить предмет заданному продавцу в количестве quantity")
    @PostMapping("/{sellerId}/{operationType}/{itemId}")
    public Status actionWithItem(@PathVariable OperationType operationType, @PathVariable long sellerId,
                                 @PathVariable UUID itemId, @RequestParam int quantity) {
        return switch (operationType) {
            case BUY -> sellerService.buy(sellerId, itemId, quantity);
            case SELL -> sellerService.sell(sellerId, itemId, quantity);
        };
    }

    enum OperationType {
        BUY,
        SELL
    }

}