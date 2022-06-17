package net.artux.pdanetwork.models;

import lombok.Data;
import net.artux.pdanetwork.models.profile.items.ArmorEntity;
import net.artux.pdanetwork.models.profile.items.ArtifactEntity;
import net.artux.pdanetwork.models.profile.items.ItemEntity;
import net.artux.pdanetwork.models.profile.items.WeaponEntity;
import net.artux.pdanetwork.service.files.ItemProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class SellerDto {

    private int id;
    private String name;
    private String avatar;
    private List<ArmorEntity> armorEntities = new ArrayList<>();
    private List<WeaponEntity> pistols = new ArrayList<>();
    private List<WeaponEntity> rifles = new ArrayList<>();
    private List<ArtifactEntity> artifactEntities = new ArrayList<>();
    private List<ItemEntity> itemEntities = new ArrayList<>();

    public SellerDto(Seller seller, ItemProvider itemProvider){
        id = seller.getId();
        name = seller.getName();
        avatar = seller.getAvatar();
        seller.getItems().forEach((integer, integers) -> {
            switch (integer){
                case 0:
                    pistols = (List<WeaponEntity>) itemProvider.getItems(integer, integers);
                    break;
                case 1:
                    rifles = (List<WeaponEntity>) itemProvider.getItems(integer, integers);
                    break;
                case 2:
                    itemEntities = (List<ItemEntity>) itemProvider.getItems(integer, integers);
                    break;
                case 3:
                    artifactEntities = (List<ArtifactEntity>) itemProvider.getItems(integer, integers);
                    break;
                case 4:
                    armorEntities = (List<ArmorEntity>) itemProvider.getItems(integer, integers);
                    break;
            }
        });
    }

    public List<ItemEntity> getAllItems() {
        List<ItemEntity> itemEntities = new ArrayList<>(armorEntities);
        itemEntities.addAll(pistols);
        itemEntities.addAll(rifles);
        itemEntities.addAll(artifactEntities);
        itemEntities.addAll(this.itemEntities);
        return itemEntities;
    }

    public Optional<ItemEntity> getItemByHashCode(int hashCode){
        for (ItemEntity itemEntity :getAllItems()){
            if (itemEntity.hashCode() == hashCode){
                return Optional.of(itemEntity);
            }
        }
        return Optional.empty();
    }

}
