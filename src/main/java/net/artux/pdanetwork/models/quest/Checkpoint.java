package net.artux.pdanetwork.models.quest;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class Checkpoint {

    private String parameter; // наличие параметра показывает актуальность чека (он становится текущим)
    private String title;
    private CheckpointType type;
    private HashMap<String, List<String>> condition; // условие выполнения, по выполнении добавляется параметр следующего чека
    private HashMap<String, List<String>> actions;//действия, выполняемые по достижении условия, кроме добавления следующего параметра и удаления текущего

}
