package bitnagil.report.domain.enums;

import bitnagil.enums.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReportCategory implements EnumType {
    TRANSPORTATION("교통"),
    LIGHTING("조명"),
    WATERFACILITY("상하수도"),
    AMENITY("편의시설");

    private final String description;

}
