package net.artux.pdanetwork.models.quest;

import net.artux.pdanetwork.entity.quest.StoryBackup;
import net.artux.pdanetwork.models.items.ItemMapper;
import net.artux.pdanetwork.models.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface BackupMapper {

    @Mapping(target = "storageId", source = "storyBackup.id")
    StoryBackupDto dto(StoryBackup storyBackup);

}
