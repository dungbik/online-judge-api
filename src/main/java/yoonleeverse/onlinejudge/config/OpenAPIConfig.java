package yoonleeverse.onlinejudge.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yoonleeverse.onlinejudge.api.common.constant.Constants;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("local server");

        Server alphaServer = new Server();
        alphaServer.setUrl("http://online-judge-api.yoonleeverse.com");
        alphaServer.setDescription("alpha server");

        return new OpenAPI()
                .info(new Info()
                        .title("Online Judge API Doc")
                        .version("0.0.1")
                )
                .components(new Components()
                        .addSecuritySchemes("Bearer",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("Bearer")
                        )
                        .addSecuritySchemes("Refresh Token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.COOKIE)
                                        .name(Constants.Cookie.REFRESH_TOKEN)
                        )
                )
                .servers(List.of(localServer, alphaServer));
    }

}
