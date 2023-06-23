package net.artux.pdanetwork.service.quest;

import net.artux.pdanetwork.models.Status;
import org.springframework.web.multipart.MultipartFile;

public interface QuestManagerService {

    Status downloadStories();

    Status uploadStories(MultipartFile storiesArchive);

}
