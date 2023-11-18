package net.artux.pdanetwork.models.communication;

import net.artux.pdanetwork.entity.communication.ConversationEntity;
import net.artux.pdanetwork.models.user.UserMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MessageMapper.class, UserMapper.class})
public interface CommunicationMapper {

    ConversationDTO dto(ConversationEntity entity);
}
