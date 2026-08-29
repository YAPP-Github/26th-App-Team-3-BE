package bitnagil.report.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportInfoResponse {

    @Schema(description = "날짜 별 제보 목록\n\n제보한 기록이 없을 경우 빈 Map을 반환합니다.")
    private Map<LocalDate, List<ReportInfo>> reportInfos;
}
