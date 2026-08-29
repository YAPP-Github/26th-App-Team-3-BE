-- 활동일지 조회/뱃지 발급이 (user_id, date) 범위로 emotion_marble을 조회하므로 복합 인덱스를 추가한다.
-- (기존에는 user_id 인덱스가 없어 월별 조회/카운트가 풀스캔되었음)
CREATE INDEX idx_emotion_marble_user_id_date ON emotion_marble (user_id, date);
