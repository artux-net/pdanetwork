package net.artux.pdanetwork.repository.feed;

import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.models.feed.ArticleDto;
import net.artux.pdanetwork.models.feed.ArticleSimpleDto;
import net.artux.pdanetwork.models.feed.ArticleWithLikesCountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, UUID> {

    @Query("select new net.artux.pdanetwork.models.feed.ArticleSimpleDto(a, count(distinct al.id), count(distinct c.id)) from ArticleEntity a " +
            "left join a.likes al " +
            "left join a.comments c " +
            "join a.tags t where t.title in :tags group by a.id having count(a.id) = :#{#tags.size}")
    Page<ArticleSimpleDto> findAllByTagsIn(@Param("tags") Set<String> tags, Pageable pageable);

    @Query("select new net.artux.pdanetwork.models.feed.ArticleDto(a, count(distinct al.id), count(distinct c.id)) from ArticleEntity a " +
            "left join a.likes al " +
            "left join a.comments c " +
            "where a.id = ?1 group by a")
    Optional<ArticleDto> findArticleDtoById(UUID id);

    @Query("select new net.artux.pdanetwork.models.feed.ArticleSimpleDto(a, count(distinct al.id), count(distinct c.id)) from ArticleEntity a " +
            "left join a.likes al " +
            "left join a.comments c " +
            "where a.id = ?1 group by a")
    ArticleSimpleDto findSimpleArticleDtoById(UUID id);

    @Query("select new net.artux.pdanetwork.models.feed.ArticleSimpleDto(a, count(distinct al.id), count(distinct c.id)) from ArticleEntity a " +
            "left join a.likes al " +
            "left join a.comments c " +
            "group by a")
    Page<ArticleSimpleDto> findAllSimple(Pageable pageable);
}
