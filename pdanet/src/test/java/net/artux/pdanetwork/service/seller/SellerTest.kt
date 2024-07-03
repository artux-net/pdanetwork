package net.artux.pdanetwork.service.seller

import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.models.items.ItemDto
import net.artux.pdanetwork.models.items.WeaponDto
import net.artux.pdanetwork.repository.items.ItemRepository
import net.artux.pdanetwork.service.action.ActionService
import net.artux.pdanetwork.service.items.ItemService
import net.artux.pdanetwork.service.items.SellerService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.context.support.WithUserDetails
import java.util.function.Consumer
import kotlin.random.Random

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithUserDetails(value = "admin@artux.net")
class SellerTest : AbstractTest() {

    @Autowired
    lateinit var sellerService: SellerService

    @Autowired
    lateinit var actionService: ActionService

    @Autowired
    lateinit var itemRepository: ItemRepository

    @Autowired
    lateinit var itemService: ItemService

    private val sellerId: Long = 1

    @Order(0)
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun testGetSeller() {
        Assertions.assertNotNull(sellerService.getSeller(sellerId))
    }

    @Test
    @Order(1)
    fun addCountableItemsToSeller() {
        sellerService.addSellerItems(sellerId, listOf("29:50", "29:50"))
        val itemDto = sellerService.getSeller(sellerId).bullets
            .stream()
            .filter { dto: ItemDto -> dto.baseId == 29 }
            .findFirst().get()
        Assertions.assertEquals(300, itemDto.quantity) // изначально там 200 патрон
    }

    @Test
    @Order(8)
    fun addUncountableItemsToSeller() {
        // unique type chests for uncountable items
    }

    @Test
    @Order(9)
    fun testBuy() {
        actionService.applyCommands(mapOf("money" to listOf("10000")))
        actionService.applyCommands(emptyMap()).weapons.forEach(
            Consumer { x: WeaponDto? -> println(x) }
        )
        val weaponDto = sellerService.getSeller(sellerId).weapons.stream().findFirst().get()
        val status = sellerService.buy(sellerId, weaponDto.id, 1)
        actionService.applyCommands(emptyMap()).weapons.forEach(
            Consumer { x: WeaponDto? -> println(x) }
        )
        Assertions.assertTrue(status.isSuccess)
    }

    @Test
    @Order(10)
    fun testDeleteSellerItems() {
        sellerService.fixSellersItems()
        val weaponDto = sellerService.getSeller(sellerId).weapons
            .stream()
            .filter { itemDto1: WeaponDto -> itemDto1.baseId == 10 }
            .findFirst()
            .get()

        val itemDto = sellerService.getSeller(sellerId).bullets
            .stream()
            .filter { itemDto1: ItemDto -> itemDto1.baseId == 29 }
            .findFirst()
            .get()

        val itemIds = listOf(itemDto.id, weaponDto.id)
        println(weaponDto.baseId)
        println(itemDto.baseId)
        sellerService.deleteSellerItems(sellerId, itemIds)
        Assertions.assertTrue(
            sellerService.getSeller(sellerId).weapons
                .stream()
                .filter { item: WeaponDto -> item.id == weaponDto.id }
                .findFirst()
                .isEmpty
        )
        Assertions.assertTrue(
            sellerService.getSeller(sellerId).bullets
                .stream()
                .filter { item: ItemDto -> item.id == itemDto.id }
                .findFirst()
                .isEmpty
        )
        for (id in itemIds) Assertions.assertTrue(itemRepository.findById(id).isEmpty)
    }

    @Test
    @Order(11)
    fun testFixSellersItems() {
        sellerService.fixSellersItems()
        Assertions.assertTrue(
            sellerService.getSeller(sellerId).bullets.stream().anyMatch { item: ItemDto -> item.baseId == 29 }
        )
        Assertions.assertTrue(
            sellerService.getSeller(sellerId).weapons.stream().anyMatch { item: WeaponDto -> item.baseId == 10 }
        )
        var dto = sellerService.getSeller(
            sellerId
        ).bullets.stream().filter { item: ItemDto -> item.baseId == 29 }.findFirst()
            .get()
        sellerService.buy(sellerId, dto.id, 180)
        sellerService.fixSellersItems()
        dto =
            sellerService.getSeller(sellerId).bullets.stream().filter { item: ItemDto -> item.baseId == 29 }.findFirst()
                .get()
        Assertions.assertEquals(200, dto.quantity)
    }

    @Order(12)
    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun testGetHeavySeller() {
        val items = itemService.allItems.map {
            val quantity = Random.nextLong(3) + 1
            val item = itemService.getItem(it.basedId)
            "${item.base.id}:$quantity"
        }
        sellerService.addSellerItems(sellerId, items)

        Assertions.assertNotNull(sellerService.getSeller(sellerId))
    }
}
