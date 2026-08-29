package bitnagil.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BooleanListConverter implements AttributeConverter<List<Boolean>, String> {

    @Override
    public String convertToDatabaseColumn(List<Boolean> attribute) {
        if (attribute == null || attribute.isEmpty()) return "";
        return attribute.stream()
            .map(b -> b ? "Y" : "N") // boolean 값에 따라 DB에 저장되는 값(Y,N)
            .collect(Collectors.joining(","));
    }

    @Override
    public List<Boolean> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return List.of();
        return Arrays.stream(dbData.split(","))
            .map(s -> {
                if ("Y".equals(s)) return true; // DB에 'Y'로 저장되어 있으면 true로
                else if ("N".equals(s)) return false; // DB에 'N'로 저장되어 있으면 false로
                else return false;
            })
            .collect(Collectors.toList());
    }
}
