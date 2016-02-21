
import com.beolnix.marvin.config.api.model.Property;
import com.beolnix.marvin.history.api.model.ChatDTO;
import com.beolnix.marvin.history.api.model.CreateChatDTO;
import com.beolnix.marvin.im.api.IMSessionManager;
import com.beolnix.marvin.im.api.model.IMIncomingMessage;
import com.beolnix.marvin.im.api.model.IMIncomingMessageBuilder;
import com.beolnix.marvin.im.plugin.history.HistoryService;
import com.beolnix.marvin.im.plugin.history.configuration.Constants;
import com.beolnix.marvin.im.plugin.history.configuration.HistoryConfiguration;
import com.beolnix.marvin.plugins.api.PluginConfig;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnit44Runner;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ChatControllerIntegrationTest {

    private Integer port;
    private Logger logger = Logger.getLogger(ChatControllerIntegrationTest.class);

    private String CHAT_NAME = "testChat";

    @Mock
    IMSessionManager imSessionManager;

    @Test
    @Ignore
    public void createChat() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getBeanFactory().registerSingleton("pluginConfig", getPluginConfig());
        ctx.getBeanFactory().registerSingleton("logger", logger);
        ctx.getBeanFactory().registerSingleton("imSessionManager", imSessionManager);
        ctx.register(HistoryConfiguration.class);
        ctx.refresh();

        HistoryService historyService = ctx.getBean(HistoryService.class);
        historyService.newMessage(getHelpMsg());
    }


    public PluginConfig getPluginConfig() {
        List<Property> props = new ArrayList<>();
        props.add(new Property(Constants.PROP_SERVICE_URL, "history.buildloft.com"));

        PluginConfig pluginConfig = new PluginConfig("target", new File("target"), props);
        return pluginConfig;
    }

    public IMIncomingMessage getHelpMsg() {
        return new IMIncomingMessageBuilder()
                .withAutor("Sam")
                .withBotName("testBot")
                .withCommand(true)
                .withCommandName("help")
                .withCommandAttributes(null)
                .withCommandSymbol("!")
                .withConference(true)
                .withConferenceName("test_conf")
                .withProtocol("IRC")
                .withRawMessageBody("!help")
                .build();
    }


}
