package net.artux.pdanetwork.authentication;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.models.profile.Equipment;
import net.artux.pdanetwork.models.profile.Story;
import net.artux.pdanetwork.models.profile.items.Armor;
import net.artux.pdanetwork.models.profile.items.Artifact;
import net.artux.pdanetwork.models.profile.items.Item;
import net.artux.pdanetwork.models.profile.items.Weapon;
import net.artux.pdanetwork.utills.Items;
import net.artux.pdanetwork.utills.Sellers;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static net.artux.pdanetwork.utills.ServletContext.mongoUsers;

public class UserManager {

    private Items items = new Items();

    public Member doUserActions(HashMap<String, List<String>> map, String token) {
        Member member = mongoUsers.getByToken(token);

        if (member != null)
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
                            // TODO remove items
                            for (String pass : map.get(key)) {
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
                        case "set":
                            for (String pass : map.get(key)) {
                                String[] vals = pass.split(":");
                                if (vals.length == 4) {
                                    if (vals[0].equals("story")) {
                                        boolean found = false;
                                        for (int i = 0; i < data.stories.size(); i++) {
                                            Story story = data.stories.get(i);
                                            if (story.getStoryId() == Integer.parseInt(vals[1])) {
                                                found = true;
                                                story.setLastChapter(Integer.parseInt(vals[2]));
                                                story.setLastStage(Integer.parseInt(vals[3]));
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
                            System.out.println("unsupported: " + key + "_" + map.get(key));
                            break;
                    }
                }
                member.setData(data);
                mongoUsers.updateMember(member);

                return member;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        else return null;
    }

    public Status setWeapon(String token, @NotNull Weapon item) {
        Member member = mongoUsers.getByToken(token);
        Equipment equipment = member.getData().getEquipment();
        Data data = member.getData();
        if (member.getData().getWeapons().contains(item)) {
            Weapon old;

            if (item.type == 0)
                old = equipment.getFirstWeapon();
            else
                old = equipment.getSecondWeapon();


            member.setData(
                    addItems(data, old));
            member.setData(
                    deleteItem(data, item));

            if (item.type == 0)
                equipment.setFirstWeapon(item);
            else
                equipment.setSecondWeapon(item);

            data.setEquipment(equipment);
            mongoUsers.updateMember(member);
            return new Status(true, "Success.");
        } else
            return new Status(false, "You don't have this item.");
    }

    public Status setArmor(String token, @NotNull Armor item) {
        Member member = mongoUsers.getByToken(token);
        Equipment equipment = member.getData().getEquipment();
        Data data = member.getData();
        if (member.getData().getArmors().contains(item)) {
            Armor old = equipment.getArmor();

            member.setData(
                    addItems(data, old));
            member.setData(
                    deleteItem(data, item));

            equipment.setArmor(item);
            data.setEquipment(equipment);
            mongoUsers.updateMember(member);
            return new Status(true, "Success.");
        } else
            return new Status(false, "You don't have this item.");
    }

    public Status sell(String token, Item item) {
        if (item != null) {
            Member member = mongoUsers.getByToken(token);
            Data data = member.getData();
            switch (item.type) {
                case 0:
                case 1:
                    for (int i = 0; i < data.weapons.size(); i++) {
                        if (data.weapons.get(i).equals(item)) {
                            member.setMoney(member.getMoney() + data.weapons.get(i).priceToSell());
                            data.weapons.remove(i);
                            break;
                        }
                    }
                case 2:
                    for (int i = 0; i < data.items.size(); i++) {
                        if (data.items.get(i).equals(item)) {
                            member.setMoney(member.getMoney() + data.items.get(i).priceToSell());
                            data.items.remove(i);
                            break;
                        }
                    }
                case 3:
                    for (int i = 0; i < data.artifacts.size(); i++) {
                        if (data.artifacts.get(i).equals(item)) {
                            member.setMoney(member.getMoney() + data.artifacts.get(i).priceToSell());
                            data.artifacts.remove(i);
                            break;
                        }
                    }
                case 4:
                    for (int i = 0; i < data.armors.size(); i++) {
                        if (data.armors.get(i).equals(item)) {
                            member.setMoney(member.getMoney() + data.armors.get(i).priceToSell());
                            data.armors.remove(i);
                            break;
                        }
                    }
            }
            member.setData(data);
            return new Status(true, "Предмет продан.");
        } else
            return new Status(false, "Неудалось продать предмет.");
    }

    public Status buy(String token, Item item, Sellers sellers, int sellerId) {
        Member member = mongoUsers.getByToken(token);
        if (sellers.getSeller(sellerId).getAllItems().contains(item)) {
            if (member.buy(item.priceToSell())) {
                member.setData(addItems(member.getData(), item));
                return new Status(true, "Покупка прошла успешно.");
            } else
                return new Status(false, "Недостаточно средств на счете.");

        } else
            return new Status(false, "У продавца отсутствует товар.");
    }

    public void addItem(int pdaId, int type, int id, int quantity) {
        Member member = mongoUsers.getById(pdaId);
        Data data = member.getData();
        member.setData(addItems(data, new String[]{String.valueOf(type), String.valueOf(id), String.valueOf(quantity)}));
        mongoUsers.updateMember(member);
    }

    private Data addItems(Data data, String[] values) {
        if (values.length == 3) {
            int type = Integer.parseInt(values[0]);
            int id = Integer.parseInt(values[1]);
            int quantity = Integer.parseInt(values[2]);
            Item item = items.getItem(type, id);
            item.quantity = quantity;
            return addItems(data, item);
        } else
            return data;
    }

    private Data addItems(Data data, Item item) {
        if (item != null) {
            if (item instanceof Weapon)
                return items.addWeapon(data, (Weapon) item, item.quantity);
            else if (item instanceof Armor)
                return items.addArmor(data, (Armor) item, item.quantity);
            else if (item instanceof Artifact)
                return items.addArtifact(data, (Artifact) item);
            else
                return items.addItem(data, item);
        } else
            return data;
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
}
