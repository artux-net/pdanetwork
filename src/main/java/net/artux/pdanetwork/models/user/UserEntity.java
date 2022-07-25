package net.artux.pdanetwork.models.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.models.BaseEntity;
import net.artux.pdanetwork.entity.UserAchievementEntity;
import net.artux.pdanetwork.models.gang.Gang;
import net.artux.pdanetwork.models.achievement.GangRelationEntity;
import net.artux.pdanetwork.models.note.NoteEntity;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pda_user")
public class UserEntity extends BaseEntity {

    private UUID uid;
    private String login;
    private String password;
    private String email;
    private String name;
    private String nickname;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Gang gang;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private GangRelationEntity gangRelation;
    private int xp;
    private int money;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    public List<NoteEntity> notes;
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<UserAchievementEntity> achievements;

    @Enumerated(EnumType.STRING)
    private Role role;
    private Long registration;
    private Long lastLoginAt;

    public UserEntity(RegisterUserDto registerUser, PasswordEncoder passwordEncoder) {
        login = registerUser.getLogin();
        uid = UUID.randomUUID();
        password = passwordEncoder.encode(registerUser.getPassword());
        email = registerUser.getEmail();
        name = registerUser.getName();
        nickname = registerUser.getNickname();
        avatar = registerUser.getAvatar();
        role = Role.USER;
        gang = Gang.LONERS;
        gangRelation = new GangRelationEntity(this);
        xp = 0;
        money = 500;
        lastLoginAt = registration = Instant.now().toEpochMilli();
    }

    public long getPdaId() {
        return id;
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
