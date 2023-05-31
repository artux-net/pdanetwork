package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.models.quest.map.Point;
import net.artux.pdanetwork.models.quest.map.Spawn;
import net.artux.pdanetwork.models.quest.mission.Mission;
import net.artux.pdanetwork.models.quest.stage.Stage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class Chapter {

    private long id;
    private Collection<Stage> stages;
    private Map<Long, List<Point>> points;
    private Map<Long, List<Spawn>> spawns;
    private Mission mission;

    public void setStages(List<Stage> stageList) {
        stages = stageList;
    }
}
