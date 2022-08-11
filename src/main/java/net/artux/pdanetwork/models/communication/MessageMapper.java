package net.artux.pdanetwork.models.communication;

import net.artux.pdanetwork.entity.communication.MessageEntity;
import net.artux.pdanetwork.models.user.UserMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface MessageMapper {

    MessageDTO dto(MessageEntity entity);

    List<MessageDTO> list(List<MessageEntity> entity);

}
