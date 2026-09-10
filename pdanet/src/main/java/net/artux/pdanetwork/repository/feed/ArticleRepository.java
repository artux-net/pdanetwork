package net.artux.pdanetwork.repository.feed;

import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.models.feed.ArticleDto;
import net.artux.pdanetwork.models.feed.ArticleSimpleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, UUID> {

    @Query("select new net.artux.pdanetwork.dto.ArticleSimpleDto(a, " +
            "(select count(al) from a.likes al), (select count(c) from a.comments c)) from ArticleEntity a " +
            "join a.tags t where t.title in :tags group by a.id having count(a.id) = :#{#tags.size}")
    Page<net.artux.pdanetwork.dto.ArticleSimpleDto> findAllByTagsIn(@Param("tags") Set<String> tags, Pageable pageable);

    @Query("select new net.artux.pdanetwork.dto.ArticleDto(a, " +
            "(select count(al) from a.likes al), (select count(c) from a.comments c)) from ArticleEntity a " +
            "where a.id = ?1")
    Optional<net.artux.pdanetwork.dto.ArticleDto> findArticleDtoById(UUID id);

    @Query("select new net.artux.pdanetwork.dto.ArticleSimpleDto(a, " +
            "(select count(al) from a.likes al), (select count(c) from a.comments c)) from ArticleEntity a " +
            "where a.id = ?1")
    net.artux.pdanetwork.dto.ArticleSimpleDto findSimpleArticleDtoById(UUID id);

    @Query("select new net.artux.pdanetwork.dto.ArticleSimpleDto(a, " +
            "(select count(al) from a.likes al), (select count(c) from a.comments c)) from ArticleEntity a")
    Page<net.artux.pdanetwork.dto.ArticleSimpleDto> findAllSimple(Pageable pageable);
}
