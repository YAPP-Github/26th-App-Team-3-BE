CREATE TABLE user_onboarding_info (
  user_onboarding_info_id BIGINT NOT NULL AUTO_INCREMENT,
  time_slot TIME(6) NOT NULL,
  emotion_types VARCHAR(200) NOT NULL,
  target_outing_frequency VARCHAR(40) NOT NULL,
  user_id BIGINT,
  created_at TIMESTAMP NOT NULL,
  deleted_at DATETIME(6),
  updated_at TIMESTAMP NULL,
  PRIMARY KEY (user_onboarding_info_id),
  UNIQUE KEY (user_id),
  CONSTRAINT fk_user_onboarding_info_user_id
      FOREIGN KEY (user_id)
          REFERENCES user (user_id)
);