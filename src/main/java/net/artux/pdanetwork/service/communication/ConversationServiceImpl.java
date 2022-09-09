package net.artux.pdanetwork.service.communication;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.communication.ConversationEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.communication.CommunicationMapper;
import net.artux.pdanetwork.models.communication.ConversationCreateDTO;
import net.artux.pdanetwork.models.communication.ConversationDTO;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.repository.comminication.ConversationRepository;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.PageService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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
        //todo check if friends
        conversationEntity.setMembers(userRepository.findAllByIdIn(createDTO.getUsers()));
        conversationEntity.getMembers().add(userService.getUserById());
        conversationEntity.setTitle(createDTO.getTitle());
        conversationEntity.setIcon(createDTO.getIcon());
        conversationEntity.setOwner(userService.getUserById());
        conversationEntity.setType(ConversationEntity.Type.GROUP);
        conversationEntity.setTime(Instant.now());
        return mapper.dto(repository.save(conversationEntity));
    }

    @Override
    public ConversationDTO editConversation(UUID id, ConversationCreateDTO createDTO) {
        ConversationEntity conversationEntity = repository.findByIdAndOwner(id, userService.getUserById()).orElseThrow();
        //todo check if friends
        conversationEntity.setMembers(userRepository.findAllByIdIn(createDTO.getUsers()));
        conversationEntity.getMembers().add(userService.getUserById());
        conversationEntity.setTitle(createDTO.getTitle());
        conversationEntity.setIcon(createDTO.getIcon());
        conversationEntity.setTime(Instant.now());
        //todo messages about creation and edition
        return mapper.dto(repository.save(conversationEntity));
    }

    @Override
    public ConversationDTO getConversation(UUID id) {
        return repository.findByIdAndMembersContains(id, userService.getUserById()).map(mapper::dto).orElseThrow();
    }

    @Override
    public Slice<ConversationDTO> getConversations(Pageable queryPage) {
        return repository.findByMembersContains(queryPage, userService.getCurrentId()).map(mapper::dto);
    }

    @Override
    public Slice<ConversationDTO> getConversations(QueryPage queryPage) {
        return getConversations(pageService.getPageable(queryPage));
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
    public ConversationEntity getPrivateConversation(UUID pda1, UUID pda2) {
        Optional<ConversationEntity> conversation = repository.findByMembersContainsAndType(ConversationEntity.Type.PRIVATE, pda1, pda2);
        return conversation.orElse(null);
    }

    public ConversationEntity getConversationByIdForUser(UUID id, UserEntity user) {
        return repository.findByIdAndMembersContains(id, user).orElseThrow();
    }

    @Override
    public ConversationEntity createPrivateConversation(UUID pdaId, UUID id) {
        ConversationEntity conversationEntity = new ConversationEntity();
        List<UUID> list = new LinkedList<>();
        list.add(pdaId);
        list.add(id);
        conversationEntity.setMembers(userRepository.findAllByIdIn(list));
        conversationEntity.setTitle("");
        conversationEntity.setIcon("");
        conversationEntity.setOwner(userService.getUserById());
        conversationEntity.setType(ConversationEntity.Type.PRIVATE);
        conversationEntity.setTime(Instant.now());
        return repository.save(conversationEntity);
    }
}
