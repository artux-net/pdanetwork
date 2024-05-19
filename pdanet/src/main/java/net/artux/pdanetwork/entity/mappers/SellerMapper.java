package net.artux.pdanetwork.entity.mappers;

import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.seller.SellerEntity;
import net.artux.pdanetwork.models.seller.SellerAdminDto;
import net.artux.pdanetwork.models.seller.SellerDto;
import net.artux.pdanetwork.service.items.ItemService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, ItemService.class})
public abstract class SellerMapper {

    @Autowired
    private ItemService provider;

    public abstract SellerDto dto(SellerEntity entity);

    @Mapping(target = "items", expression = "java(encode(entity))")
    public abstract SellerAdminDto adminDto(SellerEntity entity);

    public SellerEntity entity(SellerAdminDto sellerAdminDto) {
        SellerEntity sellerEntity = new SellerEntity();
        sellerEntity.setId(sellerAdminDto.getId());
        sellerEntity.setName(sellerAdminDto.getName());
        sellerEntity.setIcon(sellerAdminDto.getIcon());
        sellerEntity.setImage(sellerAdminDto.getImage());
        sellerEntity.setBuyCoefficient(sellerAdminDto.getBuyCoefficient());
        sellerEntity.setSellCoefficient(sellerAdminDto.getSellCoefficient());
        items(sellerEntity, sellerAdminDto.getItems());

        return sellerEntity;
    }

    public void items(SellerEntity entity, Collection<String> encodedItems) {
        HashMap<Long, Integer> map = new HashMap<>();
        for (String decoded : encodedItems) {
            String[] values = decoded.split(":");
            long baseId = Long.parseLong(values[0]);
            int quantity = Integer.parseInt(values[1]);
            map.put(baseId, quantity);
        }
        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            ItemEntity item = provider.getItem(entry.getKey());
            item.setQuantity(entry.getValue());
            entity.addItem(item);
        }
    }

    public List<String> encode(SellerEntity entity) {
        List<String> list = new ArrayList<>();
        for (ItemEntity item : entity.getAllItems()) {
            list.add(item.getId() + ":" + item.getQuantity());
        }
        return list;
    }

    public abstract List<SellerEntity> entity(List<SellerAdminDto> sellerDto);
}
