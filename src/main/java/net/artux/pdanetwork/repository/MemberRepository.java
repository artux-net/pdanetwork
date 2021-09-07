package net.artux.pdanetwork.repository;

import net.artux.pdanetwork.models.Member;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface MemberRepository extends MongoRepository<Member, ObjectId> {

    Optional<Member> getMemberBy_id(ObjectId objectId);
    Optional<Member> getMemberByPdaId(int pdaId);
    Optional<Member> getMemberByLogin(String login);
    Optional<Member> getMemberByEmail(String email);
    Optional<Member> findTopByOrderByPdaIdDesc();


}
