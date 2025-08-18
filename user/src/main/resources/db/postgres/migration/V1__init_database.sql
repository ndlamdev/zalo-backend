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

CREATE TABLE "friendships"
(
    id                  BIGSERIAL PRIMARY KEY,
    owner_phone_number  VARCHAR(255) NOT NULL,
    friend_phone_number VARCHAR(255) NOT NULL,
    display_name        VARCHAR(255),
    created_at          TIMESTAMP,
    created_by          VARCHAR(255),
    updated_at          TIMESTAMP,
    updated_by          VARCHAR(255),
    locked              BOOL DEFAULT false,
    deleted             BOOL DEFAULT false,
    CONSTRAINT "friend_ships_users_owner_phone_number_fk" FOREIGN KEY (owner_phone_number) REFERENCES users (phone_number),
    CONSTRAINT "friend_ships_users_friend_phone_number_fk" FOREIGN KEY (friend_phone_number) REFERENCES users (phone_number)
);

CREATE TABLE "invite_add_friends"
(
    id                    BIGSERIAL    NOT NULL PRIMARY KEY,
    phone_number_sender   VARCHAR(255) NOT NULL,
    phone_number_receiver VARCHAR(255) NOT NULL,
    accepted              BOOL              default false,
    message               VARCHAR(255) NULL DEFAULT 'Xin chào bạn! Chúng mình kết bạn làm quen nhé!',
    created_at            TIMESTAMP,
    created_by            VARCHAR(255),
    updated_at            TIMESTAMP,
    updated_by            VARCHAR(255),
    locked                BOOL              DEFAULT false,
    deleted               BOOL              DEFAULT false,
    CONSTRAINT "friend_ships_users_phone_number_sender_fk" FOREIGN KEY (phone_number_sender) REFERENCES users (phone_number),
    CONSTRAINT "friend_ships_users_phone_number_receiver_fk" FOREIGN KEY (phone_number_receiver) REFERENCES users (phone_number)
);
