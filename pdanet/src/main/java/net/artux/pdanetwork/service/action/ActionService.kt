package net.artux.pdanetwork.service.action

import mu.KLogging
import net.artux.pdanetwork.entity.items.ArmorEntity
import net.artux.pdanetwork.entity.items.ConditionalEntity
import net.artux.pdanetwork.entity.items.ItemEntity
import net.artux.pdanetwork.entity.items.WeaponEntity
import net.artux.pdanetwork.entity.mappers.StoryMapper
import net.artux.pdanetwork.entity.security.SecurityUser
import net.artux.pdanetwork.entity.user.ParameterEntity
import net.artux.pdanetwork.entity.user.StoryStateEntity
import net.artux.pdanetwork.entity.user.UserEntity
import net.artux.pdanetwork.models.command.ServerCommand
import net.artux.pdanetwork.models.note.NoteCreateDto
import net.artux.pdanetwork.models.user.dto.StoryData
import net.artux.pdanetwork.models.user.gang.Gang
import net.artux.pdanetwork.repository.achievement.AchievementRepository
import net.artux.pdanetwork.repository.user.UserRepository
import net.artux.pdanetwork.service.items.ItemService
import net.artux.pdanetwork.service.note.NoteService
import net.artux.pdanetwork.service.quest.QuestService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import java.util.UUID
import java.util.function.Consumer

@Service
@Suppress("TooManyFunctions")
open class ActionService(
    private val itemsService: ItemService,
    private val questService: QuestService,
    private val noteService: NoteService,
    private val storyMapper: StoryMapper,
    private val userRepository: UserRepository,
    private val achievementRepository: AchievementRepository,
) {

    private val timer = Timer()

    fun applyCommands(id: UUID, map: Map<String, List<String>>): StoryData {
        val userEntity = userRepository.findById(id).orElseThrow()
        operateActions(map, userEntity)
        userEntity.fixAllItems()
        return storyMapper.storyData(userRepository.saveAndFlush(userEntity))
    }

    fun applyCommands(map: Map<String, List<String>>): StoryData {
        val id = (
            SecurityContextHolder.getContext()
                .authentication.principal as SecurityUser
            ).id
        return applyCommands(id, map)
    }

    @Suppress("TooGenericExceptionCaught")
    private fun operateActions(actions: Map<String, List<String>>, userEntity: UserEntity) {
        if (actions.isEmpty()) return
        logger.info("Commands for {} : {}", userEntity.login, actions)
        for (command in actions.keys) {
            try {
                val params = actions[command]!!.toMutableList()
                val timerParam =
                    params.stream().filter { s: String -> s.lowercase(Locale.getDefault()).contains("timer:") }
                        .findFirst()
                if (timerParam.isPresent) {
                    val minutes = timerParam.get().substring(TIMER_START_TIME_INDEX).toInt()
                    params.remove(timerParam.get())
                    timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                doCommand(command, params, userEntity)
                            }
                        },
                        Duration.ofMinutes(minutes.toLong()).toMillis()
                    )
                } else {
                    doCommand(command, params, userEntity)
                }
            } catch (e: Exception) {
                logger.error("Error executing command {} for user {}", e, userEntity.login)
            }
        }
    }

    @Suppress("LongMethod", "CyclomaticComplexMethod", "NestedBlockDepth")
    fun doCommand(command: String, params: List<String>, userEntity: UserEntity) {
        val enumServerCommand = ServerCommand.of(command)
        if (enumServerCommand == null) {
            logger.warn("Unknown command: {}, value: {}", command, params)
            return
        }
        when (enumServerCommand) {
            ServerCommand.ADD -> {
                for (value in params) {
                    val param = value.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (isNumber(param[0])) {
                        val baseId = param[0].toLong()
                        val quantity = param[1].toInt()
                        itemsService.addItem(userEntity, baseId, quantity)
                    } else if (param[0].contains("relation")) {
                        // "+":["relation_1:5"]
                        val group =
                            param[0].split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()
                        val gangRelation = userEntity.gangRelation
                        val gang = Gang.getById(group)
                        if (gang != null) gangRelation.addRelation(gang, param[1].toInt())
                    } else if (param.size == 2) {
                        // add_value
                        addValue(userEntity, param[0], param[1].toInt())
                    } else {
                        // add_param
                        addKey(userEntity, value)
                    }
                }
            }

            ServerCommand.REMOVE -> {
                for (pass in params) {
                    val values = pass.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (values[0].contains("relation")) {
                        val group =
                            values[0].split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()
                        val gangRelation = userEntity.gangRelation
                        val gang = Gang.getById(group)
                        if (gang != null) gangRelation.addRelation(gang, -values[1].toInt())
                    } else if (values.size == 2) {
                        if (isNumber(values[0])) {
                            val baseId = values[0].toInt()
                            val quantity = values[1].toInt()
                            itemsService.deleteItem(userEntity, baseId, quantity)
                        } else {
                            addValue(userEntity, values[0], -values[1].toInt())
                        }
                    } else {
                        userEntity.parameters
                            .removeIf { parameterEntity: ParameterEntity -> parameterEntity.getKey() == pass }
                    }
                }
            }

            ServerCommand.SET_VALUE -> {
                for (pass in params) {
                    val values = pass.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    setValue(userEntity, values[0], values[1].toInt())
                }
            }

            ServerCommand.MULTIPLY_VALUE -> {
                for (pass in params) {
                    val values = pass.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    multiplyValue(userEntity, values[0], values[1].toInt())
                }
            }

            ServerCommand.SET_MONEY -> {
                for (pass in params) userEntity.money(pass.toInt())
            }

            ServerCommand.SET_ITEM_QUANTITY -> {
                val items = userEntity.getAllItems()
                val quantityMap: MutableMap<UUID, Int> = HashMap()
                for (pass in params) {
                    val key = pass.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val quantity = key[1].toInt()
                    if (!isNumber(key[0])) {
                        val id = UUID.fromString(key[0])
                        quantityMap[id] = quantity
                    } else {
                        val baseId = key[0].toInt()
                        itemsService.addItem(userEntity, baseId.toLong(), quantity)
                    }
                }
                if (!quantityMap.isEmpty()) {
                    items.forEach(
                        Consumer<ItemEntity> { itemEntity: ItemEntity ->
                            if (quantityMap.containsKey(itemEntity.id)) {
                                itemEntity.quantity = quantityMap[itemEntity.id]!!
                                logger.debug(
                                    "Set quantity for " +
                                        itemEntity.id.toString() + ", " + itemEntity.quantity
                                )
                            }
                        }
                    )
                }
            }

            ServerCommand.SET_ITEM_CONDITION -> {
                val conditionMap: MutableMap<UUID, Float> = HashMap()
                for (pass in params) {
                    val key = pass.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val id = UUID.fromString(key[0])
                    val quantity = key[1].toFloat()
                    conditionMap[id] = quantity
                }
                if (conditionMap.isNotEmpty()) {
                    var items: Set<ConditionalEntity> = userEntity.armors
                    setConditions(items, conditionMap)

                    items = userEntity.weapons
                    setConditions(items, conditionMap)
                }
            }

            ServerCommand.SET_XP -> {
                for (pass in params) userEntity.xp(pass.toInt())
            }

            ServerCommand.SET_MAIN_ITEM -> {
                for (pass in params) {
                    if (isNumber(pass)) {
                        itemsService.setWearableItemById(userEntity, pass.toInt())
                    } else {
                        itemsService.setWearableItemById(userEntity, UUID.fromString(pass))
                    }
                }
            }

            ServerCommand.ADD_NOTE -> {
                if (params.size == 2) {
                    noteService.createNote(
                        NoteCreateDto(
                            params[0],
                            params[1]
                        )
                    )
                } else if (params.size == 1) noteService.createNote(NoteCreateDto("Новая заметка", params[0]))
            }

            ServerCommand.ACHIEVE -> {
                for (pass in params) userEntity.addAchievement(achievementRepository.findAchievementEntityByName(pass))
            }

            ServerCommand.SET_RELATION -> {
                val gang: String
                val relation: String
                when (params.size) {
                    2 -> {
                        gang = params[0]
                        relation = params[1]
                    }

                    1 -> {
                        val values = params[0].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        gang = values[0]
                        relation = values[1]
                    }

                    else -> return
                }
                userEntity.gangRelation
                    .setRelation(Gang.valueOf(gang), relation.toInt())
            }

            ServerCommand.SET_GROUP -> {
                val key = params[0]
                if (isNumber(key)) {
                    userEntity.gang = Gang.getById(key.toInt())
                } else {
                    userEntity.gang = Gang.valueOf(key)
                }
            }

            ServerCommand.RESET -> {
                if (params.isEmpty()) {
                    userEntity.reset()
                } else {
                    for (pass in params) if (pass.matches("-?\\d+".toRegex())) {
                        val storyId = pass.toInt()
                        val storyOptional = userEntity.getStoryState(storyId)
                        if (storyOptional != null) {
                            storyOptional.chapterId = 1
                            storyOptional.stageId = 0
                            storyOptional.isOver = false
                        }
                    } else if (pass.contains("relation")) {
                        val group = pass.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()
                        val gangRelation = userEntity.gangRelation
                        val gang = Gang.getById(group)
                        if (gang != null) gangRelation.addRelation(gang, 0)
                    } else if (pass.contains("wearable")) {
                        for (wearable in userEntity.getWearableItems()) {
                            wearable.isEquipped = false
                        }
                    } else if (pass.contains("weapons")) {
                        for (wearable in userEntity.getItemsByClass(WeaponEntity::class.java)) {
                            wearable.isEquipped = false
                        }
                    } else if (pass.contains("armors")) {
                        for (wearable in userEntity.getItemsByClass(ArmorEntity::class.java)) {
                            wearable.isEquipped = false
                        }
                    } else if (pass.contains("items")) {
                        userEntity.resetItems()
                    }
                }
            }

            ServerCommand.EXIT_STORY -> exitStory(userEntity)
            ServerCommand.FINISH_STORY -> finishStory(userEntity)
            ServerCommand.STATE -> {
                val states = params.toTypedArray<String>()
                if (states.isNotEmpty()) {
                    var values = states[0].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (values.size == STATE_ELEMENTS_COUNT) {
                        val story = values[0].toInt()
                        var chapter = values[2].toInt()
                        var stage = values[2].toInt()
                        var storyStateEntity = userEntity.getStoryState(story)
                        if (storyStateEntity == null) {
                            storyStateEntity = StoryStateEntity()
                            storyStateEntity.storyId = story
                            storyStateEntity.chapterId = chapter
                            storyStateEntity.stageId = stage
                            storyStateEntity.user = userEntity
                            userEntity.storyStates.add(storyStateEntity)
                        } else {
                            /*if (actualStage.getTransfers() != null && actualStage.getTransfers().size() > 0) {
                                int finalStage = stage;
                                boolean checkStart = actualStage.getTransfers().stream()
                                        .filter(transfer -> transfer.getStage() == finalStage)
                                        .toList()
                                        .size() > 0;
                                if (!checkStart)
                                    throw new RuntimeException();
                            }*/
                        }
                        storyStateEntity.isCurrent = true
                        for (state in states) {
                            values = state.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            chapter = values[1].toInt()
                            stage = values[2].toInt()
                            storyStateEntity.chapterId = chapter
                            storyStateEntity.stageId = stage
                            logger.debug("Process actions for {},{},{}", story, chapter, stage)
                            operateActions(
                                questService.getActionsOfStage(
                                    story.toLong(),
                                    chapter.toLong(),
                                    stage.toLong()
                                ),
                                userEntity
                            )
                        }
                    }
                }
            }

            else -> logger.error("Unsupported command: $command, value: $params")
        }
    }

    private fun setConditions(
        items: Set<ConditionalEntity>,
        conditionMap: MutableMap<UUID, Float>
    ) {
        items.forEach { itemEntity: ConditionalEntity ->
            if (conditionMap.containsKey(itemEntity.id)) {
                itemEntity.condition = conditionMap[itemEntity.id]!!
                logger.debug(
                    "Set condition for " +
                        itemEntity.id.toString() + ", " + itemEntity.quantity
                )
            }
        }
    }

    fun isNumber(s: String): Boolean {
        return s.matches("[0-9]+[\\\\.]?[0-9]*".toRegex())
    }

    fun finishStory(userEntity: UserEntity) {
        userEntity.currentStoryState.isOver = true
        exitStory(userEntity)
    }

    fun exitStory(userEntity: UserEntity) {
        val storyOptional = userEntity.currentStoryState
        if (storyOptional != null) storyOptional.isCurrent = false
    }

    fun multiplyValue(user: UserEntity, key: String, integer: Int?) {
        val s = user.parameters
            .stream()
            .filter { parameterEntity: ParameterEntity -> parameterEntity.getKey() == key }
            .findFirst()
        val entity: ParameterEntity
        if (s.isPresent) {
            entity = s.get()
            entity.value *= integer!!
        } else {
            entity = ParameterEntity(user, key, integer)
            user.parameters.add(entity)
        }
    }

    fun setValue(user: UserEntity, key: String, integer: Int?) {
        val s = user.parameters
            .stream()
            .filter { parameterEntity: ParameterEntity -> parameterEntity.getKey() == key }
            .findFirst()
        val entity: ParameterEntity
        if (s.isPresent) {
            entity = s.get()
            entity.value = integer
        } else {
            entity = ParameterEntity(user, key, integer)
            user.parameters.add(entity)
        }
    }

    fun addValue(user: UserEntity, key: String, integer: Int?) {
        val s = user.parameters
            .stream()
            .filter { parameterEntity: ParameterEntity -> parameterEntity.getKey() == key }
            .findFirst()
        val entity: ParameterEntity
        if (s.isPresent) {
            entity = s.get()
            entity.value += integer!!
        } else {
            entity = ParameterEntity(user, key, integer)
            user.parameters.add(entity)
        }
    }

    fun addKey(user: UserEntity, key: String) {
        val s = user.parameters
            .stream()
            .filter { parameterEntity: ParameterEntity -> parameterEntity.getKey() == key }
            .findFirst()
        if (s.isEmpty) {
            user.parameters.add(ParameterEntity(user, key, 0))
        }
    }

    companion object : KLogging() {
        private const val TIMER_START_TIME_INDEX = 6
        private const val STATE_ELEMENTS_COUNT = 3
    }
}
