package org.example.scheduleevent.rest.controller.util;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CustomSwaggerConfigController {

    @GetMapping("/v3/api-docs/swagger-config")
    public Map<String, String> customSwaggerConfig() {

        Map<String, String> defaultConfig = new HashMap<>();
        defaultConfig.put("oauth2RedirectUrl", "https://api.quqee.tech/swagger-ui/oauth2-redirect.html");
        defaultConfig.put("configUrl", "/v3/api-docs/swagger-config");
        defaultConfig.put("url", "/v3/api-docs");
        defaultConfig.put("validatorUrl", "");

        return defaultConfig;
    }
}
