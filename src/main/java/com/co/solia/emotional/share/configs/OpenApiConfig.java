package com.co.solia.emotional.share.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Contact;

import java.util.Collections;

/**
 * This class configures the swagger.
 * You can still add the header manually if you'd like.
 *
 * @author luis.bolivar
 */
@Configuration
public class OpenApiConfig {

    /**
     * The bean definition
     *
     * @return OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String title = String.format("%s API", "Campaign Generator.");
        final String version = "1";
        final String description = "The all endpoints to create campaigns and much more.";
        final String summary = "The endpoints to create a campaign and individual use.";
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description)
                        .contact(new Contact()
                                .email("lebolivarc@sol-ia.mx")
                                .name("Luis Bolivar"))
                        .summary(summary))
                .servers(Collections.singletonList(
                        new Server().url("http://localhost:8080")
                ));
    }

}
