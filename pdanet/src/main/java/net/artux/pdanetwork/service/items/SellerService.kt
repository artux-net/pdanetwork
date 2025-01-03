package net.artux.pdanetwork.service.items

import net.artux.pdanetwork.models.Status
import net.artux.pdanetwork.models.seller.SellerAdminDto
import net.artux.pdanetwork.models.seller.SellerDto
import java.util.UUID

interface SellerService {
    fun getSeller(id: Long): SellerDto

    fun getSellers(): List<SellerAdminDto>

    fun buy(sellerId: Long, id: UUID, quantity: Int): Status

    fun sell(sellerId: Long, id: UUID, quantity: Int): Status

    fun createSeller(dto: SellerAdminDto): SellerDto

    fun updateSeller(id: Long, dto: SellerAdminDto): SellerDto

    fun addSellerItems(sellerId: Long, s: List<String>): SellerDto

    fun deleteSellerItems(sellerId: Long, ids: List<UUID>): SellerDto

    fun deleteSeller(id: Long)

    fun restoreSellersItems()
}
