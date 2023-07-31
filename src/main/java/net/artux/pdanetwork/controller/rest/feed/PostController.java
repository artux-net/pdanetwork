package net.artux.pdanetwork.controller.rest.feed;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.feed.PostCreateDto;
import net.artux.pdanetwork.models.feed.PostDto;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import net.artux.pdanetwork.service.feed.PostService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Посты")
@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts/recent")
    @Operation(summary = "Актуальные посты, сорт по кол-ву лайков")
    public ResponsePage<PostDto> getRecentPosts(@Valid QueryPage page) {
        return postService.getRecentPosts(page);
    }

    @GetMapping("/posts/all")
    @Operation(summary = "Актуальные посты")
    public ResponsePage<PostDto> getAllPosts(@Valid QueryPage page) {
        return postService.getAllPosts(page);
    }

    @DeleteMapping("/post/{id}")
    @Operation(summary = "Удаление поста")
    public boolean deletePost(@PathVariable UUID id) {
        return postService.deletePost(id);
    }

    @PostMapping("/post")
    @Operation(summary = "Разместить пост")
    public PostDto getPagePosts(@RequestBody PostCreateDto dto) {
        return postService.createPost(dto);
    }

    @GetMapping("/post/{id}/like")
    @Operation(summary = "Лайк поста")
    public boolean likePost(@PathVariable UUID id) {
        return postService.likePost(id);
    }

}
