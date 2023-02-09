package net.artux.pdanetwork.service.user.ban;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.user.BanEntity;
import net.artux.pdanetwork.models.user.ban.BanDto;
import net.artux.pdanetwork.models.user.ban.BanMapper;
import net.artux.pdanetwork.repository.user.BanRepository;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BanServiceImpl implements BanService {

    private static final HashMap<UUID, BanEntity> currentBanMap = new HashMap<>();
    private final Timer timer = new Timer();

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
    public BanDto applyBan(UUID userId, String reason, String message, int secs) {
        return applyBan(userId, null, reason, message, secs);
    }

    @Override
    public BanDto applyBan(UUID userId, UUID by, String reason, String message, int secs) {
        BanEntity banEntity = new BanEntity();
        banEntity.setBy(userService.getUserById(by));
        banEntity.setUser(userService.getUserById(userId));
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
        return getCurrentBan(userId);
    }

    @Override
    public BanDto getCurrentBan(UUID userId) {
        return banMapper.dto(currentBanMap.get(userId));
    }

}
