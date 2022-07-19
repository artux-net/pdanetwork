package net.artux.pdanetwork.service.action;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.ItemMapper;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.models.story.StoryMapper;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.repository.user.ParametersRepository;
import net.artux.pdanetwork.repository.user.StoryRepository;
import net.artux.pdanetwork.service.ItemsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StateService {

    private final ItemsService itemsService;
    private final ParametersRepository parametersRepository;
    private final StoryRepository storyRepository;
    private final ItemMapper itemMapper;
    private final StoryMapper storyMapper;

    public StoryData getStoryData(UserEntity userEntity) {
        StoryData storyData = new StoryData();
        storyData.setParameters(storyMapper.params(parametersRepository.findAllByUser(userEntity)));
        storyData.setStoryStates(storyMapper.states(storyRepository.findAllByPlayer(userEntity)));

        storyData.setArmors(itemMapper.armors(itemsService.getAllByUserAndType(userEntity, ItemType.ARMOR)));
        storyData.setArtifacts(itemMapper.artifacts(itemsService.getAllByUserAndType(userEntity, ItemType.ARTIFACT)));
        storyData.setDetectors(itemMapper.detectors(itemsService.getAllByUserAndType(userEntity, ItemType.DETECTOR)));
        storyData.setMedicines(itemMapper.medicines(itemsService.getAllByUserAndType(userEntity, ItemType.MEDICINE)));
        storyData.setWeapons(itemMapper.weapons(itemsService.getAllByUserAndType(userEntity, ItemType.PISTOL)));
        storyData.getWeapons().addAll(itemMapper.weapons(itemsService.getAllByUserAndType(userEntity, ItemType.RIFLE)));
        storyData.setItems(itemMapper.items(itemsService.getAllByUserAndType(userEntity, ItemType.ITEM)));
        return storyData;
    }

}
