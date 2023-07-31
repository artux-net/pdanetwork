package net.artux.pdanetwork.repository.feed;

import net.artux.pdanetwork.entity.feed.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, UUID> {

    @Query("select p from PostEntity p where p.author.id = ?1")
    Page<PostEntity> findAllByAuthor(UUID id, Pageable pageable);

    @Query("select p from PostEntity p where p.published > ?1 order by count(p.likes)")
    Page<PostEntity> findAllRecent(Instant timestamp, Pageable pageable);

}
