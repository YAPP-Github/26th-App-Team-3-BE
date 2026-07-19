package bitnagil.youthPolicy.repository;

import bitnagil.user.domain.User;
import bitnagil.youthPolicy.domain.YouthPolicyBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface YouthPolicyBookmarkRepository extends JpaRepository<YouthPolicyBookmark, Long> {

    // bookmarkId 내림차순 = 최근 찜한 순.
    List<YouthPolicyBookmark> findAllByUserOrderByBookmarkIdDesc(User user);

    boolean existsByUserAndPlcyNo(User user, String plcyNo);

    Optional<YouthPolicyBookmark> findByUserAndPlcyNo(User user, String plcyNo);

    /**
     * 소프트 삭제된(tombstone) 찜을 되살립니다. @Where를 우회해야 하므로 네이티브 쿼리로 처리합니다.
     * @return 되살린 행 수(0이면 되살릴 tombstone이 없음)
     */
    @Modifying
    @Query(value = "UPDATE youth_policy_bookmark SET deleted_at = NULL "
        + "WHERE user_id = :userId AND plcy_no = :plcyNo AND deleted_at IS NOT NULL", nativeQuery = true)
    int resurrectByUserAndPlcyNo(@Param("userId") Long userId, @Param("plcyNo") String plcyNo);
}
