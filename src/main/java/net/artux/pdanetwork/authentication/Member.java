package net.artux.pdanetwork.authentication;

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
//@RequiredArgsConstructor
public class Member {

    @Id
    private ObjectId _id;
    @Indexed(unique = true)
    private String login;
    private String password;
    @Indexed(unique = true)
    private String email;
    private String name;
    @Indexed(unique = true)
    private String nickname;
    private String avatar;
    private String token;
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
    public List<Integer> friends = new ArrayList<>();
    public List<Integer> friendRequests = new ArrayList<>();
    public List<Integer> relations = new ArrayList<>();
    public List<Note> notes = new ArrayList<>();
    public List<Integer> achievements = new ArrayList<>();
    private Long lastModified;
    private Long registration;
    private Long lastLoginAt;

    public Member(RegisterUser registerUser, int id, PasswordEncoder passwordEncoder) {
        login = registerUser.getLogin();
        password = passwordEncoder.encode(registerUser.getPassword());
        email = registerUser.getEmail();
        name = registerUser.getName();
        nickname = registerUser.getNickname();
        avatar = registerUser.getAvatar();
        token = Security.encrypt(login + ":" + password);
        pdaId = id;
        role = "user";
        blocked = group = xp = 0;
        location = "Ч-4";
        data = new Data();
        money = 500;
        dialogs = friends = friendRequests = new ArrayList<>();
        lastModified = lastLoginAt = registration =Instant.now().toEpochMilli();
        relations = new ArrayList<>();
        for (int i = 0; i < 9; i++)
            relations.add(0);
    }

    public void hashPassword(String password) {
        this.password = String.valueOf(password.hashCode());
        token = Security.encrypt(login + ":" + password);
    }

    public Note addNote(String title, String content) {
        Note note;
        if (notes.size() != 0)
            note = new Note(notes.get(notes.size() - 1).cid + 1,
                    title, content);
        else
            note = new Note(1, title, content);
        notes.add(note);
        return note;
    }

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
