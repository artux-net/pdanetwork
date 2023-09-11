package net.artux.pdanetwork;

import net.artux.pdanetwork.entity.achievement.AchievementCategoryEntity;
import net.artux.pdanetwork.entity.achievement.AchievementEntity;
import net.artux.pdanetwork.entity.achievement.AchievementGroup;
import net.artux.pdanetwork.models.achievement.*;
import net.artux.pdanetwork.repository.achievement.AchievementRepository;
import net.artux.pdanetwork.service.achievement.AchievementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AchievementTest {

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private AchMapper achMapper;

    @Autowired
    private AchievementRepository achievementRepository;

    @Test
    @WithMockUser(username = "admin", roles = "MODERATOR")
    public void createAchievement() {
        Map<String, HashSet<String>> condition = constructTestCondition();
        AchievementCreateDto achievementCreateDto = new AchievementCreateDto(
                "title test",
                "name test",
                "description test",
                "image test",
                AchievementGroup.ACTIVITY,
                condition);

        AchCategoryCreateDto achCategoryCreateDto = new AchCategoryCreateDto(
                "title test",
                "name test",
                "description test",
                "image test"
        );

        AchievementCategoryEntity achievementCategory = achMapper.toEntity(achCategoryCreateDto);
        AchievementEntity achievementEntity = achMapper.toEntity(achievementCreateDto, achievementCategory);

        AchievementEntity save = achievementRepository.save(achievementEntity);
        Optional<AchievementEntity> foundOptional = achievementRepository.findById(save.getName());
        Assertions.assertTrue(foundOptional.isPresent());
        AchievementEntity found = foundOptional.get();
        Assertions.assertTrue(TestingTools.compare(false, achievementEntity, save, found));
    }

    private static Map<String, HashSet<String>> constructTestCondition() {
        Map<String, HashSet<String>> condition = new HashMap<>();
        {
            HashSet<String> attrValues = new HashSet<>();
            attrValues.add("attribute1 value1");
            attrValues.add("attribute1 value2");
            condition.put("attribute1", attrValues);
        }
        {
            HashSet<String> attrValues = new HashSet<>();
            attrValues.add("attribute2 value1");
            attrValues.add("attribute2 value2");
            condition.put("attribute2", attrValues);
        }
        return condition;
    }
}
