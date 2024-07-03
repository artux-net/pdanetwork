package net.artux.pdanetwork.service.communication

import net.artux.pdanetwork.dto.page.QueryPage
import net.artux.pdanetwork.dto.page.ResponsePage
import net.artux.pdanetwork.entity.communication.ConversationEntity
import net.artux.pdanetwork.entity.mappers.CommunicationMapper
import net.artux.pdanetwork.entity.user.UserEntity
import net.artux.pdanetwork.exception.UserNotFoundException
import net.artux.pdanetwork.models.communication.ConversationCreateDTO
import net.artux.pdanetwork.models.communication.ConversationDTO
import net.artux.pdanetwork.models.communication.ConversationType
import net.artux.pdanetwork.repository.comminication.ConversationRepository
import net.artux.pdanetwork.repository.comminication.MessageRepository
import net.artux.pdanetwork.repository.user.UserRepository
import net.artux.pdanetwork.service.user.UserService
import net.artux.pdanetwork.service.util.PageService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
@Transactional
@Suppress("TooManyFunctions")
open class ConversationServiceImpl(
    private val mapper: CommunicationMapper,
    private val repository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val pageService: PageService,
) : ConversationService {

    override fun createConversation(createDTO: ConversationCreateDTO): ConversationDTO {
        val conversationEntity = ConversationEntity()
        validateConversation(createDTO, conversationEntity)
        return mapper.dto(repository.save(conversationEntity))
    }

    override fun editConversation(id: UUID, createDTO: ConversationCreateDTO): ConversationDTO {
        val conversationEntity = repository.findByIdAndOwner(id, userService.getCurrentUser()).orElseThrow()
        validateConversation(createDTO, conversationEntity)
        return mapper.dto(repository.save(conversationEntity))
    }

    override fun getConversation(id: UUID): ConversationDTO {
        return repository.findByIdAndMembersContains(id, userService.getCurrentUser())
            .map { entity: ConversationEntity? -> mapper.dto(entity) }
            .orElseThrow()
    }

    override fun getConversationWithUser(userId: UUID): List<ConversationDTO> {
        // TODO сделать через sql запрос
        val conversations =
            repository.findAllByTypeAndMembers_Id(ConversationEntity.Type.PRIVATE, userService.getCurrentId())
        return conversations.stream().filter { conversation: ConversationEntity ->
            if (conversation.type == ConversationEntity.Type.PRIVATE) {
                return@filter conversation.members.contains(userService.getCurrentUser(userId))
            }
            false
        }.map { entity: ConversationEntity? -> mapper.dto(entity) }.toList()
    }

    override fun getConversations(queryPage: Pageable): Page<ConversationDTO> {
        val user = userService.getCurrentUser()
        val conversationEntityPage = repository.findAllByMembersContains(user, queryPage)
        conversationEntityPage.map { conversation: ConversationEntity ->
            if (conversation.type == ConversationEntity.Type.PRIVATE) {
                val companion = getUniqueUser(userService.getCurrentId(), conversation.members)
                conversation.title = companion.name + " " + companion.nickname
                conversation.icon = companion.avatar
            }
            conversation
        }
        return conversationEntityPage.map { entity: ConversationEntity? -> mapper.dto(entity) }
    }

    override fun getConversations(queryPage: QueryPage): ResponsePage<ConversationDTO> {
        val conversationEntityPage = getConversations(
            pageService.getPageable(queryPage)
        )
        return pageService.mapDataPageToResponsePage(conversationEntityPage, conversationEntityPage.content)
    }

    @Suppress("ReturnCount")
    override fun deleteConversation(id: UUID): Boolean {
        val entity = repository.findById(id).orElseThrow()
        val user = userService.getCurrentUser()
        if (entity.type == ConversationEntity.Type.PRIVATE && entity.members.contains(user)) {
            messageRepository.deleteAllByConversation(entity)
            repository.delete(entity)
            return true
        } else if (entity.owner == user) {
            messageRepository.deleteAllByConversation(entity)
            repository.delete(entity)
            return true
        }
        return false
    }

    override fun getConversationEntity(id: UUID): ConversationDTO {
        return mapper.dto(repository.findById(id).orElse(null))
    }

    override fun deleteAllConversationsWithUser(id: UUID): Boolean {
        val user = userService.getCurrentUser()
        messageRepository.deleteAllByAuthor(user)
        repository.deleteAllByMembersContainsAndTypeEquals(user, ConversationEntity.Type.PRIVATE)
        return true
    }

    private fun getUniqueUser(userId: UUID, users: Set<UserEntity>): UserEntity {
        return users.stream().filter { user: UserEntity -> user.id != userId }.findFirst().orElseThrow {
            UserNotFoundException("Пользователь не найден")
        }
    }

    private fun validateConversation(createDTO: ConversationCreateDTO, conversationEntity: ConversationEntity) {
        if (createDTO.type == ConversationType.PRIVATE && createDTO.users.size != 1) {
            throw UserNotFoundException("Необходимо указать id собеседника")
        }
        conversationEntity.members = userRepository.findAllByIdIn(createDTO.users)
        conversationEntity.members.add(userService.getCurrentUser())
        conversationEntity.title = createDTO.title
        conversationEntity.icon = createDTO.icon
        conversationEntity.owner = userService.getCurrentUser()
        conversationEntity.type = ConversationEntity.Type.valueOf(createDTO.type.name)
        conversationEntity.time = Instant.now()
    }
}
