-- 기존 운영 DB에 반영된 테이블 구조
create table changed_routine
(
    changed_execution_time  time(6)      not null,
    changed_routine_date    date         not null,
    original_routine_date   date         not null,
    created_at              timestamp    not null,
    deleted_at              datetime(6)  null,
    history_end_date_time   datetime(6)  not null,
    history_seq             bigint       not null,
    history_start_date_time datetime(6)  not null,
    updated_at              timestamp    null,
    changed_routine_id      binary(16)   not null,
    routine_id              binary(16)   null,
    user_id                 binary(16)   not null,
    changed_routine_name    varchar(255) not null,
    changed_div_code        varchar(40)  null,
    primary key (history_seq, changed_routine_id)
);

create table changed_sub_routine
(
    sort_order               int          not null,
    created_at               timestamp    not null,
    deleted_at               datetime(6)  null,
    history_end_date_time    datetime(6)  not null,
    history_seq              bigint       not null,
    history_start_date_time  datetime(6)  not null,
    updated_at               timestamp    null,
    changed_routine_id       binary(16)   not null,
    changed_sub_routine_id   binary(16)   not null,
    changed_sub_routine_name varchar(255) not null,
    primary key (history_seq, changed_sub_routine_id)
);

create table emotion_marble
(
    date                    date        not null,
    case_id                 bigint      not null,
    created_at              timestamp   not null,
    deleted_at              datetime(6) null,
    history_end_date_time   datetime(6) not null,
    history_seq             bigint      not null,
    history_start_date_time datetime(6) not null,
    updated_at              timestamp   null,
    emotion_marble_id       binary(16)  not null,
    user_id                 binary(16)  not null,
    emotion_marble_type     varchar(40) not null,
    primary key (history_seq, emotion_marble_id)
);

create table onboarding
(
    time_slot               time(6)     not null,
    case_id                 bigint      not null,
    created_at              timestamp   not null,
    deleted_at              datetime(6) null,
    onboarding_id           bigint auto_increment
        primary key,
    updated_at              timestamp   null,
    emotion_type            varchar(40) not null,
    real_outing_frequency   varchar(40) not null,
    target_outing_frequency varchar(40) not null
);

create table recommended_routine
(
    execution_time                  time(6)      null,
    case_id                         bigint       null,
    created_at                      timestamp    not null,
    deleted_at                      datetime(6)  null,
    recommended_routine_id          bigint auto_increment
        primary key,
    updated_at                      timestamp    null,
    recommended_routine_description varchar(255) null,
    recommended_routine_name        varchar(255) null,
    thumbnail_url                   varchar(255) null,
    emotion                         varchar(40)  null,
    recommended_routine_level       varchar(40)  null,
    recommended_routine_type        varchar(40)  null
);

create table recommended_sub_routine
(
    created_at                 timestamp    not null,
    deleted_at                 datetime(6)  null,
    recommended_routine_id     bigint       null,
    recommended_sub_routine_id bigint auto_increment
        primary key,
    updated_at                 timestamp    null,
    sub_routine_name           varchar(255) null
);

create table routine
(
    execution_time          time(6)      not null,
    created_at              timestamp    not null,
    deleted_at              datetime(6)  null,
    history_end_date_time   datetime(6)  not null,
    history_seq             bigint       not null,
    history_start_date_time datetime(6)  not null,
    updated_at              timestamp    null,
    routine_id              binary(16)   not null,
    user_id                 binary(16)   not null,
    name                    varchar(255) not null,
    repeat_day              varchar(255) not null,
    primary key (history_seq, routine_id)
);

create table routine_case
(
    case_id    bigint auto_increment
        primary key,
    created_at timestamp   not null,
    deleted_at datetime(6) null,
    updated_at timestamp   null
);

create table routine_completion
(
    complete_yn           bit         not null,
    performed_date        date        not null,
    created_at            timestamp   not null,
    deleted_at            datetime(6) null,
    routine_completion_id bigint auto_increment
        primary key,
    routine_history_seq   bigint      not null,
    updated_at            timestamp   null,
    routine_id            binary(16)  not null,
    routine_type          varchar(40) not null
);

create table sub_routine
(
    sort_order              int          not null,
    created_at              timestamp    not null,
    deleted_at              datetime(6)  null,
    history_end_date_time   datetime(6)  not null,
    history_seq             bigint       not null,
    history_start_date_time datetime(6)  not null,
    updated_at              timestamp    null,
    routine_id              binary(16)   not null,
    sub_routine_id          binary(16)   not null,
    name                    varchar(255) not null,
    primary key (history_seq, sub_routine_id)
);

create table user
(
    agreed_to_privacy_policy   bit          null,
    agreed_to_terms_of_service bit          null,
    is_over_fourteen           bit          null,
    created_at                 timestamp    not null,
    deleted_at                 datetime(6)  null,
    history_end_date_time      datetime(6)  not null,
    history_seq                bigint       not null,
    history_start_date_time    datetime(6)  not null,
    onboarding_id              bigint       null,
    updated_at                 timestamp    null,
    user_id                    binary(16)   not null,
    email                      varchar(255) not null,
    nickname                   varchar(255) not null,
    refresh_token              varchar(255) null,
    social_id                  varchar(255) not null,
    role                       varchar(40)  not null,
    social_type                varchar(40)  null,
    primary key (history_seq, user_id)
);

