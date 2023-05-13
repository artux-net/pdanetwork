package net.artux.pdanetwork.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@RequiredArgsConstructor
public class SwaggerConfig {

    private final ValuesService valuesService;

    @Bean
    public GroupedOpenApi restApi() {
        return GroupedOpenApi.builder()
                .group("pdanetwork-rest")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI openApi() {
        Server server = new Server();
        server.setUrl(valuesService.getAddress());
        return new OpenAPI()
                .servers(List.of(server))
                .info(new Info()
                        .title("PDANETWORK")
                        .description("Сервисы REST Api. Для использования необходимо зарегистрироваться," +
                                " подтвердить почту и войти в аккаунт. Чтобы войти в аккаунт необходимо нажать" +
                                " на замок и ввести свои данные. <br>"
                                + "<br> <a href=\"" + valuesService.getAddress() + "/utility\">Панель администратора</a>"
                                + "<br> <a href=\"" + valuesService.getAddress() + "/enc\">Энциклопедия</a>")
                        .version("0.3")
                        .license(null)
                )
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
    }

}
