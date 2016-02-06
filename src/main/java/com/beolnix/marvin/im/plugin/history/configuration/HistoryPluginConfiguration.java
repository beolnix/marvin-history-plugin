package com.beolnix.marvin.im.plugin.history.configuration;

import com.beolnix.marvin.im.plugin.history.HistoryIMPlugin;
import com.beolnix.marvin.plugins.api.IMPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by beolnix on 06/02/16.
 */
@Configuration
public class HistoryPluginConfiguration {

    @Bean
    public IMPlugin getPlugin() {
        return new HistoryIMPlugin();
    }
}
