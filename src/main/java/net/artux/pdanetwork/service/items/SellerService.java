package net.artux.pdanetwork.service.items;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.seller.SellerCreateDto;
import net.artux.pdanetwork.models.seller.SellerDto;

import java.util.List;
import java.util.UUID;

public interface SellerService {

    SellerDto getSeller(long id);

    List<SellerDto> getSellers();

    Status buy(long sellerId, UUID id, int quantity);

    Status sell(long sellerId, UUID id, int quantity);

    SellerDto createSeller(SellerCreateDto dto);

    SellerDto updateSeller(SellerCreateDto dto);

    SellerDto addSellerItems(Long sellerId, List<String> s);

    SellerDto deleteSellerItems(Long sellerId, List<UUID> ids);

    void deleteSeller(Long id);

}
