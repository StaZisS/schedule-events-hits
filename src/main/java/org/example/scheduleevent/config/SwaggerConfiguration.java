package org.example.scheduleevent.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI customizeOpenAPI(@Value("${authServer.authorizationUrl}") String authorizationUrl,
                                    @Value("${authServer.tokenUrl}") String tokenUrl,
                                    @Value("${authServer.refreshUrl}") String refreshUrl,
                                    @Value("${my-info.serverUrl}") String serverUrl) {
        final String securitySchemeName = "oauth2";
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .flows(new OAuthFlows()
                                                .authorizationCode(new OAuthFlow()
                                                        .authorizationUrl(authorizationUrl)
                                                        .tokenUrl(tokenUrl)
                                                        .scopes(new Scopes().addString("openid", "openid scope"))
                                                        .refreshUrl(refreshUrl)
                                                )
                                        )
                        )
                )
                .addServersItem(new Server().url(serverUrl).description("Сервер"));
    }
}
