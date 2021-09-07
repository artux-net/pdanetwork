package net.artux.pdanetwork.repository;

import net.artux.pdanetwork.servlets.Feed.Models.Article;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ArticleRepository extends MongoRepository<Article, ObjectId> {

  Optional<Article> findById(String id);
}
