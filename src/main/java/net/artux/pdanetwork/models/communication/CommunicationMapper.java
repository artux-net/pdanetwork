package net.artux.pdanetwork.models.communication;

import net.artux.pdanetwork.entity.communication.ConversationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = MessageMapper.class)
public interface CommunicationMapper {

    ConversationDTO dto(ConversationEntity entity);

}
