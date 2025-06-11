package bitnagil.bitnagil_backend.global;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import com.slack.api.webhook.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Service
@Slf4j
public class SlackService {

    @Value("${slack.webhook-url}")
    private String SLACK_WEBHOOK_URL;

    private final Slack slackClient = Slack.getInstance();

    /**
     * 슬랙 메시지 전송
     **/
    public void sendMessage(String title, Map<String, String> data) {
        // Production 환경에서만 슬랙 메시지를 전송합니다.
        if (SLACK_WEBHOOK_URL == null || SLACK_WEBHOOK_URL.isEmpty()) {
            return;
        }
        try {
            slackClient.send(SLACK_WEBHOOK_URL, payload(p -> p
                    .text(title) // 메시지 제목
                    .attachments(List.of(
                            Attachment.builder().color(Color.RED.getCode()) // 메시지 색상
                                    .fields( // 메시지 본문 내용
                                            data.keySet().stream().map(key -> generateSlackField(key, data.get(key))).collect(Collectors.toList())
                                    ).build())))
            );
        } catch (Exception e) {
            log.error("Slack 메시지 전송에 실패했습니다.", e);
        }
    }

    /**
     * Slack Field 생성
     **/
    private Field generateSlackField(String title, String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }
}