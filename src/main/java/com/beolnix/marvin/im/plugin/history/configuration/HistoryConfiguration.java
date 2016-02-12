package com.beolnix.marvin.im.plugin.history.configuration;

import com.beolnix.marvin.history.api.Constants;
import com.beolnix.marvin.plugins.api.PluginConfig;
import com.netflix.loadbalancer.Server;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import java.util.Collections;

import static com.beolnix.marvin.im.plugin.history.configuration.Constants.PROP_SERVICE_URL;

/**
 * Created by beolnix on 07/02/16.
 */
@Configuration
@ComponentScan("com.beolnix")
@EnableFeignClients("com.beolnix")
@EnableAutoConfiguration
@SpringBootApplication
@RibbonClient(value = "history", configuration = HistoryClientConfiguration.class)
public class HistoryConfiguration {

    @Autowired
    private Logger logger;

    @Autowired
    private PluginConfig pluginConfig;

    @Autowired
    private SpringClientFactory springClientFactory;

    @PostConstruct
    public void init() {
        String baseUrl = pluginConfig.getPropertyByName(PROP_SERVICE_URL);
        logger.info("Got history service url: " + baseUrl + " for history plugin.");

        Server service = new Server(baseUrl);
        springClientFactory.getLoadBalancer(Constants.FEIGN_CLIENT_NAME).addServers(Collections.singletonList(service));
    }

}
