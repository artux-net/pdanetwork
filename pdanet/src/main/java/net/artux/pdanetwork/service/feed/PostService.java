package net.artux.pdanetwork.service.feed;

import net.artux.pdanetwork.models.feed.PostCreateDto;
import net.artux.pdanetwork.models.feed.PostDto;
import net.artux.pdanetwork.dto.page.QueryPage;
import net.artux.pdanetwork.dto.page.ResponsePage;

import java.util.UUID;

public interface PostService {

    ResponsePage<PostDto> getUserPosts(UUID userId, QueryPage page);

    ResponsePage<PostDto> getAllPosts(QueryPage page);

    ResponsePage<PostDto> getRecentPosts(QueryPage page);

    PostDto getPost(UUID id);

    PostDto createPost(PostCreateDto createDto);

    boolean deletePost(UUID id);

    boolean likePost(UUID id);
}
