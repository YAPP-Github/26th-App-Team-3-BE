CREATE TABLE badge (
  badge_id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  badge_type VARCHAR(40) NOT NULL,
  badge_year_month VARCHAR(7) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  PRIMARY KEY (badge_id),
  -- (user_id, badge_year_month) 프리픽스로 월별 조회(findByUserAndBadgeYearMonth...)와
  -- 발급 판정(existsByUserAndBadgeTypeAndBadgeYearMonth, 3컬럼 등가라 순서 무관) 양쪽을 한 인덱스로 커버
  CONSTRAINT uk_badge_user_year_month_type UNIQUE (user_id, badge_year_month, badge_type),
  CONSTRAINT fk_badge_user_id
      FOREIGN KEY (user_id)
          REFERENCES user (user_id)
);
