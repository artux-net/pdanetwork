package net.artux.pdanetwork.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.artux.pdanetwork.entity.BaseEntity;
import net.artux.pdanetwork.entity.achievement.AchievementEntity;
import net.artux.pdanetwork.entity.feed.ArticleEntity;
import net.artux.pdanetwork.entity.feed.PostEntity;
import net.artux.pdanetwork.entity.items.ArmorEntity;
import net.artux.pdanetwork.entity.items.ArtifactEntity;
import net.artux.pdanetwork.entity.items.BulletEntity;
import net.artux.pdanetwork.entity.items.DetectorEntity;
import net.artux.pdanetwork.entity.items.ItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.entity.items.MedicineEntity;
import net.artux.pdanetwork.entity.items.UsualItemEntity;
import net.artux.pdanetwork.entity.items.WeaponEntity;
import net.artux.pdanetwork.entity.items.WearableEntity;
import net.artux.pdanetwork.entity.note.NoteEntity;
import net.artux.pdanetwork.entity.user.gang.GangRelationEntity;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.models.user.gang.Gang;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private Integer xp;
    private Integer money;
    @Column(columnDefinition = "boolean default false")
    private Boolean chatBan;
    @Column(columnDefinition = "boolean default true")
    private Boolean receiveEmails;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Instant registration;
    private Instant lastLoginAt;

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private List<NoteEntity> notes;

    //@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    @ManyToMany
    private List<AchievementEntity> achievements;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StoryStateEntity> storyStates;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ParameterEntity> parameters;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BulletEntity> bullets;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArmorEntity> armors;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WeaponEntity> weapons;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MedicineEntity> medicines;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArtifactEntity> artifacts;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DetectorEntity> detectors;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsualItemEntity> items;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "article_like",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id"))
    private Set<ArticleEntity> likedArticles;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_like",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<PostEntity> likedPosts;

    public UserEntity(RegisterUserDto registerUser, PasswordEncoder passwordEncoder, Role role) {
        login = registerUser.getLogin();
        password = passwordEncoder.encode(registerUser.getPassword());
        email = registerUser.getEmail();
        name = registerUser.getName();
        nickname = registerUser.getNickname();
        avatar = registerUser.getAvatar();
        this.role = role;
        gang = Gang.LONERS;
        chatBan = false;
        gangRelation = new GangRelationEntity(this);
        receiveEmails = true;
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

    public boolean canBuy(int price) {
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
        resetItems();
    }

    public void resetItems() {
        armors.clear();
        bullets.clear();
        weapons.clear();
        medicines.clear();
        artifacts.clear();
        detectors.clear();
        items.clear();
    }

    public Set<? extends ItemEntity> getItemsByType(ItemType type) {
        return switch (type) {
            case ARMOR -> armors;
            case RIFLE, PISTOL ->
                    weapons.stream().filter(weapon -> weapon.getBasedType() == type).collect(Collectors.toSet());
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

    public Optional<? extends ItemEntity> findItem(long baseId) {
        return Stream.of(armors, weapons, bullets, medicines, detectors, artifacts)
                .flatMap(Set::stream)
                .filter(item -> item.getBasedId() == baseId)
                .findFirst();
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

    public void fixAllItems() {
        fixItems(armors);
        fixItems(weapons);
        fixItems(bullets);
        fixItems(medicines);
        fixItems(detectors);
        fixItems(artifacts);
    }

    private void fixItems(Set<? extends ItemEntity> set) {
        set.removeIf(itemEntity -> itemEntity.getQuantity() < 1);
        set.stream()
                .filter(itemEntity -> !itemEntity.getBase().getType().isCountable())
                .forEach(itemEntity -> itemEntity.setQuantity(1));

        set.removeIf(itemEntity ->{
            if (itemEntity instanceof ArmorEntity && ((ArmorEntity) itemEntity).getCondition() < 10)
                return true;

            return itemEntity instanceof WeaponEntity && ((WeaponEntity) itemEntity).getCondition() < 10;
        });
    }

    public StoryStateEntity getStoryState(int storyId) {
        return getStoryStates().stream().filter(storyStateEntity -> storyId == storyStateEntity.getStoryId()).findFirst().orElse(null);
    }

    public StoryStateEntity getCurrentStoryState() {
        return getStoryStates().stream().filter(StoryStateEntity::isCurrent).findFirst().orElse(null);
    }

    public void addAchievement(Optional<AchievementEntity> byId) {
        if (byId.isEmpty())
            return;
        achievements.add(byId.get());
    }
}
