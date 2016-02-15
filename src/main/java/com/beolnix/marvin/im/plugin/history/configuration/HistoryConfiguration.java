package com.beolnix.marvin.im.plugin.history.configuration;

import com.beolnix.HistoryClientConfiguration;
import com.beolnix.marvin.history.api.Constants;
import com.beolnix.marvin.plugins.api.PluginConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.netflix.loadbalancer.Server;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import java.util.Collections;

import static com.beolnix.marvin.im.plugin.history.configuration.Constants.PROP_SERVICE_URL;

/**
 * Created by beolnix on 07/02/16.
 */
@Configuration
@ComponentScan("com.beolnix.marvin")
@EnableFeignClients("com.beolnix.marvin")
@EnableAutoConfiguration
@SpringBootApplication
@RibbonClient(value = Constants.FEIGN_SERVICE, configuration = HistoryClientConfiguration.class)
public class HistoryConfiguration {

    @Bean
    public ObjectMapper jsonMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        //Fully qualified path shows I am using latest enum
        objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.
                WRITE_DATES_AS_TIMESTAMPS , false);
        objectMapper.getSerializationConfig().with(new ISO8601DateFormat());
        return objectMapper;
    }

}
