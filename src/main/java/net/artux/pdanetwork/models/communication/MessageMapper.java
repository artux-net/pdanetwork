package net.artux.pdanetwork.models.communication;

import net.artux.pdanetwork.entity.communication.MessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "login", source = "entity.author.login")
    @Mapping(target = "pdaId", source = "entity.author.id")
    @Mapping(target = "avatar", source = "entity.author.avatar")
    @Mapping(target = "timestamp", expression = "java(entity.getTimestamp().toEpochMilli())")
    MessageDTO dto(MessageEntity entity);

    List<MessageDTO> list(List<MessageEntity> entity);

}
