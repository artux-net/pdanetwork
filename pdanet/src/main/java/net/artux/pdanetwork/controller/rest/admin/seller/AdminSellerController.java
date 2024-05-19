package net.artux.pdanetwork.controller.rest.admin.seller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.seller.SellerAdminDto;
import net.artux.pdanetwork.models.seller.SellerDto;
import net.artux.pdanetwork.service.items.SellerService;
import net.artux.pdanetwork.utills.security.ModeratorAccess;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Управление продавцами", description = "Доступен с роли модератора, лучше сохранять все что вводите отдельно")
@RequestMapping("/api/v1/admin/seller")
@ModeratorAccess
@RequiredArgsConstructor
public class AdminSellerController {

    private final SellerService service;

    @Operation(summary = "Продавец по id")
    @GetMapping("/{id}")
    public SellerDto getSeller(@PathVariable long id) {
        return service.getSeller(id);
    }

    @Operation(summary = "Продавец по id")
    @GetMapping("/all")
    public List<SellerAdminDto> getSellers() {
        return service.getSellers();
    }

    @PostMapping
    @Operation(summary = "Создание продавца")
    public SellerDto createSeller(@RequestBody SellerAdminDto dto) {
        return service.createSeller(dto);
    }

    @PutMapping(value = "/{sellerId}")
    @Operation(summary = "Обновление продавца")
    public SellerDto updateSeller(@RequestBody SellerAdminDto dto, @PathVariable Long sellerId) {
        return service.updateSeller(sellerId, dto);
    }

    @PutMapping(value = "/{sellerId}/items")
    @Operation(summary = "Добавить предметы продавца, в массиве указывается id:quantity")
    public SellerDto addSellerItems(@PathVariable Long sellerId, @RequestBody List<String> s) {
        return service.addSellerItems(sellerId, s);
    }

    @DeleteMapping(value = "/{sellerId}/items")
    @Operation(summary = "Удалить предметы продавца, указывается uuid предметов")
    public SellerDto deleteSellerItems(@PathVariable Long sellerId, @RequestBody List<UUID> ids) {
        return service.deleteSellerItems(sellerId, ids);
    }

    @DeleteMapping(value = "/{sellerId}")
    @Operation(summary = "Удалить продавца")
    public boolean deleteSeller(@PathVariable Long sellerId) {
        service.deleteSeller(sellerId);
        return true;
    }

}
