package net.artux.pdanetwork.controller.rest.feed;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.feed.CommentCreateDto;
import net.artux.pdanetwork.models.feed.CommentDto;
import net.artux.pdanetwork.models.feed.CommentType;
import net.artux.pdanetwork.dto.page.QueryPage;
import net.artux.pdanetwork.dto.page.ResponsePage;
import net.artux.pdanetwork.service.feed.CommentService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Комментарии")
@RestController
@RequestMapping("/api/v1/feed/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{type}/{id}")
    @Operation(summary = "Комментирование поста или статьи")
    public CommentDto commentPost(@PathVariable CommentType type, @PathVariable UUID id, @RequestBody CommentCreateDto comment) {
        return commentService.comment(type, id, comment);
    }

    @GetMapping("/{type}/{id}/all")
    @Operation(summary = "Комментарии поста или статьи")
    public ResponsePage<CommentDto> getComments(@PathVariable CommentType type, @PathVariable UUID id, @Valid @ParameterObject QueryPage page) {
        return commentService.getComments(type, id, page);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление коммента")
    public boolean deleteComment(@PathVariable UUID id) {
        return commentService.delete(id);
    }

    @GetMapping("/{id}/like")
    @Operation(summary = "Лайк коммента")
    public boolean likeComment(@PathVariable UUID id) {
        return commentService.likeComment(id);
    }

    @GetMapping("/types")
    public CommentType[] getTypes() {
        return CommentType.values();
    }

}
