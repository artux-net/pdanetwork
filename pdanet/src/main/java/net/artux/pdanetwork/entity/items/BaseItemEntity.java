package net.artux.pdanetwork.entity.items;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.Data;
import net.artux.pdanetwork.entity.MediaItem;
import net.artux.pdanetwork.models.items.ItemType;

import java.util.List;

@Data
@Entity
@Table(name = "base_item")
@NamedEntityGraph(
        name = "encItem",
        attributeNodes = {
                @NamedAttributeNode("description"),
                @NamedAttributeNode("content"),
                @NamedAttributeNode("advantages"),
                @NamedAttributeNode("disadvantages"),
        }
)
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

    @ElementCollection(fetch = FetchType.LAZY)
    protected List<String> advantages;

    @ElementCollection(fetch = FetchType.LAZY)
    protected List<String> disadvantages;
}
