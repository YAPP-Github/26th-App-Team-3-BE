package bitnagil.youthPolicy.repository;

import bitnagil.user.domain.User;
import bitnagil.youthPolicy.domain.YouthPolicyBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface YouthPolicyBookmarkRepository extends JpaRepository<YouthPolicyBookmark, Long> {

    // bookmarkId 내림차순 = 최근 찜한 순.
    List<YouthPolicyBookmark> findAllByUserOrderByBookmarkIdDesc(User user);

    /**
     * 찜을 멱등하게 등록합니다. 이미 찜한 상태(UNIQUE 충돌)면 no-op으로 흡수해
     * 더블탭·재시도로 인한 UNIQUE 위반 예외(→ Slack 알림)를 막습니다.
     */
    @Modifying
    @Query(value = "INSERT INTO youth_policy_bookmark (user_id, plcy_no, created_at) "
        + "VALUES (:userId, :plcyNo, NOW()) "
        + "ON DUPLICATE KEY UPDATE updated_at = NOW()", nativeQuery = true)
    void upsertBookmark(@Param("userId") Long userId, @Param("plcyNo") String plcyNo);

    // 하드 삭제. 없으면 0건 삭제로 멱등.
    long deleteByUserAndPlcyNo(User user, String plcyNo);
}