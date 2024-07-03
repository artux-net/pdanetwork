package net.artux.pdanetwork.service.feed;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.feed.CommentEntity;
import net.artux.pdanetwork.entity.feed.CommentLikeEntity;
import net.artux.pdanetwork.entity.feed.LikeCommentId;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.feed.CommentCreateDto;
import net.artux.pdanetwork.models.feed.CommentDto;
import net.artux.pdanetwork.models.feed.CommentType;
import net.artux.pdanetwork.entity.mappers.FeedMapper;
import net.artux.pdanetwork.dto.page.QueryPage;
import net.artux.pdanetwork.dto.page.ResponsePage;
import net.artux.pdanetwork.repository.feed.ArticleRepository;
import net.artux.pdanetwork.repository.feed.CommentLikeRepository;
import net.artux.pdanetwork.repository.feed.CommentRepository;
import net.artux.pdanetwork.repository.feed.PostRepository;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.PageService;
import net.artux.pdanetwork.utills.security.ModeratorAccess;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final ArticleRepository articleRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository repository;
    private final UserService userService;
    private final FeedMapper feedMapper;
    private final PageService pageService;

    @Override
    @ModeratorAccess
    public boolean delete(UUID id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public boolean likeComment(UUID id) {
        UserEntity user = userService.getCurrentUser();
        CommentEntity commentEntity = repository.findById(id).orElseThrow();

        LikeCommentId articleId = new LikeCommentId(user.getId(), id);

        if (commentLikeRepository.existsById(articleId)) {
            commentLikeRepository.deleteById(articleId);
            return false;
        } else {
            commentLikeRepository.save(new CommentLikeEntity(user, commentEntity));
            return true;
        }
    }

    @Override
    public CommentDto comment(CommentType type, UUID id, CommentCreateDto comment) {
        CommentEntity commentEntity = feedMapper.entity(comment, userService.getCurrentUser());
        if (type == CommentType.POST)
            commentEntity.setPost(postRepository.findById(id).orElseThrow());
        else
            commentEntity.setArticle(articleRepository.findById(id).orElseThrow());

        return feedMapper.dto(repository.save(commentEntity));
    }

    @Override
    public CommentDto getComment(UUID id) {
        return feedMapper.dto(repository.findById(id).orElseThrow());
    }

    @Override
    public ResponsePage<CommentDto> getComments(CommentType type, UUID id, QueryPage page) {
        Pageable pageable = pageService.getPageable(page);

        if (type == CommentType.POST)
            return ResponsePage.of(repository.findAllByPost(id, pageable).map(feedMapper::dto));
        else
            return ResponsePage.of(repository.findAllByArticle(id, pageable).map(feedMapper::dto));
    }
}
