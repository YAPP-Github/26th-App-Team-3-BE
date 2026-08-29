package bitnagil.infrastructure.slack;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackFileUploadOptions {
    private final String botToken;
    private final String channelId;
    private final String filename;
    private final byte[] fileBytes;
    private final String initialComment;
}
