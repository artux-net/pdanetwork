package net.artux.pdanetwork.service.quest

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import mu.KLogging
import net.artux.pdanetwork.models.Status
import net.artux.pdanetwork.models.quest.Chapter
import net.artux.pdanetwork.models.quest.GameMap
import net.artux.pdanetwork.models.quest.Story
import net.artux.pdanetwork.models.quest.map.MapEnum
import net.artux.pdanetwork.models.quest.workflow.Trigger
import net.artux.pdanetwork.models.user.enums.Role
import net.artux.pdanetwork.service.user.UserService
import net.artux.pdanetwork.service.util.ValuesService
import net.artux.pdanetwork.utils.security.AdminAccess
import net.lingala.zip4j.ZipFile
import org.apache.commons.compress.utils.IOUtils
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.util.UriComponentsBuilder
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.UUID

@Service
open class QuestManagerServiceImpl(
    private val objectMapper: ObjectMapper,
    private val userService: UserService,
    private val valuesService: ValuesService,
    private val questService: QuestService,
    private val questBackupService: QuestBackupService
) : QuestManagerService {

    @PostConstruct
    fun init() {
        if (valuesService.isInitStoriesEnabled) {
            val r2Status = readFromR2()
            if (r2Status.isFailure) readFromGit()
        }
    }

    override fun readFromR2(): Status {
        val stories = questBackupService.getPublicStories()
        questService.reloadPublicStories(stories)
        logger.info("Истории из R2, количество: {}", stories.size)
        return if (stories.isEmpty()) {
            Status(false, "Истории из R2, количество: " + 0)
        } else {
            Status(
                true,
                "Истории из R2, количество: " + stories.size
            )
        }
    }

    @AdminAccess
    override fun readFromGit(): Status {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        val builder = UriComponentsBuilder
            .fromHttpUrl(valuesService.storiesWebhookAddress)
        val entity = HttpEntity<Trigger>(headers)
        logger.info("Download stories from " + valuesService.storiesWebhookAddress)
        val response: HttpEntity<ByteArray> = restTemplate.exchange(
            builder.toUriString(),
            HttpMethod.GET,
            entity,
            ByteArray::class.java
        )
        return try {
            saveStories(ByteArrayInputStream(response.body))
        } catch (e: IOException) {
            logger.error("Downloading stories error ", e)
            logger.info("Server answer: $response")
            Status(false, "Downloading stories error")
        }
    }

    @Suppress(
        "NestedBlockDepth",
        "LongMethod",
        "LoopWithTooManyJumpStatements",
        "ReturnCount",
        "CyclomaticComplexMethod"
    )
    private fun readStories(file: File): Status {
        val storiesDirs = file.listFiles() ?: return Status(false, "Empty stories folder.")
        if (storiesDirs.size == 1) return readStories(storiesDirs[0])
        var errors = 0
        val stories = HashMap<Long, Story>()
        for (storyDir in storiesDirs) {
            if (storyDir.isFile() || storyDir.getName().contains(".")) continue
            try {
                val story = objectMapper.readValue(
                    File("$storyDir/info.json"),
                    Story::class.java
                )

                // chapters
                val chapters = HashMap<Long, Chapter>()
                val chapterFiles = storyDir.listFiles() ?: continue
                for (chapterFile in chapterFiles) {
                    val filename = chapterFile.getName().lowercase()
                    if (chapterFile.isDirectory() ||
                        filename.contains("info.json") ||
                        filename.contains("missions.json")
                    ) {
                        continue
                    }
                    try {
                        val chapter = objectMapper.readValue(chapterFile, Chapter::class.java)
                        chapters[chapter.id] = chapter
                    } catch (e: IOException) {
                        errors++
                        logger.error("Error while reading chapter " + chapterFile.getName(), e)
                    }
                }
                story.chapters = chapters.values

                // maps
                val maps = HashMap<Long, GameMap>()
                for (enumGetter in MapEnum.entries) maps[enumGetter.id.toLong()] = GameMap(enumGetter)
                val mapFiles = File("$storyDir/maps").listFiles() ?: continue
                for (mapFile in mapFiles) {
                    try {
                        val gameMap = objectMapper.readValue(mapFile, GameMap::class.java)
                        if (maps.containsKey(gameMap.id)) {
                            if (gameMap.spawns != null) maps[gameMap.id]!!.spawns.addAll(gameMap.spawns)
                            if (gameMap.points != null) maps[gameMap.id]!!.points.addAll(gameMap.points)
                        } else {
                            maps[gameMap.id] = gameMap
                        }
                    } catch (e: IOException) {
                        errors++
                        logger.error("Error while reading map " + mapFile.getName(), e)
                    }
                }
                story.maps = maps.values
                stories[story.id] = story
            } catch (e: IOException) {
                errors++
                logger.error("Error while reading story " + storyDir.getName(), e)
            }
        }
        deleteDirectory(file)
        if (stories.values.size > 0) {
            questService.reloadPublicStories(stories.values)
            logger.info("Stories updated from github, count: {}", stories.values.size)
            return Status(true, "Stories updated, errors $errors")
        }
        logger.warn("Stories not found at " + valuesService.storiesDirectory)
        return Status(false, "Stories not updated, errors $errors")
    }

    override fun uploadStories(storiesArchive: MultipartFile): Status {
        return if (userService.getCurrentUser().role != Role.ADMIN) {
            Status(false, "Wrong role.")
        } else {
            try {
                saveStories(storiesArchive.inputStream)
            } catch (e: IOException) {
                logger.error("Error while saving stories. ", e)
                Status(false, "Could not save stories, update failed.")
            }
        }
    }

    @Throws(IOException::class)
    private fun saveStories(inputStream: InputStream): Status {
        val zip = File.createTempFile(UUID.randomUUID().toString(), "temp")
        val fileOutputStream = FileOutputStream(zip)
        IOUtils.copy(inputStream, fileOutputStream)
        fileOutputStream.close()
        val zipFile = ZipFile(zip)
        zipFile.extractAll(valuesService.storiesDirectory)
        val status = readStories(File(valuesService.storiesDirectory))
        zip.delete()
        return status
    }

    companion object : KLogging() {

        @Suppress("NestedBlockDepth")
        fun deleteDirectory(path: File) {
            if (path.exists()) {
                val files = path.listFiles()
                if (files != null) {
                    for (i in files.indices) {
                        if (files[i].isDirectory()) {
                            deleteDirectory(files[i])
                        } else {
                            files[i].delete()
                        }
                    }
                }
            }
            path.delete()
        }
    }
}
