package net.artux.pdanetwork.service.action;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.gang.Gang;
import net.artux.pdanetwork.models.gang.GangRelationEntity;
import net.artux.pdanetwork.models.profile.story.ParameterEntity;
import net.artux.pdanetwork.models.profile.story.StoryStateEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.repository.user.GangRelationsRepository;
import net.artux.pdanetwork.repository.user.ParametersRepository;
import net.artux.pdanetwork.repository.user.StoryRepository;
import net.artux.pdanetwork.service.ItemsService;
import net.artux.pdanetwork.service.items.SellerService;
import net.artux.pdanetwork.service.note.NoteService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActionService {

    private final Logger logger;

    private final StateService stateService;
    private final ItemsService itemsService;
    private final SellerService sellerService;
    private final NoteService noteService;

    private final ParametersRepository parametersRepository;
    private final StoryRepository storyRepository;
    private final GangRelationsRepository gangRelationsRepository;

    public StoryData doUserActions(HashMap<String, List<String>> map, UserEntity userEntity) {
        for (String command : map.keySet()) {
            try {
                List<String> params = map.get(command);
                switch (command) {
                    case "add":
                        for (String value : params) {
                            String[] param = value.split(":");
                            if (param.length == 3) {
                                int type = Integer.parseInt(param[0]);
                                int baseId = Integer.parseInt(param[1]);
                                int quantity = Integer.parseInt(param[2]);
                                itemsService.addItem(type, baseId, quantity);
                            } else if (param.length == 2) {
                                //add_value
                                String[] values = value.split(":");
                                addValue(userEntity, values[0], Integer.parseInt(value.split(":")[1]));
                            } else {
                                //add_param
                                parametersRepository.save(new ParameterEntity(userEntity, value, 0));
                                addKey(userEntity, value);
                            }
                        }
                        break;
                    case "add_param":
                        for (String value : params) {
                            addKey(userEntity, value);
                        }
                        break;
                    case "add_value":
                        for (String value : params) {
                            String[] values = value.split(":");
                            addValue(userEntity, values[0], Integer.valueOf(values[1]));
                        }
                        break;
                    case "add_items":
                        for (String value : params) {
                            String[] values = value.split(":");
                            if (values.length == 3) {
                                int type = Integer.parseInt(values[0]);
                                int baseId = Integer.parseInt(values[1]);
                                int quantity = Integer.parseInt(values[2]);
                                itemsService.addItem(type, baseId, quantity);
                            }
                        }
                        break;
                    case "remove":
                        for (String pass : params) {
                            String[] values = pass.split(":");
                            if (values.length == 3) {
                                int type = Integer.parseInt(values[0]);
                                int baseId = Integer.parseInt(values[1]);
                                int quantity = Integer.parseInt(values[2]);
                                itemsService.deleteItem(type, baseId, quantity);
                            }
                            parametersRepository.deleteAllByUserAndKey(userEntity, pass);
                        }
                        break;
                    case "=":
                        for (String pass : params) {
                            String[] values = pass.split(":");
                            setValue(userEntity, values[0], Integer.valueOf(values[1]));
                        }
                        break;
                    case "+":
                        for (String pass : params) {
                            String[] vals = pass.split(":");
                            if (vals[0].contains("relation")) {
                                //"+":["relation_1:5"]
                                int group = Integer.parseInt(vals[0].split("_")[1]);
                                GangRelationEntity gangRelation = gangRelationsRepository.findByUser(userEntity).orElseThrow();
                                gangRelation.addRelation(Gang.getById(group), Integer.parseInt(vals[1]));
                                gangRelationsRepository.save(gangRelation);
                            } else
                                addValue(userEntity, vals[0], Integer.parseInt(vals[1]));
                        }
                        break;
                    case "-":
                        for (String pass : Objects.requireNonNull(map.get(command))) {
                            String[] vals = pass.split(":");
                            if (vals[0].contains("relation")) {
                                int group = Integer.parseInt(vals[0].split("_")[1]);
                                GangRelationEntity gangRelation = gangRelationsRepository.findByUser(userEntity).orElseThrow();
                                gangRelation.addRelation(Gang.getById(group), -Integer.parseInt(vals[1]));
                                gangRelationsRepository.save(gangRelation);
                            } else
                                addValue(userEntity, vals[0], -Integer.parseInt(vals[1]));
                        }
                        break;
                    case "*":
                        for (String pass : params) {
                            String[] values = pass.split(":");
                            multiplyValue(userEntity, values[0], Integer.parseInt(values[1]));
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
                        if (params.size() == 2)
                            noteService.createNote(params.get(0), params.get(1));
                        else if (params.size() == 1)
                            noteService.createNote("Новая заметка", params.get(0));
                        break;
                    case "achieve":
                        for (String pass : params)
                            //userEntity.achievements.add(Integer.parseInt(pass));
                            break;
                    case "reset":
                        if (map.get(command).size() == 0) {
                            userEntity.setMoney(0);
                            itemsService.resetAll();
                        } else {
                            for (String pass : params)
                                if (pass.matches("-?\\d+")) {
                                    int storyId = Integer.parseInt(pass);
                                    Optional<StoryStateEntity> storyOptional = storyRepository.findByPlayerAndStoryId(userEntity, storyId);
                                    if (storyOptional.isPresent()) {
                                        StoryStateEntity storyStateEntity = storyOptional.get();
                                        storyStateEntity.setChapterId(1);
                                        storyStateEntity.setStageId(0);
                                        storyRepository.save(storyStateEntity);
                                    }
                                } else if (pass.contains("relation")) {
                                    int group = Integer.parseInt(pass.split("_")[1]);
                                    GangRelationEntity gangRelation = gangRelationsRepository.findByUser(userEntity).orElseThrow();
                                    gangRelation.setRelation(Gang.getById(group), 0);
                                    gangRelationsRepository.save(gangRelation);
                                }
                        }
                        break;
                    case "reset_current":
                        Optional<StoryStateEntity> storyOptional = storyRepository.findByPlayerAndCurrentIsTrue(userEntity);
                        if (storyOptional.isPresent()) {
                            StoryStateEntity storyStateEntity = storyOptional.get();
                            storyStateEntity.setCurrent(false);
                            storyRepository.save(storyStateEntity);
                        }
                        break;
                    case "set":
                        for (String pass : params) {
                            String[] vals = pass.split(":");
                            if (vals.length == 4) {
                                if (vals[0].equals("story")) {
                                    int story = Integer.parseInt(vals[1]);
                                    int chapter = Integer.parseInt(vals[2]);
                                    int stage = Integer.parseInt(vals[3]);

                                    StoryStateEntity storyStateEntity;
                                    storyOptional = storyRepository.findByPlayerAndStoryId(userEntity, story);
                                    if (storyOptional.isPresent()) {
                                        storyStateEntity = storyOptional.get();
                                    } else {
                                        storyStateEntity = new StoryStateEntity();
                                        storyStateEntity.setStoryId(story);
                                    }
                                    storyStateEntity.setChapterId(chapter);
                                    storyStateEntity.setStageId(stage);
                                    storyStateEntity.setCurrent(true);
                                    storyRepository.save(storyStateEntity);
                                }
                            }
                        }
                        break;
                    default:
                        logger.error("Unsupported operation: " + command + ", value: " + params);
                        break;
                }
            } catch (Exception e) {
                logger.error("ActionService", e);
            }
        }
        return stateService.getStoryData(userEntity);

    }

    public void multiplyValue(UserEntity user, String key, Integer integer) {
        var s = parametersRepository.getParameterEntityByUserAndKey(user, key);
        ParameterEntity entity;
        if (s.isPresent()) {
            entity = s.get();
            entity.value *= integer;
        } else {
            entity = new ParameterEntity(user, key, integer);
        }
        parametersRepository.save(entity);
    }

    public void setValue(UserEntity user, String key, Integer integer) {
        var s = parametersRepository.getParameterEntityByUserAndKey(user, key);
        ParameterEntity entity;
        if (s.isPresent()) {
            entity = s.get();
            entity.value = integer;
        } else {
            entity = new ParameterEntity(user, key, integer);
        }
        parametersRepository.save(entity);
    }

    public void addValue(UserEntity user, String key, Integer integer) {
        var s = parametersRepository.getParameterEntityByUserAndKey(user, key);
        ParameterEntity entity;
        if (s.isPresent()) {
            entity = s.get();
            entity.value += integer;
        } else {
            entity = new ParameterEntity(user, key, integer);
        }
        parametersRepository.save(entity);
    }

    public void addKey(UserEntity user, String key) {
        if (!parametersRepository.existsParameterEntityByUserAndKeyEquals(user, key))
            parametersRepository.save(new ParameterEntity(user, key, 0));
    }
/*
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
*/

   /* public boolean addMoney(int pdaId, int money) {
    Member member = mongoUsers.getById(pdaId);
        if(member !=null)

    {
        member.setMoney(member.getMoney() + money);
        return true;
    } else return false;
}*//*

public Status buy(UserEntity userEntity,int itemHash,int sellerId){
        SellerDto seller=sellersService.getSellerDto(sellerId);
        Optional<ItemEntity> optionalItem=seller.getItemByHashCode(itemHash);
        if(optionalItem.isPresent()){
        ItemEntity itemEntity=optionalItem.get();
        if(userEntity.buy(itemEntity.sellerPrice())){
        userEntity.setData(addItems(userEntity.getData(),itemEntity));
        return new Status(true,"Покупка прошла успешно.");
        }else
        return new Status(false,"Недостаточно средств на счете.");

        }else
        return new Status(false,"У продавца отсутствует товар.");
        }

public void addItem(UserEntity userEntity,int type,int id,int quantity){
        Data data=userEntity.getData();
        userEntity.setData(addItems(data,new String[]{String.valueOf(type),String.valueOf(id),String.valueOf(quantity)}));
        }

private Data addItems(Data data,String[]values){
        try{
        if(values.length==3){
        int type=Integer.parseInt(values[0]);
        int id=Integer.parseInt(values[1]);
        int quantity=Integer.parseInt(values[2]);
        //log("try to add item, type: " + type + ", cid: " + id);
        ItemEntity itemEntity=types.getItem(type,id);
        itemEntity.setQuantity(quantity);
        if(quantity<=0){
        return removeItems(data,values);
        }else
        return addItems(data,itemEntity);
        }
        }catch(Exception e){
        error("Item err",e);
        }
        return data;
        }

private boolean isCorrect(ItemEntity itemEntity,int type,int id){
        if(itemEntity!=null)
        if(itemEntity.getType()==type&&itemEntity.getId()==id)
        return true;

        return false;
        }

private List<?extends ItemEntity> removeItemsFromList(List<?extends ItemEntity> items,int type,int id,int quantity){
        Iterator<?extends ItemEntity> i=items.iterator();
        while(i.hasNext()){
        ItemEntity w=i.next();
        if(isCorrect(w,type,id))
        if(w.getQuantity()>=quantity)
        i.remove();
        else
        w.setQuantity(w.getQuantity()-quantity);
        }
        return items;
        }

private Data removeItems(Data data,String[]values){
        try{
        if(values.length==3){
        int type=Integer.parseInt(values[0]);
        int id=Integer.parseInt(values[1]);
        int quantity=Integer.parseInt(values[2]);
        //log("try to add item, type: " + type + ", cid: " + id);

        if(isCorrect(data.getEquipment().getFirstWeapon(),type,id)){
        data.getEquipment().setFirstWeapon(null);
        }

        if(isCorrect(data.getEquipment().getSecondWeapon(),type,id)){
        data.getEquipment().setSecondWeapon(null);
        }

        if(isCorrect(data.getEquipment().getArmor(),type,id)){
        data.getEquipment().setArmor(null);
        }

        data.weaponEntities=(List<WeaponEntity>)removeItemsFromList(data.weaponEntities,type,id,quantity);
        data.armorEntities=(List<ArmorEntity>)removeItemsFromList(data.armorEntities,type,id,quantity);
        data.itemEntities=(List<ItemEntity>)removeItemsFromList(data.itemEntities,type,id,quantity);
        data.artifactEntities=(List<ArtifactEntity>)removeItemsFromList(data.artifactEntities,type,id,quantity);
        //TODO MORE

        return data;
        }
        }catch(Exception e){
        error("Item err",e);
        }
        return data;
        }

private Data addItems(Data data,ItemEntity itemEntity){
        if(itemEntity!=null){
        if(itemEntity instanceof WeaponEntity)
        return itemsManager.addWeapon(data,(WeaponEntity)itemEntity,1);
        else if(itemEntity instanceof ArmorEntity)
        return itemsManager.addArmor(data,(ArmorEntity)itemEntity,1);
        else if(itemEntity instanceof ArtifactEntity)
        return itemsManager.addArtifact(data,(ArtifactEntity)itemEntity);
        else
        return itemsManager.addItem(data,itemEntity);
        }else{
        //log("item null");
        return data;
        }
        }

private Data deleteItem(Data data,ItemEntity itemEntity){
        if(itemEntity instanceof WeaponEntity){
        data.weaponEntities.remove(itemEntity);
        return data;
        }else if(itemEntity instanceof ArmorEntity){
        data.armorEntities.remove(itemEntity);
        return data;
        }else if(itemEntity instanceof ArtifactEntity){
        data.artifactEntities.remove(itemEntity);
        return data;
        }else
        data.itemEntities.remove(itemEntity);
        return data;
        }

public static boolean isInteger(String str){
        if(str==null){
        return false;
        }
        int length=str.length();
        if(length==0){
        return false;
        }
        int i=0;
        if(str.charAt(0)=='-'){
        if(length==1){
        return false;
        }
        i=1;
        }
        for(;i<length; i++){
        char c=str.charAt(i);
        if(c< '0'||c>'9'){
        return false;
        }
        }
        return true;
        }*/
}
