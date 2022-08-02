package net.artux.pdanetwork.service.util;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Data
@Getter
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

  public String getAddress(){
    return getProtocol() + "://" + getHost() + getContextPath();
  }

}
