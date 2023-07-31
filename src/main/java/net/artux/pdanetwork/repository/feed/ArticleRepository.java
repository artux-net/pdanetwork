package net.artux.pdanetwork.repository.feed;

import net.artux.pdanetwork.entity.feed.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, UUID> {

    @Query("select a from ArticleEntity a join a.tags t where t.title in ?1")
    Page<ArticleEntity> findAllByTagsIn(Set<String> tagEntitySet, Pageable pageable);
}
