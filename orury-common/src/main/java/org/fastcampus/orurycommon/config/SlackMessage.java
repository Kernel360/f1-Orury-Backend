package org.fastcampus.orurycommon.config;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurycommon.error.code.LogTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;

@Slf4j
@Component
public class SlackMessage {
    @Value("${slack.token}")
    private String token;
    @Value("${slack.channel}")
    private String channel;

    public void send(LogTemplate template) {
        log.info("token : {}", token);
        log.info("channel : {}", channel);
        try {
            MethodsClient methods = Slack.getInstance()
                    .methods(token);
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channel)
                    .blocks(asBlocks(
                            header(header -> header.text(plainText("🚨" + template.uri()))),
                            divider(),
                            section(section -> section.fields(template.toTextObjects())
                            )))
                    .build();
            methods.chatPostMessage(request);
        } catch (SlackApiException | IOException e) {
            log.error("SlackApiException: {}", e.getMessage());
        }
    }
}
