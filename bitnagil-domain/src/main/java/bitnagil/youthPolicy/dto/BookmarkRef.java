package bitnagil.youthPolicy.dto;

/**
 * 찜 목록 페이징에 필요한 최소 정보입니다. bookmarkId로 최근순 정렬·커서를 만듭니다.
 */
public record BookmarkRef(Long bookmarkId, String plcyNo) {
}