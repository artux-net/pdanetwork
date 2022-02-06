
package net.artux.pdanetwork.models.profile.items;


import lombok.Data;

@Data
public class ArmorEntity extends ItemEntity {

    private float thermal_pr;
    private float electric_pr;
    private float chemical_pr;
    private float radio_pr;
    private float psy_pr;
    private float damage_pr;
    private float condition;

    private boolean isEquipped;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ArmorEntity armorEntity = (ArmorEntity) o;
        return Float.compare(armorEntity.thermal_pr, thermal_pr) == 0 &&
                Float.compare(armorEntity.electric_pr, electric_pr) == 0 &&
                Float.compare(armorEntity.chemical_pr, chemical_pr) == 0 &&
                Float.compare(armorEntity.radio_pr, radio_pr) == 0 &&
                Float.compare(armorEntity.psy_pr, psy_pr) == 0 &&
                Float.compare(armorEntity.damage_pr, damage_pr) == 0 &&
                Float.compare(armorEntity.condition, condition) == 0;
    }

    @Override
    public int priceToSell() {
        return (int) (super.priceToSell() * condition / 100);
    }

    @Override
    public String toString() {
        return "Armor{" +
                "thermal_pr=" + thermal_pr +
                ", electric_pr=" + electric_pr +
                ", chemical_pr=" + chemical_pr +
                ", radio_pr=" + radio_pr +
                ", psy_pr=" + psy_pr +
                ", damage_pr=" + damage_pr +
                ", condition=" + condition +
                '}';
    }
}
