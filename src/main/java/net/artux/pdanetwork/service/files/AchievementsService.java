package net.artux.pdanetwork.service.files;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.AchievementEntity;
import net.artux.pdanetwork.models.UserAchievementEntity;
import net.artux.pdanetwork.models.profile.Achievement;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.repository.achievements.UserAchievementsRepository;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AchievementsService implements FileService{

    private static List<Achievement> achievements;
    private final ValuesService valuesService;
    private final UserAchievementsRepository userAchievementsRepository;

    public AchievementsService(ValuesService valuesService, UserAchievementsRepository userAchievementsRepository) {
        this.userAchievementsRepository = userAchievementsRepository;
        this.valuesService = valuesService;
        reset();
    }

    private List<Achievement> setupAchievements() {
        try {
            String commonFile = readFile(valuesService.getConfigUrl() + "base/items/achievements.json");
            Type itemsListType = TypeToken.getParameterized(ArrayList.class, Achievement.class).getType();
            return new Gson().fromJson(commonFile, itemsListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Achievement> getAchievements() {
        return achievements;
    }

    public List<AchievementEntity> getForUser(UserEntity entity){
        return userAchievementsRepository.getAllByUser(entity).stream().map(new Function<UserAchievementEntity, AchievementEntity>() {
            @Override
            public AchievementEntity apply(UserAchievementEntity userAchievementEntity) {
                return userAchievementEntity.getAchievement();
            }
        }).collect(Collectors.toList());
    }
    @Override
    public void reset() {
        achievements = new ArrayList<>();
        achievements = setupAchievements();
    }
}
