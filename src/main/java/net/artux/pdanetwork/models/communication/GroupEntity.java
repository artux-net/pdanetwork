package net.artux.pdanetwork.models.communication;

import net.artux.pdanetwork.models.BaseEntity;
import net.artux.pdanetwork.models.user.UserEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "groups")
public class GroupEntity extends BaseEntity {

    private String avatar;
    private String title;
    @OneToMany
    private List<UserEntity> members;

}
