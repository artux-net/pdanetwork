package net.artux.pdanetwork.service.items

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.Nullable
import mu.KLogging
import net.artux.pdanetwork.dto.ItemsContainer
import net.artux.pdanetwork.entity.CloningMapper
import net.artux.pdanetwork.entity.items.ArmorEntity
import net.artux.pdanetwork.entity.items.ArtifactEntity
import net.artux.pdanetwork.entity.items.BulletEntity
import net.artux.pdanetwork.entity.items.DetectorEntity
import net.artux.pdanetwork.entity.items.ItemEntity
import net.artux.pdanetwork.entity.items.MedicineEntity
import net.artux.pdanetwork.entity.items.WeaponEntity
import net.artux.pdanetwork.entity.items.WearableEntity
import net.artux.pdanetwork.entity.mappers.ItemMapper
import net.artux.pdanetwork.entity.user.UserEntity
import net.artux.pdanetwork.models.Status
import net.artux.pdanetwork.models.items.ArtifactDto
import net.artux.pdanetwork.models.items.ItemDto
import net.artux.pdanetwork.repository.user.UserRepository
import net.artux.pdanetwork.service.user.UserService
import org.apache.poi.ss.formula.functions.T
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.LinkedList
import java.util.UUID

@Service
@Suppress("TooManyFunctions")
class ItemService(
    private val objectMapper: ObjectMapper,
    private val itemMapper: ItemMapper,
    private val baseItemService: BaseItemService,
    private val cloningMapper: CloningMapper,
    private val userService: UserService,
    private val userRepository: UserRepository
) {

    private val itemsMap: HashMap<Long, ItemEntity>
    val itemsContainer: ItemsContainer

    init {
        itemsMap = HashMap()
        itemsContainer = ItemsContainer()
        try {
            for (type in ItemType.entries.toTypedArray()) {
                val items: List<out ItemEntity> = readType(type)
                for (item in items) itemsMap[item.basedId] = item
                itemsContainer[itemMapper.anyList(items, type)] = type
            }
            logger.info("Total read {} items", itemsMap.size)
        } catch (e: IOException) {
            logger.error("Failed to read items", e.message)
        }
    }

    @Throws(IOException::class)
    private fun <T : ItemEntity?> readType(type: ItemType): List<T> {
        val resource: Resource = ClassPathResource("static/base/items/types/" + type.typeId + ".json")
        val list: MutableList<T> = LinkedList()
        val node = objectMapper.readTree(resource.inputStream)
        if (node.isArray) {
            for (i in 0 until node.size()) {
                val item = node[i]
                val t = objectMapper.treeToValue(item, type.typeClass) as T
                val baseId = item["baseId"].asLong()
                t!!.base = baseItemService.getBaseItem(baseId)
                list.add(t)
            }
        }
        logger.info("Read {} items of type {}", list.size, type.name)
        return list
    }

    @Nullable
    fun getItem(baseId: Long): ItemEntity {
        return cloningMapper.itemEntity(itemsMap[baseId])
    }

    val allItems: Collection<ItemEntity>
        get() = itemsMap.values

    fun getItemDto(baseId: Long): ItemDto {
        return itemMapper.any(getItem(baseId))
    }

    fun addItem(user: UserEntity, baseId: Long, quantity: Int) {
        val itemEntity = getItem(baseId)
        val type = itemEntity.basedType
        logger.info("Add item {} for {}", itemEntity.base.title, user.login)
        if (type.isCountable) {
            itemEntity.quantity = quantity
            addAsCountable(user, itemEntity)
        } else {
            addAsNotCountable(user, itemEntity)
        }
    }

    fun deleteItem(user: UserEntity, baseId: Int, quantity: Int) {
        val type = getItem(baseId.toLong()).basedType
        val optional = user.getItemsByType(type)
            .stream()
            .filter { item: ItemEntity -> item.basedId == baseId.toLong() }
            .toList()
        if (optional.size == 1) {
            val item = optional[0]
            item.quantity = item.quantity - quantity
            if (item.quantity <= 0) user.getItemsByType(type).remove(item)
        } else {
            val q = intArrayOf(quantity)
            user.getItemsByType(type)
                .removeIf { itemEntity: ItemEntity ->
                    val toRemove = itemEntity.basedId == baseId.toLong()
                    if (toRemove) q[0]--
                    if (q[0] < 0) return@removeIf false
                    toRemove
                }
        }
    }

    @Suppress("ReturnCount")
    private fun setWearableItem(user: UserEntity, item: WearableEntity): Status {
        val type = item.basedType
        if (!type.isWearable) return Status(false, "Невозможно надеть предмет")
        val isEquippedNow = !item.isEquipped
        item.isEquipped = isEquippedNow
        logger.info("Set wearable item {} for {}", item.base.title, user.login, isEquippedNow)
        if (isEquippedNow) {
            user.getItemsByType(type)
                .forEach { wearableEntity: ItemEntity ->
                    if (wearableEntity !== item) {
                        (wearableEntity as WearableEntity).isEquipped = false
                    }
                }
            return Status(true, "Предмет надет.")
        }
        return Status(true, "Предмет снят.")
    }

    fun setWearableItemById(user: UserEntity, id: UUID): Status {
        val item = user.getAllItems()
            .stream()
            .filter { itemEntity: ItemEntity -> itemEntity.id == id }
            .findFirst()
            .orElseThrow()
        return setWearableItem(user, item as WearableEntity)
    }

    fun setWearableItemById(user: UserEntity, id: Int): Status {
        val item = user.getAllItems()
            .stream()
            .filter { itemEntity: ItemEntity -> itemEntity.basedId == id.toLong() }
            .sorted(
                Comparator { o1: ItemEntity, o2: ItemEntity? -> if (o1.id == null) -1 else 0 } as Comparator<ItemEntity>
            )
            .findFirst()
            .orElseThrow()
        return setWearableItem(user, item as WearableEntity)
    }

    fun setWearableItemById(id: UUID): Status {
        val user = userService.getCurrentUser()
        val status = setWearableItemById(user, id)
        if (status.isSuccess) userRepository.save(user)
        return status
    }

    private fun <T : ItemEntity?> addAsNotCountable(user: UserEntity, item: T) {
        val type = item!!.base.type
        if (type.isWearable) {
            if (type.typeClass != ArtifactDto::class.java) {
                val userWears = user
                    .getItemsByType(type)
                    .stream()
                    .anyMatch { userItem: ItemEntity -> (userItem as WearableEntity).isEquipped }
                (item as WearableEntity).isEquipped = !userWears
                logger.info("User {} wears {} - ", user.login, item.getBase().title, !userWears)
            }
        }
        item.quantity = 1
        addAsIs<T>(user, item)
    }

    private fun <T : ItemEntity?> addAsCountable(user: UserEntity, itemEntity: T) {
        val optionalItem = user.getItemsByType(itemEntity!!.basedType)
            .stream()
            .filter { item: ItemEntity -> item.basedId == itemEntity.basedId }
            .findFirst()
        if (optionalItem.isPresent) {
            val item = optionalItem.get()
            item.quantity += itemEntity.quantity
        } else {
            addAsIs<T>(user, itemEntity)
        }
    }

    private fun <T : ItemEntity?> addAsIs(user: UserEntity, itemEntity: T) {
        itemEntity!!.owner = user
        when (itemEntity.basedType) {
            net.artux.pdanetwork.models.items.ItemType.BULLET -> user.bullets.add(itemEntity as BulletEntity)
            net.artux.pdanetwork.models.items.ItemType.ARMOR -> user.armors.add(itemEntity as ArmorEntity)
            net.artux.pdanetwork.models.items.ItemType.PISTOL,
            net.artux.pdanetwork.models.items.ItemType.RIFLE -> user.weapons.add(itemEntity as WeaponEntity)
            net.artux.pdanetwork.models.items.ItemType.ARTIFACT -> user.artifacts.add(itemEntity as ArtifactEntity)
            net.artux.pdanetwork.models.items.ItemType.DETECTOR -> user.detectors.add(itemEntity as DetectorEntity)
            net.artux.pdanetwork.models.items.ItemType.MEDICINE -> user.medicines.add(itemEntity as MedicineEntity)
            net.artux.pdanetwork.models.items.ItemType.ITEM -> TODO()
        }
    }

    companion object : KLogging()
}
