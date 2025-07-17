CREATE TABLE "users"
(
    phone_number VARCHAR(15) PRIMARY KEY UNIQUE,
    created_at   TIMESTAMP,
    created_by   VARCHAR(255),
    updated_at   TIMESTAMP,
    updated_by   VARCHAR(255),
    locked       BOOL DEFAULT false,
    deleted      BOOL DEFAULT false,
    email        VARCHAR(255),
    full_name    VARCHAR(255),
    avatar       VARCHAR(255),
    birth_date   DATE
);

CREATE TABLE "friend_ships"
(
    phone_number_user_1 VARCHAR(255) NOT NULL,
    phone_number_user_2 VARCHAR(255) NOT NULL,
    CONSTRAINT "friend_ships_pk" PRIMARY KEY (phone_number_user_1, phone_number_user_2)
);
