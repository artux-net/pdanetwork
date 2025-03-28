package net.artux.pdanetwork.service.quest

import mu.KLogging
import net.artux.pdanetwork.entity.mappers.QuestMapper
import net.artux.pdanetwork.models.Status
import net.artux.pdanetwork.models.quest.ChapterDto
import net.artux.pdanetwork.models.quest.GameMap
import net.artux.pdanetwork.models.quest.Story
import net.artux.pdanetwork.models.quest.StoryDto
import net.artux.pdanetwork.models.quest.StoryInfo
import net.artux.pdanetwork.models.quest.admin.StoriesStatus
import net.artux.pdanetwork.models.quest.stage.Stage
import net.artux.pdanetwork.service.user.UserService
import net.artux.pdanetwork.utils.security.AdminAccess
import net.artux.pdanetwork.utils.security.CreatorAccess
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Locale
import java.util.UUID

@Service
@Suppress("TooManyFunctions")
open class QuestServiceImpl(
    private val questMapper: QuestMapper,
    private val userService: UserService,
    private val questBackupService: QuestBackupService
) : QuestService {

    private val stories: MutableList<StoryDto> = mutableListOf()
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
        val story = stories.find { it.id == storyId }
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
            val index = this.stories.indexOfFirst { it.id == story.id }
            if (index != -1) {
                this.stories[index] = questMapper.dto(story)
            }
            counter++
        }

        updatedTime = Instant.now()
        val message = "Установлены публичные истории, количество: $counter"
        logger.info(message)
        return Status(true, message)
    }

    override fun getPublicStories(locale: Locale): Collection<StoryInfo> {
        val user = userService.getCurrentUser()

        val storiesInfo = questMapper.info(stories.filter { it.access.priority <= user.role.priority })
            .filter { it.locale.language == locale.language }
            .toMutableList()

        // пусть истории пользователя не будут отсеиваться по локали (пока)
        val userStory = questMapper.info(usersStories[user.id])
        if (userStory != null) {
            storiesInfo.add(userStory)
        }

        return storiesInfo
    }

    override fun getCommunityStories(locale: Locale): Collection<StoryInfo> {
        return questMapper.info(
            questMapper.dto(questBackupService.communityStories.filter { it.locale.language == locale.language })
        )
    }

    override fun getStatus(): StoriesStatus {
        return StoriesStatus.builder()
            .readTime(updatedTime)
            .userStories(usersStories.size)
            .stories(questMapper.adminInfo(stories))
            .build()
    }

    companion object : KLogging()
}
