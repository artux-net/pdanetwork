package net.artux.pdanetwork.service.achievement

import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.models.achievement.AchCategoryCreateDto
import net.artux.pdanetwork.models.achievement.AchievementCreateDto
import net.artux.pdanetwork.models.achievement.AchievementGroup
import net.artux.pdanetwork.repository.achievement.AchievementCategoryRepository
import net.artux.pdanetwork.repository.achievement.AchievementRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import java.util.UUID

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ActiveProfiles(profiles = ["default", "dev", "test"])
class AchievementTest : AbstractTest() {

    @Autowired
    private val achievementService: AchievementService? = null

    @Autowired
    private val achievementCategoryRepository: AchievementCategoryRepository? = null

    @Autowired
    private val achievementRepository: AchievementRepository? = null

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = ["MODERATOR"])
    fun createCategory() {
        val achCategoryCreateDto = getTemplateCategoryCreateDto(1)
        val categoryDTO = achievementService!!.createCategory(achCategoryCreateDto)
        Assertions.assertNotNull(categoryDTO)
        categoryId = categoryDTO.id
        Assertions.assertTrue(achievementCategoryRepository!!.findById(categoryDTO.id).isPresent)
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = ["MODERATOR"])
    fun createAchievement() {
        val achievementCreateDto = getTemplateAchievementCreateDto(1)
        val achievementDto = achievementService!!.createAchievement(categoryId, achievementCreateDto)
        achievementId = achievementDto.id
        Assertions.assertNotNull(achievementDto)
        Assertions.assertTrue(achievementRepository!!.findById(achievementDto.id).isPresent)
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = ["MODERATOR"])
    fun updateCategory() {
        val achCategoryCreateDto = getTemplateCategoryCreateDto(2)
        val categoryDTO = achievementService!!.updateCategory(categoryId, achCategoryCreateDto)
        Assertions.assertNotNull(categoryDTO)
        Assertions.assertTrue(achievementCategoryRepository!!.findById(categoryDTO.id).isPresent)
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", roles = ["MODERATOR"])
    fun updateAchievement() {
        val achievementCreateDto = getTemplateAchievementCreateDto(2)
        val achievementDto = achievementService!!.updateAchievement(achievementId, achievementCreateDto)
        Assertions.assertNotNull(achievementDto)
        Assertions.assertTrue(achievementRepository!!.findById(achievementDto.id).isPresent)
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", roles = ["MODERATOR"])
    fun deleteAchievement() {
        achievementService!!.deleteAchievement(achievementId)
        Assertions.assertFalse(achievementRepository!!.findById(achievementId).isPresent)
    }

    @Test
    @Order(6)
    @WithMockUser(username = "admin", roles = ["MODERATOR"])
    fun deleteCategory() {
        achievementService!!.deleteCategory(categoryId)
        Assertions.assertFalse(achievementCategoryRepository!!.findById(categoryId).isPresent)
    }

    companion object {
        private var categoryId: UUID? = null
        private var achievementId: UUID? = null
        private fun getTemplateCategoryCreateDto(postfix: Int): AchCategoryCreateDto {
            return AchCategoryCreateDto(
                "title test$postfix",
                "name test$postfix",
                "description test$postfix",
                "image test$postfix"
            )
        }

        private fun getTemplateAchievementCreateDto(postfix: Int): AchievementCreateDto {
            return AchievementCreateDto(
                "title test$postfix",
                "name test$postfix",
                "description test$postfix",
                "image test$postfix",
                AchievementGroup.ACTIVITY,
                getTemplateCondition(postfix)
            )
        }

        private fun getTemplateCondition(postfix: Int): Map<String, HashSet<String>> {
            val condition: MutableMap<String, HashSet<String>> = HashMap()
            run {
                val attrValues = HashSet<String>()
                attrValues.add("attribute1 value1 test$postfix")
                attrValues.add("attribute1 value2 test$postfix")
                condition.put("attribute1 test$postfix", attrValues)
            }
            run {
                val attrValues = HashSet<String>()
                attrValues.add("attribute2 value1 test$postfix")
                attrValues.add("attribute2 value2 test$postfix")
                condition.put("attribute2 test$postfix", attrValues)
            }
            return condition
        }
    }
}
