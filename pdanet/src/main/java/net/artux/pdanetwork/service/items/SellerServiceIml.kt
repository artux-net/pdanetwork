package net.artux.pdanetwork.service.items

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import lombok.RequiredArgsConstructor
import mu.KLogging
import net.artux.pdanetwork.entity.items.ConditionalEntity
import net.artux.pdanetwork.entity.items.ItemEntity
import net.artux.pdanetwork.entity.items.WearableEntity
import net.artux.pdanetwork.entity.mappers.SellerMapper
import net.artux.pdanetwork.entity.mappers.StoryMapper
import net.artux.pdanetwork.entity.seller.SellerEntity
import net.artux.pdanetwork.models.Status
import net.artux.pdanetwork.models.seller.SellerAdminDto
import net.artux.pdanetwork.models.seller.SellerDto
import net.artux.pdanetwork.repository.items.ItemRepository
import net.artux.pdanetwork.repository.items.SellerRepository
import net.artux.pdanetwork.repository.user.UserRepository
import net.artux.pdanetwork.service.user.UserService
import net.artux.pdanetwork.utills.security.ModeratorAccess
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.util.Arrays
import java.util.LinkedList
import java.util.UUID
import java.util.stream.Collectors

@Service
@Transactional
@RequiredArgsConstructor
@Suppress("LongParameterList", "TooManyFunctions")
open class SellerServiceIml(
    private val sellerRepository: SellerRepository,
    private val itemRepository: ItemRepository,
    private val itemService: ItemService,
    private val userRepository: UserRepository,

    private val storyMapper: StoryMapper,
    private val sellerMapper: SellerMapper,
    private val userService: UserService,
    private val mapper: ObjectMapper,
) : SellerService {

    private lateinit var initialSellers: MutableList<SellerAdminDto>

    @PostConstruct
    @Throws(IOException::class)
    fun init() {
        initialSellers = readSellers()
        logger.info("Прочитан список продавцов, продавцы:")

        for (seller in initialSellers) {
            logger.info("{}, покупка: {}, продажа: {}", seller.name, seller.buyCoefficient, seller.sellCoefficient)

            if (!sellerRepository.existsById(seller.id)) {
                sellerRepository.save(sellerMapper.entity(seller))
            }
        }
    }

    @Throws(IOException::class)
    private fun readSellers(): MutableList<SellerAdminDto> {
        logger.info("Чтение списка продавцов")
        val resource = ClassPathResource("static/base/sellers/info.json")
        val arr = mapper.readValue(resource.inputStream, Array<SellerAdminDto>::class.java)
        return Arrays.stream(arr).collect(Collectors.toList())
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    override fun restoreSellersItems() {
        val sellerEntities = sellerRepository.findAllByIconIsNotNull()
        logger.info("Обновление ассортимента продавцов")
        sellerEntities.forEach { seller ->
            val sellerEntityOptional = initialSellers.stream()
                .filter { initialSeller: SellerAdminDto -> initialSeller.id == seller.id }
                .map { sellerAdminDto: SellerAdminDto -> sellerMapper.entity(sellerAdminDto) }
                .findFirst()

            if (sellerEntityOptional.isEmpty) return@forEach

            // add new items
            val initialSeller = sellerEntityOptional.get()
            restoreSellerItems(sellerId = seller.id, initialSeller)
        }

        logger.info("Обновлен ассортимент продавцов")
    }

    @Transactional
    open fun restoreSellerItems(sellerId: Long, initialSellerEntity: SellerEntity) {
        val sellerEntity = sellerRepository.findById(sellerId).orElseThrow()

        initialSellerEntity.allItems.forEach { initialItem: ItemEntity ->
            val basedId = initialItem.basedId
            val sellerItem = sellerEntity.findItem(basedId)
            if (sellerItem == null) {
                sellerEntity.addItem(initialItem)
                logger.info(
                    "{}: отсутствует предмет {} ({}), добавление.",
                    sellerEntity.name,
                    initialItem.base.title,
                    basedId
                )
            } else {
                val basedQuantity = initialItem.quantity
                if (sellerItem.quantity < basedQuantity) {
                    sellerItem.quantity = basedQuantity
                    logger.info(
                        "{}: обновление количества {} ({})",
                        sellerEntity.name,
                        initialItem.base.title,
                        basedId
                    )
                }
            }
        }

        val basedCount = HashMap<Long, Int>()
        sellerEntity.allItems.forEach { item: ItemEntity ->
            val basedId = item.basedId
            if (!basedCount.containsKey(basedId)) {
                basedCount[basedId] = 1
                return@forEach
            }

            val previousCount = basedCount[basedId]
            if (previousCount != null) {
                if (previousCount > 1) {
                    sellerEntity.removeItem(item)
                } else {
                    basedCount[basedId] = previousCount + 1
                }
            }
        }

        sellerRepository.save(sellerEntity)
    }

    @Transactional(readOnly = true)
    override fun getSeller(id: Long): SellerDto {
        logger.debug("Getting seller {}", id)
        return sellerRepository.findById(id).map(sellerMapper::dto).orElseThrow()
    }

    @Transactional(readOnly = true)
    override fun getSellers(): List<SellerAdminDto> {
        return sellerRepository.findAll().stream().map { entity: SellerEntity? -> sellerMapper.adminDto(entity) }
            .collect(Collectors.toList())
    }

    @Transactional
    override fun buy(sellerId: Long, id: UUID, quantity: Int): Status {
        var quantity = quantity
        val userEntity = userService.getCurrentUser()
        val sellerEntity = sellerRepository.findById(sellerId).orElseThrow()
        // todo check if seller does not have item
        val sellerItem = itemRepository.findById(id).orElseThrow()
        val baseId = sellerItem.basedId
        val type = sellerItem.basedType

        val price = getPrice(sellerItem, sellerEntity.buyCoefficient, quantity)

        val optionalUserItem = userEntity.findItem(baseId)
        logger.info(
            userEntity.login + " покупает " + sellerItem.base.title +
                " (" + sellerItem.id + ") в количестве " + quantity +
                " у " + sellerEntity.name +
                " за " + price + "RU, имеет: " + userEntity.money + "RU"
        )

        if (!sellerItem.base.type.isCountable) quantity = 1

        return if (quantity > 0 && userEntity.canBuy(price)) {
            if (quantity == sellerItem.quantity) {
                sellerEntity.removeItem(sellerItem)
                sellerRepository.save(sellerEntity)
                if (optionalUserItem.isEmpty || !type.isCountable) {
                    sellerItem.owner = userEntity
                    itemRepository.save(sellerItem)
                } else {
                    val userItem = optionalUserItem.get()
                    userItem.quantity += quantity
                    itemRepository.deleteById(sellerItem.id)
                }
            } else if (quantity < sellerItem.quantity) {
                sellerItem.quantity -= quantity
                itemRepository.save(sellerItem)

                val separatedItem = itemService.getItem(baseId)

                if (optionalUserItem.isEmpty) {
                    // user does not have an item
                    separatedItem.owner = userEntity
                    separatedItem.quantity = quantity
                    itemRepository.save(separatedItem)
                } else {
                    // user has an item
                    val userItem = optionalUserItem.get()
                    userItem.quantity += quantity
                    itemRepository.save(userItem)
                }
            } else {
                Status(false, "У продавца столько нет.")
            }

            userEntity.statistic.setBoughtItems(userEntity.statistic.getBoughtItems() + 1)
            val storyData = storyMapper.storyData(userRepository.save(userEntity))

            Status(true, "Ok.", storyData)
        } else {
            Status(false, "Недостаточно средств.")
        }
    }

    @Transactional
    override fun sell(sellerId: Long, id: UUID, quantity: Int): Status {
        var quantity = quantity
        val userEntity = userService.getCurrentUser()
        val sellerEntity = sellerRepository.findById(sellerId).orElseThrow()
        var item = itemRepository.findById(id).orElseThrow()
        val price = getPrice(item, sellerEntity.sellCoefficient, quantity)

        logger.info(
            userEntity.login + " продает " + item.base.title +
                " (" + item.id + ") в количестве " + quantity +
                ":" + sellerEntity.name +
                " за " + price + "RU"
        )

        if (!item.base.type.isCountable) {
            quantity = 1
        }
        if (item is WearableEntity) {
            item.isEquipped = false
        }

        if (item is ConditionalEntity && item.condition < CONDITION_TO_DELETE_ITEM) {
            itemRepository.delete(item)
        } else {
            if (quantity > item.quantity && quantity < 0) {
                return Status(false, "У вас столько нет")
            }

            sellItemToSeller(item, quantity, sellerEntity)
        }

        userEntity.money += price
        userEntity.statistic.setSoldItems(userEntity.statistic.getSoldItems() + 1)

        val storyData = storyMapper.storyData(userRepository.save(userEntity))
        return Status(true, "Ok.", storyData)
    }

    private fun sellItemToSeller(
        item: ItemEntity,
        quantity: Int,
        sellerEntity: SellerEntity
    ) {
        var item1 = item
        if (item1.quantity > quantity) {
            item1.quantity -= quantity
            item1 = itemRepository.save(item1)

            val baseId = item1.base.id
            var sellerItem = sellerEntity.findItem(baseId)
            if (sellerItem != null) {
                sellerItem.quantity += quantity
            } else {
                // new item for seller
                sellerItem = itemService.getItem(baseId)
                sellerItem.quantity = quantity
                sellerEntity.addItem(sellerItem)
            }
            sellerRepository.save(sellerEntity)
        } else {
            // if quantities equals
            item1.owner = null
            item1 = itemRepository.save(item1)
            sellerEntity.addItem(item1)
            sellerRepository.save(sellerEntity)
        }
    }

    private fun getPrice(item: ItemEntity, sellerCoefficient: Float, quantity: Int): Int {
        var additionalCoefficient = 1f
        if (item is ConditionalEntity) {
            additionalCoefficient = item.condition / FULL_ITEM_CONDITION
        }

        return (item.base.price * additionalCoefficient * sellerCoefficient).toInt() * quantity
    }

    @ModeratorAccess
    override fun createSeller(dto: SellerAdminDto): SellerDto {
        val userEntity = userService.getCurrentUser()
        var sellerEntity = SellerEntity()
        sellerEntity.id = dto.id
        sellerEntity.name = dto.name
        sellerEntity.icon = dto.name
        sellerEntity.image = dto.image
        sellerEntity.buyCoefficient = dto.buyCoefficient
        sellerEntity.sellCoefficient = dto.sellCoefficient
        sellerEntity = sellerRepository.save(sellerEntity)
        initialSellers.add(sellerMapper.adminDto(sellerEntity))
        logger.info("Создание продавца {} ({}) пользователем {}", sellerEntity.name, sellerEntity.id, userEntity.login)

        return sellerMapper.dto(sellerEntity)
    }

    @ModeratorAccess
    override fun updateSeller(id: Long, dto: SellerAdminDto): SellerDto {
        dto.id = id
        initialSellers.removeIf { s: SellerAdminDto -> s.id == id }
        initialSellers.add(dto)

        val sellerEntity = sellerRepository.findById(id).orElseThrow()
        sellerEntity.name = dto.name
        sellerEntity.icon = dto.icon
        sellerEntity.image = dto.image
        sellerEntity.buyCoefficient = dto.buyCoefficient
        sellerEntity.sellCoefficient = dto.sellCoefficient

        return sellerMapper.dto(sellerRepository.save(sellerEntity))
    }

    @ModeratorAccess
    override fun addSellerItems(sellerId: Long, s: List<String>): SellerDto {
        val sellerEntity = sellerRepository.findById(sellerId).orElseThrow()
        for (couple in s) {
            if (!couple.contains(":")) continue

            val parts = couple.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            try {
                val id = parts[0].toInt()
                val quantity = parts[1].toInt()

                val item = itemService.getItem(id.toLong())
                item.quantity = quantity

                addItemToSeller(item, sellerEntity, id, quantity)
            } catch (ignored: Exception) {
            }
        }

        return sellerMapper.dto(sellerRepository.save(sellerEntity))
    }

    private fun addItemToSeller(
        item: ItemEntity,
        sellerEntity: SellerEntity,
        id: Int,
        quantity: Int
    ) {
        var stackTarget: ItemEntity? = null
        if (item.base.type.isCountable) {
            val sellerItems = sellerEntity.allItems
            val sellerItem = sellerItems
                .stream()
                .filter { itemEntity: ItemEntity -> itemEntity.basedId == id.toLong() }
                .findAny()
            if (sellerItem.isPresent) {
                stackTarget = sellerItem.get()
            }
        }

        if (stackTarget != null) {
            stackTarget.quantity += quantity
        } else {
            sellerEntity.addItem(itemRepository.save(item))
        }
    }

    @ModeratorAccess
    @Transactional
    override fun deleteSellerItems(sellerId: Long, ids: List<UUID>): SellerDto {
        val finalIds: MutableList<UUID> = LinkedList(ids)

        val sellerEntity = sellerRepository.findById(sellerId).orElseThrow()
        logger.info("Удаление предметов у {}", sellerEntity.name)
        val sellerItemIds: MutableList<UUID> = LinkedList()
        sellerEntity.allItems
            .stream()
            .filter { entity: ItemEntity -> finalIds.contains(entity.id) }
            .forEach { item: ItemEntity ->
                sellerItemIds.add(item.id)
                logger.info("Удаление {} у {}", item.id, sellerEntity.name)
                sellerEntity.removeItem(item)
            }

        finalIds.removeIf { id: UUID -> !sellerItemIds.contains(id) }
        val dto = sellerMapper.dto(sellerRepository.save(sellerEntity))
        itemRepository.deleteAllById(finalIds)

        return dto
    }

    @ModeratorAccess
    override fun deleteSeller(id: Long) {
        sellerRepository.deleteById(id)
    }

    companion object : KLogging() {
        private const val CONDITION_TO_DELETE_ITEM = 70
        private const val FULL_ITEM_CONDITION = 100
    }
}
