package net.artux.pdanetwork.models.note;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class NoteDto{

    private UUID id;
    private String title;
    private String content;
    private Instant time;

}
