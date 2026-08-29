-- emotion marble 테이블 백업
RENAME TABLE emotion_marble TO emotion_marble_old;

-- 새로운 emotion_marble 테이블 생성(이력 테이블에서 일반 테이블로 재구성. UUID 제거, history_seq 제거, PK 변경)
CREATE TABLE emotion_marble (
    date DATE NOT NULL,
    case_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    deleted_at DATETIME(6),
    emotion_marble_id BIGINT NOT NULL AUTO_INCREMENT,
    history_end_date_time DATETIME(6) NOT NULL,
    history_start_date_time DATETIME(6) NOT NULL,
    updated_at TIMESTAMP NULL,
    user_id BIGINT NOT NULL,
    emotion_marble_type VARCHAR(40) NOT NULL,
    PRIMARY KEY (emotion_marble_id)
);


-- routine 테이블 백업
RENAME TABLE routine TO routine_old;

-- 새로운 routine 테이블 생성(user_id를 UUID에서 BIGINT로 변경)
create table routine (
     execution_time time(6) not null,
     created_at TIMESTAMP not null,
     deleted_at datetime(6),
     history_end_date_time datetime(6) not null,
     history_seq bigint not null,
     history_start_date_time datetime(6) not null,
     updated_at TIMESTAMP null,
     user_id bigint not null,
     routine_id binary(16) not null,
     name varchar(255) not null,
     repeat_day varchar(255) not null,
     primary key (history_seq, routine_id)
);

-- v2를 위한 routine_infov2 생성
create table routine_infov2 (
    routine_end_date date not null,
    routine_execution_time time(6) not null,
    routine_start_date date not null,
    routine_info_id bigint not null auto_increment,
    user_id bigint,
    routine_name varchar(255) not null,
    routine_repeat_day varchar(255) not null,
    primary key (routine_info_id)
);

-- v2를 위한 routinev2 생성
create table routinev2 (
   routine_complete_yn bit not null,
   routine_date date not null,
   routine_id bigint not null auto_increment,
   routine_info_id bigint,
   sub_routine_complete_yn varchar(255) not null,
   sub_routine_names varchar(255) not null,
   primary key (routine_id)
);

-- user 테이블 백업
RENAME TABLE user TO user_old;

-- 새로운 user 테이블 생성(이력 테이블에서 일반 테이블로 재구성. UUID 제거, history_seq 제거, PK 변경)
create table user (
  agreed_to_privacy_policy bit,
  agreed_to_terms_of_service bit,
  is_over_fourteen bit,
  created_at TIMESTAMP not null,
  deleted_at datetime(6),
  onboarding_id bigint,
  updated_at TIMESTAMP null,
  user_id bigint not null auto_increment,
  email varchar(255) not null,
  nickname varchar(255) not null,
  refresh_token varchar(255),
  social_id varchar(255) not null,
  role varchar(40) not null,
  social_type varchar(40),
  primary key (user_id)
);


alter table emotion_marble
    add constraint FKte7yhv7ov29ugokq957g5qmmk
        foreign key (case_id)
            references routine_case (case_id);

alter table onboarding
    add constraint FKfc8jstk9agqu5j9qj490gr2jj
        foreign key (case_id)
            references routine_case (case_id);

alter table recommended_routine
    add constraint FKt4eirwywowa6an7qkimt3df6n
        foreign key (case_id)
            references routine_case (case_id);

alter table recommended_sub_routine
    add constraint FKp7i2oxf4pgrqjr1u91otkpkhl
        foreign key (recommended_routine_id)
            references recommended_routine (recommended_routine_id);

alter table routine_infov2
    add constraint FKn1lmdefo4tyu868b8d3x0m3c0
        foreign key (user_id)
            references user (user_id);

alter table routinev2
    add constraint FKomkes8k4o9ad92pj05nyd8w6e
        foreign key (routine_info_id)
            references routine_infov2 (routine_info_id);

alter table user
    add constraint FKgyq5wxekqb3h4n9i9ilfuro5v
        foreign key (onboarding_id)
            references onboarding (onboarding_id);