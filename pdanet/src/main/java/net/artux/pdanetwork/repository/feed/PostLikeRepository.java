package net.artux.pdanetwork.repository.feed;

import net.artux.pdanetwork.entity.feed.LikePostId;
import net.artux.pdanetwork.entity.feed.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikeEntity, LikePostId> {

}
