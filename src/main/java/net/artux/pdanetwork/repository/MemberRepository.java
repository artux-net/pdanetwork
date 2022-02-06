package net.artux.pdanetwork.repository;

import net.artux.pdanetwork.models.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface MemberRepository extends MongoRepository<UserEntity, ObjectId> {

    Optional<UserEntity> getMemberBy_id(ObjectId objectId);
    Optional<UserEntity> getMemberByPdaId(int pdaId);
    Optional<UserEntity> getMemberByLogin(String login);
    Optional<UserEntity> getMemberByEmail(String email);
    Optional<UserEntity> findTopByOrderByPdaIdDesc();


}
