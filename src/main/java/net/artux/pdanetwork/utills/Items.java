package net.artux.pdanetwork.utills;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.models.profile.items.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static net.artux.pdanetwork.utills.FileGenerator.readFile;
import static net.artux.pdanetwork.utills.ServletContext.getPath;

public class Items {

    private static final Types types = new Types();

    static class Types {
        private final List<Armor> armors;
        private final List<Artifact> artifacts;
        private List<Detector> detectors;
        private final List<Weapon> rifles;
        private final List<Weapon> pistols;
        private final List<Item> items;

        /*
        0 - pistols
        1 - rifles
        2 - bullets
        3 - artifacts
        4 - armors
         */

        static Gson gson = new Gson();

        Types() {
            pistols = getType(0, Weapon.class);
            rifles = getType(1, Weapon.class);
            items = getType(2, Item.class);
            artifacts = getType(3, Artifact.class);
            armors = getType(4, Armor.class);
        }

        <T extends Item> List<T> getType(int type, Class c) {
            try {
                String commonFile = readFile(getPath() + "base/items/types/" + type, StandardCharsets.UTF_8);
                Type itemsListType = TypeToken.getParameterized(ArrayList.class, c).getType();
                return gson.fromJson(commonFile, itemsListType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public Item getItem(int type, int id) {
        switch (type) {
            case 0:
                return getPistol(id);
            case 1:
                return getRifle(id);
            case 2:
                return getItem(id);
            case 3:
                return getArtifact(id);
            case 4:
                return getArmor(id);

        }
        return null;
    }

    private Weapon getPistol(int id) {
        for (Weapon item : types.pistols) {
            if (item.id == id) {
                item.type = 0;
                return item;
            }
        }
        return null;
    }

    private Weapon getRifle(int id) {
        for (Weapon item : types.rifles) {
            if (item.id == id) {
                item.type = 1;
                return item;
            }
        }
        return null;
    }

    private Item getItem(int id) {
        for (Item item : types.items) {
            if (item.id == id) {
                item.type = 2;
                return item;
            }
        }
        return null;
    }

    private Armor getArmor(int id) {
        for (Armor item : types.armors) {
            if (item.id == id) {
                item.type = 4;
                return item;
            }
        }
        return null;
    }

    private Artifact getArtifact(int id) {
        for (Artifact item : types.artifacts) {
            if (item.id == id) {
                item.type = 3;
                return item;
            }
        }
        return null;
    }

    public Data addWeapon(Data data, Weapon weapon, int quantity) {
        weapon.quantity = quantity;
        for (int i = 0; i < quantity; i++) {
            data.weapons.add(weapon);
        }
        return data;
    }

    public Data addArmor(Data data, Armor armor, int quantity) {
        armor.quantity = quantity;
        for (int i = 0; i < quantity; i++) {
            data.armors.add(armor);
        }
        return data;
    }

    public Data addItem(Data data, Item item) {
        for (int i = 0; i < data.items.size(); i++) {
            Item item1 = data.items.get(i);
            if (item.equals(item1)) {
                item1.quantity += item.quantity;
                data.items.set(i, item1);
                return data;
            }
        }
        data.items.add(item);
        return data;
    }

    public Data addArtifact(Data data, Artifact item) {
        for (int i = 0; i < data.artifacts.size(); i++) {
            Artifact artifact1 = data.artifacts.get(i);
            if (item.equals(artifact1)) {
                artifact1.quantity += item.quantity;
                data.artifacts.set(i, artifact1);
                return data;
            }
        }
        data.artifacts.add(item);
        return data;
    }
}
