package com.beolnix.marvin.im.plugin.history;

import com.beolnix.marvin.history.api.ChatApi;
import com.beolnix.marvin.history.api.MessageApi;
import com.beolnix.marvin.history.api.model.ChatDTO;
import com.beolnix.marvin.history.api.model.CreateChatDTO;
import com.beolnix.marvin.history.api.model.CreateMessageDTO;
import com.beolnix.marvin.history.api.model.MessageDTO;
import com.beolnix.marvin.im.api.model.IMIncomingMessage;
import com.beolnix.marvin.plugins.api.PluginConfig;
import feign.FeignException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by beolnix on 07/02/16.
 */
@Service
public class HistoryService {

    // dependencies
    private final ChatApi chatApi;
    private final MessageApi messageApi;
    private final Logger logger;
    private final PluginConfig pluginConfig;

    @Autowired
    public HistoryService(PluginConfig pluginConfig, Logger logger, ChatApi chatApi, MessageApi messageApi) {
        this.messageApi = messageApi;
        this.chatApi = chatApi;
        this.pluginConfig = pluginConfig;
        this.logger = logger;
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

        MessageDTO messageDTO = messageApi.createMessate(convert(msg, chatDTO));
        logger.debug("Saved message: " + messageDTO.toString() + "; from chat: " + chatDTO.toString());
    }

    private CreateMessageDTO convert(IMIncomingMessage msg, ChatDTO chatDTO) {
        CreateMessageDTO createMessageDTO = new CreateMessageDTO();
        createMessageDTO.setAutor(msg.getAuthor());
        createMessageDTO.setChatId(chatDTO.getId());
        createMessageDTO.setMsg(msg.getRawMessageBody());
        return createMessageDTO;
    }

    private ChatDTO getOrCreateChatByName(String chatName, String protocol) {
        ChatDTO chatDTO = null;
        try {
            chatDTO = chatApi.getChatByName(chatName);
        } catch (FeignException e) {
            // nop
        }
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
