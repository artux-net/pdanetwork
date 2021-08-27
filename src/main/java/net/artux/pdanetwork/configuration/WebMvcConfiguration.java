package net.artux.pdanetwork.configuration;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.*;

@EnableWebMvc
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

  private final ValuesService valuesService;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String baseUrl = StringUtils.trimTrailingCharacter(valuesService.getAddress(), '/');
    registry.addResourceHandler(baseUrl + "/swagger-ui/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
            .resourceChain(false);
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController(valuesService.getAddress() + "/swagger-ui/")
            .setViewName("forward:" + valuesService.getAddress() + "/swagger-ui/index.html");
  }
}
