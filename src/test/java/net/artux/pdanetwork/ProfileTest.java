package net.artux.pdanetwork;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import net.artux.pdanetwork.entity.user.UserEntity;
import net.artux.pdanetwork.models.security.SecurityUser;
import net.artux.pdanetwork.models.user.Profile;
import net.artux.pdanetwork.models.user.dto.RegisterUserDto;
import net.artux.pdanetwork.repository.user.UserRepository;
import net.artux.pdanetwork.service.user.UserService;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfileTest {

    @Autowired
    private WebApplicationContext applicationContext;
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper mapper;

    private UUID id;

    @PostConstruct
    public void init() {
        RegisterUserDto user = RegisterUserDto.builder()
                .login("admin")
                .email("test@gmail.com")
                .avatar("0")
                .name("name")
                .nickname("nickname")
                .password("12345678")
                .build();

        id = userService.getUserByLogin("admin").getId();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext).apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    public SecurityUser getSecurityUser() {
        UserEntity user = userRepository.findById(id).orElseThrow();
        return new SecurityUser(user.getLogin(), user.getPassword(), listOf(new SimpleGrantedAuthority("ADMIN")), user.getId());
    }

    @Ignore
    @Test
    public void getProfile() throws Exception {
        mockMvc.perform(get("/api/v1/profile").with(user(getSecurityUser()))).andDo(print());
    }

    @Test
    @Ignore
    public void ratingIndexWorks() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/profile").with(user(getSecurityUser())))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Profile profile = mapper.readValue(mvcResult.getResponse().getContentAsString(), Profile.class);


        // mockMvc.perform(get("/api/v1/profile").with(user(getSecurityUser()))).andDo(print());
    }

}