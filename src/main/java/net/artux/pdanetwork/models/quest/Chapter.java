package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.models.quest.map.MapEnum;
import net.artux.pdanetwork.models.quest.map.Point;
import net.artux.pdanetwork.models.quest.map.Spawn;
import net.artux.pdanetwork.models.quest.mission.Mission;
import net.artux.pdanetwork.models.quest.stage.Stage;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Chapter {

    private long id;
    private Map<Long, Stage> stages;
    private Map<Long, List<Point>> points;
    private Map<Long, List<Spawn>> spawns;
    private Mission mission;

    public void setStages(List<Stage> stageList) {
        stages = new HashMap<>();
        for (var stage : stageList)
            stages.put(stage.getId(), stage);
    }

    public Stage getStage(long id){
        return stages.get(id);
    }
}
