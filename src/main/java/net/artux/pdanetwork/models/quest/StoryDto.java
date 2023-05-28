package net.artux.pdanetwork.models.quest;

import lombok.Data;

import java.util.Map;

@Data
public class StoryDto {

    private Long id;
    private String title;
    private String desc;
    private String icon;
    private Map<Long, ChapterDto> chapters;
    private Map<Long, GameMap> maps;

}
