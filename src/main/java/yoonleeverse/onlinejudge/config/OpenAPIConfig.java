package yoonleeverse.onlinejudge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import yoonleeverse.onlinejudge.api.common.constant.Constants;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenAPIConfig {

    public OpenAPIConfig(ObjectMapper objectMapper, MappingJackson2HttpMessageConverter converter) {
        ModelConverters.getInstance().addConverter(new ModelResolver(objectMapper));

        var supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        supportedMediaTypes.add(new MediaType("application", "octet-stream"));
        converter.setSupportedMediaTypes(supportedMediaTypes);
    }

    @Bean
    public OpenAPI openAPI() {
        Server alphaServer = new Server();
        alphaServer.setUrl("https://online-judge-api.yoonleeverse.com");
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
                .servers(List.of(alphaServer));
    }

}
