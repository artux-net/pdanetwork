package net.artux.pdanetwork.models.profile.items;

import lombok.Data;
import net.artux.pdanetwork.models.BaseEntity;
import net.artux.pdanetwork.models.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

@Data
@Entity
@Table(name = "item")
@Inheritance(strategy = TABLE_PER_CLASS)
public class ItemEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    protected ItemType type;
    protected String icon;
    protected String title;
    protected int baseId;
    protected float weight;
    protected int price;
    protected int quantity;
    @ManyToOne
    private UserEntity owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemEntity itemEntity = (ItemEntity) o;
        return Objects.equals(super.id, itemEntity.id) &&
                type == itemEntity.type &&
                Float.compare(itemEntity.weight, weight) == 0 &&
                price == itemEntity.price &&
                Objects.equals(icon, itemEntity.icon) &&
                title.equals(itemEntity.title);
    }

}