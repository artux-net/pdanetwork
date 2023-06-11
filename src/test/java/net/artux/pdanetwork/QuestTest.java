package net.artux.pdanetwork;

import net.artux.pdanetwork.controller.rest.admin.AdminQuestsController;
import net.artux.pdanetwork.controller.rest.quest.QuestController;
import net.artux.pdanetwork.models.quest.Chapter;
import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.models.quest.map.Point;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestTest {

    @Autowired
    private QuestController questController;
    @Autowired
    private AdminQuestsController adminQuestsController;

    @Test
    @WithMockUser(username = "admin", roles = "MODERATOR")
    public void isStatisticClosed() {
        Story story = new Story();
        story.setId(1L);
        story.setTitle("title");
        Chapter chapter = new Chapter();
        chapter.setId(1L);
        chapter.setPoints(Map.of(1l, List.of(new Point())));
        //adminQuestsController.uploadPublicStory();
        //assertThrows(AccessDeniedException.class, () -> statisticController.getStatistic());
    }

    /**
     * this method is testing adding new story with two chapters of ten stages and two points in each chapter *
     */
    @Test
    @WithMockUser(username = "admin", roles = "MODERATOR")
    public void testStory() {
        Story story = new Story();
        story.setId(1L);
        story.setTitle("title");
        Chapter chapter1 = new Chapter();
        chapter1.setId(1L);
        chapter1.setPoints(Map.of(1l, List.of(generatePoint())));
        Chapter chapter2 = new Chapter();
        chapter2.setId(2L);
        chapter2.setPoints(Map.of(2l, List.of(generatePoint())));
        Chapter chapter3 = new Chapter();
        chapter3.setId(3L);
        chapter3.setPoints(Map.of(3l, List.of(generatePoint())));
        Chapter chapter4 = new Chapter();
        chapter4.setId(4L);
        chapter4.setPoints(Map.of(4l, List.of(generatePoint())));
        Chapter chapter5 = new Chapter();
        chapter5.setId(5L);
        chapter5.setPoints(Map.of(5l, List.of()));
        story.setChapters(List.of(chapter1, chapter2, chapter3, chapter4, chapter5));
        adminQuestsController.uploadPublicStory(story);
        System.out.println(adminQuestsController.getStory(1L).getChapters().stream().findFirst().get().getPoints().size());
    }

    int pointId = 0;

    /**
     * @return random point with random information
     */
    public Point generatePoint() {
        Point point = new Point();
        point.setId(++pointId);
        point.setName("Some point");
        return point;
    }


    public Point generateRandomPoint() {
        Point point = new Point();
        point.setId(++pointId);
        point.setName("Some point");
        return point;
    }

}