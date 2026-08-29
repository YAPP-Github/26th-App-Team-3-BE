package bitnagil.kpi.controller.spec;

import bitnagil.global.response.CustomResponseDto;
import bitnagil.global.swagger.ApiTags;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = ApiTags.KPI)
public interface KpiSpec {

    @Operation(summary = "월별 KPI 계산 및 저장",
        description = "지정한 연·월의 KPI를 계산하여 DB에 저장합니다. 이미 해당 월 데이터가 있으면 저장하지 않습니다. (로컬/스웨거 테스트용)")
    @Parameters({
        @Parameter(name = "year", description = "연도", required = true, example = "2025"),
        @Parameter(name = "month", description = "월 (1~12)", required = true, example = "2")
    })
    @PostMapping("/calculate")
    CustomResponseDto<String> calculateAndSave(
        @RequestParam int year,
        @RequestParam int month
    );
}
