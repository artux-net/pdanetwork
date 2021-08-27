package net.artux.pdanetwork.service.files;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.artux.pdanetwork.models.profile.Achievement;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static net.artux.pdanetwork.utills.FileGenerator.readFile;

@Service
public class AchievementsService implements FileService{

    private static List<Achievement> achievements;
    private final ValuesService valuesService;

    public AchievementsService(ValuesService valuesService) {
        this.valuesService = valuesService;
        reset();
    }

    private List<Achievement> setupAchievements() {
        try {
            String commonFile = readFile(valuesService.getWorkingDirectory() + "base/items/achievements", StandardCharsets.UTF_8);
            Type itemsListType = TypeToken.getParameterized(ArrayList.class, Achievement.class).getType();
            return new Gson().fromJson(commonFile, itemsListType);
        } catch (IOException e) {
            //ServletContext.error("Achievements error", e);
        }
        return null;
    }

    public static List<Achievement> getAchievements() {
        return achievements;
    }

    @Override
    public void reset() {
        achievements = new ArrayList<>();
        achievements = setupAchievements();
    }
}
