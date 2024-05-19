package net.artux.pdanetwork.service.feed;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.feed.LikePostId;
import net.artux.pdanetwork.entity.feed.PostEntity;
import net.artux.pdanetwork.entity.feed.PostLikeEntity;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.entity.mappers.FeedMapper;
import net.artux.pdanetwork.models.feed.PostCreateDto;
import net.artux.pdanetwork.models.feed.PostDto;
import net.artux.pdanetwork.dto.page.QueryPage;
import net.artux.pdanetwork.dto.page.ResponsePage;
import net.artux.pdanetwork.repository.feed.PostLikeRepository;
import net.artux.pdanetwork.repository.feed.PostRepository;
import net.artux.pdanetwork.service.user.UserService;
import net.artux.pdanetwork.service.util.PageService;
import net.artux.pdanetwork.utills.security.ModeratorAccess;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserService userService;
    private final PostRepository repository;
    private final PostLikeRepository postLikeRepository;
    private final FeedMapper feedMapper;
    private final PageService pageService;

    @Override
    public ResponsePage<PostDto> getUserPosts(UUID userId, QueryPage page) {
        return ResponsePage.of(repository.findAllByAuthor(userId, pageService.getPageable(page)).map(feedMapper::dto));
    }

    @Override
    public ResponsePage<PostDto> getAllPosts(QueryPage page) {
        return ResponsePage.of(repository.findAll(pageService.getPageable(page)).map(feedMapper::dto));
    }

    @Override
    public ResponsePage<PostDto> getRecentPosts(QueryPage page) {
        return ResponsePage.of(repository.findAllByPublishedIsAfter(Instant.now().minus(7, ChronoUnit.DAYS), pageService.getPageable(page)).map(feedMapper::dto));
    }

    @Override
    public PostDto getPost(UUID id) {
        return feedMapper.dto(repository.findById(id).orElseThrow());
    }

    @Override
    public PostDto createPost(PostCreateDto createDto) {
        PostEntity postEntity = feedMapper.entity(createDto, userService.getUserById());
        return feedMapper.dto(repository.save(postEntity));
    }

    @Override
    @ModeratorAccess
    public boolean deletePost(UUID id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public boolean likePost(UUID id) {
        UserEntity user = userService.getUserById();
        PostEntity postEntity = repository.findById(id).orElseThrow();
        LikePostId articleId = new LikePostId(user.getId(), postEntity.getId());

        if (postLikeRepository.existsById(articleId)) {
            postLikeRepository.deleteById(articleId);
            return false;
        } else {
            postLikeRepository.save(new PostLikeEntity(user, postEntity));
            return true;
        }
    }
}
