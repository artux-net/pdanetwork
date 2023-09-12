package net.artux.pdanetwork;

import net.artux.pdanetwork.entity.achievement.AchievementGroup;
import net.artux.pdanetwork.models.achievement.AchCategoryCreateDto;
import net.artux.pdanetwork.models.achievement.AchCategoryDto;
import net.artux.pdanetwork.models.achievement.AchDto;
import net.artux.pdanetwork.models.achievement.AchievementCreateDto;
import net.artux.pdanetwork.repository.achievement.AchievementCategoryRepository;
import net.artux.pdanetwork.repository.achievement.AchievementRepository;
import net.artux.pdanetwork.service.achievement.AchievementService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AchievementTest {

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private AchievementCategoryRepository achievementCategoryRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = "MODERATOR")
    public void createCategory() {
        AchCategoryCreateDto achCategoryCreateDto = getTemplateCategoryCreateDto(1);
        AchCategoryDto categoryDTO = achievementService.createCategory(achCategoryCreateDto);
        Assertions.assertNotNull(categoryDTO.name());
        Assertions.assertTrue(achievementCategoryRepository.findById(achCategoryCreateDto.name()).isPresent());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = "MODERATOR")
    public void createAchievement() {
        AchievementCreateDto achievementCreateDto = getTemplateAchievementCreateDto(1);
        AchDto achievementDTO = achievementService.createAchievement(getTemplateAchievementCreateDto(1).name(), achievementCreateDto);
        Assertions.assertNotNull(achievementDTO.name());
        Assertions.assertTrue(achievementRepository.findById(achievementCreateDto.name()).isPresent());
    }

    private static AchCategoryCreateDto getTemplateCategoryCreateDto(int postfix) {
        return new AchCategoryCreateDto(
                "title test" + postfix,
                "name test" + postfix,
                "description test" + postfix,
                "image test" + postfix
        );
    }

    private static AchievementCreateDto getTemplateAchievementCreateDto(int postfix) {
        return new AchievementCreateDto(
                "title test" + postfix,
                "name test" + postfix,
                "description test" + postfix,
                "image test" + postfix,
                AchievementGroup.ACTIVITY,
                getTemplateCondition(postfix));
    }

    private static Map<String, HashSet<String>> getTemplateCondition(int postfix) {
        Map<String, HashSet<String>> condition = new HashMap<>();
        {
            HashSet<String> attrValues = new HashSet<>();
            attrValues.add("attribute1 value1 test" + postfix);
            attrValues.add("attribute1 value2 test" + postfix);
            condition.put("attribute1 test" + postfix, attrValues);
        }
        {
            HashSet<String> attrValues = new HashSet<>();
            attrValues.add("attribute2 value1 test" + postfix);
            attrValues.add("attribute2 value2 test" + postfix);
            condition.put("attribute2 test" + postfix, attrValues);
        }
        return condition;
    }
}
