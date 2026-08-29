package bitnagil.utils;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    // DB에 저장하기 위해 List 타입을 String 타입으로 변환
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) return "";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // List<String> → JSON 문자열 변환
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_CONVERT_ERROR);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return List.of();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // JSON 문자열을 List<String>으로 변환
            return objectMapper.readValue(dbData, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_PARSE_ERROR);
        }
    }
}
