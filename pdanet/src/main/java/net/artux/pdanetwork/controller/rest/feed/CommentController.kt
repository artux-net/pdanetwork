package net.artux.pdanetwork.controller.rest.feed

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import net.artux.pdanetwork.dto.page.QueryPage
import net.artux.pdanetwork.dto.page.ResponsePage
import net.artux.pdanetwork.models.feed.CommentCreateDto
import net.artux.pdanetwork.models.feed.CommentDto
import net.artux.pdanetwork.models.feed.CommentType
import net.artux.pdanetwork.service.feed.CommentService
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "Комментарии")
@RestController
@RequestMapping("/api/v1/feed/comment")
@Suppress("UnusedParameter")
@RequiredArgsConstructor
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping("/{type}/{id}")
    @Operation(summary = "Комментирование поста или статьи")
    fun commentPost(
        @PathVariable type: CommentType,
        @PathVariable id: UUID,
        @RequestBody comment: CommentCreateDto
    ): CommentDto {
        return commentService.comment(type, id, comment)
    }

    @GetMapping("/{type}/{id}/all")
    @Operation(summary = "Комментарии поста или статьи")
    fun getComments(
        @PathVariable type: CommentType,
        @PathVariable id: UUID,
        @ParameterObject page: @Valid QueryPage
    ): ResponsePage<CommentDto> = ResponsePage.of(Page.empty())

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление коммента")
    fun deleteComment(@PathVariable id: UUID): Boolean {
        return commentService.delete(id)
    }

    @GetMapping("/{id}/like")
    @Operation(summary = "Лайк коммента")
    fun likeComment(@PathVariable id: UUID?): Boolean {
        return commentService.likeComment(id)
    }

    @GetMapping("/types")
    @Operation(summary = "Типы комментариев")
    fun getTypes(): Array<CommentType> = CommentType.entries.toTypedArray()
}
