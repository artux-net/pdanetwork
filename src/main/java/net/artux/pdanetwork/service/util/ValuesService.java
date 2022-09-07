package net.artux.pdanetwork.service.util;

import lombok.Data;
import lombok.Getter;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Value("${spring.mail.username}")
    private String email;

    public String getAddress() {
        return getProtocol() + "://" + getHost() + getContextPath();
    }

    @Named("articleUrl")
    public String getArticleUrl(UUID id) {
        return getAddress() + "/feed/" + id;
    }

}
