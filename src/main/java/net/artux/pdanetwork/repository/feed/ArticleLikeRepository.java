package net.artux.pdanetwork.repository.feed;

import net.artux.pdanetwork.entity.feed.ArticleLikeEntity;
import net.artux.pdanetwork.entity.feed.LikeArticleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLikeEntity, LikeArticleId> {

    long deleteLikeById(LikeArticleId likeId);

}
