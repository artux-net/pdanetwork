package net.artux.pdanetwork.service.action;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.ParameterEntity;
import net.artux.pdanetwork.entity.user.StoryStateEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.entity.user.gang.GangRelationEntity;
import net.artux.pdanetwork.models.note.NoteCreateDto;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.models.user.gang.Gang;
import net.artux.pdanetwork.repository.user.GangRelationsRepository;
import net.artux.pdanetwork.repository.user.ParametersRepository;
import net.artux.pdanetwork.repository.user.StoryRepository;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.items.ItemService;
import net.artux.pdanetwork.service.note.NoteService;
import net.artux.pdanetwork.service.quest.QuestService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ActionService {

    private final Logger logger;

    private final StateService stateService;
    private final ItemService itemsService;
    private final NoteService noteService;
    private final QuestService questService;

    private final ParametersRepository parametersRepository;
    private final StoryRepository storyRepository;
    private final GangRelationsRepository gangRelationsRepository;
    private final UserRepository userRepository;

    public StoryData doUserActions(HashMap<String, List<String>> map, UserEntity userEntity) {
        operateActions(map, userEntity);
        userRepository.save(userEntity);
        return stateService.getStoryData();
    }

    private void operateActions(HashMap<String, List<String>> actions, UserEntity userEntity){
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
                                //todo remove type
                            } else
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
                            String[] values = pass.split(":");
                            if (values[0].contains("relation")) {
                                //"+":["relation_1:5"]
                                int group = Integer.parseInt(values[0].split("_")[1]);
                                GangRelationEntity gangRelation = gangRelationsRepository.findByUser(userEntity).orElseThrow();
                                gangRelation.addRelation(Gang.getById(group), Integer.parseInt(values[1]));
                                gangRelationsRepository.save(gangRelation);
                            } else
                                addValue(userEntity, values[0], Integer.parseInt(values[1]));
                        }
                        break;
                    case "-":
                        for (String pass : Objects.requireNonNull(actions.get(command))) {
                            String[] values = pass.split(":");
                            if (values[0].contains("relation")) {
                                int group = Integer.parseInt(values[0].split("_")[1]);
                                GangRelationEntity gangRelation = gangRelationsRepository.findByUser(userEntity).orElseThrow();
                                gangRelation.addRelation(Gang.getById(group), -Integer.parseInt(values[1]));
                                gangRelationsRepository.save(gangRelation);
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
                            userEntity.setMoney(0);
                            resetAllData(userEntity);
                        } else {
                            for (String pass : params)
                                if (pass.matches("-?\\d+")) {
                                    int storyId = Integer.parseInt(pass);
                                    Optional<StoryStateEntity> storyOptional = storyRepository.findByUserAndStoryId(storyId);
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
                        Optional<StoryStateEntity> storyOptional = storyRepository.findByUserAndCurrentIsTrue();
                        if (storyOptional.isPresent()) {
                            StoryStateEntity storyStateEntity = storyOptional.get();
                            storyStateEntity.setCurrent(false);
                            storyRepository.save(storyStateEntity);
                        }
                        break;
                    case "set":
                        for (String pass : params) {
                            String[] values = pass.split(":");
                            if (values.length == 4) {
                                if (values[0].equals("story")) {
                                    int story = Integer.parseInt(values[1]);
                                    int chapter = Integer.parseInt(values[2]);
                                    int stage = Integer.parseInt(values[3]);

                                    StoryStateEntity storyStateEntity;
                                    storyOptional = storyRepository.findByUserAndStoryId(story);
                                    if (storyOptional.isPresent()) {
                                        storyStateEntity = storyOptional.get();
                                    } else {
                                        storyStateEntity = new StoryStateEntity();
                                        storyStateEntity.setStoryId(story);
                                        storyStateEntity.setUser(userEntity);
                                    }
                                    storyStateEntity.setChapterId(chapter);
                                    storyStateEntity.setStageId(stage);
                                    storyStateEntity.setCurrent(true);
                                    storyRepository.save(storyStateEntity);
                                }
                            }
                        }
                        break;
                    case "state":
                        for (String pass : params) {
                            String[] values = pass.split(":");
                            if (values.length == 3) {
                                int story = Integer.parseInt(values[0]);
                                int chapter = Integer.parseInt(values[1]);
                                int stage = Integer.parseInt(values[2]);

                                StoryStateEntity storyStateEntity;
                                storyOptional = storyRepository.findByUserAndStoryId(story);
                                if (storyOptional.isPresent()) {
                                    storyStateEntity = storyOptional.get();
                                } else {
                                    storyStateEntity = new StoryStateEntity();
                                    storyStateEntity.setStoryId(story);
                                    storyStateEntity.setUser(userEntity);
                                }
                                storyStateEntity.setChapterId(chapter);
                                storyStateEntity.setStageId(stage);
                                storyStateEntity.setCurrent(true);
                                storyRepository.save(storyStateEntity);

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

    @Transactional
    public void resetAllData(UserEntity userEntity) {
        itemsService.resetAll(userEntity);
        parametersRepository.deleteAllByUser(userEntity);
        storyRepository.deleteAllByUser(userEntity);
    }
}
