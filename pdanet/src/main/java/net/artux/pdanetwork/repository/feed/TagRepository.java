package net.artux.pdanetwork.repository.feed;

import net.artux.pdanetwork.entity.feed.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, UUID> {

    Set<TagEntity> findAllByTitleIn(Set<String> title);

    @Query("select t.title from TagEntity t join ArticleEntity a on a member of t.articles where a.id = ?1")
    List<String> findAllByArticleId(UUID id);

}
