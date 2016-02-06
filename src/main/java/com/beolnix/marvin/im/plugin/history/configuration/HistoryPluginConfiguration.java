package com.beolnix.marvin.im.plugin.history.configuration;

import com.beolnix.marvin.history.api.ChatApi;
import com.beolnix.marvin.history.api.MessageApi;
import com.beolnix.marvin.im.plugin.history.HistoryIMPlugin;
import com.beolnix.marvin.plugins.api.IMPlugin;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by beolnix on 06/02/16.
 */
@Configuration
public class HistoryPluginConfiguration {

    @Bean
    public ChatApi getChatApi() {
        ChatApi chatApi = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(ChatApi.class, "https://test");
        return chatApi;
    }

    @Bean
    public IMPlugin getPlugin() {
        return new HistoryIMPlugin();
    }
}
