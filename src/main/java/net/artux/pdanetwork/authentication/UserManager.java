package net.artux.pdanetwork.authentication;

import com.google.gson.Gson;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.models.profile.Story;
import net.artux.pdanetwork.models.profile.items.Item;
import net.artux.pdanetwork.utills.FileGenerator;
import net.artux.pdanetwork.utills.ServletContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class UserManager {

    private Gson gson = new Gson();

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

    public Member doUserActions(HashMap<String, List<String>> map, String token) {
        FileGenerator fileGenerator = new FileGenerator();

        Member member = ServletContext.mongoUsers.getByToken(token);
        if (member!=null)
        try {
            Data data = member.getData(gson);
            for (String key : map.keySet()) {
                switch (key) {
                    case "add":
                        for (String value : map.get(key)) {
                            if (isInteger(value)) {
                                //add_items
                                data.getItems().add(fileGenerator.getItem(Integer.parseInt(value)));
                            } else if (value.contains(":")) {
                                //add_value
                                if (!data.params.values.containsKey(value.split(":")[0])) {
                                    data.params.values.put(value.split(":")[0], Integer.parseInt(value.split(":")[1]));
                                }
                            } else {
                                //add_param
                                if (!data.params.params.contains(value)) data.params.params.add(value);
                            }
                        }
                        break;
                    case "add_param":
                        for (String value : map.get(key)) {
                            if (!data.params.params.contains(value)) data.params.params.add(value);
                        }
                        break;
                    case "add_value":
                        for (String value : map.get(key)) {
                            if (!data.params.values.containsKey(value.split(":")[0])) {
                                data.params.values.put(value.split(":")[0], Integer.parseInt(value.split(":")[1]));
                            } else {
                                data.params.
                                        values.put(value.split(":")[0],
                                        data.params.values.get(value.split(":")[0]) + Integer.parseInt(value.split(":")[1]));
                            }
                        }
                        break;
                    case "add_items":
                        for (String item_id : map.get(key)) {
                            data.getItems().add(fileGenerator.getItem(Integer.parseInt(item_id)));
                        }
                        break;
                    case "remove":
                        for (String pass : map.get(key)) {
                            if (isInteger(pass)) {
                                int id = Integer.parseInt(pass);
                                data.getItems().remove(fileGenerator.getItem(id));
                            } else {
                                data.params.params.remove(pass);
                                data.params.values.remove(pass);
                            }
                        }
                        break;
                    case "=":
                        for (String pass : map.get(key)) {
                            String[] vals = pass.split(":");
                            data.params.values.put(vals[0], Integer.valueOf(vals[1]));
                        }
                        break;
                    case "+":
                        for (String pass : map.get(key)) {
                            String[] vals = pass.split(":");
                            data.params.values.put(vals[0], data.params.values.get(vals[0]) + Integer.valueOf(vals[1]));
                        }
                        break;
                    case "-":
                        for (String pass : Objects.requireNonNull(map.get(key))) {
                            String[] vals = pass.split(":");
                            data.params.values.put(vals[0], data.params.values.get(vals[0]) - Integer.parseInt(vals[1]));
                        }
                        break;
                    case "*":
                        for (String pass : map.get(key)) {
                            String[] vals = pass.split(":");
                            data.params.values.put(vals[0], data.params.values.get(vals[0]) * Integer.parseInt(vals[1]));
                        }
                        break;
                    case "set":
                        for (String pass : map.get(key)) {
                            String[] vals = pass.split(":");
                            if (vals.length==2) {
                                if (vals[0].equals("type0"))
                                    for (Item item : data.getItems()) {
                                        if (item.id == Integer.parseInt(vals[1])) {
                                            if (item.type == 0) {
                                                /*Item old = data.getEquipment().getType0();
                                                data.getItems().add(old);
                                                data.getEquipment().setType0((Type0) item);
                                                data.getItems().remove(item);*/
                                            } else {
                                                System.out.println("Wrong type");
                                            }
                                        }
                                    }
                            } else if (vals.length==4)
                            if (vals[0].equals("story")){
                                boolean found = false;
                                for (Story story : data.getStories()){
                                    if(story.getId() == Integer.parseInt(vals[1])){
                                        found = true;
                                        story.setLastChapter(Integer.parseInt(vals[2]));
                                        story.setLastStage(Integer.parseInt(vals[3]));
                                    }
                                }
                                if (!found){
                                    Story  story = new Story(Arrays.copyOfRange(vals, 1, 4));
                                    data.getStories().add(story);
                                }
                                data.getTemp().put("currentStory", vals[1]);
                            }
                        }
                        break;
                    default:
                        System.out.println("unsupported: " + key + "_" + map.get(key));
                        break;
                }
            }

            ServletContext.mongoUsers.changeField(token, "data", new Gson().toJson(data));
            member.setData(new Gson().toJson(data));
            return member;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        else return null;
    }

}
