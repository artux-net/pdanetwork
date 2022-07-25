package net.artux.pdanetwork.models.communication;

import net.artux.pdanetwork.entity.MessageEntity;
import net.artux.pdanetwork.entity.conversation.ConversationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MessageMapper.class)
public interface CommunicationMapper {

    ConversationDTO dto(ConversationEntity entity);

}
