package net.artux.pdanetwork.repository.user;

import net.artux.pdanetwork.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Transactional
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    List<UserEntity> findAllByReceiveEmailsTrue();

    Optional<UserEntity> getMemberByLogin(String login);

    Optional<UserEntity> findMemberByEmail(String email);

    Set<UserEntity> findAllByIdIn(List<UUID> list);

    @Query(value = "select row_number() over(ORDER BY xp) from pda_user pu where pu.id = :id", nativeQuery = true)
    long ratingPosition(UUID id);

    @Query(value = "select * from pda_user pu join " +
            "(select r.user1_id, r.user2_id from relationship r where r.id not in (select r1.id from relationship r1  " +
            "join relationship r2 on r1.user1_id = r2.user2_id and r1.user2_id = r2.user1_id)) " +
            "ur on ur.user2_id = pu.id where ur.user1_id = ?1", nativeQuery = true)
    List<UserEntity> getRequestsById(UUID id);

    @Query(value = "select * from pda_user pu join " +
            "(select r.user1_id, r.user2_id from relationship r where r.id not in (select r1.id from relationship r1 " +
            "join relationship r2 on r1.user1_id = r2.user2_id and r1.user2_id = r2.user1_id)) " +
            "ur on ur.user1_id = pu.id where ur.user2_id = ?1", nativeQuery = true)
    List<UserEntity> getSubsById(UUID id);

    @Query(value = "select * from pda_user pu join (select r.user2_id  from relationship r " +
            "join relationship r1 on r.user1_id = r1.user2_id and r.user2_id = r1.user1_id where r.user1_id = ?1) r " +
            "on r.user2_id = pu.id", nativeQuery = true)
    List<UserEntity> getFriendsById(UUID id);

    Page<UserEntity> findByLoginContainingIgnoreCase(String title, Pageable pageable);

    int countAllByLastLoginAtAfter(Instant afterTime);

    int countAllByRegistrationAfter(Instant afterTime);
}
