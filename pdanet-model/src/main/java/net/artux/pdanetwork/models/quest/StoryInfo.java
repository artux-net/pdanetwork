package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.models.user.enums.Role;

@Data
public class StoryInfo {

    private Long id;
    private String title;
    private String desc;
    private String icon;
    private int[] needs;
    private Role access = Role.TESTER;

}
