package net.artux.pdanetwork.models.user.ban;

import net.artux.pdanetwork.entity.user.BanEntity;
import net.artux.pdanetwork.models.user.UserMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface BanMapper {

    BanDto dto(BanEntity ban);

    List<BanDto> dto(List<BanEntity> ban);

}
