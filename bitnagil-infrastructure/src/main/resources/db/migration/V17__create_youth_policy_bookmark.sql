CREATE TABLE youth_policy_bookmark (
  bookmark_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '찜 PK',
  user_id BIGINT NOT NULL COMMENT '찜한 사용자 ID',
  plcy_no VARCHAR(30) NOT NULL COMMENT '온통청년 공고 번호 (plcyNo, 원본 20자)',
  created_at TIMESTAMP NOT NULL COMMENT '찜 등록 시각',
  updated_at TIMESTAMP NULL COMMENT '수정 시각',
  deleted_at DATETIME(6) COMMENT '소프트 삭제 시각 (미사용, 항상 NULL)',
  PRIMARY KEY (bookmark_id),
  CONSTRAINT uk_youth_policy_bookmark_user_plcy_no UNIQUE (user_id, plcy_no)
) COMMENT = '청년 공고 찜 (사용자별 공고 번호만 저장)';
