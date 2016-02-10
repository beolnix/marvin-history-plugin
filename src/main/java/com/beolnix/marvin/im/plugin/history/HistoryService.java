package com.beolnix.marvin.im.plugin.history;

import com.beolnix.marvin.history.api.ChatApi;
import com.beolnix.marvin.history.api.MessageApi;
import com.beolnix.marvin.history.api.model.ChatDTO;
import com.beolnix.marvin.history.api.model.CreateChatDTO;
import com.beolnix.marvin.history.api.model.CreateMessageDTO;
import com.beolnix.marvin.im.api.model.IMIncomingMessage;
import com.beolnix.marvin.im.plugin.PluginUtils;
import com.beolnix.marvin.plugins.api.PluginConfig;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by beolnix on 07/02/16.
 */
@Service
public class HistoryService {

    // initial config
    private final String baseUrl;
    private final String authKey;
    private final String authHeader;

    // dependencies
    private ChatApi chatApi;
    private MessageApi messageApi;
    private final Logger logger;
    private final PluginConfig pluginConfig;

    // constants
    public static final String PROP_SERVICE_URL = "service.url";
    public static final String PROP_SERVICE_AUTH_KEY = "service.auth.key";
    public static final String PROP_SERVICE_AUTH_PASS = "service.auth.pass";

    @Autowired
    public HistoryService(PluginConfig pluginConfig, Logger logger) {
        this.pluginConfig = pluginConfig;
        this.logger = logger;
        this.baseUrl = pluginConfig.getPropertyByName(PROP_SERVICE_URL);
        this.authKey = pluginConfig.getPropertyByName(PROP_SERVICE_AUTH_KEY);
        this.authHeader = pluginConfig.getPropertyByName(PROP_SERVICE_AUTH_PASS);
    }

    public void newMessage(IMIncomingMessage msg) {
        if (!msg.isConference()) {
            logger.debug("ignore nonConference message: " + msg.getAuthor() +
                    " - " + msg.getRawMessageBody());
        }

        ChatDTO chatDTO = getOrCreateChatByName(msg.getConferenceName(), msg.getProtocol());
        if (chatDTO == null) {
            logger.error("Can't get chat for name " + msg.getConferenceName());
            return;
        }
        messageApi.createMessate(convert(msg, chatDTO));
    }

    private CreateMessageDTO convert(IMIncomingMessage msg, ChatDTO chatDTO) {
        CreateMessageDTO createMessageDTO = new CreateMessageDTO();
        createMessageDTO.setAutor(msg.getAuthor());
        createMessageDTO.setChatId(chatDTO.getId());
        createMessageDTO.setMsg(msg.getRawMessageBody());
        return createMessageDTO;
    }

    private ChatDTO getOrCreateChatByName(String chatName, String protocol) {
        ChatDTO chatDTO = chatApi.getChatByName(chatName);
        if (chatDTO == null) {
            logger.debug("Got null chat. Creating new one with name " + chatName);
            CreateChatDTO createChatDTO = new CreateChatDTO();
            createChatDTO.setConference(true);
            createChatDTO.setName(chatName);
            createChatDTO.setProtocol(protocol);
            chatDTO = chatApi.createChat(createChatDTO);
            logger.debug("Created chat: " + chatDTO);
        }

        return chatDTO;
    }


}
