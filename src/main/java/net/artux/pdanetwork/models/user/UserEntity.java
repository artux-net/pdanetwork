package net.artux.pdanetwork.models.user;

import lombok.NoArgsConstructor;
import net.artux.pdanetwork.models.BaseEntity;
import net.artux.pdanetwork.models.RegisterUser;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.models.profile.NoteEntity;
import net.artux.pdanetwork.utills.Security;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@lombok.Data
@NoArgsConstructor
//@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    private int pdaId;
    private String login;
    private String password;
    private String email;
    private String name;
    private String nickname;
    private String avatar;
    private String location;
    private Role role;
    private Group group;
    private int xp;
    private int money;

    //public Integer[] relations;


    @OneToMany(fetch = FetchType.LAZY)
    public List<NoteEntity> noteEntities;

    //private Data data; - ok
    /*public List<Integer> dialogs = new ArrayList<>();
    public List<ObjectId> subs = new ArrayList<>();
    public List<ObjectId> friends = new ArrayList<>();
    public List<ObjectId> requests = new ArrayList<>();


    public List<Integer> achievements = new ArrayList<>();*/
    private Long registration;
    private Long lastLoginAt;

    public UserEntity(RegisterUser registerUser, int id, PasswordEncoder passwordEncoder) {
        login = registerUser.getLogin();
        password = passwordEncoder.encode(registerUser.getPassword());
        email = registerUser.getEmail();
        name = registerUser.getName();
        nickname = registerUser.getNickname();
        avatar = registerUser.getAvatar();
        pdaId = id;
        role = Role.USER;
        group = Group.LONERS;
        xp = 0;
        location = "Ч-4";
        money = 500;
        lastLoginAt = registration = Instant.now().toEpochMilli();

        /*data = new Data();
        dialogs = new ArrayList<>();
        subs = friends = requests = new ArrayList<>();
        relations = new ArrayList<>();
        for (int i = 0; i < 9; i++)
            relations.add(0);*/
    }

    public void hashPassword(String password) {
        this.password = String.valueOf(password.hashCode());
    }

    /*public NoteEntity addNote(String title, String content) {
        NoteEntity noteEntity;
        if (noteEntities.size() != 0)
            noteEntity = new NoteEntity(noteEntities.get(noteEntities.size() - 1).cid + 1,
                    title, content);
        else
            noteEntity = new NoteEntity(title, content);
        noteEntities.add(noteEntity);
        return noteEntity;
    }*/

    public void money(int money) {
        this.money += money;
        if (this.money < 0)
            this.money = 0;
    }

    public void xp(int xp) {
        this.xp += xp;
        if (this.xp < 0)
            this.xp = 0;
    }

    public boolean buy(int price) {
        if (money >= price) {
            money -= price;
            return true;
        } else
            return false;
    }
}
