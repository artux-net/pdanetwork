package net.artux.pdanetwork.repository.comminication;

import net.artux.pdanetwork.models.communication.GroupEntity;
import net.artux.pdanetwork.models.communication.MessageEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.Member;
import java.util.List;
import java.util.UUID;

@Component
public interface GroupsRepository extends JpaRepository<GroupEntity, UUID> {

    List<GroupEntity> getAllByMembersContains(UserEntity entity);

}
