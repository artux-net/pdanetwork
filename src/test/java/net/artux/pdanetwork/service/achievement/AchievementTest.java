package net.artux.pdanetwork.service.achievement;

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
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AchievementTest {

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private AchievementCategoryRepository achievementCategoryRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    private static UUID categoryId;
    private static UUID achievementId;

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = "MODERATOR")
    public void createCategory() {
        AchCategoryCreateDto achCategoryCreateDto = getTemplateCategoryCreateDto(1);
        AchCategoryDto categoryDTO = achievementService.createCategory(achCategoryCreateDto);
        Assertions.assertNotNull(categoryDTO);
        categoryId = categoryDTO.id();
        Assertions.assertTrue(achievementCategoryRepository.findById(categoryDTO.id()).isPresent());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = "MODERATOR")
    public void createAchievement() {
        AchievementCreateDto achievementCreateDto = getTemplateAchievementCreateDto(1);
        AchDto achievementDto = achievementService.createAchievement(categoryId, achievementCreateDto);
        achievementId = achievementDto.id();
        Assertions.assertNotNull(achievementDto);
        Assertions.assertTrue(achievementRepository.findById(achievementDto.id()).isPresent());
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = "MODERATOR")
    public void updateCategory() {
        AchCategoryCreateDto achCategoryCreateDto = getTemplateCategoryCreateDto(2);
        AchCategoryDto categoryDTO = achievementService.updateCategory(categoryId, achCategoryCreateDto);
        Assertions.assertNotNull(categoryDTO);
        Assertions.assertTrue(achievementCategoryRepository.findById(categoryDTO.id()).isPresent());
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", roles = "MODERATOR")
    public void updateAchievement() {
        AchievementCreateDto achievementCreateDto = getTemplateAchievementCreateDto(2);
        AchDto achievementDto = achievementService.updateAchievement(achievementId, achievementCreateDto);
        Assertions.assertNotNull(achievementDto);
        Assertions.assertTrue(achievementRepository.findById(achievementDto.id()).isPresent());
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", roles = "MODERATOR")
    public void deleteAchievement() {
        achievementService.deleteAchievement(achievementId);
        Assertions.assertFalse(achievementRepository.findById(achievementId).isPresent());
    }

    @Test
    @Order(6)
    @WithMockUser(username = "admin", roles = "MODERATOR")
    public void deleteCategory() {
        achievementService.deleteCategory(categoryId);
        Assertions.assertFalse(achievementCategoryRepository.findById(categoryId).isPresent());
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
