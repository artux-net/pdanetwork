package net.artux.pdanetwork.models.feed;

import lombok.Data;
import net.artux.pdanetwork.models.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "article")
public class ArticleEntity extends BaseEntity {

    private String title;
    private String image;
    //private List<String> tags = new ArrayList<>();
    private String description;
    private long published;

    private String content;

    public ArticleEntity() {
        setPublished(Instant.now().toEpochMilli());
    }

    public ArticleEntity(Long id, String title, String image, String description, long published, String content) {
        super(id);
        this.title = title;
        this.image = image;
        //this.tags = tags;
        this.description = description;
        this.published = published;
        this.content = content;
    }
}
