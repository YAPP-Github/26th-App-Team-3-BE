package bitnagil.infrastructure.slack;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class SlackMessageOptions {
    private final String title;
    private final Map<String, String> data;
    private final String webhookUrl;
    private final String colorCode;
    @Builder.Default
    private final boolean enabled = true;
}
