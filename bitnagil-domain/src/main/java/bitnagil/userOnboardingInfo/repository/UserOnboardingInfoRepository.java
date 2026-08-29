package bitnagil.userOnboardingInfo.repository;


import bitnagil.user.domain.User;
import bitnagil.userOnboardingInfo.domain.UserOnboardingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOnboardingInfoRepository extends JpaRepository<UserOnboardingInfo, Long> {
    UserOnboardingInfo findByUser(User user);
}
