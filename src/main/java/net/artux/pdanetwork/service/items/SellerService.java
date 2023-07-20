package net.artux.pdanetwork.service.items;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.seller.SellerAdminDto;
import net.artux.pdanetwork.models.seller.SellerDto;

import java.util.List;
import java.util.UUID;

public interface SellerService {

    SellerDto getSeller(long id);

    List<SellerAdminDto> getSellers();

    Status buy(long sellerId, UUID id, int quantity);

    Status sell(long sellerId, UUID id, int quantity);

    SellerDto createSeller(SellerAdminDto dto);

    SellerDto updateSeller(Long id, SellerAdminDto dto);

    SellerDto addSellerItems(Long sellerId, List<String> s);

    SellerDto deleteSellerItems(Long sellerId, List<UUID> ids);

    void deleteSeller(Long id);

}
