package net.artux.pdanetwork.models;

import lombok.Data;
import lombok.Getter;
import net.artux.pdanetwork.models.profile.items.Armor;
import net.artux.pdanetwork.models.profile.items.Artifact;
import net.artux.pdanetwork.models.profile.items.Item;
import net.artux.pdanetwork.models.profile.items.Weapon;
import net.artux.pdanetwork.service.files.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Getter
public class SellerDto {

    private int id;
    private String name;
    private String avatar;
    private List<Armor> armors = new ArrayList<>();
    private List<Weapon> pistols = new ArrayList<>();
    private List<Weapon> rifles = new ArrayList<>();
    private List<Artifact> artifacts = new ArrayList<>();
    private List<Item> items = new ArrayList<>();

    public SellerDto(Seller seller, Types types){
        id = seller.getId();
        name = seller.getName();
        avatar = seller.getAvatar();
        seller.getItems().forEach((integer, integers) -> {
            switch (integer){
                case 0:
                    pistols = (List<Weapon>) types.getItems(integer, integers);
                    break;
                case 1:
                    rifles = (List<Weapon>) types.getItems(integer, integers);
                    break;
                case 2:
                    items = (List<Item>) types.getItems(integer, integers);
                    break;
                case 3:
                    artifacts = (List<Artifact>) types.getItems(integer, integers);
                    break;
                case 4:
                    armors = (List<Armor>) types.getItems(integer, integers);
                    break;
            }
        });
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>(armors);
        items.addAll(pistols);
        items.addAll(rifles);
        items.addAll(artifacts);
        items.addAll(this.items);
        return items;
    }

    public Optional<Item> getItemByHashCode(int hashCode){
        for (Item item:getAllItems()){
            if (item.hashCode() == hashCode){
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

}
