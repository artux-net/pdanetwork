package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.SellerDto;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.service.items.ItemsService;
import net.artux.pdanetwork.service.files.SellersService;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;

@RestController
@RequiredArgsConstructor
@Api(tags = "Предметы")
@RequestMapping("/items")
public class ItemsController {

  private final SellersService sellersService;
  private final ItemsService itemsService;

  @ApiOperation(value = "Продавец")
  @GetMapping("/{id}")
  public SellerDto getSeller(@PathVariable Integer id){
    return sellersService.getSellerDto(id);
  }

  @ApiOperation(value = "Операция с предметом")
  @PostMapping("/{action}")
  public Status actionWithItem(@PathVariable String action, @QueryParam("seller") Integer sellerId, @QueryParam("hash") int itemHash){
    switch (action) {
      case "buy":
        return itemsService.buy(sellerId, itemHash);
      case "sell":
        return itemsService.sell(itemHash);
      case "set":
        return itemsService.set(itemHash);
      default:
        return new Status(false, "Action wrong");
    }
  }

  @ApiOperation(value = "Добавить предмет (только для админа)")
  @PostMapping("/add")
  public Status addItem(@QueryParam("pdaId") Integer pdaId, @QueryParam("type") int type, @QueryParam("id") int id, @QueryParam("quantity") int quantity){
    return itemsService.add(pdaId, type, id, quantity);
  }

}