package net.artux.pdanetwork.models.profile.items;

import lombok.Data;
import net.artux.pdanetwork.models.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@Table(name = "item", schema = "artuxpda")
public class ItemEntity extends BaseEntity {

    protected int id;
    protected int type;
    protected String icon;
    protected String title;
    protected float weight;
    protected int price;
    protected int quantity;
    private UUID userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemEntity itemEntity = (ItemEntity) o;
        return Objects.equals(super.uid, itemEntity.uid) &&
                type == itemEntity.type &&
                Float.compare(itemEntity.weight, weight) == 0 &&
                price == itemEntity.price &&
                Objects.equals(icon, itemEntity.icon) &&
                title.equals(itemEntity.title);
    }

    public int sellerPrice() {
        return quantity > 0 ? quantity * price : price;
    }

    public int priceToSell() {
        return (int) (0.9 * price);
    }

}