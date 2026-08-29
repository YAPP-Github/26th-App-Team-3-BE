package bitnagil.utils;


import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * 이 클래스는 JPA 엔티티의 List<DayOfWeek> 필드를
 * 데이터베이스에 하나의 문자열 컬럼으로 저장하고,
 * 다시 List<DayOfWeek>로 변환해주는 AttributeConverter입니다.
 *
 * 요일 리스트를 한 컬럼에 저장할 수 있습니다.
 * 코드에서는 타입 안정적인 List<DayOfWeek>로 다룰 수 있습니다.
 *
 * 사용 예시:
 * @Convert(converter = DayOfWeekListConverter.class)
 * private List<DayOfWeek> repeatDay;
 */
@Converter
public class DayOfWeekConverter implements AttributeConverter<List<DayOfWeek>, String> {

    // DB에 저장하기 위해 List 타입을 String 타입으로 변환
    @Override
    public String convertToDatabaseColumn(List<DayOfWeek> attribute) {
        if (attribute == null || attribute.isEmpty()) return "";
        return attribute.stream()
            .map(DayOfWeek::name)
            .collect(Collectors.joining(","));
    }

    // 코드레벨에서 사용하기 위해 String 타입에서 List 타입으로 변환
    @Override
    public List<DayOfWeek> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return List.of();
        return Arrays.stream(dbData.split(","))
            .map(DayOfWeek::valueOf)
            .collect(Collectors.toList());
    }
}
