package net.artux.pdanetwork.models.communication;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "login", source = "entity.author.login")
    @Mapping(target = "pdaId", source = "entity.author.id")
    @Mapping(target = "avatar", source = "entity.author.avatar")
    MessageDTO dto(MessageEntity entity);

    List<MessageDTO> list(List<MessageEntity> entity);

}
