package net.artux.pdanetwork.entity.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;
import net.artux.pdanetwork.entity.achievement.UserAchievementEntity;
import net.artux.pdanetwork.entity.items.*;
import net.artux.pdanetwork.entity.note.NoteEntity;
import net.artux.pdanetwork.entity.user.gang.GangRelationEntity;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.models.user.gang.Gang;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pda_user") //todo entityGraph for eager
public class UserEntity extends BaseEntity {

    @Column(unique = true)
    private String login;
    private String password;
    @Column(unique = true)
    private String email;
    private String name;
    private String nickname;
    private String avatar;

    @Enumerated(EnumType.STRING)
    private Gang gang;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private GangRelationEntity gangRelation;
    private int xp;
    private int money;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Instant registration;
    private Instant lastLoginAt;

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private List<NoteEntity> notes;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<UserAchievementEntity> achievements;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StoryStateEntity> storyStates = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ParameterEntity> parameters = new HashSet<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BulletEntity> bullets = new HashSet<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArmorEntity> armors = new HashSet<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WeaponEntity> weapons = new HashSet<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MedicineEntity> medicines = new HashSet<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArtifactEntity> artifacts = new HashSet<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DetectorEntity> detectors = new HashSet<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsualItemEntity> items = new HashSet<>();

    public UserEntity(RegisterUserDto registerUser, PasswordEncoder passwordEncoder) {
        login = registerUser.getLogin();
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
        lastLoginAt = registration = Instant.now();
    }

    public long getPdaId() {
        return Math.abs(id.getMostSignificantBits()) % 100000;
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

    public void reset() {
        money = 500;
        gangRelation.resetAll();
        notes.clear();
        parameters.clear();
        armors.clear();
        bullets.clear();
        weapons.clear();
        medicines.clear();
        artifacts.clear();
        detectors.clear();
    }

    public Set<? extends ItemEntity> getItemsByType(ItemType type) {
        return switch (type) {
            case ARMOR -> armors;
            case RIFLE, PISTOL -> weapons;
            case MEDICINE -> medicines;
            case ARTIFACT -> artifacts;
            case DETECTOR -> detectors;
            case BULLET -> bullets;
            case ITEM -> items;
        };
    }

    @SuppressWarnings("unchecked")
    public <T> Set<T> getItemsByClass(Class<T> tClass) {
        return (Set<T>) getItemsByType(ItemType.getByClass(tClass));
    }

    public Set<WearableEntity> getWearableItems() {
        HashSet<WearableEntity> itemEntities = new HashSet<>();
        itemEntities.addAll(armors);
        itemEntities.addAll(weapons);
        itemEntities.addAll(detectors);
        itemEntities.addAll(artifacts);
        return itemEntities;
    }

    public Set<? extends ItemEntity> getAllItems() {
        HashSet<ItemEntity> itemEntities = new HashSet<>();
        itemEntities.addAll(armors);
        itemEntities.addAll(weapons);
        itemEntities.addAll(bullets);
        itemEntities.addAll(medicines);
        itemEntities.addAll(detectors);
        itemEntities.addAll(artifacts);
        return itemEntities;
    }

    public StoryStateEntity getStoryState(int storyId) {
        return getStoryStates().stream().filter(storyStateEntity -> storyId == storyStateEntity.getStoryId()).findFirst().orElse(null);
    }

    public StoryStateEntity getCurrentStoryState() {
        return getStoryStates().stream().filter(StoryStateEntity::isCurrent).findFirst().orElse(null);
    }
}
