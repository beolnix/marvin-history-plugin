package com.beolnix.marvin.im.plugin.history;

import com.beolnix.marvin.im.api.IMSessionManager;
import com.beolnix.marvin.im.api.model.IMIncomingMessage;
import com.beolnix.marvin.im.api.model.IMOutgoingMessage;
import com.beolnix.marvin.im.api.model.IMOutgoingMessageBuilder;
import com.beolnix.marvin.im.plugin.HistoryConfiguration;
import com.beolnix.marvin.im.plugin.PluginUtils;
import com.beolnix.marvin.plugins.api.IMPlugin;
import com.beolnix.marvin.plugins.api.IMPluginState;
import com.beolnix.marvin.plugins.api.PluginConfig;
import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;

/**
 * Created by DAtmakin on 11/9/2015.
 */

public class HistoryIMPlugin implements IMPlugin {

    // dependencies
    private Logger logger;
    private File pluginDirPath;

    // state
    private IMSessionManager imSessionManager;
    private IMPluginState state = IMPluginState.NOT_INITIALIZED;
    private PluginConfig pluginConfig;
    private String serviceUrl;
    private HistoryService historyService;
    private ApplicationContext ctx;

    // constants
    public static final String PLUGIN_NAME = "HistoryIMPlugin";
    private static final String COMMAND_HELP = "help";
    public static final String COMMAND_GET_LINK = "link";
    private static final List<String> commandsList = Arrays.asList(COMMAND_HELP, COMMAND_GET_LINK);

    @Override
    public void init(PluginConfig pluginConfig, IMSessionManager imSessionManager) {
        this.imSessionManager = imSessionManager;
        this.pluginConfig = pluginConfig;
        PluginUtils pluginUtils = new PluginUtils();
        Logger logger = pluginUtils.getLogger(pluginConfig.getLogsDirPath(), PLUGIN_NAME);

        if (StringUtils.isEmpty(serviceUrl)) {
            logger.error("Got empty history service url, plugin will not process any request.");
        } else {
            this.state = IMPluginState.INITIALIZED;
        }

        this.ctx = createContext(pluginConfig, logger, imSessionManager);
        this.historyService = ctx.getBean(HistoryService.class);
    }

    private ApplicationContext createContext(PluginConfig pluginConfig, Logger logger, IMSessionManager imSessionManager) {
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getBeanFactory().registerSingleton("pluginConfig", pluginConfig);
        ctx.getBeanFactory().registerSingleton("logger", logger);
        ctx.getBeanFactory().registerSingleton("imSessionManager", imSessionManager);
        ctx.register(HistoryConfiguration.class);
        ctx.refresh();

        return ctx;
    }

    @Override
    public String getPluginName() {
        if (logger != null) {
            logger.trace("getPluginName invoked");
        }
        return PLUGIN_NAME;
    }

    @Override
    public boolean isProcessAll() {
        return true;
    }

    @Override
    public List<String> getCommandsList() {
        return commandsList;
    }

    @Override
    public boolean isCommandSupported(String s) {
        return commandsList.contains(s);
    }

    @Override
    public void process(IMIncomingMessage msg) {
        if (!IMPluginState.INITIALIZED.equals(state)) {
            if (logger != null) {
                logger.error("plugin hasn't been initialized yet. can't process msg: " + msg);
                return;
            }
        }

        if (COMMAND_GET_LINK.equals(msg.getCommandName())) {
            imSessionManager.sendMessage(createOutMsg(msg, "new post invoked"));
        } else if (COMMAND_HELP.equals(msg.getCommandName())) {
            imSessionManager.sendMessage(
                    createOutMsg(
                            msg,
                            "* - history plugin intercept all messages"
                    ),
                    createOutMsg(
                            msg,
                            COMMAND_GET_LINK + " - returns link to the history page"
                    )
            );
        } else {
            logger.debug("Persisting new msg: " + msg.getAuthor() + " - " + msg.getRawMessageBody());
            historyService.newMessage(msg);
        }
    }

    private IMOutgoingMessage createOutMsg(IMIncomingMessage msg, String answer) {
        return new IMOutgoingMessageBuilder(msg)
                .withRawMessageBody(answer)
                .withFromPlugin(getPluginName())
                .build();
    }

    @Override
    public IMPluginState getPluginState() {
        return state;
    }

    @Override
    public String getErrorDescription() {
        return null;
    }

    @Override
    public Set<String> getSupportedProtocols() {
        return Collections.emptySet();
    }

    @Override
    public boolean isProtocolSupported(String s) {
        return true;
    }

    @Override
    public boolean isAllProtocolsSupported() {
        return true;
    }
}
