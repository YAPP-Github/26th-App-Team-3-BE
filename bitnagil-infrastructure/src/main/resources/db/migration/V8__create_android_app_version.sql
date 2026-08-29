-- AndroidAppVersion 테이블 추가

CREATE TABLE android_app_version (
     version_id BIGINT NOT NULL AUTO_INCREMENT,
     major INT NOT NULL,
     minor INT NOT NULL,
     patch INT NOT NULL,
     created_at TIMESTAMP NOT NULL,
     updated_at TIMESTAMP NULL,
     deleted_at DATETIME(6),
     PRIMARY KEY (version_id)
);