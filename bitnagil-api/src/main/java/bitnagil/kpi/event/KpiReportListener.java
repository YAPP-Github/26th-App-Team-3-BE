package bitnagil.kpi.event;

import bitnagil.infrastructure.file.FileService;
import bitnagil.infrastructure.slack.SlackFileSender;
import bitnagil.infrastructure.slack.SlackFileUploadOptions;
import bitnagil.kpi.domain.MonthlyKpi;
import bitnagil.kpi.service.KpiExcelExporterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class KpiReportListener {

    private static final String EXCEL_CONTENT_TYPE =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Value("${slack.kpi-bot-token}")
    private String slackBotToken;

    @Value("${slack.kpi-channel-id}")
    private String slackChannelId;

    private final KpiExcelExporterService kpiExcelExporterService;
    private final FileService fileService;
    private final SlackFileSender slackFileSender;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMonthlyKpiSaved(MonthlyKpiSavedEvent event) {
        MonthlyKpi kpi = event.getMonthlyKpi();
        if (kpi == null) {
            return;
        }

        try {
            byte[] excelBytes = kpiExcelExporterService.exportMonthlyKpi(kpi);
            String fileName = "monthly-kpi-" + kpi.getTargetMonth() + ".xlsx";
            String key = "kpi/" + kpi.getTargetMonth() + "/" + fileName;
            String s3Url = fileService.uploadBytes(key, excelBytes, EXCEL_CONTENT_TYPE);

            String comment = "Monthly KPI report\nS3 URL: " + s3Url;
            SlackFileUploadOptions options = SlackFileUploadOptions.builder()
                    .botToken(slackBotToken)
                    .channelId(slackChannelId)
                    .filename(fileName)
                    .fileBytes(excelBytes)
                    .initialComment(comment)
                    .build();

            slackFileSender.upload(options);
        } catch (Exception e) {
            log.error("KPI 리포트 업로드/슬랙 전송 실패", e);
        }
    }
}
