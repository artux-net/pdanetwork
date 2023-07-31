package net.artux.pdanetwork.models.feed;

import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.entity.feed.CommentEntity;
import net.artux.pdanetwork.entity.feed.PostEntity;
import net.artux.pdanetwork.entity.feed.TagEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.user.UserMapper;
import net.artux.pdanetwork.service.util.ValuesService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {ValuesService.class, UserMapper.class})
public interface FeedMapper {

    @Mapping(target = "url", source = "id", qualifiedByName = "articleUrl")
    @Mapping(target = "likes", expression = "java(entity.getLikes().size())")
    @Mapping(target = "comments", expression = "java(entity.getComments().size())")
    ArticleSimpleDto dto(ArticleEntity entity);

    @Mapping(target = "url", source = "id", qualifiedByName = "articleUrl")
    @Mapping(target = "s", ignore = true)
    @Mapping(target = "likes", expression = "java(entity.getLikes().size())")
    @Mapping(target = "comments", expression = "java(entity.getComments().size())")
    ArticleDto fullDto(ArticleEntity entity);

    List<ArticleSimpleDto> dto(List<ArticleEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "article", ignore = true)
    @Mapping(target = "published", expression = "java(java.time.Instant.now())")
    CommentEntity entity(CommentCreateDto dto, UserEntity author);

    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "published", expression = "java(java.time.Instant.now())")
    ArticleEntity entity(ArticleCreateDto createDto);

    @Mapping(target = "likes", expression = "java(entity.getLikes().size())")
    CommentDto dto(CommentEntity entity);

    default String tag(TagEntity tagEntity) {
        return tagEntity.getTitle();
    }

    default TagEntity tag(String title) {
        TagEntity tagEntity = new TagEntity();
        tagEntity.setTitle(title);
        return tagEntity;
    }

    default Set<TagEntity> tag(Set<String> titles) {
        Set<TagEntity> tags = new HashSet<>();
        if (titles != null)
            for (var tag : titles)
                tags.add(tag(tag));

        return tags;
    }

    @Mapping(target = "likes", expression = "java(entity.getLikes().size())")
    @Mapping(target = "comments", expression = "java(entity.getComments().size())")
    PostDto dto(PostEntity entity);

    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "published", expression = "java(java.time.Instant.now())")
    PostEntity entity(PostCreateDto createDto, UserEntity author);
}
