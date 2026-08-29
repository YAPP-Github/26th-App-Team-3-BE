package bitnagil.infrastructure.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.files.FilesUploadV2Request;
import com.slack.api.methods.request.files.FilesUploadV2Request.UploadFile;
import com.slack.api.methods.response.files.FilesUploadV2Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SlackFileSender {

    private final Slack slack = Slack.getInstance();

    public void upload(SlackFileUploadOptions options) {
        if (options == null) {
            return;
        }

        if (options.getBotToken() == null || options.getBotToken().isEmpty()) {
            return;
        }

        if (options.getChannelId() == null || options.getChannelId().isEmpty()) {
            return;
        }

        if (options.getFilename() == null || options.getFilename().isEmpty()) {
            return;
        }

        if (options.getFileBytes() == null || options.getFileBytes().length == 0) {
            return;
        }

        try {
            MethodsClient client = slack.methods(options.getBotToken());
            UploadFile uploadFile = UploadFile.builder()
                    .filename(options.getFilename())
                    .fileData(options.getFileBytes())
                    .title(options.getFilename())
                    .build();

            FilesUploadV2Request request = FilesUploadV2Request.builder()
                    .channel(options.getChannelId())
                    .initialComment(options.getInitialComment())
                    .uploadFiles(List.of(uploadFile))
                    .build();

            FilesUploadV2Response response = client.filesUploadV2(request);
            if (!response.isOk()) {
                log.error("Slack 파일 업로드 실패: {}", response.getError());
            }
        } catch (Exception e) {
            log.error("Slack 파일 업로드에 실패했습니다.", e);
        }
    }
}
