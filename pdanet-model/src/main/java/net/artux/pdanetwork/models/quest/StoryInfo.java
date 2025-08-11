package net.artux.pdanetwork.models.quest;

import lombok.Data;
import net.artux.pdanetwork.models.user.enums.Role;

import java.util.Locale;

@Data
public class StoryInfo {

    private Long id;
    private String title;
    private String desc;
    private String icon;
    private int[] needs;
    private Role access = Role.TESTER;
    private Locale locale = Locale.forLanguageTag("RU");

}
