package net.artux.pdanetwork.models.profile.items;

import java.util.Objects;

public class Item {

    public int id;
    public int type;
    String icon;
    public String title;
    public float weight;
    int library_id;
    int price;
    public int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id &&
                type == item.type &&
                Float.compare(item.weight, weight) == 0 &&
                library_id == item.library_id &&
                price == item.price &&
                Objects.equals(icon, item.icon) &&
                title.equals(item.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, icon, title, weight, library_id, price, quantity);
    }

    public Item() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getLibrary_id() {
        return library_id;
    }

    public void setLibrary_id(int library_id) {
        this.library_id = library_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    @Override
    public String toString() {
        return "Item{" +
                "cid=" + id +
                ", type=" + type +
                ", icon='" + icon + '\'' +
                ", title='" + title + '\'' +
                ", weight=" + weight +
                ", library_id=" + library_id +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
