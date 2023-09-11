package net.artux.pdanetwork;

import net.artux.pdanetwork.entity.achievement.AchievementGroup;
import net.artux.pdanetwork.models.achievement.AchCategoryCreateDto;
import net.artux.pdanetwork.models.achievement.AchCategoryDto;
import net.artux.pdanetwork.models.achievement.AchDto;
import net.artux.pdanetwork.models.achievement.AchievementCreateDto;
import net.artux.pdanetwork.service.achievement.AchievementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AchievementTest {

    @Autowired
    private AchievementService achievementService;
    private String testCategoryName;

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = "MODERATOR")
    public void createCategory() {
        AchCategoryCreateDto achCategoryCreateDto = new AchCategoryCreateDto(
                "title test",
                "name test",
                "description test",
                "image test"
        );
        AchCategoryDto categoryDTO = achievementService.createCategory(achCategoryCreateDto);
        Assertions.assertNotNull(categoryDTO.id());
        testCategoryName = categoryDTO.name();
    }

    @Test
    @Order(2)
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
        AchDto achievementDTO = achievementService.createAchievement(testCategoryName, achievementCreateDto);
        Assertions.assertNotNull(achievementDTO.id());
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
