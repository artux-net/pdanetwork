package net.artux.pdanetwork.models.profile.items;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Item {

    protected int id;
    protected int type;
    protected String icon;
    protected String title;
    protected float weight;
    protected int price;
    protected int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id &&
                type == item.type &&
                Float.compare(item.weight, weight) == 0 &&
                price == item.price &&
                Objects.equals(icon, item.icon) &&
                title.equals(item.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, icon, title, weight, price, quantity);
    }

    public Item() {
    }


    public int sellerPrice() {
        if (quantity > 0)
            return quantity * price;
        else
            return price;
    }

    public int priceToSell() {
        return (int) (0.9 * price);
    }

}