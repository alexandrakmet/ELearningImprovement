create sequence announcement_id_seq
    as integer;

create type profile_status as enum ('activated', 'deactivated', 'unactivated');

create type question_type as enum ('select_option', 'select_sequence', 'true_false', 'enter_answer');

create type quiz_status as enum ('activated', 'deactivated', 'unvalidated', 'unpublished', 'unsaved');

create type system_action_token_type as enum ('registration', 'password_recovery');

create type user_role as enum ('super_admin', 'admin', 'moderator', 'user');

create type condition_operator as enum ('greater', 'equals', 'less');

create type lang as enum ('en', 'ua');

create table if not exists category
(
    id   serial      not null,
    name varchar(50) not null,
    constraint category_pkey
        primary key (id),
    constraint category_name_key
        unique (name)
);

create table if not exists chat
(
    id            serial    not null,
    name          varchar(50),
    creation_date timestamp not null,
    constraint chat_pkey
        primary key (id)
);

create table if not exists image
(
    id  serial not null,
    src text   not null,
    constraint image_pkey
        primary key (id)
);

create table if not exists spring_session
(
    primary_id            char(36) not null,
    session_id            char(36) not null,
    creation_time         bigint   not null,
    last_access_time      bigint   not null,
    max_inactive_interval integer  not null,
    expiry_time           bigint   not null,
    principal_name        varchar(100),
    constraint spring_session_pk
        primary key (primary_id)
);

create unique index if not exists spring_session_ix1
    on spring_session (session_id);

create index if not exists spring_session_ix2
    on spring_session (expiry_time);

create index if not exists spring_session_ix3
    on spring_session (principal_name);

create table if not exists spring_session_attributes
(
    session_primary_id char(36)     not null,
    attribute_name     varchar(200) not null,
    attribute_bytes    bytea        not null,
    constraint spring_session_attributes_pk
        primary key (session_primary_id, attribute_name),
    constraint spring_session_attributes_fk
        foreign key (session_primary_id) references spring_session
            on delete cascade
);

create table if not exists tag
(
    id   serial      not null,
    name varchar(50) not null,
    constraint tag_pkey
        primary key (id),
    constraint tag_name_key
        unique (name)
);

create table if not exists users
(
    id              serial                  not null,
    login           varchar(25)             not null,
    password        varchar(100)            not null,
    email           varchar(50)             not null,
    status          profile_status          not null,
    role            user_role               not null,
    first_name      varchar(50),
    second_name     varchar(50),
    registered_date timestamp               not null,
    profile         text,
    total_score     integer,
    image_id        integer,
    language        lang default 'en'::lang not null,
    constraint users_pkey
        primary key (id),
    constraint users_email_key
        unique (email),
    constraint users_login_key
        unique (login)
);

create table if not exists chat_members
(
    chat_id integer not null,
    user_id integer not null,
    constraint chat_members_pkey
        primary key (chat_id, user_id),
    constraint chat_members_chat_id_fkey
        foreign key (chat_id) references chat,
    constraint chat_members_user_id_fkey
        foreign key (user_id) references users
);

create table if not exists friendlist
(
    user_id   integer   not null,
    friend_id integer   not null,
    add_date  timestamp not null,
    constraint friendlist_pkey
        primary key (user_id, friend_id),
    constraint friendlist_friend_id_fkey
        foreign key (friend_id) references users,
    constraint friendlist_user_id_fkey
        foreign key (user_id) references users
);

create table if not exists message
(
    id            serial    not null,
    chat_id       integer   not null,
    author_id     integer   not null,
    creation_date timestamp not null,
    message_text  text      not null,
    constraint message_pkey
        primary key (id),
    constraint message_user_id_fkey
        foreign key (author_id) references users,
    constraint messages_chat_id_fkey
        foreign key (chat_id) references chat
);

create table if not exists quiz
(
    id               serial      not null,
    name             varchar(50) not null,
    author_id        integer     not null,
    category_id      integer     not null,
    status           quiz_status not null,
    published_date   timestamp,
    updated_date     timestamp,
    created_date     timestamp   not null,
    questions_number integer,
    max_score        integer,
    image_id         integer     not null,
    description      text,
    constraint quiz_pkey
        primary key (id),
    constraint quiz_author_id_fkey
        foreign key (author_id) references users,
    constraint quiz_category_id_fkey
        foreign key (category_id) references category
);

create table if not exists favorite_mark
(
    user_id   integer   not null,
    quiz_id   integer   not null,
    mark_date timestamp not null,
    constraint favorite_mark_pkey
        primary key (user_id, quiz_id),
    constraint favorite_mark_quiz_id_fkey
        foreign key (quiz_id) references quiz,
    constraint favorite_mark_user_id_fkey
        foreign key (user_id) references users
);

create table if not exists question
(
    id       serial        not null,
    quiz_id  integer       not null,
    type     question_type not null,
    content  varchar(255),
    score    integer default 20,
    image_id integer,
    constraint question_pkey
        primary key (id),
    constraint question_quiz_id_fkey
        foreign key (quiz_id) references quiz
);

create table if not exists question_option
(
    id             serial       not null,
    question_id    integer      not null,
    content        varchar(255) not null,
    is_correct     boolean,
    sequence_order integer,
    image_id       integer,
    constraint question_option_pkey
        primary key (id),
    constraint question_option_question_id_fkey
        foreign key (question_id) references question
            on update cascade on delete cascade
);

create table if not exists quiz_tag
(
    quiz_id integer not null,
    tag_id  integer not null,
    constraint quiz_tag_pkey
        primary key (quiz_id, tag_id),
    constraint quiz_tag_quiz_id_fkey
        foreign key (quiz_id) references quiz,
    constraint quiz_tag_tag_id_fkey
        foreign key (tag_id) references tag
);

create table if not exists system_action_tokens
(
    token        varchar(50)              not null,
    token_type   system_action_token_type not null,
    user_id      integer                  not null,
    expired_date timestamp                not null,
    constraint system_action_token_type_pkey
        primary key (token, user_id),
    constraint system_action_tokens_user_id_fkey
        foreign key (user_id) references users
);

create table if not exists take_quiz
(
    user_id                    integer not null,
    quiz_id                    integer not null,
    is_completed               boolean not null,
    score                      integer,
    correct_answers_persentage numeric(5, 2),
    take_date                  timestamp,
    constraint take_quiz_pkey
        primary key (user_id, quiz_id),
    constraint take_quiz_quiz_id_fkey
        foreign key (quiz_id) references quiz,
    constraint take_quiz_user_id_fkey
        foreign key (user_id) references users
);

create table if not exists announcement
(
    id           integer default nextval('announcement_id_seq'::regclass) not null,
    author_id    integer                                                  not null,
    title        varchar(200)                                             not null,
    subtitle     varchar(500),
    full_text    text,
    created_date timestamp                                                not null,
    is_published boolean                                                  not null,
    image_id     integer,
    constraint announcement_pkey
        primary key (id),
    constraint announcement_author_id_fkey
        foreign key (author_id) references users
);

create table if not exists achievement
(
    id          serial       not null,
    description varchar      not null,
    name        varchar(255) not null,
    constraint achievement_pk
        primary key (id)
);

create unique index if not exists achievement_id_uindex
    on achievement (id);

create table if not exists notification
(
    id            serial                  not null,
    user_id       integer                 not null,
    is_viewed     boolean   default false not null,
    author        varchar(255)            not null,
    action        varchar(255)            not null,
    author_link   varchar                 not null,
    action_link   varchar                 not null,
    is_message    boolean   default false not null,
    creation_date timestamp default now() not null,
    constraint notifications_pk
        primary key (id),
    constraint notifications_users_id_fk
        foreign key (user_id) references users
);

create table if not exists achievement_characteristic
(
    id     serial      not null,
    name   varchar(50) not null,
    script text        not null
);

create unique index if not exists achievement_characteristic_id_uindex
    on achievement_characteristic (id);

create table if not exists achievement_condition
(
    id                            serial not null,
    operator                      condition_operator,
    value                         integer,
    achievement_id                integer,
    achievement_characteristic_id integer,
    constraint achievement_condition_pkey
        primary key (id),
    constraint achievement_condition_achievement_id_fkey
        foreign key (achievement_id) references achievement
            on delete cascade,
    constraint achievement_condition_achievement_characteristic_id_fkey
        foreign key (achievement_characteristic_id) references achievement_characteristic (id)
            on delete cascade
);

create table if not exists user_achievements_list
(
    user_id          integer,
    achievement_id   integer,
    achievement_date date,
    id               serial not null,
    constraint user_achievements_list_pk
        primary key (id),
    constraint user_achievements_list_user_id_fkey
        foreign key (user_id) references users
            on delete cascade,
    constraint user_achievements_list_achievement_id_fkey
        foreign key (achievement_id) references achievement
            on delete cascade
);

create table if not exists notification_settings
(
    id                serial                not null,
    new_quiz          boolean default false not null,
    new_announcement  boolean default false not null,
    game_invitation   boolean default false not null,
    friend_invitation boolean default false not null,
    user_id           integer               not null,
    constraint notification_settings_pk
        primary key (id)
);

