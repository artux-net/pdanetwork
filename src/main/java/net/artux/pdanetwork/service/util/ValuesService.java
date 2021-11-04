package net.artux.pdanetwork.service.util;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Data
@Getter
public class ValuesService {

  @Value("${pdanetwork.configsUrl}")
  private String configUrl;

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

  @Value("${spring.data.mongodb.host}")
  private String mongoHost;
  @Value("${spring.data.mongodb.port}")
  private String mongoPort;
  @Value("${spring.data.mongodb.username:#{null}}")
  private String mongoUsername;
  @Value("${spring.data.mongodb.password:#{null}}")
  private String mongoPassword;

  public String getAddress(){
    return getProtocol() + "://" + getHost() + getContextPath();
  }

  public String getMongoUri(){
    if (!Utils.isEmpty(getMongoUsername()) && !Utils.isEmpty(getMongoPassword()))
      return "mongodb://" + getMongoUsername() + ":" + getMongoPassword() + "@" + getMongoHost() + ":" + getMongoPort();
    else return "mongodb://" + getMongoHost() + ":" + getMongoPort();

  }

}
