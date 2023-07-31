package net.artux.pdanetwork.repository.feed;

import net.artux.pdanetwork.entity.feed.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, UUID> {

    Set<TagEntity> findAllByTitleIn(Set<String> title);

}
