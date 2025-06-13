package bitnagil.bitnagil_backend.global.swagger;

import io.swagger.v3.oas.models.examples.Example;
import lombok.Builder;
import lombok.Getter;

/**
 * Swagger에서 Example을 사용하기 위한 Holder 클래스
 * Example 객체를 담고, 추가적인 정보를 포함할 수 있습니다.
 */
@Getter
@Builder
public class ExampleHolder {

    private Example holder;
    private String name;
    private int code;
}