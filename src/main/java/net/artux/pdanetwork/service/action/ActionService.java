package net.artux.pdanetwork.service.action;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.ParameterEntity;
import net.artux.pdanetwork.entity.user.StoryStateEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.entity.user.gang.GangRelationEntity;
import net.artux.pdanetwork.models.note.NoteCreateDto;
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

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    public StoryData doUserActions(HashMap<String, List<String>> map) {
        UUID id = ((SecurityUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getId();
        UserEntity userEntity = userRepository.getById(id);
        logger.info("Start actions for {}", userEntity.getLogin());
        operateActions(map, userEntity);
        return storyMapper.storyData(userRepository.save(userEntity));
    }

    protected void operateActions(HashMap<String, List<String>> actions, UserEntity userEntity) {
        if (actions == null)
            return;

        for (String command : actions.keySet()) {
            try {
                List<String> params = actions.get(command);
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
                                gangRelation.addRelation(Gang.getById(group), Integer.parseInt(values[1]));
                            } else
                                addValue(userEntity, values[0], Integer.parseInt(values[1]));
                        }
                        break;
                    case "-":
                        for (String pass : Objects.requireNonNull(actions.get(command))) {
                            String[] values = pass.split(":");
                            if (values[0].contains("relation")) {
                                int group = Integer.parseInt(values[0].split("_")[1]);
                                GangRelationEntity gangRelation = userEntity.getGangRelation();
                                gangRelation.addRelation(Gang.getById(group), -Integer.parseInt(values[1]));
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
                    case "xp":
                        for (String pass : params)
                            userEntity.xp(Integer.parseInt(pass));
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
                        if (actions.get(command).size() == 0) {
                            userEntity.reset();
                        } else {
                            for (String pass : params)
                                if (pass.matches("-?\\d+")) {
                                    int storyId = Integer.parseInt(pass);
                                    StoryStateEntity storyOptional = userEntity.getStoryState(storyId);
                                    if (storyOptional != null) {
                                        storyOptional.setChapterId(1);
                                        storyOptional.setStageId(0);
                                    }
                                } else if (pass.contains("relation")) {
                                    int group = Integer.parseInt(pass.split("_")[1]);
                                    GangRelationEntity gangRelation = userEntity.getGangRelation();
                                    gangRelation.setRelation(Gang.getById(group), 0);
                                }
                        }
                        break;
                    case "reset_current":
                        StoryStateEntity storyOptional = userEntity.getCurrentStoryState();
                        if (storyOptional != null) {
                            storyOptional.setCurrent(false);
                        }
                        break;
                    case "state":
                        for (String pass : params) {
                            String[] values = pass.split(":");
                            if (values.length == 3) {
                                int story = Integer.parseInt(values[0]);
                                int chapter = Integer.parseInt(values[1]);
                                int stage = Integer.parseInt(values[2]);

                                StoryStateEntity storyStateEntity = userEntity.getStoryState(story);
                                if (storyStateEntity == null) {
                                    storyStateEntity = new StoryStateEntity();
                                    storyStateEntity.setStoryId(story);
                                    storyStateEntity.setUser(userEntity);
                                    userEntity.getStoryStates().add(storyStateEntity);
                                }
                                storyStateEntity.setChapterId(chapter);
                                storyStateEntity.setStageId(stage);
                                storyStateEntity.setCurrent(true);

                                logger.info("Process actions for {},{},{}", story, chapter, stage);
                                operateActions(questService.getActionsOfStage(story, chapter, stage), userEntity);
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
