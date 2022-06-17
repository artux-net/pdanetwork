package net.artux.pdanetwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.SellerDto;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.service.items.SellerService;
import net.artux.pdanetwork.service.files.SellerManager;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;

@RestController
@RequiredArgsConstructor
@Api(tags = "Предметы")
@RequestMapping("/items")
public class ItemsController {

  private final SellerManager sellerManager;
  private final SellerService sellerService;

  @ApiOperation(value = "Получить продавца")
  @GetMapping("/{id}")
  public SellerDto getSeller(@PathVariable Integer id){
    return sellerManager.getSellerDto(id);
  }

  @ApiOperation(value = "Операция с предметом")
  @PostMapping("/{action}")
  public Status actionWithItem(@PathVariable String action, @QueryParam("seller") Integer seller, @QueryParam("hash") int hash){
    switch (action) {
      case "buy":
        return sellerService.buy(seller, hash);
      case "sell":
        return sellerService.sell(hash);
      case "set":
        return sellerService.set(hash);
      default:
        return new Status(false, "Action wrong");
    }
  }

  @ApiOperation(value = "Добавить предмет (только для админа)")
  @PostMapping("/add")
  public Status addItem(@QueryParam("pdaId") Long pdaId, @QueryParam("type") int type, @QueryParam("id") int id, @QueryParam("quantity") int quantity){
    return sellerService.add(pdaId, type, id, quantity);
  }

}