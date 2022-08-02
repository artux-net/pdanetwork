package net.artux.pdanetwork.service.action;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.items.ItemMapper;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.models.story.StoryMapper;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.repository.user.ParametersRepository;
import net.artux.pdanetwork.repository.user.StoryRepository;
import net.artux.pdanetwork.service.items.CommonItemsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StateService {

    private final CommonItemsRepository itemsService;
    private final ParametersRepository parametersRepository;
    private final StoryRepository storyRepository;
    private final ItemMapper itemMapper;
    private final StoryMapper storyMapper;

    public StoryData getStoryData(UserEntity userEntity) {
        StoryData storyData = new StoryData();
        storyData.setParameters(storyMapper.params(parametersRepository.findAllByUser(userEntity)));
        storyData.setStoryStates(storyMapper.states(storyRepository.findAllByPlayer(userEntity)));

        storyData.setArmors(itemMapper.armors(itemsService.findAllByUserAndType(userEntity, ItemType.ARMOR)));
        storyData.setArtifacts(itemMapper.artifacts(itemsService.findAllByUserAndType(userEntity, ItemType.ARTIFACT)));
        storyData.setDetectors(itemMapper.detectors(itemsService.findAllByUserAndType(userEntity, ItemType.DETECTOR)));
        storyData.setMedicines(itemMapper.medicines(itemsService.findAllByUserAndType(userEntity, ItemType.MEDICINE)));
        storyData.setWeapons(itemMapper.weapons(itemsService.findAllByUserAndType(userEntity, ItemType.PISTOL)));
        storyData.getWeapons().addAll(itemMapper.weapons(itemsService.findAllByUserAndType(userEntity, ItemType.RIFLE)));
        storyData.setItems(itemMapper.items(itemsService.findAllByUserAndType(userEntity, ItemType.ITEM)));
        return storyData;
    }

}
