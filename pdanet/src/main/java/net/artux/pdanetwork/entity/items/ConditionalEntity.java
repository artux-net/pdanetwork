package net.artux.pdanetwork.entity.items;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class ConditionalEntity extends WearableEntity {

    protected float condition;

}
