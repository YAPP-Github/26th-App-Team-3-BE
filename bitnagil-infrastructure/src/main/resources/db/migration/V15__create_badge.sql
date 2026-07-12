CREATE TABLE badge (
  badge_id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  badge_type VARCHAR(40) NOT NULL,
  badge_year_month VARCHAR(7) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  PRIMARY KEY (badge_id),
  CONSTRAINT uk_badge_user_type_year_month UNIQUE (user_id, badge_type, badge_year_month),
  CONSTRAINT fk_badge_user_id
      FOREIGN KEY (user_id)
          REFERENCES user (user_id)
);
