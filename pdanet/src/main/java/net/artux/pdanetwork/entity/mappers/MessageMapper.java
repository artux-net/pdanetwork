package net.artux.pdanetwork.entity.mappers;

import net.artux.pdanetwork.entity.communication.MessageEntity;
import net.artux.pdanetwork.models.communication.MessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = MessageDTO.Type.class, uses = UserMapper.class)
public interface MessageMapper {

    @Mapping(target = "type", expression = "java(Type.NEW)")
    MessageDTO dto(MessageEntity entity);

    List<MessageDTO> list(List<MessageEntity> entity);

    MessageEntity entity(MessageDTO dto);
}
