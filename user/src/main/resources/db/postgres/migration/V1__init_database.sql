CREATE TABLE "users"
(
    phone_number VARCHAR(15) PRIMARY KEY UNIQUE,
    email        VARCHAR(255),
    full_name    VARCHAR(255),
    avatar       VARCHAR(255),
    birth_date   DATE,
    created_at   TIMESTAMP,
    created_by   VARCHAR(255),
    updated_at   TIMESTAMP,
    updated_by   VARCHAR(255),
    locked       BOOL DEFAULT false,
    deleted      BOOL DEFAULT false
);

CREATE TABLE "friend_ships"
(
    phone_number_user_1 VARCHAR(255) NOT NULL,
    phone_number_user_2 VARCHAR(255) NOT NULL,
    created_at   TIMESTAMP,
    created_by   VARCHAR(255),
    updated_at   TIMESTAMP,
    updated_by   VARCHAR(255),
    locked       BOOL DEFAULT false,
    deleted      BOOL DEFAULT false,
    CONSTRAINT "friend_ships_pk" PRIMARY KEY (phone_number_user_1, phone_number_user_2)
);

CREATE TABLE "invite_add_friends"
(
    phone_number_sender   VARCHAR(255) NOT NULL,
    phone_number_receiver VARCHAR(255) NOT NULL,
    accepted               BOOL default false,
    message VARCHAR(255) NULL DEFAULT 'Xin chào bạn! Chúng mình kết bạn làm quen nhé!',
    created_at   TIMESTAMP,
    created_by   VARCHAR(255),
    updated_at   TIMESTAMP,
    updated_by   VARCHAR(255),
    locked       BOOL DEFAULT false,
    deleted      BOOL DEFAULT false,
    CONSTRAINT "invite_add_friends_pk" PRIMARY KEY (phone_number_sender, phone_number_receiver)
);
