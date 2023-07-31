package net.artux.pdanetwork.repository.feed;

import net.artux.pdanetwork.entity.feed.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {

    @Query("select c from CommentEntity c where c.article.id = ?1")
    Page<CommentEntity> findAllByArticle(UUID articleId, Pageable page);

    @Query("select c from CommentEntity c where c.post.id = ?1")
    Page<CommentEntity> findAllByPost(UUID postId, Pageable page);

}
