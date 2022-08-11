package net.artux.pdanetwork.models.seller;

import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.seller.SellerEntity;
import net.artux.pdanetwork.models.items.ItemMapper;
import net.artux.pdanetwork.service.items.ItemService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, ItemService.class})
public abstract class SellerMapper {

    @Autowired
    private ItemService provider;

    public abstract SellerDto dto(SellerEntity entity);

    public SellerEntity entity(Seller seller) {
        SellerEntity sellerEntity = new SellerEntity();
        sellerEntity.setId(seller.getId());
        sellerEntity.setName(seller.getName());
        sellerEntity.setIcon(seller.getIcon());
        sellerEntity.setImage(seller.getImage());
        sellerEntity.setBuyCoefficient(seller.getBuyCoefficient());
        sellerEntity.setSellCoefficient(seller.getSellCoefficient());
        for (String decoded : seller.getItems()) {
            String[] values = decoded.split(":");
            long baseId = Long.parseLong(values[0]);
            int quantity = Integer.parseInt(values[1]);
            ItemEntity item = provider.getItem(baseId);
            item.setQuantity(quantity);
            sellerEntity.addItem(item);
        }
        return sellerEntity;
    }
}
