package com.beolnix.marvin.im.plugin;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by beolnix on 07/02/16.
 */
@Configuration
@ComponentScan("com.beolnix")
@EnableFeignClients("com.beolnix")
@EnableAutoConfiguration
@SpringBootApplication
@RibbonClient("history")
public class HistoryConfiguration {
}
