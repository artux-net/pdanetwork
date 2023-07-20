package net.artux.pdanetwork.models.quest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoryBackupEditDto {

    @NotNull
    private Long storyId;

    @NotBlank
    private String comment;

}
