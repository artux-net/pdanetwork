package net.artux.pdanetwork.models.feed;

import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.service.util.ValuesService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = ValuesService.class)
public interface ArticleMapper {

    @Mapping(target = "url", source = "id", qualifiedByName = "articleUrl")
    @Mapping(target = "tags", ignore = true)
    ArticleDto dto(ArticleEntity entity);

    List<ArticleDto> dto(List<ArticleEntity> entities);


}
