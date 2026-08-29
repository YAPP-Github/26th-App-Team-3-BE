CREATE TABLE monthly_kpi (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    target_month DATE NOT NULL,
    routine_completion_within_seven_days_rate DECIMAL(5, 2),
    monthly_routine_active_user_rate DECIMAL(5, 2),
    routine_registration_rate DECIMAL(5, 2),
    outing_routine_completion_rate DECIMAL(5, 2),
    emotion_record_day_rate DECIMAL(5, 2),
    positive_emotion_rate DECIMAL(5, 2),
    CONSTRAINT uk_monthly_kpi_target_month UNIQUE (target_month)
);
