package net.artux.pdanetwork.service.quest

import mu.KLogging
import net.artux.pdanetwork.entity.mappers.QuestMapper
import net.artux.pdanetwork.entity.user.UserEntity
import net.artux.pdanetwork.models.Status
import net.artux.pdanetwork.models.quest.ChapterDto
import net.artux.pdanetwork.models.quest.GameMap
import net.artux.pdanetwork.models.quest.Story
import net.artux.pdanetwork.models.quest.StoryDto
import net.artux.pdanetwork.models.quest.StoryInfo
import net.artux.pdanetwork.models.quest.admin.StoriesStatus
import net.artux.pdanetwork.models.quest.stage.Stage
import net.artux.pdanetwork.models.user.enums.Role
import net.artux.pdanetwork.service.user.UserService
import net.artux.pdanetwork.utils.security.AdminAccess
import net.artux.pdanetwork.utils.security.CreatorAccess
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.EnumMap
import java.util.LinkedList
import java.util.Locale
import java.util.UUID

@Service
@Suppress("TooManyFunctions")
open class QuestServiceImpl(
    private val questMapper: QuestMapper,
    private val userService: UserService,
    private val questBackupService: QuestBackupService
) : QuestService {

    private val stories: MutableMap<Long, StoryDto> = HashMap()
    private val roleStories: MutableMap<Role, List<StoryDto>> = EnumMap(Role::class.java)
    private val usersStories: MutableMap<UUID, StoryDto> = HashMap()
    private var updatedTime: Instant? = null

    @Suppress("MagicNumber")
    private var lastStoryId: Long = -2 // TODO refactor

    @CreatorAccess
    override fun setUserStory(story: Story, message: String): Status {
        story.id = lastStoryId + 1
        usersStories[userService.getCurrentId()] = questMapper.dto(story)
        questBackupService.saveStory(story, message)

        return Status(true, "История загружена, размещена в архиве. Сбросьте кэш пда для появления.")
    }

    @CreatorAccess
    override fun setUserStory(backupId: UUID): Status {
        val story = questBackupService.getBackup(backupId)
        usersStories[userService.getCurrentId()] = questMapper.dto(story)

        return Status(true, "Пользовательская история взята из хранилища и установлена текущей.")
    }

    @AdminAccess
    override fun setPublicStory(story: Story, message: String): Status {
        // questBackupService.saveStory(story, StoryType.PUBLIC, message);
        return reloadPublicStories(listOf(story))
    }

    override fun getStage(storyId: Long, chapterId: Long, stageId: Long): Stage {
        return getChapter(storyId, chapterId).getStage(stageId)
    }

    override fun getActionsOfStage(storyId: Long, chapterId: Long, stageId: Long): Map<String, List<String>> {
        return getStage(storyId, chapterId, stageId).actions
    }

    override fun getChapter(storyId: Long, chapterId: Long): ChapterDto {
        return getStory(storyId).chapters[chapterId]!!
    }

    override fun getMap(storyId: Long, mapId: Long): GameMap {
        return getStory(storyId).maps[mapId]!!
    }

    override fun getStory(storyId: Long): StoryDto {
        val user = userService.getCurrentUser()
        val story = stories[storyId]
            ?: usersStories[user.id]
            ?: questMapper.dto(questBackupService.getCommunityStory(storyId))

        if (story == null) {
            logger.error("Истории $storyId не существует")
        }
        if (story != null && story.access.priority > user.role.priority) {
            logger.error("User has no access to the story")
        }

        return story
    }

    override fun reloadPublicStories(stories: Collection<Story>): Status {
        var counter = 0
        for (story in stories) {
            if (story.id > lastStoryId) {
                lastStoryId = story.id
            }
            this.stories[story.id] = questMapper.dto(story)
            counter++
        }

        for (role in Role.entries) {
            val roleStories: MutableList<StoryDto> = LinkedList()
            for (story in this.stories.values) {
                if (story.access.priority <= role.priority) {
                    roleStories.add(story)
                }
            }
            this.roleStories[role] = roleStories
        }

        updatedTime = Instant.now()
        val message = "Установлены публичные истории, количество: $counter"
        logger.info(message)
        return Status(true, message)
    }

    private fun getStories(user: UserEntity): Collection<StoryDto> {
        val role = user.role
        return roleStories[role]!!
    }

    override fun getPublicStories(locale: Locale): Collection<StoryInfo> {
        val user = userService.getCurrentUser()

        val storiesInfo = getStories(user)
            .filter { it.locale == null || it.locale.language == locale.language }
            .toMutableList()
            .also {
                val userStory = usersStories[user.id]
                if (userStory != null) {
                    it.add(userStory)
                }
            }

        return storiesInfo.map { questMapper.info(it) }
    }

    override fun getCommunityStories(locale: Locale): Collection<StoryInfo> {
        return questMapper.info(
            questMapper.dto(
                questBackupService.communityStories
                    .filter { it.locale == null || it.locale.language == locale.language }
            )
        )
    }

    override fun getStatus(): StoriesStatus {
        return StoriesStatus.builder()
            .readTime(updatedTime)
            .userStories(usersStories.size)
            .stories(questMapper.adminInfo(stories.values))
            .build()
    }

    companion object : KLogging()
}
