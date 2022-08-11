package net.artux.pdanetwork.entity.feed;

import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "article")
public class ArticleEntity extends BaseEntity {

    private String title;
    private String image;
    //private List<String> tags = new HashSet<>();//todo
    private String description;
    private Instant published;

    @Lob
    private String content;

    public ArticleEntity() {
        setPublished(Instant.now());
    }

}
