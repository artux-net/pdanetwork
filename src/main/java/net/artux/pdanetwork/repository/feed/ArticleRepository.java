package net.artux.pdanetwork.repository.feed;

import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.entity.feed.TagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, UUID> {

    @Query("select a from ArticleEntity a join a.tags t where t.title in :tags group by a.id having count(a.id) = :#{#tags.size}")
    Page<ArticleEntity> findAllByTagsIn(@Param("tags") Set<String> tags, Pageable pageable);
}
