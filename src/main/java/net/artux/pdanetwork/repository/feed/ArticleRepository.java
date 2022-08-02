package net.artux.pdanetwork.repository.feed;

import net.artux.pdanetwork.entity.feed.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

}
