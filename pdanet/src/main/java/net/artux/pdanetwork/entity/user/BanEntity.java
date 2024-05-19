package net.artux.pdanetwork.entity.user;

import lombok.Getter;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user_ban")
public class BanEntity extends BaseEntity {

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private UserEntity by;
    private String reason;
    private String message;
    private int seconds;
    private Instant timestamp;

    public BanEntity() {
        timestamp = Instant.now();
    }

}
