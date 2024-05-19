package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.models.quest.stage.Stage;

import java.util.Map;

@Data
public class ChapterDto {

    private long id;
    private Map<Long, Stage> stages;

    public Stage getStage(long id) {
        return stages.get(id);
    }
}
