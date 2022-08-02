package net.artux.pdanetwork.controller.items;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.seller.SellerDto;
import net.artux.pdanetwork.service.items.SellerService;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;

@RestController
@RequiredArgsConstructor
@Api(tags = "Продавцы")
@RequestMapping("/sellers")
public class ItemsController {

    private final SellerService sellerService;

    @ApiOperation(value = "Получить продавца")
    @GetMapping("/{id}")
    public SellerDto getSeller(@PathVariable long id) {
        return sellerService.getSeller(id);
    }

    @ApiOperation(value = "Операция с предметом")
    @PostMapping("/{sellerId}/{itemId}")
    public Status actionWithItem(OperationType operationType, @PathVariable long sellerId,
                                 @PathVariable long itemId, ItemType type, int quantity) {
        return switch (operationType) {
            case BUY -> sellerService.buy(sellerId, type, itemId, quantity);
            case SELL -> sellerService.sell(sellerId, type, itemId, quantity);
            default -> new Status(false, "Action wrong");
        };
    }

    @ApiOperation(value = "Сделать предмет по умолчанию")
    @PostMapping("/set/{itemId}")
    public Status setItem(@PathVariable long itemId, ItemType type) {
        return sellerService.set(type, itemId);
    }

    @ApiOperation(value = "Добавить предмет (только для админа)")
    @PostMapping("/add")
    public Status addItem(@QueryParam("pdaId") Long pdaId,
                          @QueryParam("baseId") int id, @QueryParam("quantity") int quantity) {
        return sellerService.add(pdaId, id, quantity);
    }

    enum OperationType {
        BUY,
        SELL
    }

}