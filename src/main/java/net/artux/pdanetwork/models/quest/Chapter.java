package net.artux.pdanetwork.models.quest;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class Chapter {

    private long id;
    private HashMap<Long, Stage> stages;
    private HashMap<Long, Point> points;
    private HashMap<Long, Spawn> spawns;
    private Mission mission;

    public void setStages(List<Stage> stageList) {
        stages = new HashMap<>();
        for (var stage : stageList)
            stages.put(stage.getId(), stage);
    }

    public List<Stage> getStages() {
        return stages.values().stream().toList();
    }

    public Stage getStage(long id){
        return stages.get(id);
    }
}
