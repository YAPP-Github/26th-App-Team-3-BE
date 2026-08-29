package bitnagil.kpi.event;

import bitnagil.kpi.domain.MonthlyKpi;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MonthlyKpiSavedEvent {
    private final MonthlyKpi monthlyKpi;
}

