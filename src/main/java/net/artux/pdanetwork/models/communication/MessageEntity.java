package net.artux.pdanetwork.models.communication;

import net.artux.pdanetwork.models.BaseEntity;
import net.artux.pdanetwork.models.MemberDto;
import net.artux.pdanetwork.models.user.UserEntity;

import javax.persistence.OneToOne;

public class MessageEntity extends BaseEntity {

    @OneToOne
    private UserEntity author;
    private String content;
    private long timestamp;

}
