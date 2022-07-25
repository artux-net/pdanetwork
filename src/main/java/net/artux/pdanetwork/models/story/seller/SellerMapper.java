package net.artux.pdanetwork.models.story.seller;

import net.artux.pdanetwork.entity.items.ItemMapper;
import net.artux.pdanetwork.entity.seller.SellerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ItemMapper.class)
public interface SellerMapper {

    SellerDto dto(SellerEntity entity);

}
