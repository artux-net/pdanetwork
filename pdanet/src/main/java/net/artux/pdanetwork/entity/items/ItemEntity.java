package net.artux.pdanetwork.entity.items;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.items.ItemType;

import static jakarta.persistence.InheritanceType.TABLE_PER_CLASS;

@Getter
@Setter
@Entity
@Inheritance(strategy = TABLE_PER_CLASS)
public abstract class ItemEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    protected BaseItemEntity base;
    protected int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    protected UserEntity owner;

    @Transient
    public ItemType getBasedType() {
        return base.type;
    }

    @Transient
    public long getBasedId() {
        return base.id;
    }

}