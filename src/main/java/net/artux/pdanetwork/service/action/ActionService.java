package net.artux.pdanetwork.service.action;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Member;
import net.artux.pdanetwork.models.SellerDto;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.models.profile.Equipment;
import net.artux.pdanetwork.models.profile.Story;
import net.artux.pdanetwork.models.profile.items.Armor;
import net.artux.pdanetwork.models.profile.items.Artifact;
import net.artux.pdanetwork.models.profile.items.Item;
import net.artux.pdanetwork.models.profile.items.Weapon;
import net.artux.pdanetwork.service.files.SellersService;
import net.artux.pdanetwork.service.files.Types;
import net.artux.pdanetwork.utills.ItemsManager;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

import static net.artux.pdanetwork.utills.ServletContext.error;
import static net.artux.pdanetwork.utills.ServletContext.mongoUsers;

@Service
@RequiredArgsConstructor
public class ActionService {

    private final ItemsManager itemsManager;
    private final Types types;
    private final SellersService sellersService;

    public Member doUserActions(HashMap<String, List<String>> map, Member member) {
        try {
                Data data = member.getData();
                for (String key : map.keySet()) {
                    switch (key) {
                        case "add":
                            for (String value : map.get(key)) {
                                String[] values = value.split(":");
                                if (values.length == 3) {
                                    data = addItems(data, values);
                                } else if (values.length == 2) {
                                    //add_value
                                    if (!data.parameters.values.containsKey(value.split(":")[0])) {
                                        data.parameters.values.put(value.split(":")[0], Integer.parseInt(value.split(":")[1]));
                                    }
                                } else {
                                    //add_param
                                    if (!data.parameters.keys.contains(value)) data.parameters.keys.add(value);
                                }
                            }
                            break;
                        case "add_param":
                            for (String value : map.get(key)) {
                                if (!data.parameters.keys.contains(value)) data.parameters.keys.add(value);
                            }
                            break;
                        case "add_value":
                            for (String value : map.get(key)) {
                                if (!data.parameters.values.containsKey(value.split(":")[0])) {
                                    data.parameters.values.put(value.split(":")[0], Integer.parseInt(value.split(":")[1]));
                                } else {
                                    data.parameters.
                                            values.put(value.split(":")[0],
                                            data.parameters.values.get(value.split(":")[0]) + Integer.parseInt(value.split(":")[1]));
                                }
                            }
                            break;
                        case "add_items":
                            for (String value : map.get(key)) {
                                String[] values = value.split(":");
                                if (values.length == 3) {
                                    data = addItems(data, values);
                                }
                            }
                            break;
                        case "remove":
                            for (String pass : map.get(key)) {
                                if (isInteger(pass)) {
                                    int id = Integer.parseInt(pass);
                                    data.weapons.removeIf(weapon -> weapon.getId() == id);
                                }
                                data.parameters.keys.remove(pass);
                                data.parameters.values.remove(pass);
                            }
                            break;
                        case "=":
                            for (String pass : map.get(key)) {
                                String[] vals = pass.split(":");
                                data.parameters.values.put(vals[0], Integer.valueOf(vals[1]));
                            }
                            break;
                        case "+":
                            for (String pass : map.get(key)) {
                                String[] vals = pass.split(":");
                                if (vals[0].contains("relation")) {
                                    int group = Integer.parseInt(vals[0].split("_")[1]);
                                    member.relations.set(group, member.relations.get(group) + Integer.parseInt(vals[1]));
                                } else
                                    data.parameters.values.put(vals[0], data.parameters.values.get(vals[0]) + Integer.valueOf(vals[1]));
                            }
                            break;
                        case "-":
                            for (String pass : Objects.requireNonNull(map.get(key))) {
                                String[] vals = pass.split(":");
                                if (vals[0].contains("relation")) {
                                    int group = Integer.parseInt(vals[0].split("_")[1]);
                                    member.relations.set(group, member.relations.get(group) - Integer.parseInt(vals[1]));
                                } else
                                    data.parameters.values.put(vals[0], data.parameters.values.get(vals[0]) - Integer.parseInt(vals[1]));
                            }
                            break;
                        case "*":
                            for (String pass : map.get(key)) {
                                String[] vals = pass.split(":");
                                data.parameters.values.put(vals[0], data.parameters.values.get(vals[0]) * Integer.parseInt(vals[1]));
                            }
                            break;
                        case "money":
                            for (String pass : map.get(key))
                                member.money(Integer.parseInt(pass));
                            break;
                        case "xp":
                            for (String pass : map.get(key))
                                member.xp(Integer.parseInt(pass));
                            break;
                        case "note":
                            List<String> content = map.get(key);
                            if (content.size()==2)
                                member.addNote(content.get(0), content.get(1));
                            else if(content.size()==1)
                                member.addNote("Новая заметка", content.get(1));
                            break;
                        case "achieve":
                            for (String pass : map.get(key))
                                member.achievements.add(Integer.parseInt(pass));
                            break;
                        case "location":
                            for (String pass : map.get(key))
                                member.setLocation(pass);
                            break;
                        case "reset":
                            if (map.get(key).size() == 0) {
                                data.weapons = new ArrayList<>();
                                data.armors = new ArrayList<>();
                                data.artifacts = new ArrayList<>();
                                data.items = new ArrayList<>();
                            } else {
                                for (String pass : map.get(key))
                                    if (isInteger(pass)) {
                                        int storyId = Integer.parseInt(pass);
                                        for (Story story : data.stories)
                                            if (story.storyId == storyId) {
                                                story.lastChapter = 1;
                                                story.lastStage = 0;
                                            }
                                    } else if (pass.contains("relation")) {
                                        int group = Integer.parseInt(pass.split("_")[1]);
                                        member.relations.set(group, 0);
                                    }
                            }
                            break;
                        case "reset_current":
                            data.getTemp().remove("currentStory");
                            break;
                        case "set":
                            for (String pass : map.get(key)) {
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
                            System.out.println("UserManager, unsupported operation: " + key + ", value: " + map.get(key));
                            break;
                    }
                }
                member.setData(data);
                return member;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
        }
    }

    public Status set(Member member, @NotNull int hashCode) {
        Equipment equipment = member.getData().getEquipment();
        Data data = member.getData();
        Optional<Item> itemOptional = data.getItemByHashCode(hashCode);
        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();
            if (item instanceof Weapon) {
                Weapon old;

                if (item.getType() == 0)
                    old = equipment.getFirstWeapon();
                else
                    old = equipment.getSecondWeapon();


                member.setData(
                        addItems(data, old));
                member.setData(
                        deleteItem(data, item));

                if (item.getType() == 0)
                    equipment.setFirstWeapon((Weapon) item);
                else
                    equipment.setSecondWeapon((Weapon) item);

                data.setEquipment(equipment);
                return new Status(true, "Success.");
            }else if (item instanceof Armor){
                Armor old = equipment.getArmor();

                member.setData(
                        addItems(data, old));
                member.setData(
                        deleteItem(data, item));

                equipment.setArmor((Armor) item);
                data.setEquipment(equipment);
                return new Status(true, "Success.");
            }else return new Status(false, "Can not set this item.");
        } else
            return new Status(false, "You don't have this item.");
    }

    public Status sell(Member member, int hashCode) {
        Data data = member.getData();
        Optional<Item> itemOptional = data.getItemByHashCode(hashCode);
        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();
            boolean removed = false;

            switch (item.getType()) {
                case 0:
                case 1:
                    for (int i = 0; i < data.weapons.size(); i++) {
                        if (data.weapons.get(i).equals(item)) {
                            member.setMoney(member.getMoney() + data.weapons.get(i).priceToSell());
                            data.weapons.remove(i);
                            removed = true;
                            break;
                        }
                    }
                case 2:
                    for (int i = 0; i < data.items.size(); i++) {
                        if (data.items.get(i).equals(item)) {
                            member.setMoney(member.getMoney() + data.items.get(i).priceToSell());
                            data.items.remove(i);
                            removed = true;
                            break;
                        }
                    }
                case 3:
                    for (int i = 0; i < data.artifacts.size(); i++) {
                        if (data.artifacts.get(i).equals(item)) {
                            member.setMoney(member.getMoney() + data.artifacts.get(i).priceToSell());
                            data.artifacts.remove(i);
                            removed = true;
                            break;
                        }
                    }
                case 4:
                    for (int i = 0; i < data.armors.size(); i++) {
                        if (data.armors.get(i).equals(item)) {
                            member.setMoney(member.getMoney() + data.armors.get(i).priceToSell());
                            data.armors.remove(i);
                            removed = true;
                            break;
                        }
                    }
            }
            member.setData(data);
            if (removed)
                return new Status(true, "Предмет продан.");
            else
                return new Status(false, "Не удалось продать предмет.");
        } else
            return new Status(false, "Не удалось продать предмет.");
    }

    public boolean addMoney(int pdaId, int money) {
        Member member = mongoUsers.getById(pdaId);
        if (member != null) {
            member.setMoney(member.getMoney() + money);
            return true;
        } else return false;
    }

    public Status buy(Member member, int itemHash, int sellerId) {
        SellerDto seller = sellersService.getSellerDto(sellerId);
        Optional<Item> optionalItem = seller.getItemByHashCode(itemHash);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            if (member.buy(item.sellerPrice())) {
                member.setData(addItems(member.getData(), item));
                return new Status(true, "Покупка прошла успешно.");
            } else
                return new Status(false, "Недостаточно средств на счете.");

        } else
            return new Status(false, "У продавца отсутствует товар.");
    }

    public void addItem(Member member, int type, int id, int quantity) {
        Data data = member.getData();
        member.setData(addItems(data, new String[]{String.valueOf(type), String.valueOf(id), String.valueOf(quantity)}));
    }

    private Data addItems(Data data, String[] values) {
        try {
            if (values.length == 3) {
                int type = Integer.parseInt(values[0]);
                int id = Integer.parseInt(values[1]);
                int quantity = Integer.parseInt(values[2]);
                //log("try to add item, type: " + type + ", cid: " + id);
                Item item = types.getItem(type, id);
                item.setQuantity(quantity);
                return addItems(data, item);
            }
        } catch (Exception e) {
            error("Item err", e);
        }
        return data;
    }

    private Data addItems(Data data, Item item) {
        if (item != null) {
            if (item instanceof Weapon)
                return itemsManager.addWeapon(data, (Weapon) item, 1);
            else if (item instanceof Armor)
                return itemsManager.addArmor(data, (Armor) item, 1);
            else if (item instanceof Artifact)
                return itemsManager.addArtifact(data, (Artifact) item);
            else
                return itemsManager.addItem(data, item);
        } else {
            //log("item null");
            return data;
        }
    }

    private Data deleteItem(Data data, Item item) {
        if (item instanceof Weapon) {
            data.weapons.remove(item);
            return data;
        } else if (item instanceof Armor) {
            data.armors.remove(item);
            return data;
        } else if (item instanceof Artifact) {
            data.artifacts.remove(item);
            return data;
        } else
            data.items.remove(item);
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
