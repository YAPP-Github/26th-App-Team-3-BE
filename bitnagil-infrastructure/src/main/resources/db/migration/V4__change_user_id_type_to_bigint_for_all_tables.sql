-- routine 테이블 백업
RENAME TABLE changed_routine TO changed_routine_old;

-- 새로운 changed_routine 테이블 생성(user_id를 UUID에서 BIGINT로 변경)
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
    user_id                 bigint   not null,
    changed_routine_name    varchar(255) not null,
    changed_div_code        varchar(40)  null,
    primary key (history_seq, changed_routine_id)
);
