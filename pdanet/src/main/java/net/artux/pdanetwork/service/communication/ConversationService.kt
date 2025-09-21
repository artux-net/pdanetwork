package net.artux.pdanetwork.service.communication

import net.artux.pdanetwork.dto.page.QueryPage
import net.artux.pdanetwork.dto.page.ResponsePage
import net.artux.pdanetwork.models.communication.ConversationCreateDTO
import net.artux.pdanetwork.models.communication.ConversationDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ConversationService {
    fun createConversation(createDTO: ConversationCreateDTO): ConversationDTO
    fun editConversation(id: UUID, createDTO: ConversationCreateDTO): ConversationDTO
    fun getConversation(id: UUID): ConversationDTO
    fun getConversationWithUser(userId: UUID): List<ConversationDTO>
    fun getConversations(queryPage: Pageable): Page<ConversationDTO>
    fun getConversations(queryPage: QueryPage): ResponsePage<ConversationDTO>
    fun deleteConversation(id: UUID): Boolean
    fun getConversationEntity(id: UUID): ConversationDTO
    fun deleteAllConversationsWithUser(id: UUID): Boolean
}
