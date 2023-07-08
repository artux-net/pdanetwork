package net.artux.pdanetwork.entity.items;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

    @Basic(fetch = FetchType.LAZY)
    protected String description;

    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "TEXT")
    protected String content;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection(fetch = FetchType.LAZY)
    protected List<String> advantages;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection(fetch = FetchType.LAZY)
    protected List<String> disadvantages;

}
