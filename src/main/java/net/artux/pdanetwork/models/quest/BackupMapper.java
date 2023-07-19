package net.artux.pdanetwork.models.quest;

import net.artux.pdanetwork.entity.quest.StoryBackup;
import net.artux.pdanetwork.models.items.ItemMapper;
import net.artux.pdanetwork.models.user.UserMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface BackupMapper {

    StoryBackupDto dto(StoryBackup storyBackup);

}
