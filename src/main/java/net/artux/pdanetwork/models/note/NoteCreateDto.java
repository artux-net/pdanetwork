package net.artux.pdanetwork.models.note;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NoteCreateDto {

    private String title;
    private String content;

    public NoteCreateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
