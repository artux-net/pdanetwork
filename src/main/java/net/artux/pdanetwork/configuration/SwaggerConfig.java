package net.artux.pdanetwork.configuration;

import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final ValuesService valuesService;

    public SwaggerConfig(ValuesService valuesService) {
        this.valuesService = valuesService;
    }

    private SecurityScheme basicAuthScheme() {
        return new BasicAuth("basicAuth");
    }

    private SecurityReference basicAuthReference() {
        return new SecurityReference("basicAuth", new AuthorizationScope[0]);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(List.of(basicAuthReference()))
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(basicAuthScheme()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("net.artux.pdanetwork"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "PDA NETWORK API",
                "Описание сервисов Rest API. Админ панель: " + valuesService.getAddress() + "/utility",
                "1.0",
                "",
                null,
                "",
                "",
                Collections.emptyList());
    }

}
