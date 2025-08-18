CREATE TABLE "room_chats"
(
    id         BIGSERIAL PRIMARY KEY UNIQUE,
    title      VARCHAR(15),
    avatar     VARCHAR(255),
    theme      VARCHAR(255),
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),
    locked     BOOL DEFAULT false,
    deleted    BOOL DEFAULT false
);

CREATE TABLE "room_chat_members"
(
    id           BIGSERIAL   NOT NULL PRIMARY KEY,
    phone_number VARCHAR(20) NOT NULL,
    rom_chat_id  BIGINT      NOT NULL,
    username     VARCHAR(255),
    display_name VARCHAR(255),
    created_at   TIMESTAMP,
    created_by   VARCHAR(255),
    updated_at   TIMESTAMP,
    updated_by   VARCHAR(255),
    locked       BOOL DEFAULT false,
    deleted      BOOL DEFAULT false,
    CONSTRAINT "rom_chat_members_rom_chats_fk" FOREIGN KEY (rom_chat_id) REFERENCES room_chats ("id")
);

CREATE TABLE "pin_room_chats"
(
    id           BIGSERIAL   NOT NULL PRIMARY KEY,
    phone_number VARCHAR(20) NOT NULL,
    rom_chat_id  BIGINT      NOT NULL,
    created_at   TIMESTAMP,
    created_by   VARCHAR(255),
    updated_at   TIMESTAMP,
    updated_by   VARCHAR(255),
    locked       BOOL DEFAULT false,
    deleted      BOOL DEFAULT false,
    CONSTRAINT "pin_room_chats_rom_chats_fk" FOREIGN KEY (rom_chat_id) REFERENCES room_chats ("id")
);

CREATE TABLE "messages"
(
    id                  BIGSERIAL   NOT NULL PRIMARY KEY,
    owner_phone_number  VARCHAR(20) NOT NULL,
    sender_phone_number VARCHAR(20) NOT NULL,
    rom_chat_id         BIGINT      NOT NULL,
    content             TEXT,
    type                VARCHAR(255),
    url_media           VARCHAR(255),
    created_at          TIMESTAMP,
    created_by          VARCHAR(255),
    updated_at          TIMESTAMP,
    updated_by          VARCHAR(255),
    locked              BOOL DEFAULT false,
    deleted             BOOL DEFAULT false,
    CONSTRAINT "message_rom_chats_fk" FOREIGN KEY (rom_chat_id) REFERENCES room_chats ("id")
);
