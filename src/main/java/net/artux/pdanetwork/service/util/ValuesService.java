package net.artux.pdanetwork.service.util;

import lombok.Data;
import lombok.Getter;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.UUID;


@Service
@Data
@Getter
@Mapper
public class ValuesService {

    @Value("${server.servlet.contextPath}")
    private String contextPath;

    @Value("${server.protocol}")
    private String protocol;

    @Value("${server.websocket.protocol}")
    private String webSocketProtocol;

    @Value("${server.host}")
    private String host;

    @Value("${files.server.address}")
    private String filesAddress;

    @Value("${files.server.user}")
    private String filesUser;

    @Value("${files.server.password}")
    private String filesPassword;

    @Value("${spring.mail.username}")
    private String email;

    @PostConstruct
    public void setFilesServerPassword(){
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(filesUser, filesPassword.toCharArray());
            }
        });
    }

    public String getStoryFilesUrl() {
        return filesAddress + "stories";
    }

    public String getAddress() {
        return getProtocol() + "://" + getHost() + getContextPath();
    }

    @Named("articleUrl")
    public String getArticleUrl(UUID id) {
        return getAddress() + "/feed/" + id;
    }

}
