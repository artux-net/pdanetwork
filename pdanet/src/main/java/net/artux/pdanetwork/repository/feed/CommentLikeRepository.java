package net.artux.pdanetwork.repository.feed;

import net.artux.pdanetwork.entity.feed.CommentLikeEntity;
import net.artux.pdanetwork.entity.feed.LikeCommentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity, LikeCommentId> {

}
