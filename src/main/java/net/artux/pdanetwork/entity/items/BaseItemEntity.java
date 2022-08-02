package net.artux.pdanetwork.entity.items;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    @Lob
    protected String content;
    @ElementCollection
    protected List<String> dignities;
    @ElementCollection
    protected List<String> disadvantages;

}
