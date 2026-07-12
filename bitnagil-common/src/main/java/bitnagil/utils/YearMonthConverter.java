package bitnagil.utils;

import java.time.YearMonth;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA 엔티티의 {@link YearMonth} 필드를 "yyyy-MM" 형식의 문자열 컬럼으로 저장하고,
 * 다시 {@link YearMonth}로 변환해주는 AttributeConverter입니다.
 *
 * 사용 예시:
 * @Convert(converter = YearMonthConverter.class)
 * private YearMonth badgeYearMonth;
 */
@Converter
public class YearMonthConverter implements AttributeConverter<YearMonth, String> {

    // DB에 저장하기 위해 YearMonth를 "2026-07" 형태의 문자열로 변환
    @Override
    public String convertToDatabaseColumn(YearMonth attribute) {
        return attribute == null ? null : attribute.toString();
    }

    // 코드레벨에서 사용하기 위해 문자열을 YearMonth로 변환
    @Override
    public YearMonth convertToEntityAttribute(String dbData) {
        return dbData == null ? null : YearMonth.parse(dbData);
    }
}
