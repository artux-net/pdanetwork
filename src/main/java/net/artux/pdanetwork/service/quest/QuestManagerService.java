package net.artux.pdanetwork.service.quest;

import net.artux.pdanetwork.models.Status;
import org.springframework.web.multipart.MultipartFile;

public interface QuestManagerService {

    Status readFromGit();

    Status uploadStories(MultipartFile storiesArchive);

    Status readFromR2();
}
