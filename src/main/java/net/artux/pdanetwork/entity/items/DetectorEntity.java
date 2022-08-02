package net.artux.pdanetwork.entity.items;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "user_detector")
public class DetectorEntity extends WearableEntity {

    private DetectorType detectorType;
}
