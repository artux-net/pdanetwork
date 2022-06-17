package net.artux.pdanetwork.repository;

import net.artux.pdanetwork.models.feed.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

}
