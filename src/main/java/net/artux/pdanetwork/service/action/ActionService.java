package net.artux.pdanetwork.service.action;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.profile.ParameterEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.models.SellerDto;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.models.profile.Equipment;
import net.artux.pdanetwork.models.profile.Story;
import net.artux.pdanetwork.models.profile.items.ArmorEntity;
import net.artux.pdanetwork.models.profile.items.ArtifactEntity;
import net.artux.pdanetwork.models.profile.items.ItemEntity;
import net.artux.pdanetwork.models.profile.items.WeaponEntity;
import net.artux.pdanetwork.repository.user.ParametersRepository;
import net.artux.pdanetwork.service.ItemsManager;
import net.artux.pdanetwork.service.files.SellersService;
import net.artux.pdanetwork.service.files.Types;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

import static net.artux.pdanetwork.utills.ServletContext.error;

@Service
@RequiredArgsConstructor
public class ActionService {

    private final ItemsManager itemsManager;
    private final Types types;
    private final SellersService sellersService;
    private final ParametersRepository parametersRepository;

    public UserEntity doUserActions(HashMap<String, List<String>> map, UserEntity userEntity) {
        List<ParameterEntity> parameterEntity = parametersRepository.getByUserId(userEntity.getUid());

        try {
            Data data = userEntity.getData();
            for (String command : map.keySet()) {
                List<String> params = map.get(command);
                switch (command) {
                    case "add":
                        for (String value : params) {
                            String[] param = value.split(":");
                            if (param.length == 3) {
                                data = addItems(data, param);
                            } else if (param.length == 2) {
                                //add_value
                                if (!data.parameterEntity.values.containsKey(value.split(":")[0])) {
                                    data.parameterEntity.values.put(value.split(":")[0], Integer.parseInt(value.split(":")[1]));
                                }
                            } else {
                                //add_param
                                if (!data.parameterEntity.keys.contains(value)) data.parameterEntity.keys.add(value);
                            }
                        }
                        break;
                    case "add_param":
                        for (String value : params) {
                            if (!data.parameterEntity.keys.contains(value)) data.parameterEntity.keys.add(value);
                        }
                        break;
                    case "add_value":
                        for (String value : params) {
                            if (!data.parameterEntity.values.containsKey(value.split(":")[0])) {
                                data.parameterEntity.values.put(value.split(":")[0], Integer.parseInt(value.split(":")[1]));
                            } else {
                                data.parameterEntity.
                                        values.put(value.split(":")[0],
                                                data.parameterEntity.values.get(value.split(":")[0]) + Integer.parseInt(value.split(":")[1]));
                            }
                        }
                        break;
                    case "add_items":
                        for (String value : params) {
                            String[] values = value.split(":");
                            if (values.length == 3) {
                                data = addItems(data, values);
                            }
                        }
                        break;
                    case "remove":
                        for (String pass : params) {
                            String[] vals = pass.split(":");
                            if (vals.length == 3) {
                                data = removeItems(data, vals);
                            }
                            data.parameterEntity.keys.remove(pass);
                            data.parameterEntity.values.remove(pass);
                        }
                        break;
                    case "=":
                        for (String pass : params) {
                            String[] vals = pass.split(":");
                            data.parameterEntity.values.put(vals[0], Integer.valueOf(vals[1]));
                        }
                        break;
                    case "+":
                        for (String pass : params) {
                            String[] vals = pass.split(":");
                            if (vals[0].contains("relation")) {
                                int group = Integer.parseInt(vals[0].split("_")[1]);
                                userEntity.relations.set(group, userEntity.relations.get(group) + Integer.parseInt(vals[1]));
                            } else
                                data.parameterEntity.values.put(vals[0], data.parameterEntity.values.get(vals[0]) + Integer.valueOf(vals[1]));
                        }
                        break;
                    case "-":
                        for (String pass : Objects.requireNonNull(map.get(command))) {
                            String[] vals = pass.split(":");
                            if (vals[0].contains("relation")) {
                                int group = Integer.parseInt(vals[0].split("_")[1]);
                                userEntity.relations.set(group, userEntity.relations.get(group) - Integer.parseInt(vals[1]));
                            } else
                                data.parameterEntity.values.put(vals[0], data.parameterEntity.values.get(vals[0]) - Integer.parseInt(vals[1]));
                        }
                        break;
                    case "*":
                        for (String pass : params) {
                            String[] vals = pass.split(":");
                            data.parameterEntity.values.put(vals[0], data.parameterEntity.values.get(vals[0]) * Integer.parseInt(vals[1]));
                        }
                        break;
                    case "money":
                        for (String pass : params)
                            userEntity.money(Integer.parseInt(pass));
                        break;
                    case "xp":
                        for (String pass : params)
                            userEntity.xp(Integer.parseInt(pass));
                        break;
                    case "note":
                        List<String> content =params;
                        //TODO with notes service
                       /* if (content.size() == 2)
                            userEntity.addNote(content.get(0), content.get(1));
                        else if (content.size() == 1)
                            userEntity.addNote("Новая заметка", content.get(1));*/
                        break;
                    case "achieve":
                        for (String pass : params)
                            userEntity.achievements.add(Integer.parseInt(pass));
                        break;
                    case "location":
                        for (String pass : params)
                            userEntity.setLocation(pass);
                        break;
                    case "reset":
                        if (map.get(command).size() == 0) {
                            userEntity.setMoney(0);
                            data.weaponEntities = new ArrayList<>();
                            data.armorEntities = new ArrayList<>();
                            data.artifactEntities = new ArrayList<>();
                            data.itemEntities = new ArrayList<>();
                        } else {
                            for (String pass : params)
                                if (isInteger(pass)) {
                                    int storyId = Integer.parseInt(pass);
                                    for (Story story : data.stories)
                                        if (story.storyId == storyId) {
                                            story.lastChapter = 1;
                                            story.lastStage = 0;
                                        }
                                } else if (pass.contains("relation")) {
                                    int group = Integer.parseInt(pass.split("_")[1]);
                                    userEntity.relations.set(group, 0);
                                }
                        }
                        break;
                    case "reset_current":
                        data.getTemp().remove("currentStory");
                        break;
                    case "set":
                        for (String pass :params) {
                            String[] vals = pass.split(":");
                            if (vals.length == 4) {
                                if (vals[0].equals("story")) {
                                    boolean found = false;
                                    for (int i = 0; i < data.stories.size(); i++) {
                                        Story story = data.stories.get(i);
                                        if (story.storyId == Integer.parseInt(vals[1])) {
                                            found = true;
                                            story.lastChapter = Integer.parseInt(vals[2]);
                                            story.lastStage = Integer.parseInt(vals[3]);
                                            data.stories.set(i, story);
                                        }
                                    }
                                    if (!found) {
                                        int story = Integer.parseInt(vals[1]);
                                        int chapter = Integer.parseInt(vals[2]);
                                        int stage = Integer.parseInt(vals[3]);
                                        data.stories.add(new Story(story, chapter, stage));
                                    }
                                    data.getTemp().put("currentStory", vals[1]);
                                }
                            }
                        }
                        break;
                    default:
                        System.out.println("UserManager, unsupported operation: " + command + ", value: " +params);
                        break;
                }
            }
            userEntity.setData(data);
            return userEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return userEntity;
        }
    }

    public Status set(UserEntity userEntity, @NotNull int hashCode) {
        Equipment equipment = userEntity.getData().getEquipment();
        Data data = userEntity.getData();
        Optional<ItemEntity> itemOptional = data.getItemByHashCode(hashCode);
        if (itemOptional.isPresent()) {
            ItemEntity itemEntity = itemOptional.get();
            if (itemEntity instanceof WeaponEntity) {
                WeaponEntity old;

                if (itemEntity.getType() == 0)
                    old = equipment.getFirstWeapon();
                else
                    old = equipment.getSecondWeapon();


                userEntity.setData(
                        addItems(data, old));
                userEntity.setData(
                        deleteItem(data, itemEntity));

                if (itemEntity.getType() == 0)
                    equipment.setFirstWeapon((WeaponEntity) itemEntity);
                else
                    equipment.setSecondWeapon((WeaponEntity) itemEntity);

                data.setEquipment(equipment);
                return new Status(true, "Success.");
            } else if (itemEntity instanceof ArmorEntity) {
                ArmorEntity old = equipment.getArmor();

                userEntity.setData(
                        addItems(data, old));
                userEntity.setData(
                        deleteItem(data, itemEntity));

                equipment.setArmor((ArmorEntity) itemEntity);
                data.setEquipment(equipment);
                return new Status(true, "Success.");
            } else return new Status(false, "Can not set this item.");
        } else
            return new Status(false, "You don't have this item.");
    }

    public Status sell(UserEntity userEntity, int hashCode) {
        Data data = userEntity.getData();
        Optional<ItemEntity> itemOptional = data.getItemByHashCode(hashCode);
        if (itemOptional.isPresent()) {
            ItemEntity itemEntity = itemOptional.get();
            boolean removed = false;

            switch (itemEntity.getType()) {
                case 0:
                case 1:
                    for (int i = 0; i < data.weaponEntities.size(); i++) {
                        if (data.weaponEntities.get(i).equals(itemEntity)) {
                            userEntity.setMoney(userEntity.getMoney() + data.weaponEntities.get(i).priceToSell());
                            data.weaponEntities.remove(i);
                            removed = true;
                            break;
                        }
                    }
                case 2:
                    for (int i = 0; i < data.itemEntities.size(); i++) {
                        if (data.itemEntities.get(i).equals(itemEntity)) {
                            userEntity.setMoney(userEntity.getMoney() + data.itemEntities.get(i).priceToSell());
                            data.itemEntities.remove(i);
                            removed = true;
                            break;
                        }
                    }
                case 3:
                    for (int i = 0; i < data.artifactEntities.size(); i++) {
                        if (data.artifactEntities.get(i).equals(itemEntity)) {
                            userEntity.setMoney(userEntity.getMoney() + data.artifactEntities.get(i).priceToSell());
                            data.artifactEntities.remove(i);
                            removed = true;
                            break;
                        }
                    }
                case 4:
                    for (int i = 0; i < data.armorEntities.size(); i++) {
                        if (data.armorEntities.get(i).equals(itemEntity)) {
                            userEntity.setMoney(userEntity.getMoney() + data.armorEntities.get(i).priceToSell());
                            data.armorEntities.remove(i);
                            removed = true;
                            break;
                        }
                    }
            }
            userEntity.setData(data);
            if (removed)
                return new Status(true, "Предмет продан.");
            else
                return new Status(false, "Не удалось продать предмет.");
        } else
            return new Status(false, "Не удалось продать предмет.");
    }

   /* public boolean addMoney(int pdaId, int money) {
        Member member = mongoUsers.getById(pdaId);
        if (member != null) {
            member.setMoney(member.getMoney() + money);
            return true;
        } else return false;
    }*/

    public Status buy(UserEntity userEntity, int itemHash, int sellerId) {
        SellerDto seller = sellersService.getSellerDto(sellerId);
        Optional<ItemEntity> optionalItem = seller.getItemByHashCode(itemHash);
        if (optionalItem.isPresent()) {
            ItemEntity itemEntity = optionalItem.get();
            if (userEntity.buy(itemEntity.sellerPrice())) {
                userEntity.setData(addItems(userEntity.getData(), itemEntity));
                return new Status(true, "Покупка прошла успешно.");
            } else
                return new Status(false, "Недостаточно средств на счете.");

        } else
            return new Status(false, "У продавца отсутствует товар.");
    }

    public void addItem(UserEntity userEntity, int type, int id, int quantity) {
        Data data = userEntity.getData();
        userEntity.setData(addItems(data, new String[]{String.valueOf(type), String.valueOf(id), String.valueOf(quantity)}));
    }

    private Data addItems(Data data, String[] values) {
        try {
            if (values.length == 3) {
                int type = Integer.parseInt(values[0]);
                int id = Integer.parseInt(values[1]);
                int quantity = Integer.parseInt(values[2]);
                //log("try to add item, type: " + type + ", cid: " + id);
                ItemEntity itemEntity = types.getItem(type, id);
                itemEntity.setQuantity(quantity);
                if (quantity<=0){
                    return removeItems(data, values);
                }else
                    return addItems(data, itemEntity);

            }
        } catch (Exception e) {
            error("Item err", e);
        }
        return data;
    }

    private boolean isCorrect(ItemEntity itemEntity, int type, int id) {
        if (itemEntity != null)
            if (itemEntity.getType() == type && itemEntity.getId() == id)
                return true;

        return false;
    }

    private List<? extends ItemEntity> removeItemsFromList(List<? extends ItemEntity> items, int type, int id, int quantity ) {
        Iterator<? extends ItemEntity> i = items.iterator();
        while (i.hasNext()) {
            ItemEntity w = i.next();
            if (isCorrect(w, type, id))
                if (w.getQuantity() >= quantity)
                    i.remove();
                else
                    w.setQuantity(w.getQuantity() - quantity);
        }
        return items;
    }

    private Data removeItems(Data data, String[] values) {
        try {
            if (values.length == 3) {
                int type = Integer.parseInt(values[0]);
                int id = Integer.parseInt(values[1]);
                int quantity = Integer.parseInt(values[2]);
                //log("try to add item, type: " + type + ", cid: " + id);

                if (isCorrect(data.getEquipment().getFirstWeapon(), type, id)){
                    data.getEquipment().setFirstWeapon(null);
                }

                if (isCorrect(data.getEquipment().getSecondWeapon(), type, id)){
                    data.getEquipment().setSecondWeapon(null);
                }

                if (isCorrect(data.getEquipment().getArmor(), type, id)){
                    data.getEquipment().setArmor(null);
                }

                data.weaponEntities = (List<WeaponEntity>) removeItemsFromList(data.weaponEntities, type, id, quantity);
                data.armorEntities = (List<ArmorEntity>) removeItemsFromList(data.armorEntities, type, id, quantity);
                data.itemEntities = (List<ItemEntity>) removeItemsFromList(data.itemEntities, type, id, quantity);
                data.artifactEntities = (List<ArtifactEntity>) removeItemsFromList(data.artifactEntities, type, id, quantity);
                //TODO MORE

                return data;
            }
        } catch (Exception e) {
            error("Item err", e);
        }
        return data;
    }

    private Data addItems(Data data, ItemEntity itemEntity) {
        if (itemEntity != null) {
            if (itemEntity instanceof WeaponEntity)
                return itemsManager.addWeapon(data, (WeaponEntity) itemEntity, 1);
            else if (itemEntity instanceof ArmorEntity)
                return itemsManager.addArmor(data, (ArmorEntity) itemEntity, 1);
            else if (itemEntity instanceof ArtifactEntity)
                return itemsManager.addArtifact(data, (ArtifactEntity) itemEntity);
            else
                return itemsManager.addItem(data, itemEntity);
        } else {
            //log("item null");
            return data;
        }
    }

    private Data deleteItem(Data data, ItemEntity itemEntity) {
        if (itemEntity instanceof WeaponEntity) {
            data.weaponEntities.remove(itemEntity);
            return data;
        } else if (itemEntity instanceof ArmorEntity) {
            data.armorEntities.remove(itemEntity);
            return data;
        } else if (itemEntity instanceof ArtifactEntity) {
            data.artifactEntities.remove(itemEntity);
            return data;
        } else
            data.itemEntities.remove(itemEntity);
        return data;
    }

    public SellersService getSellersService() {
        return sellersService;
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
