
package net.artux.pdanetwork.models.profile.items;


import java.util.Objects;

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

    public Armor() {
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

    public float getThermal_pr() {
        return thermal_pr;
    }

    public void setThermal_pr(float thermal_pr) {
        this.thermal_pr = thermal_pr;
    }

    public float getElectric_pr() {
        return electric_pr;
    }

    public void setElectric_pr(float electric_pr) {
        this.electric_pr = electric_pr;
    }

    public float getChemical_pr() {
        return chemical_pr;
    }

    public void setChemical_pr(float chemical_pr) {
        this.chemical_pr = chemical_pr;
    }

    public float getRadio_pr() {
        return radio_pr;
    }

    public void setRadio_pr(float radio_pr) {
        this.radio_pr = radio_pr;
    }

    public float getPsy_pr() {
        return psy_pr;
    }

    public void setPsy_pr(float psy_pr) {
        this.psy_pr = psy_pr;
    }

    public float getDamage_pr() {
        return damage_pr;
    }

    public void setDamage_pr(float damage_pr) {
        this.damage_pr = damage_pr;
    }

    public float getCondition() {
        return condition;
    }

    public void setCondition(float condition) {
        this.condition = condition;
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
