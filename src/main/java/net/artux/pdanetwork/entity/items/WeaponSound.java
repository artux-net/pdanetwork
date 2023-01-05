package net.artux.pdanetwork.entity.items;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class WeaponSound {

    private String shot;
    private String reload;

}
