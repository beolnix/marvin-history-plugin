package com.beolnix.marvin.im.plugin.history;

import com.beolnix.marvin.im.api.IMSessionManager;
import com.beolnix.marvin.im.api.model.IMIncomingMessage;
import com.beolnix.marvin.im.api.model.IMOutgoingMessage;
import com.beolnix.marvin.im.api.model.IMOutgoingMessageBuilder;
import com.beolnix.marvin.im.plugin.PluginUtils;
import com.beolnix.marvin.plugins.api.IMPlugin;
import com.beolnix.marvin.plugins.api.IMPluginState;
import com.beolnix.marvin.plugins.api.PluginConfig;
import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;
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

    // constants
    public static final String PROP_SERVICE_URL = "service.url";
    private static final String COMMAND_HELP = "help";
    public static final String COMMAND_GET_LINK = "link";
    private static final List<String> commandsList = Arrays.asList(COMMAND_HELP, COMMAND_GET_LINK);

    @Override
    public void init(PluginConfig pluginConfig, IMSessionManager imSessionManager) {
        this.imSessionManager = imSessionManager;
        this.pluginConfig = pluginConfig;
        PluginUtils pluginUtils = new PluginUtils();
        this.logger = pluginUtils.getLogger(pluginConfig.getLogsDirPath(), getPluginName());
        this.serviceUrl = pluginConfig.getPropertyByName(PROP_SERVICE_URL);

        if (StringUtils.isEmpty(serviceUrl)) {
            logger.error("Got empty history service url, plugin will not process any request.");
        } else {
            this.state = IMPluginState.INITIALIZED;
        }

    }

    @Override
    public String getPluginName() {
        if (logger != null) {
            logger.trace("getPluginName invoked");
        }
        return this.getClass().getSimpleName();
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
    public void process(IMIncomingMessage imIncomingMessage) {
        if (COMMAND_GET_LINK.equals(imIncomingMessage.getCommandName())) {
            imSessionManager.sendMessage(createOutMsg(imIncomingMessage, "new post invoked"));
        } else if (COMMAND_HELP.equals(imIncomingMessage.getCommandName())) {
            imSessionManager.sendMessage(
                    createOutMsg(
                            imIncomingMessage,
                            "* - history plugin intercept all messages"
                    ),
                    createOutMsg(
                            imIncomingMessage,
                            COMMAND_GET_LINK + " - returns link to the history page"
                    )
            );
        } else {

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
