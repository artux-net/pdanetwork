package net.artux.pdanetwork.service.action;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.story.StoryMapper;
import net.artux.pdanetwork.models.user.dto.StoryData;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StateService {

    private final StoryMapper storyMapper;
    private final UserService userService;

    public StoryData getStoryData() {
        return storyMapper.storyData(userService.getUserById());
    }

}
