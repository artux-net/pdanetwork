package net.artux.pdanetwork.models.profile;

import lombok.Data;
import net.artux.pdanetwork.models.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "parameter", schema = "artuxpda")
public class ParameterEntity extends BaseEntity {

    public UUID userId;
    public String key;
    public Integer value;

    public ParameterEntity(UUID userId, String key, Integer value) {
        this.userId = userId;
        this.key = key;
        this.value = value;
    }

    public ParameterEntity() {

    }
}
