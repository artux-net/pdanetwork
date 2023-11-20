package net.artux.pdanetwork.service.communication;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.communication.ConversationEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.communication.CommunicationMapper;
import net.artux.pdanetwork.models.communication.ConversationCreateDTO;
import net.artux.pdanetwork.models.communication.ConversationDTO;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.repository.comminication.ConversationRepository;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.PageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final CommunicationMapper mapper;
    private final ConversationRepository repository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PageService pageService;

    @Override
    public ConversationDTO createConversation(ConversationCreateDTO createDTO) {
        ConversationEntity conversationEntity = new ConversationEntity();

        validateConversation(createDTO, conversationEntity);

        return mapper.dto(repository.save(conversationEntity));
    }

    @Override
    public ConversationDTO editConversation(UUID id, ConversationCreateDTO createDTO) {
        ConversationEntity conversationEntity = repository.findByIdAndOwner(id, userService.getUserById()).orElseThrow();

        validateConversation(createDTO, conversationEntity);

        return mapper.dto(repository.save(conversationEntity));
    }

    @Override
    public ConversationDTO getConversation(UUID id) {
        return repository.findByIdAndMembersContains(id, userService.getUserById()).map(mapper::dto).orElseThrow();
    }

    @Override
    public ConversationDTO getConversationWithUser(UUID userId)  {
        return mapper.dto(repository.findByTypeAndMembers(ConversationEntity.Type.PRIVATE, Set.of(userService.getUserById(), userService.getUserById(userId))).orElseThrow());
    }

    @Override
    public Page<ConversationDTO> getConversations(Pageable queryPage) {
        UserEntity user = userService.getUserById();
        Page<ConversationEntity> conversationEntityPage = repository.findAllByMembersContains(user, queryPage);

        conversationEntityPage.map(conversation -> {
            if(conversation.getType() == ConversationEntity.Type.PRIVATE) {
                UserEntity companion = getUniqueUser(userService.getCurrentId(), conversation.getMembers());
                conversation.setTitle(companion.getName() + " " + companion.getNickname());
                conversation.setIcon(companion.getAvatar());
            }
            return conversation;
        });

        return conversationEntityPage.map(mapper::dto);
    }

    @Override
    public ResponsePage<ConversationDTO> getConversations(QueryPage queryPage) {
        Page<ConversationDTO> conversationEntityPage = getConversations(pageService.getPageable(queryPage));
        return pageService.mapDataPageToResponsePage(conversationEntityPage, conversationEntityPage.getContent());
    }

    @Override
    public boolean deleteConversation(UUID id) {
        ConversationEntity entity = repository.findById(id).orElseThrow();
        UserEntity user = userService.getUserById();
        if (entity.getType().equals(ConversationEntity.Type.PRIVATE) && entity.getMembers().contains(user)) {
            repository.delete(entity);
            return true;
        } else if (entity.getOwner().equals(user)) {
            repository.delete(entity);
            return true;
        }
        return false;
    }

    @Override
    public ConversationDTO getConversationEntity(UUID id) {
        return mapper.dto(repository.findById(id).orElse(null));
    }

    private UserEntity getUniqueUser(UUID userId, Set<UserEntity> users) {
        return users.stream().filter(user -> !user.getId().equals(userId)).findFirst().orElse(null);
    }

    private void validateConversation(ConversationCreateDTO createDTO, ConversationEntity conversationEntity) {
        if(createDTO.getType() == ConversationEntity.Type.PRIVATE && createDTO.getUsers().size() != 1) {
            throw new RuntimeException("Необходимо указать id собеседника");
        }

        conversationEntity.setMembers(userRepository.findAllByIdIn(createDTO.getUsers()));
        conversationEntity.getMembers().add(userService.getUserById());

        conversationEntity.setTitle(createDTO.getTitle());
        conversationEntity.setIcon(createDTO.getIcon());
        conversationEntity.setOwner(userService.getUserById());
        conversationEntity.setType(createDTO.getType());
        conversationEntity.setTime(Instant.now());
    }
}
