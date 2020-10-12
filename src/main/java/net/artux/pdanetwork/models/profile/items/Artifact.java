package net.artux.pdanetwork.models.profile.items;

import java.util.Objects;

public class Artifact extends Item {

    private int anomal_id;

    private int health;
    private int radio;
    private int damage;
    private int bleeding;
    private int thermal;
    private int chemical;
    private int endurance;
    private int electric;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Artifact artifact = (Artifact) o;
        return anomal_id == artifact.anomal_id &&
                health == artifact.health &&
                radio == artifact.radio &&
                damage == artifact.damage &&
                bleeding == artifact.bleeding &&
                thermal == artifact.thermal &&
                chemical == artifact.chemical &&
                endurance == artifact.endurance &&
                electric == artifact.electric;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), anomal_id, health, radio, damage, bleeding, thermal, chemical, endurance, electric);
    }

    public Artifact() {
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

    public int getAnomal_id() {
        return anomal_id;
    }

    public void setAnomal_id(int anomal_id) {
        this.anomal_id = anomal_id;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getBleeding() {
        return bleeding;
    }

    public void setBleeding(int bleeding) {
        this.bleeding = bleeding;
    }

    public int getThermal() {
        return thermal;
    }

    public void setThermal(int thermal) {
        this.thermal = thermal;
    }

    public int getChemical() {
        return chemical;
    }

    public void setChemical(int chemical) {
        this.chemical = chemical;
    }

    public int getEndurance() {
        return endurance;
    }

    public void setEndurance(int endurance) {
        this.endurance = endurance;
    }

    public int getElectric() {
        return electric;
    }

    public void setElectric(int electric) {
        this.electric = electric;
    }
}
