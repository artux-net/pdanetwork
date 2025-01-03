package net.artux.pdanetwork.repository.items;

import net.artux.pdanetwork.entity.seller.SellerEntity;
import net.artux.pdanetwork.entity.seller.SellerView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SellerRepository extends JpaRepository<SellerEntity, Long> {

    List<SellerView> findAllByIconIsNotNull();

}
