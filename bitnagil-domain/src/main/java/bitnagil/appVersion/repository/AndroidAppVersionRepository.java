package bitnagil.appVersion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bitnagil.appVersion.domain.AndroidAppVersion;

@Repository
public interface AndroidAppVersionRepository extends JpaRepository<AndroidAppVersion, Long> {

    Optional<AndroidAppVersion> findFirstByOrderByMajorDescMinorDesc();
}
