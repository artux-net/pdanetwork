package net.artux.pdanetwork.entity.user;

import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "user_parameter")
public class ParameterEntity extends BaseEntity {

    @ManyToOne()
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
