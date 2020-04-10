package net.artux.pdanetwork.authentication;

import com.google.gson.Gson;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.utills.FileGenerator;
import net.artux.pdanetwork.utills.ServletContext;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class UserManager {

    private static boolean isInteger(String str) {
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

    public boolean doUserActions(HashMap<String, List<String>> map, String token) {
        FileGenerator fileGenerator = new FileGenerator();
        Data data = ServletContext.mongoUsers.getByToken(token).getData();
        for (String key : map.keySet()) {
            switch (key) {
                case "add":
                    for (String value: Objects.requireNonNull(map.get(key))) {
                        if(isInteger(value)) {
                            System.out.println("add_items");
                            data.getItems().add(fileGenerator.getItem(Integer.parseInt(value)));
                        } else if (value.contains(":")) {
                            System.out.println("add_value");
                            if(!data.params.values.containsKey(value.split(":")[0])){
                                data.params.values.put(value.split(":")[0], Integer.parseInt(value.split(":")[1]));
                            }
                        } else {
                            System.out.println("add_param");
                            if(!data.params.params.contains(value)) data.params.params.add(value);
                        }
                    }
                    break;
                case "add_param":
                    System.out.println("add_param");
                    data.params.params.addAll(Objects.requireNonNull(map.get(key)));
                    break;
                case "add_value":
                    System.out.println("add_value");
                    HashMap<String, Integer> addMap = new HashMap<>();
                    for (String name : Objects.requireNonNull(map.get(key))) {
                        addMap.put(name, 0);
                    }
                    data.params.values.putAll(addMap);
                    
                case "add_items":
                    for (String item_id : Objects.requireNonNull(map.get(key))) {
                        data.getItems().add(fileGenerator.getItem(Integer.parseInt(item_id)));
                    }


                case "remove":
                    for (String pass : Objects.requireNonNull(map.get(key))) {
                        try{
                            int id = Integer.parseInt(pass);
                            data.getItems().remove(fileGenerator.getItem(id));
                        } catch (NumberFormatException e) {
                            data.params.params.remove(pass);
                        }
                    }

                case "=":
                    for (String pass : Objects.requireNonNull(map.get(key))){
                        String[] vals = pass.split(":");
                        data.params.values.put(vals[0], data.params.values.get(Integer.parseInt(vals[1])));
                    }

                case "+":
                    for (String pass : Objects.requireNonNull(map.get(key))){
                        String[] vals = pass.split(":");
                        data.params.values.put(vals[0], data.params.values.get(data.params.values.get(vals[0] + Integer.parseInt(vals[1]))));
                    }

                case "-":
                    for (String pass : Objects.requireNonNull(map.get(key))){
                        String[] vals = pass.split(":");
                        data.params.values.put(vals[0], data.params.values.get(data.params.values.get(vals[0]) - Integer.parseInt(vals[1])));
                    }

                case "*":
                    for (String pass : Objects.requireNonNull(map.get(key))){
                        String[] vals = pass.split(":");
                        data.params.values.put(vals[0], data.params.values.get(data.params.values.get(vals[0]) * Integer.parseInt(vals[1])));
                    }
                    default:
                        System.out.println("nothing");
            }
        }

        System.out.println("UserManager" + new Gson().toJson(data));

        ServletContext.mongoUsers.changeField(token,"data", new Gson().toJson(data));

        return true;
    }

}
