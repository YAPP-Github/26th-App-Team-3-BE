CREATE TABLE report (
    report_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    report_category VARCHAR(50) NOT NULL,
    report_image_urls TEXT,
    report_title VARCHAR(255) NOT NULL,
    report_content TEXT,
    report_location VARCHAR(255),
    latitude DECIMAL(10,7),
    longitude DECIMAL(10,7),
    user_id BIGINT,
    created_at TIMESTAMP NOT NULL,
    deleted_at DATETIME(6),
    updated_at TIMESTAMP NULL,
    CONSTRAINT fk_report_user
        FOREIGN KEY (user_id) REFERENCES user(user_id)
);