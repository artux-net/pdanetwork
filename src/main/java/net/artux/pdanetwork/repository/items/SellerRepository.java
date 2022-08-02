package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.entity.seller.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface SellerRepository extends JpaRepository<SellerEntity, Long> {

}
