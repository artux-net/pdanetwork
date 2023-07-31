package net.artux.pdanetwork.service.feed;

import net.artux.pdanetwork.models.feed.CommentCreateDto;
import net.artux.pdanetwork.models.feed.CommentDto;
import net.artux.pdanetwork.models.feed.CommentType;
import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;

import java.util.UUID;

public interface CommentService {
    boolean delete(UUID id);

    boolean likeComment(UUID id);

    CommentDto comment(CommentType type, UUID id, CommentCreateDto comment);

    CommentDto getComment(UUID id);

    ResponsePage<CommentDto> getComments(CommentType type, UUID id, QueryPage page);
}
