package bitnagil.appVersion.domain;

import bitnagil.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AndroidAppVersion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long versionId; // 버전 ID

    @NotNull
    private Integer major; // 버전의 가장 좌측 숫자

    @NotNull
    private Integer minor; // 버전의 가장 중앙 숫자

    @NotNull
    private Integer patch; // 버전의 가장 우측 숫자
}
