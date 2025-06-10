package bitnagil.bitnagil_backend.user.entity;

import bitnagil.bitnagil_backend.enums.Role;
import bitnagil.bitnagil_backend.enums.SocialType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @NotBlank
    private String socialId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @NotBlank
    private String email;

    @Builder
    public User(SocialType socialType, String socialId, Role role, String email) {
        this.socialType = socialType;
        this.socialId = socialId;
        this.role = role;
        this.email = email;
    }
}
