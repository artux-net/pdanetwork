package net.artux.pdanetwork.models.profile.items;

import java.util.Objects;

public class Weapon extends Item {

    private float precision;
    private float speed;
    private float damage;
    private float condition;
    private int bullet_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Weapon weapon = (Weapon) o;
        return Float.compare(weapon.precision, precision) == 0 &&
                Float.compare(weapon.speed, speed) == 0 &&
                Float.compare(weapon.damage, damage) == 0 &&
                Float.compare(weapon.condition, condition) == 0 &&
                bullet_id == weapon.bullet_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), precision, speed, damage, condition, bullet_id);
    }

    public Weapon() {
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

    public float getPrecision() {
        return precision;
    }

    public void setPrecision(float precision) {
        this.precision = precision;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getCondition() {
        return condition;
    }

    public void setCondition(float condition) {
        this.condition = condition;
    }

    public int getBullet_id() {
        return bullet_id;
    }

    public void setBullet_id(int bullet_id) {
        this.bullet_id = bullet_id;
    }

    @Override
    public int priceToSell() {
        return (int) (super.priceToSell() * condition / 100);
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "precision=" + precision +
                ", speed=" + speed +
                ", damage=" + damage +
                ", condition=" + condition +
                ", bullet_id=" + bullet_id +
                '}';
    }
}
