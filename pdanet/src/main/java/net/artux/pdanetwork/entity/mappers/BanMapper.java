package net.artux.pdanetwork.entity.mappers;

import net.artux.pdanetwork.entity.user.BanEntity;
import net.artux.pdanetwork.models.user.ban.BanDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface BanMapper {

    BanDto dto(BanEntity ban);

    List<BanDto> dto(List<BanEntity> ban);

}
