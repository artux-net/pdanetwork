package net.artux.pdanetwork.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.models.profile.Note;
import net.artux.pdanetwork.utills.Security;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Id;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {

    @Id
    private ObjectId _id;
    private String login;
    @Indexed(unique = true)
    private String email;
    private String name;
    @Indexed(unique = true)
    private String nickname;
    private String avatar;
    @Indexed(unique = true)
    private int pdaId;
    private String role;
    private int blocked;
    private int group;
    private int xp;
    private int money;
    private String location;
    private Data data;
    public List<Integer> dialogs = new ArrayList<>();
    public List<String> subs = new ArrayList<>();
    public List<String> friends = new ArrayList<>();
    public List<String> requests = new ArrayList<>();
    public List<Integer> relations = new ArrayList<>();
    public List<Note> notes = new ArrayList<>();
    public List<Integer> achievements = new ArrayList<>();
    private Long lastModified;
    private Long registration;
    private Long lastLoginAt;

}
