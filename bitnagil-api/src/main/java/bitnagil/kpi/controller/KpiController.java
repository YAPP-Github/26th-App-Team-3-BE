package bitnagil.kpi.controller;

import bitnagil.global.response.CustomResponseDto;
import bitnagil.kpi.controller.spec.KpiSpec;
import bitnagil.kpi.service.KpiService;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/kpi")
public class KpiController implements KpiSpec {

    private final KpiService kpiService;

    @Override
    public CustomResponseDto<String> calculateAndSave(int year, int month) {
        YearMonth targetMonth = YearMonth.of(year, month);
        kpiService.calculateAndSave(targetMonth);
        return CustomResponseDto.from(targetMonth + " KPI 저장 완료");
    }
}
