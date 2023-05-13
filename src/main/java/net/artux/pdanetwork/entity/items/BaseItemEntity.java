package net.artux.pdanetwork.entity.items;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import jakarta.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "base_item")
public class BaseItemEntity {

    @Id
    protected Long id;
    @Enumerated(EnumType.STRING)
    protected ItemType type;
    protected String icon;
    protected String title;
    protected float weight;
    protected int price;

    protected String description;
    @Column(columnDefinition = "TEXT")
    protected String content;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    protected List<String> advantages;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    protected List<String> disadvantages;

}
