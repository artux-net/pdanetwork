package net.artux.pdanetwork.models.quest;

import net.artux.pdanetwork.models.quest.admin.StoryInfoAdmin;
import net.artux.pdanetwork.models.quest.map.GameMapDto;
import net.artux.pdanetwork.models.quest.map.MapEnumGetter;
import net.artux.pdanetwork.models.quest.stage.Stage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface QuestMapper {

    @Mapping(target = "stages", expression = "java(story.stageCount())")
    @Mapping(target = "chapters", expression = "java(story.chapterCount())")
    @Mapping(target = "points", expression = "java(story.pointCount())")
    StoryInfoAdmin adminInfo(StoryDto story);

    List<StoryInfoAdmin> adminInfo(Collection<StoryDto> story);

    StoryInfo info(StoryDto story);

    List<StoryInfo> info(Collection<StoryDto> story);

    @Mapping(target = "id", expression = "java(enumGetter.getId())")
    @Mapping(target = "name", expression = "java(enumGetter.getName())")
    @Mapping(target = "background", expression = "java(enumGetter.getBackground())")
    @Mapping(target = "title", expression = "java(enumGetter.getTitle())")
    @Mapping(target = "tmx", expression = "java(enumGetter.getTmx())")
    @Mapping(target = "defaultPosition", expression = "java(enumGetter.getDefaultPosition())")
    GameMapDto dto(MapEnumGetter enumGetter);

    @Mapping(target = "maps", expression = "java(mapsToMap(story.getMaps(), story.getChapters()))")
    StoryDto dto(Story story);

    default Map<Long, GameMap> mapsToMap(Collection<GameMap> maps, Collection<Chapter> chapters) {
        HashMap<Long, GameMap> map = new HashMap<>();
        for (GameMap gameMap : maps) {
            for (Chapter chapter : chapters) {
                //compile maps
                if (chapter.getSpawns() != null)
                    if (chapter.getSpawns().get(gameMap.getId()) != null) {
                        gameMap.getSpawns().addAll(chapter.getSpawns().get(gameMap.getId()));
                    }
                if (chapter.getPoints() != null)
                    if (chapter.getPoints().get(gameMap.getId()) != null) {
                        gameMap.getPoints().addAll(chapter.getPoints().get(gameMap.getId()));
                    }
            }

            map.put(gameMap.getId(), gameMap);
        }
        return map;
    }

    default Map<Long, ChapterDto> chaptersToMap(Collection<Chapter> chapters) {
        HashMap<Long, ChapterDto> map = new HashMap<>();
        for (Chapter chapter : chapters) {
            map.put(chapter.getId(), dto(chapter));
        }
        return map;
    }

    default Map<Long, Stage> stagesToMap(Collection<Stage> stages) {
        HashMap<Long, Stage> map = new HashMap<>();
        for (Stage stage : stages) {
            map.put(stage.getId(), stage);
        }
        return map;
    }

    ChapterDto dto(Chapter chapter);

}
