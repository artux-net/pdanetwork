package net.artux.pdanetwork.service.quest;

import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.quest.Story;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface QuestManagerService {

    Status downloadStories();

    Status uploadStories(MultipartFile storiesArchive);

    Status setUserStory(Story story);

    Collection<Story> getStories();

}
