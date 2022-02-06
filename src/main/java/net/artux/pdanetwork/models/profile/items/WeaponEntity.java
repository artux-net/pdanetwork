package net.artux.pdanetwork.models.profile.items;

import lombok.Data;

import java.util.Objects;

@Data
public class WeaponEntity extends ItemEntity {

    private float precision;
    private float speed;
    private float damage;
    private float condition;
    private int bullet_quantity;
    private int bullet_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WeaponEntity weaponEntity = (WeaponEntity) o;
        return Float.compare(weaponEntity.precision, precision) == 0 &&
                Float.compare(weaponEntity.speed, speed) == 0 &&
                Float.compare(weaponEntity.damage, damage) == 0 &&
                Float.compare(weaponEntity.condition, condition) == 0 &&
                bullet_id == weaponEntity.bullet_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), precision, speed, damage, condition, bullet_quantity, bullet_id);
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
