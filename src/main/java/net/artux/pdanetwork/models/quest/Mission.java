package net.artux.pdanetwork.models.quest;

import lombok.Data;

import java.util.List;

@Data
public class Mission {

    private String title;
    private List<Checkpoint> checkpoints;

    public Checkpoint currentCheckpoint(String param){
        for (Checkpoint checkpoint : checkpoints){
            if (checkpoint.getParameter().equals(param)){
                return checkpoint;
            }
        }
        return null;
    }
}
