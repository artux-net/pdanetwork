package net.artux.pdanetwork.entity.items;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;
import net.artux.pdanetwork.entity.user.UserEntity;

import javax.persistence.*;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

@Getter
@Setter
@Entity
@Table(name = "user_item")
@Inheritance(strategy = TABLE_PER_CLASS)
public class ItemEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    protected BaseItemEntity base;
    protected int quantity;
    @ManyToOne
    private UserEntity owner;

}