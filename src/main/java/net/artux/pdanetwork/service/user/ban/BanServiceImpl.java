package net.artux.pdanetwork.service.user.ban;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.BanEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.user.ban.BanDto;
import net.artux.pdanetwork.models.user.ban.BanMapper;
import net.artux.pdanetwork.repository.user.BanRepository;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.utills.security.IsModerator;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class BanServiceImpl implements BanService {

    private static final ConcurrentHashMap<UUID, BanEntity> currentBanMap = new ConcurrentHashMap<>();
    private final Timer timer = new Timer();

    private final Logger logger;
    private final BanMapper banMapper;
    private final BanRepository repository;
    private final UserService userService;

    @Override
    public List<BanDto> getBans(UUID userId) {
        return banMapper.dto(repository.findAllByUserId(userId));
    }

    @Override
    public boolean isBanned(UUID userId) {
        return currentBanMap.containsKey(userId);
    }

    @Override
    public boolean setChatBan(UUID userId) {
        return userService.setChatBan(userId);
    }

    @Override
    public BanDto applySystemBan(UUID userId, String reason, String message, int secs) {
        BanEntity banEntity = new BanEntity();
        banEntity.setBy(null);
        return banUser(userId, reason, message, secs, banEntity);
    }

    private BanDto banUser(UUID userId, String reason, String message, int secs, BanEntity banEntity) {
        UserEntity user = userService.getUserById(userId);
        banEntity.setUser(user);
        banEntity.setReason(reason);
        banEntity.setMessage(message);
        banEntity.setSeconds(secs);

        currentBanMap.put(userId, repository.save(banEntity));
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentBanMap.remove(userId);
            }
        }, 1000L * secs);
        logger.info("User {} banned for {} seconds, reason: ", user.getLogin(), secs, reason);
        return getCurrentBan(userId);
    }

    @Override
    @IsModerator
    public BanDto applyBan(UUID userId, String reason, String message, int secs) {
        UserEntity user = userService.getUserById();
        BanEntity banEntity = new BanEntity();
        banEntity.setBy(user);
        return banUser(userId, reason, message, secs, banEntity);
    }

    @Override
    public BanDto getCurrentBan(UUID userId) {
        return banMapper.dto(currentBanMap.get(userId));
    }

    @Override
    @Transactional
    public boolean clearAllBans(UUID uuid) {
        currentBanMap.remove(uuid);
        repository.deleteAllByUserId(uuid);
        return true;
    }

    @Override
    @Transactional
    public boolean clearBan(UUID banId) {
        BanEntity ban = repository.findById(banId).orElseThrow(() -> new RuntimeException("Ban not found"));
        currentBanMap.remove(ban.getUser().getId());
        repository.deleteById(banId);
        return true;
    }

    @Override
    public Map<UUID, BanDto> getCurrentBans() {
        Map<UUID, BanDto> bans = new HashMap<>();
        currentBanMap.forEach((uuid, banEntity) -> {
            bans.put(uuid, banMapper.dto(banEntity));
        });
        return bans;
    }

}
