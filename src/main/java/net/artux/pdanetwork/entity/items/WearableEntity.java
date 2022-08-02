package net.artux.pdanetwork.entity.items;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public class WearableEntity extends ItemEntity {

    protected boolean isEquipped;

}
