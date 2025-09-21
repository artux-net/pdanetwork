create table if not exists achievement_category
(
    name        varchar(255) not null
        primary key,
    description varchar(255),
    image       varchar(255),
    title       varchar(255),
    id          uuid         not null
);

create table if not exists achievement
(
    name          varchar(255) not null
        primary key,
    description   varchar(255),
    image         varchar(255),
    title         varchar(255),
    type          varchar(255),
    category_name varchar(255)
        constraint fk22fbey8y1o8mcv2qwmnlm7fit
            references achievement_category,
    id            uuid         not null,
    category_id   uuid
);

create table if not exists achievement_condition
(
    achievement_name varchar(255) not null
        constraint fk6ve47lnk6cu4lj5ikmfsfd61x
            references achievement,
    value            varchar(255)[],
    key              varchar(255) not null,
    achievement_id   uuid         not null,
    primary key (achievement_name, key)
);

create table if not exists article
(
    id          uuid not null
        primary key,
    content     text,
    description varchar(250),
    image       varchar(255),
    published   timestamp(6) with time zone,
    title       varchar(255),
    views       integer
);

create table if not exists base_item
(
    id          bigint  not null
        primary key,
    content     text,
    description varchar(255),
    icon        varchar(255),
    price       integer not null,
    title       varchar(255),
    type        varchar(255),
    weight      real    not null
);

create table if not exists base_item_entity_advantages
(
    base_item_entity_id bigint not null
        constraint fkoybeafxwvmff24nl3yqwxqno8
            references base_item,
    advantages          varchar(255)
);

create table if not exists base_item_entity_disadvantages
(
    base_item_entity_id bigint not null
        constraint fkmdjcusx6wewaoovxyrldm73aa
            references base_item,
    disadvantages       varchar(255)
);

create table if not exists pda_user
(
    id               uuid not null
        primary key,
    avatar           varchar(255),
    chat_ban         boolean default false,
    email            varchar(255)
        constraint uk_6lxvs4koowar5wme5m6qpydcx
            unique,
    gang             varchar(255),
    last_login_at    timestamp(6) with time zone,
    login            varchar(255)
        constraint uk_fvogamiw367o4wrwawoo7dyud
            unique,
    money            integer,
    name             varchar(255),
    nickname         varchar(255),
    password         varchar(255),
    receive_emails   boolean default true,
    registration     timestamp(6) with time zone,
    role             varchar(255),
    xp               integer,
    gang_relation_id uuid
);

create table if not exists achievement_users
(
    achievement_entity_name varchar(255) not null
        constraint fkao1j37sn0bwqthvopb30sup4f
            references achievement,
    users_id                uuid         not null
        constraint fkmhd088ar8lldqvy0jls7mhuno
            references pda_user,
    achievement_entity_id   uuid         not null
);

create table if not exists article_like
(
    article_id uuid not null
        constraint fkabthli6g1qjriusniw93pbesw
            references article,
    user_id    uuid not null
        constraint fk6pp98gvqxjfu001qi80xeb65f
            references pda_user,
    primary key (article_id, user_id)
);

create table if not exists pda_user_achievements
(
    user_entity_id    uuid         not null
        constraint fkekjoor3b3nahmwr3sq5o0ms9d
            references pda_user,
    achievements_name varchar(255) not null
        constraint fk3fvsq4yjadvdatvop05ipcckm
            references achievement,
    achievements_id   uuid         not null
);

create table if not exists seller
(
    id               bigint not null
        primary key,
    buy_coefficient  real   not null,
    icon             varchar(255),
    image            varchar(255),
    name             varchar(255),
    sell_coefficient real   not null
);

create table if not exists story_backup
(
    id        uuid    not null
        primary key,
    access    smallint,
    archive   boolean not null,
    hashcode  integer not null,
    icon      varchar(255),
    message   varchar(255),
    needs     integer[],
    story_id  bigint,
    timestamp timestamp(6) with time zone,
    title     varchar(255),
    type      smallint,
    author_id uuid
        constraint fkj5r42xvdfftnrfh4hb4sq6u4b
            references pda_user
);

create table if not exists tag
(
    title varchar(255) not null
        primary key
);

create table if not exists article_tag
(
    tag_id     varchar(255) not null
        constraint fkesqp7s9jj2wumlnhssbme5ule
            references tag,
    article_id uuid         not null
        constraint fkenqeees0y8hkm7x1p1ittuuye
            references article,
    primary key (tag_id, article_id)
);

create table if not exists user_armor
(
    id                  uuid    not null
        primary key,
    quantity            integer not null,
    base_id             bigint  not null
        constraint fk_mu3yol97bta50qohsb70omlrx
            references base_item,
    owner_id            uuid
        constraint fk_entvws81o2yxuvqoq0qrqqth8
            references pda_user,
    is_equipped         boolean not null,
    condition           real    not null,
    chemical_protection real    not null,
    damage_protection   real    not null,
    electric_protection real    not null,
    psy_protection      real    not null,
    radio_protection    real    not null,
    thermal_protection  real    not null
);

create table if not exists seller_armor
(
    seller_id bigint not null
        constraint fkltphq55gdo0v8yltyitvmi9jn
            references seller,
    armor_id  uuid   not null
        constraint fkg0doqj92wns0l00dsn3noppda
            references user_armor,
    primary key (seller_id, armor_id)
);

create table if not exists user_artifact
(
    id          uuid    not null
        primary key,
    quantity    integer not null,
    base_id     bigint  not null
        constraint fk_mcqgf4y2i9nhd7jdl0nify8xc
            references base_item,
    owner_id    uuid
        constraint fk_ed1ujsvrn2by6tppvlshy68ue
            references pda_user,
    is_equipped boolean not null,
    bleeding    integer not null,
    chemical    integer not null,
    damage      integer not null,
    electric    integer not null,
    endurance   integer not null,
    health      integer not null,
    radio       integer not null,
    thermal     integer not null
);

create table if not exists seller_artifact
(
    seller_id   bigint not null
        constraint fkd7pbldokdqc73db0mgi9wuu3k
            references seller,
    artifact_id uuid   not null
        constraint fka21e5bnjdyqqg8jkjlxkqn8wn
            references user_artifact,
    primary key (seller_id, artifact_id)
);

create table if not exists user_ban
(
    id        uuid    not null
        primary key,
    message   varchar(255),
    reason    varchar(255),
    seconds   integer not null,
    timestamp timestamp(6) with time zone,
    by_id     uuid
        constraint fk4bl9dlv1yojdhbdiyys64rdpj
            references pda_user,
    user_id   uuid
        constraint fkqr45tr28546peoo0f0n1n7nui
            references pda_user
);

create table if not exists user_bullet
(
    id       uuid    not null
        primary key,
    quantity integer not null,
    base_id  bigint  not null
        constraint fk_teqa6050p7cwvd97374yxsi0
            references base_item,
    owner_id uuid
        constraint fk_a4648qvst9enk8j1dd6vsyunw
            references pda_user
);

create table if not exists seller_bullet
(
    seller_id bigint not null
        constraint fkifc3t4law95fbs4oj6fd3f7ds
            references seller,
    bullet_id uuid   not null
        constraint fkkmbwknxqlplnri8oesbhvwtbs
            references user_bullet,
    primary key (seller_id, bullet_id)
);

create table if not exists user_comment
(
    id        uuid not null
        primary key,
    content   varchar(255),
    published timestamp(6) with time zone,
    author_id uuid
        constraint fki2i2mp7temx2rav57v2uth77j
            references pda_user
);

create table if not exists article_comment
(
    article_id uuid not null
        constraint fkghmocqkgqs5tkmucf5putw64t
            references article,
    comment_id uuid not null
        constraint fkcbehcd1beqen57hkld96tqew7
            references user_comment,
    primary key (comment_id, article_id)
);

create table if not exists comment_like
(
    comment_id uuid not null
        constraint fk4pnyl983cukv5mk3u70gvwcyx
            references user_comment,
    user_id    uuid not null
        constraint uk_nn8tib8043ktu61gqvioc088c
            unique
        constraint fkjt018cwtmikunnwdqktist35w
            references pda_user,
    primary key (comment_id, user_id)
);

create table if not exists user_conversation
(
    id              uuid not null
        primary key,
    icon            varchar(255),
    time            timestamp(6) with time zone,
    title           varchar(255),
    type            varchar(255),
    last_message_id uuid,
    owner_id        uuid
        constraint fkpfp86dpfvak40cfoi8tvxqfs6
            references pda_user
);

create table if not exists user_detector
(
    id            uuid    not null
        primary key,
    quantity      integer not null,
    base_id       bigint  not null
        constraint fk_hkc58jqdkdnt7cvtsyrltwk9m
            references base_item,
    owner_id      uuid
        constraint fk_qf5pxidkl2k3cabi754cg700r
            references pda_user,
    is_equipped   boolean not null,
    detector_type smallint
);

create table if not exists seller_detector
(
    seller_id   bigint not null
        constraint fk35o4jgh2aedjc6wh2w5ywaiyb
            references seller,
    detector_id uuid   not null
        constraint fkbr7a5f5ediidslt8d7uptoyu3
            references user_detector,
    primary key (seller_id, detector_id)
);

create table if not exists user_gang_relation
(
    id          uuid    not null
        primary key,
    bandits     integer not null,
    clear_sky   integer not null,
    duty        integer not null,
    liberty     integer not null,
    loners      integer not null,
    mercenaries integer not null,
    military    integer not null,
    monolith    integer not null,
    scientists  integer not null,
    user_id     uuid
        constraint fknlpttkx58i691s1ejyhq019u0
            references pda_user
);

create table if not exists user_medicine
(
    id        uuid    not null
        primary key,
    quantity  integer not null,
    base_id   bigint  not null
        constraint fk_5mskkly3iapyqcfoyec7is96w
            references base_item,
    owner_id  uuid
        constraint fk_o5wf3oxpha8addqx2tqql1p6a
            references pda_user,
    health    real    not null,
    radiation real    not null,
    stamina   real    not null
);

create table if not exists seller_medicine
(
    seller_id   bigint not null
        constraint fkhuo5132ushibjif21gkxdgudd
            references seller,
    medicine_id uuid   not null
        constraint fkbxs5wim80af0a5vsofvel9fqq
            references user_medicine,
    primary key (seller_id, medicine_id)
);

create table if not exists user_note
(
    id        uuid not null
        primary key,
    content   text,
    time      timestamp(6) with time zone,
    title     varchar(255),
    author_id uuid
        constraint fkqs2w5exqabo4pcvl6skbhht7a
            references pda_user
);

create table if not exists user_parameter
(
    id      uuid not null
        primary key,
    key     varchar(255),
    value   integer,
    user_id uuid
        constraint fkau043trko63rses7ub5e3hw2d
            references pda_user
);

create table if not exists user_post
(
    id        uuid not null
        primary key,
    content   text,
    published timestamp(6) with time zone,
    title     varchar(255),
    author_id uuid
        constraint fk4wdxwjfxrmkd9n6clxnit9oe2
            references pda_user
);

create table if not exists post_comment
(
    post_id    uuid
        constraint fkgypkvko7o7axq62udrgdidxnh
            references user_post,
    comment_id uuid not null
        primary key
        constraint fkbjkw848a0kyko4rj421e8l6ki
            references user_comment
);

create table if not exists post_like
(
    post_id uuid not null
        constraint fkiso0xdwsougiamemv4u4743p9
            references user_post,
    user_id uuid not null
        constraint fk3l2q204hr7tt4v6tm4u4evf74
            references pda_user,
    primary key (post_id, user_id)
);

create table if not exists user_relationship
(
    user1_id uuid not null
        constraint fk2ucn3rpt268kjjygi22pgnx43
            references pda_user,
    user2_id uuid not null
        constraint fkempjsyiot3fnh476aria7c326
            references pda_user,
    primary key (user1_id, user2_id)
);

create table if not exists user_statistic
(
    bought_items        integer,
    collected_artifacts integer,
    distance            integer,
    killed_enemies      integer,
    killed_mutants      integer,
    secret_found        integer,
    sold_items          integer,
    user_id             uuid
        constraint fkregape8txl0n7gwt1j5l5hoqa
            references pda_user
);

create table if not exists user_story_state
(
    id         uuid    not null
        primary key,
    chapter_id integer not null,
    current    boolean not null,
    over       boolean not null,
    stage_id   integer not null,
    story_id   integer not null,
    user_id    uuid
        constraint fkmjbis3t8klsu5tdlc5orlm0td
            references pda_user
);

create table if not exists user_usual_item
(
    id       uuid    not null
        primary key,
    quantity integer not null,
    base_id  bigint  not null
        constraint fk_hcrycia58tl5jgvrohsg6uy6g
            references base_item,
    owner_id uuid
        constraint fk_du4nouqryk8ri1wx3q2kitx2a
            references pda_user
);

create table if not exists user_weapon
(
    id              uuid    not null
        primary key,
    quantity        integer not null,
    base_id         bigint  not null
        constraint fk_sl648uqxnqsk1pj7kokq4yvdd
            references base_item,
    owner_id        uuid
        constraint fk_miafi1xs7qecvjrwycrkqv8i1
            references pda_user,
    is_equipped     boolean not null,
    condition       real    not null,
    bullet_base_id  integer not null,
    bullet_quantity integer not null,
    damage          real    not null,
    precision       real    not null,
    speed           real    not null,
    distance        real
);

create table if not exists seller_weapon
(
    seller_id bigint not null
        constraint fk1wpeplu5spwu0f702vurt95l6
            references seller,
    weapon_id uuid   not null
        constraint fkbviimap113rl13yjs0t3465n8
            references user_weapon,
    primary key (seller_id, weapon_id)
);

create table if not exists user_conversation_members
(
    conversation_entity_id uuid not null
        constraint fkeeg4pka54j566oy9suarypi05
            references user_conversation,
    members_id             uuid not null
        constraint fk5ld6g6asbrpq84xna4hfkibnn
            references pda_user,
    primary key (conversation_entity_id, members_id)
);

create table if not exists user_message
(
    id              uuid not null
        primary key,
    content         varchar(255),
    timestamp       timestamp(6) with time zone,
    author_id       uuid
        constraint fkndj15lbdhtirlv6dctaqpre7a
            references pda_user,
    conversation_id uuid
        constraint fkdbnnnwdaxl8kiqaxkung9kmlq
            references user_conversation
);

create table if not exists user_conversation_messages
(
    conversation_entity_id uuid not null
        constraint fk4gaufs2oc7sbajct99pnqrjy4
            references user_conversation,
    messages_id            uuid not null
        constraint uk_eg3luysmn72cgvor6wok9vp6n
            unique
        constraint fkn5wqvgk1nrq1fkovv3g81rphe
            references user_message
);

create table if not exists user_confirmation
(
    token         varchar(255) primary key unique,
    user_id             uuid
        constraint user_confirmation_pda_user_c
            references pda_user
);

alter table pda_user add column if not exists is_confirmed boolean default false;
