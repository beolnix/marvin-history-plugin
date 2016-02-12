package com.beolnix.marvin.im.plugin.history.configuration;

import static com.beolnix.marvin.im.plugin.history.configuration.Constants.PROP_SERVICE_AUTH_KEY;
import static com.beolnix.marvin.im.plugin.history.configuration.Constants.PROP_SERVICE_AUTH_PASS;

import static com.beolnix.marvin.history.api.Constants.API_KEY_HEADER;
import static com.beolnix.marvin.history.api.Constants.API_AUTH_HEADER;

import com.beolnix.marvin.plugins.api.PluginConfig;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by beolnix on 12/02/16.
 */
@Component
public class FeignRequestAuthInterceptor implements RequestInterceptor {

    private final PluginConfig pluginConfig;
    private final String apiKey;
    private final String apiAuth;

    @Autowired
    public FeignRequestAuthInterceptor(PluginConfig pluginConfig) {
        this.pluginConfig = pluginConfig;
        this.apiKey = pluginConfig.getPropertyByName(PROP_SERVICE_AUTH_KEY);
        this.apiAuth = pluginConfig.getPropertyByName(PROP_SERVICE_AUTH_PASS);
    }

    @Override
    public void apply(feign.RequestTemplate template) {
        template.header(API_KEY_HEADER, apiKey);
        template.header(API_AUTH_HEADER, apiAuth);
    }
}
