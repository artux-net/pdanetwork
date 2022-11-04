package net.artux.pdanetwork.models.quest;

import lombok.Data;

import java.util.List;

@Data
public class Mission {

    private String title;
    private String name;
    private List<Checkpoint> checkpoints;

    public Checkpoint getCheckpointWithParam(String param){
        for (Checkpoint checkpoint : checkpoints){
            if (checkpoint.getParameter().equals(param)){
                return checkpoint;
            }
        }
        return null;
    }

    public Checkpoint getNextCheckpoint(String param){
        for (int i = 0; i < checkpoints.size() - 1; i++) {
            if (checkpoints.get(i).getParameter().equals(param))
                return checkpoints.get(i+1);
        }
        return null;
    }
}
