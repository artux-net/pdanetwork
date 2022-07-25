package net.artux.pdanetwork.service.communication;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.conversation.ConversationEntity;
import net.artux.pdanetwork.models.communication.CommunicationMapper;
import net.artux.pdanetwork.models.communication.ConversationCreateDTO;
import net.artux.pdanetwork.models.communication.ConversationDTO;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.repository.items.ConversationRepository;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.member.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final CommunicationMapper mapper;
    private final ConversationRepository repository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public ConversationDTO createConversation(ConversationCreateDTO createDTO) {
        ConversationEntity conversationEntity = new ConversationEntity();
        //todo check if friends
        conversationEntity.setMembers(userRepository.findAllByIdIn(createDTO.getUsers()));
        conversationEntity.getMembers().add(userService.getMember());
        conversationEntity.setTitle(createDTO.getTitle());
        conversationEntity.setIcon(createDTO.getIcon());
        conversationEntity.setOwner(userService.getMember());
        conversationEntity.setType(ConversationEntity.Type.GROUP);
        conversationEntity.setTime(Instant.now());
        return mapper.dto(repository.save(conversationEntity));
    }

    @Override
    public ConversationDTO editConversation(Long id, ConversationCreateDTO createDTO) {
        ConversationEntity conversationEntity = repository.findByIdAndOwner(id, userService.getMember()).orElseThrow();
        //todo check if friends
        conversationEntity.setMembers(userRepository.findAllByIdIn(createDTO.getUsers()));
        conversationEntity.getMembers().add(userService.getMember());
        conversationEntity.setTitle(createDTO.getTitle());
        conversationEntity.setIcon(createDTO.getIcon());
        conversationEntity.setTime(Instant.now());
        //todo messages about creation and edition
        return mapper.dto(repository.save(conversationEntity));
    }

    @Override
    public ConversationDTO getConversation(Long id) {
        return repository.findByIdAndMembersContains(id, userService.getMember())
                .map(mapper::dto).orElseThrow();
    }

    @Override
    public Slice<ConversationDTO> getConversations(Pageable queryPage) {
        return repository.findByMembersContains(queryPage, userService.getMember().getPdaId()).map(mapper::dto);
    }

    @Override
    public boolean deleteConversation(Long id) {
        ConversationEntity entity = repository.findById(id).orElseThrow();
        UserEntity user = userService.getMember();
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
    public ConversationEntity getPrivateConversation(Long pda1, long pda2) {
        Optional<ConversationEntity> conversation = repository
                .findByMembersContainsAndType(ConversationEntity.Type.PRIVATE, pda1, pda2);
        return conversation.orElse(null);
    }

    @Override
    public ConversationEntity getConversationByIdForUser(Long id, UserEntity user) {
        return repository.findByIdAndMembersContains(id, user).orElseThrow();
    }

    @Override
    public ConversationEntity createPrivateConversation(long pdaId, long id) {
        ConversationEntity conversationEntity = new ConversationEntity();
        List<Long> list = new LinkedList<>();
        list.add(pdaId);
        list.add(id);
        conversationEntity.setMembers(userRepository.findAllByIdIn(list));
        conversationEntity.setTitle("");
        conversationEntity.setIcon("");
        conversationEntity.setOwner(userService.getMember());
        conversationEntity.setType(ConversationEntity.Type.PRIVATE);
        conversationEntity.setTime(Instant.now());
        return repository.save(conversationEntity);
    }
}
