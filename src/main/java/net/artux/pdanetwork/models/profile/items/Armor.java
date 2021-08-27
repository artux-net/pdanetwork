
package net.artux.pdanetwork.models.profile.items;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Data
@Getter
@Setter
public class Armor extends Item {

    private float thermal_pr;
    private float electric_pr;
    private float chemical_pr;
    private float radio_pr;
    private float psy_pr;
    private float damage_pr;
    private float condition;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Armor armor = (Armor) o;
        return Float.compare(armor.thermal_pr, thermal_pr) == 0 &&
                Float.compare(armor.electric_pr, electric_pr) == 0 &&
                Float.compare(armor.chemical_pr, chemical_pr) == 0 &&
                Float.compare(armor.radio_pr, radio_pr) == 0 &&
                Float.compare(armor.psy_pr, psy_pr) == 0 &&
                Float.compare(armor.damage_pr, damage_pr) == 0 &&
                Float.compare(armor.condition, condition) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), thermal_pr, electric_pr, chemical_pr, radio_pr, psy_pr, damage_pr, condition);
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
