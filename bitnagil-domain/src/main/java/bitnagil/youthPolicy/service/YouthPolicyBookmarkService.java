package bitnagil.youthPolicy.service;

import bitnagil.user.domain.User;
import bitnagil.youthPolicy.domain.YouthPolicyBookmark;
import bitnagil.youthPolicy.dto.BookmarkRef;
import bitnagil.youthPolicy.repository.YouthPolicyBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 청년 공고 찜 도메인 서비스입니다. 찜 등록/해제는 멱등하게 동작합니다.
 */
@Service
@RequiredArgsConstructor
public class YouthPolicyBookmarkService {

    private final YouthPolicyBookmarkRepository youthPolicyBookmarkRepository;

    /**
     * 사용자가 현재 찜한 공고 번호 집합을 반환합니다. (전체 탭의 찜 여부 표시용, 순서 무관)
     */
    @Transactional(readOnly = true)
    public Set<String> findBookmarkedPlcyNos(User user) {
        return youthPolicyBookmarkRepository.findAllByUserOrderByBookmarkIdDesc(user).stream()
            .map(YouthPolicyBookmark::getPlcyNo)
            .collect(Collectors.toSet());
    }

    /**
     * 사용자가 찜한 공고를 최근순으로 반환합니다. (찜 탭의 목록·커서용)
     */
    @Transactional(readOnly = true)
    public List<BookmarkRef> findRecentBookmarks(User user) {
        return youthPolicyBookmarkRepository.findAllByUserOrderByBookmarkIdDesc(user).stream()
            .map(bookmark -> new BookmarkRef(bookmark.getBookmarkId(), bookmark.getPlcyNo()))
            .toList();
    }

    /**
     * 공고를 찜합니다. 이미 찜한 상태면 no-op(멱등), 아니면 새 행으로 등록합니다.
     * 더블탭·네트워크 재시도로 동일 요청이 겹쳐도 upsert가 UNIQUE 충돌을 no-op으로 흡수합니다.
     */
    @Transactional
    public void bookmark(User user, String plcyNo) {
        youthPolicyBookmarkRepository.upsertBookmark(user.getUserId(), plcyNo);
    }

    /**
     * 찜을 해제합니다(하드 삭제). 찜 상태가 아니면 아무 것도 하지 않습니다.
     */
    @Transactional
    public void unbookmark(User user, String plcyNo) {
        youthPolicyBookmarkRepository.deleteByUserAndPlcyNo(user, plcyNo);
    }
}