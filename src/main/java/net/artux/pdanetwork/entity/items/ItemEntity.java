package net.artux.pdanetwork.entity.items;

import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

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
    @NotFound(action = NotFoundAction.IGNORE)
    protected UserEntity owner;

}