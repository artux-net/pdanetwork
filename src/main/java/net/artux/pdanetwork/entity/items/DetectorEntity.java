package net.artux.pdanetwork.entity.items;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "detector")
public class DetectorEntity extends WearableEntity {

    private DetectorType detectorType;
}
