package net.artux.pdanetwork.service.action;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.*;
import net.artux.pdanetwork.entity.user.ParameterEntity;
import net.artux.pdanetwork.entity.user.StoryStateEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.entity.user.gang.GangRelationEntity;
import net.artux.pdanetwork.models.note.NoteCreateDto;
import net.artux.pdanetwork.models.quest.Checkpoint;
import net.artux.pdanetwork.models.quest.Mission;
import net.artux.pdanetwork.models.quest.Stage;
import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.models.security.SecurityUser;
import net.artux.pdanetwork.models.story.StoryMapper;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.models.user.gang.Gang;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.items.ItemService;
import net.artux.pdanetwork.service.note.NoteService;
import net.artux.pdanetwork.service.quest.QuestService;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;

@Service
@Transactional
@RequiredArgsConstructor
public class ActionService {

    private final Logger logger;

    private final ItemService itemsService;
    private final QuestService questService;
    private final NoteService noteService;
    private final StoryMapper storyMapper;

    private final UserRepository userRepository;

    private final Timer timer = new Timer();

    public StoryData doUserActions(UUID id, HashMap<String, List<String>> map) {
        UserEntity userEntity = userRepository.getById(id);
        logger.info("Start actions for {}", userEntity.getLogin());
        operateActions(map, userEntity);
        userEntity.fixAllItems();
        return storyMapper.storyData(userRepository.save(userEntity));
    }

    public StoryData doUserActions(HashMap<String, List<String>> map) {
        UUID id = ((SecurityUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getId();
        return doUserActions(id, map);
    }

    protected void operateActions(HashMap<String, List<String>> actions, UserEntity userEntity) {
        if (actions == null)
            return;

        for (String command : actions.keySet()) {
            try {
                List<String> params = actions.get(command);
                Optional<String> timerParam = params.stream().filter(s -> s.toLowerCase().contains("timer:")).findFirst();
                if (timerParam.isPresent()) {
                    int minutes = Integer.parseInt(timerParam.get().substring(6));
                    params.remove(timerParam.get());
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            doCommand(command, params, userEntity);
                        }
                    }, 1000L * 60 * minutes);
                } else
                    doCommand(command, params, userEntity);
            } catch (Exception e) {
                logger.error("ActionService", e);
            }
        }
    }

    public void doCommand(String command, List<String> params, UserEntity userEntity) {
        switch (command) {
            case "add":
                for (String value : params) {
                    String[] param = value.split(":");
                    if (param[0].matches("[0-9]+[\\\\.]?[0-9]*")) {
                        long baseId = Long.parseLong(param[0]);
                        int quantity = Integer.parseInt(param[1]);
                        itemsService.addItem(userEntity, baseId, quantity);
                    } else if (param.length == 2) {
                        //add_value
                        addValue(userEntity, param[0], Integer.parseInt(param[1]));
                    } else {
                        //add_param
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
                    if (values.length == 2) {
                        long baseId = Long.parseLong(values[0]);
                        int quantity = Integer.parseInt(values[1]);
                        itemsService.addItem(userEntity, baseId, quantity);
                    }
                }
                break;
            case "remove":
                for (String pass : params) {
                    String[] values = pass.split(":");
                    if (values.length == 2) {
                        int baseId = Integer.parseInt(values[0]);
                        int quantity = Integer.parseInt(values[1]);
                        itemsService.deleteItem(userEntity, baseId, quantity);
                    } else
                        userEntity.getParameters()
                                .removeIf(parameterEntity -> parameterEntity.getKey().equals(pass));
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
                    String[] values = pass.split(":");
                    if (values[0].contains("relation")) {
                        //"+":["relation_1:5"]
                        int group = Integer.parseInt(values[0].split("_")[1]);
                        GangRelationEntity gangRelation = userEntity.getGangRelation();
                        Gang gang = Gang.getById(group);
                        if (gang != null)
                            gangRelation.addRelation(gang, Integer.parseInt(values[1]));
                    } else
                        addValue(userEntity, values[0], Integer.parseInt(values[1]));
                }
                break;
            case "-":
                for (String pass : params) {
                    String[] values = pass.split(":");
                    if (values[0].contains("relation")) {
                        int group = Integer.parseInt(values[0].split("_")[1]);
                        GangRelationEntity gangRelation = userEntity.getGangRelation();
                        Gang gang = Gang.getById(group);
                        if (gang != null)
                            gangRelation.addRelation(gang, -Integer.parseInt(values[1]));
                    } else
                        addValue(userEntity, values[0], -Integer.parseInt(values[1]));
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
            case "item": {
                Set<? extends ItemEntity> items = userEntity.getAllItems();
                Map<UUID, Integer> quantityMap = new HashMap<>();
                for (String pass : params) {
                    String[] key = pass.split(":");
                    UUID id = UUID.fromString(key[0]);
                    int quantity = Integer.parseInt(key[1]);
                    quantityMap.put(id, quantity);
                }
                if (quantityMap.size() > 0) {
                    items.forEach((Consumer<ItemEntity>) itemEntity -> {
                        if (quantityMap.containsKey(itemEntity.getId())) {
                            itemEntity.setQuantity(quantityMap.get(itemEntity.getId()));
                            logger.debug("Set quantity for " +
                                    itemEntity.getId().toString() + ", " + itemEntity.getQuantity());
                        }
                    });
                }
            }
            break;
            case "item_condition": {
                Map<UUID, Float> conditionMap = new HashMap<>();
                for (String pass : params) {
                    String[] key = pass.split(":");
                    UUID id = UUID.fromString(key[0]);
                    float quantity = Float.parseFloat(key[1]);
                    conditionMap.put(id, quantity);
                }
                if (conditionMap.size() > 0) {
                    Set<? extends ConditionalEntity> items = userEntity.getArmors();

                    items.forEach((Consumer<ConditionalEntity>) itemEntity -> {
                        if (conditionMap.containsKey(itemEntity.getId())) {
                            itemEntity.setCondition(conditionMap.get(itemEntity.getId()));
                            logger.debug("Set condition for " +
                                    itemEntity.getId().toString() + ", " + itemEntity.getQuantity());
                        }
                    });
                    items = userEntity.getWeapons();

                    items.forEach((Consumer<ConditionalEntity>) itemEntity -> {
                        if (conditionMap.containsKey(itemEntity.getId())) {
                            itemEntity.setCondition(conditionMap.get(itemEntity.getId()));
                            logger.debug("Set condition for " +
                                    itemEntity.getId().toString() + ", " + itemEntity.getQuantity());
                        }
                    });
                }
            }
            break;

            case "xp":
                for (String pass : params)
                    userEntity.xp(Integer.parseInt(pass));
                break;

            case "set":
                for (String pass : params) {
                    itemsService.setWearable(userEntity, UUID.fromString(pass));
                }
                break;
            case "note":
                if (params.size() == 2)
                    noteService.createNote(new NoteCreateDto(params.get(0), params.get(1)));
                else if (params.size() == 1)
                    noteService.createNote(new NoteCreateDto("Новая заметка", params.get(0)));
                break;
            case "achieve":
                //TODO
                //userEntity.achievements.add(Integer.parseInt(pass));
                break;
            case "reset":
                if (params.size() == 0) {
                    userEntity.reset();
                } else {
                    for (String pass : params)
                        if (pass.matches("-?\\d+")) {
                            int storyId = Integer.parseInt(pass);
                            StoryStateEntity storyOptional = userEntity.getStoryState(storyId);
                            if (storyOptional != null) {
                                storyOptional.setChapterId(1);
                                storyOptional.setStageId(0);
                                storyOptional.setOver(false);
                            }
                        } else if (pass.contains("relation")) {
                            int group = Integer.parseInt(pass.split("_")[1]);
                            GangRelationEntity gangRelation = userEntity.getGangRelation();
                            Gang gang = Gang.getById(group);
                            if (gang != null)
                                gangRelation.addRelation(gang, 0);
                        } else if (pass.contains("wearable")) {
                            for (WearableEntity wearable : userEntity.getWearableItems()) {
                                wearable.setEquipped(false);
                            }
                        } else if (pass.contains("weapons")) {
                            for (WeaponEntity wearable : userEntity.getItemsByClass(WeaponEntity.class)) {
                                wearable.setEquipped(false);
                            }
                        } else if (pass.contains("armors")) {
                            for (ArmorEntity wearable : userEntity.getItemsByClass(ArmorEntity.class)) {
                                wearable.setEquipped(false);
                            }
                        } else if (pass.contains("items")) {
                            userEntity.resetItems();
                        }
                }
                break;
            case "exitStory":
                exitStory(userEntity);
                break;
            case "check": {
                int storyId = userEntity.getCurrentStoryState().getStoryId();
                Story story = questService.getStory(storyId);
                for (String param : params) {
                    Mission currentMission = story.getMissionByParam(param);
                    if (currentMission != null) {
                        Checkpoint checkPoint = currentMission.getNextCheckpoint(param);
                        addKey(userEntity, checkPoint.getParameter());
                        userEntity.getParameters()
                                .removeIf(parameterEntity -> parameterEntity.getKey().equals(param));
                    } else
                        logger.error("Check failed, param " + param + " is not within any of missions");
                }
            }
            break;
            case "finishStory":
                finishStory(userEntity);
                break;
            case "state": {
                String[] states = params.toArray(new String[0]);
                if (states.length > 0) {
                    String[] values = states[0].split(":");
                    if (values.length == 3) {
                        int story = Integer.parseInt(values[0]);
                        int chapter = Integer.parseInt(values[2]);
                        int stage = Integer.parseInt(values[2]);

                        StoryStateEntity storyStateEntity = userEntity.getStoryState(story);
                        if (storyStateEntity == null) {
                            storyStateEntity = new StoryStateEntity();
                            storyStateEntity.setStoryId(story);
                            storyStateEntity.setChapterId(chapter);
                            storyStateEntity.setStageId(stage);
                            storyStateEntity.setUser(userEntity);
                            userEntity.getStoryStates().add(storyStateEntity);
                        } else {
                            Stage actualStage = questService.getStage(storyStateEntity.getStoryId(),
                                    storyStateEntity.getChapterId(),
                                    storyStateEntity.getStageId());

                            if (actualStage.getTransfers() != null && actualStage.getTransfers().size() > 0) {
                                int finalStage = stage;
                                boolean checkStart = actualStage.getTransfers().stream()
                                        .filter(transfer -> transfer.getStage() == finalStage)
                                        .toList()
                                        .size() > 0;
                                if (!checkStart)
                                    throw new RuntimeException();
                            }
                        }
                        storyStateEntity.setCurrent(true);

                        for (String state : states) {
                            values = state.split(":");
                            chapter = Integer.parseInt(values[1]);
                            stage = Integer.parseInt(values[2]);

                            storyStateEntity.setChapterId(chapter);
                            storyStateEntity.setStageId(stage);

                            logger.info("Process actions for {},{},{}", story, chapter, stage);
                            operateActions(questService.getActionsOfStage(story, chapter, stage), userEntity);
                        }
                    }
                }
            }
            break;
            default:
                logger.error("Unsupported operation: " + command + ", value: " + params);
                break;
        }
    }

    public void finishStory(UserEntity userEntity){
        userEntity.getCurrentStoryState().setOver(true);
        exitStory(userEntity);
    }

    public void exitStory(UserEntity userEntity){
        StoryStateEntity storyOptional = userEntity.getCurrentStoryState();
        if (storyOptional != null) {
            storyOptional.setCurrent(false);
        }
    }

    public void multiplyValue(UserEntity user, String key, Integer integer) {
        var s = user.getParameters()
                .stream()
                .filter(parameterEntity -> parameterEntity.getKey().equals(key))
                .findFirst();

        ParameterEntity entity;
        if (s.isPresent()) {
            entity = s.get();
            entity.value *= integer;
        } else {
            entity = new ParameterEntity(user, key, integer);
            user.getParameters().add(entity);
        }
    }

    public void setValue(UserEntity user, String key, Integer integer) {
        var s = user.getParameters()
                .stream()
                .filter(parameterEntity -> parameterEntity.getKey().equals(key))
                .findFirst();
        ParameterEntity entity;
        if (s.isPresent()) {
            entity = s.get();
            entity.value = integer;
        } else {
            entity = new ParameterEntity(user, key, integer);
            user.getParameters().add(entity);
        }
    }

    public void addValue(UserEntity user, String key, Integer integer) {
        var s = user.getParameters()
                .stream()
                .filter(parameterEntity -> parameterEntity.getKey().equals(key))
                .findFirst();
        ParameterEntity entity;
        if (s.isPresent()) {
            entity = s.get();
            entity.value += integer;
        } else {
            entity = new ParameterEntity(user, key, integer);
            user.getParameters().add(entity);
        }
    }

    public void addKey(UserEntity user, String key) {
        var s = user.getParameters()
                .stream()
                .filter(parameterEntity -> parameterEntity.getKey().equals(key))
                .findFirst();
        if (s.isEmpty()) {
            user.getParameters().add(new ParameterEntity(user, key, 0));
        }
    }

}
