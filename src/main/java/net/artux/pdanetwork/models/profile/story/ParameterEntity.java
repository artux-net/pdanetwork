package net.artux.pdanetwork.models.profile.story;

import lombok.Data;
import net.artux.pdanetwork.models.BaseEntity;
import net.artux.pdanetwork.models.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "parameter")
public class ParameterEntity extends BaseEntity {

    @ManyToOne
    public UserEntity user;
    public String key;
    public Integer value;

    public ParameterEntity(UserEntity user, String key, Integer value) {
        this.user = user;
        this.key = key;
        this.value = value;
    }

    public ParameterEntity() {

    }
}
