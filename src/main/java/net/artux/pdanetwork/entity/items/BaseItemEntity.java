package net.artux.pdanetwork.entity.items;

import jakarta.persistence.*;
import lombok.Data;
import net.artux.pdanetwork.entity.MediaItem;

import java.util.List;

@Data
@Entity
@Table(name = "base_item")
public class BaseItemEntity implements MediaItem {

    @Id
    protected Long id;
    @Enumerated(EnumType.STRING)
    protected ItemType type;
    protected String icon;
    protected String title;
    protected float weight;
    protected int price;

    @Basic(fetch = FetchType.LAZY)
    protected String description;

    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "TEXT")
    protected String content;

    @ElementCollection(fetch = FetchType.EAGER)
    protected List<String> advantages;

    @ElementCollection(fetch = FetchType.EAGER)
    protected List<String> disadvantages;
}
