package net.artux.pdanetwork.models.profile;

import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.models.profile.items.ArmorEntity;
import net.artux.pdanetwork.models.profile.items.ArtifactEntity;
import net.artux.pdanetwork.models.profile.items.ItemEntity;
import net.artux.pdanetwork.models.profile.items.WeaponEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@lombok.Data
@Getter
@Setter
public class Data {

    private Equipment equipment = new Equipment();
    private Stats stats = new Stats();

    public List<ArmorEntity> armorEntities = new ArrayList<>();
    public List<WeaponEntity> weaponEntities = new ArrayList<>();
    public List<ArtifactEntity> artifactEntities = new ArrayList<>();
    public List<ItemEntity> itemEntities = new ArrayList<>();

    public List<Story> stories = new ArrayList<>();
    public ParameterEntity parameterEntity = new ParameterEntity();
    private HashMap<String, String> temp = new HashMap<>();

    public Data() {
    }

    public List<ItemEntity> getAllItems() {
        List<ItemEntity> itemEntities = new ArrayList<>(armorEntities);
        itemEntities.addAll(weaponEntities);
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
