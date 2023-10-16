package net.artux.pdanetwork.repository.feed;

import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.entity.feed.ArticleLikeEntity;
import net.artux.pdanetwork.entity.feed.LikeArticleId;
import net.artux.pdanetwork.entity.feed.LikePostId;
import net.artux.pdanetwork.entity.feed.PostLikeEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikeEntity, LikePostId> {

}
