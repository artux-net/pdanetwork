package net.artux.pdanetwork.configuration;

import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.models.quest.Chapter;
import net.artux.pdanetwork.models.quest.GameMap;
import net.artux.pdanetwork.models.quest.Stage;
import net.artux.pdanetwork.models.quest.Story;
import net.artux.pdanetwork.models.quest.StoryDto;
import net.artux.pdanetwork.models.quest.admin.StoriesStatus;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.service.email.EmailService;
import net.artux.pdanetwork.service.quest.QuestService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@TestConfiguration
public class ServiceTestConfiguration {

    @Autowired
    private WebApplicationContext applicationContext;
    @Autowired
    private Logger logger;
    private MockMvc mockMvc;

    @Bean
    @Primary
    public EmailService employeeService() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext).apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        return new EmailService() {
            @Override
            public void askForPassword(UserEntity user, String token) {

            }

            @Override
            public void sendRegisterLetter(RegisterUserDto user, Long pdaId) {
                logger.info("sendRegisterLetter");
            }

            @Override
            public void sendConfirmLetter(RegisterUserDto user, String token) {
                logger.info("Registration token handled");
                try {
                    mockMvc.perform(get("/confirmation/register?t=" + token))
                            .andDo(print())
                            .andReturn();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            // implement methods
        };
    }

    @Bean
    @Primary
    public QuestService questService() {
        return new QuestService() {

            @Override
            public Stage getStage(long storyId, long chapterId, long stageId) {
                return null;
            }

            @Override
            public HashMap<String, List<String>> getActionsOfStage(long storyId, long chapterId, long stageId) {
                return null;
            }

            @Override
            public Chapter getChapter(long storyId, long chapterId) {
                return null;
            }

            @Override
            public GameMap getMap(long storyId, long mapId) {
                return null;
            }

            @Override
            public Story getStory(long storyId) {
                return null;
            }

            @Override
            public Status downloadStories() {
                return null;
            }

            @Override
            public Status setStories(MultipartFile storiesArchive) {
                return null;
            }

            @Override
            public Status setUserStory(Story story) {
                return null;
            }

            @Override
            public Collection<Story> getStories() {
                return null;
            }

            @Override
            public Collection<StoryDto> getStoriesDto() {
                return null;
            }

            @Override
            public Instant getReadTime() {
                return null;
            }

            @Override
            public StoriesStatus getStatus() {
                return null;
            }
        };
    }

}
