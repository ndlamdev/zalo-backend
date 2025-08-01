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
    phone_number VARCHAR(255) NOT NULL,
    rom_chat_id  BIGINT       NOT NULL,
    username     VARCHAR(255),
    display_name VARCHAR(255),
    created_at   TIMESTAMP,
    created_by   VARCHAR(255),
    updated_at   TIMESTAMP,
    updated_by   VARCHAR(255),
    locked       BOOL DEFAULT false,
    deleted      BOOL DEFAULT false,
    CONSTRAINT "rom_chat_members_rom_chats_fk" FOREIGN KEY (rom_chat_id) REFERENCES room_chats ("id"),
    CONSTRAINT "rom_chat_members_pk" PRIMARY KEY (phone_number, rom_chat_id)
);
