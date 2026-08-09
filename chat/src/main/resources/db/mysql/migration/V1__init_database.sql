CREATE TABLE conversation
(
    id         VARCHAR(50) PRIMARY KEY,
    soft_id    VARCHAR(255),
    type       ENUM ('PRIVATE', 'GROUP') NOT NULL,
    title      VARCHAR(100),
    avatar_url VARCHAR(255),
    theme      VARCHAR(255),
    admin      VARCHAR(50),
    is_deleted BOOL     DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE TABLE member
(
    id              VARCHAR(50) PRIMARY KEY,
    conversation_id VARCHAR(50)            NOT NULL,
    phoneNumber     VARCHAR(25)            NOT NULL,
    role            ENUM ('USER', 'ADMIN') NOT NULL,
    joined_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    joined_by       VARCHAR(50),
    is_muted        BOOL     DEFAULT FALSE,
    is_active       BOOL     DEFAULT TRUE,
    is_deleted      BOOL     DEFAULT FALSE,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    created_by      VARCHAR(255),
    updated_by      VARCHAR(255),

    CONSTRAINT uq_conversation_member
        UNIQUE (conversation_id, phoneNumber),

    CONSTRAINT fk_member_conversation
        FOREIGN KEY (conversation_id)
            REFERENCES conversation (id)
            ON DELETE CASCADE
);

CREATE TABLE conversation_member_metadata
(
    id                     VARCHAR(50) PRIMARY KEY,
    conversation_id        VARCHAR(50) NOT NULL,
    member_id              VARCHAR(50) NOT NULL,
    delete_conversation_at DATETIME,
    last_read_message_at   DATETIME,
    is_pinned              BOOL     DEFAULT FALSE,
    is_muted               BOOL     DEFAULT FALSE,
    is_archived            BOOL     DEFAULT FALSE,
    is_deleted             BOOL     DEFAULT FALSE,
    created_at             DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at             DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    created_by             VARCHAR(255),
    updated_by             VARCHAR(255),

    CONSTRAINT fk_conversation_member_metadata_conversation
        FOREIGN KEY (conversation_id)
            REFERENCES conversation (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_conversation_member_metadata_member
        FOREIGN KEY (member_id)
            REFERENCES member (id)
            ON DELETE CASCADE,

    CONSTRAINT conversation_member_metadata
        UNIQUE (conversation_id, member_id)
);

CREATE INDEX idx_conversation_member_metadata_member
    ON conversation_member_metadata (member_id);

CREATE INDEX idx_conversation_member_metadata_conversation
    ON conversation_member_metadata (conversation_id);

CREATE TABLE message
(
    id              VARCHAR(50) PRIMARY KEY,
    conversation_id VARCHAR(50)                                       NOT NULL,
    sender_id       VARCHAR(50)                                       NOT NULL,
    content         TEXT,
    message_type    ENUM ( 'IMAGE', 'TEXT', 'AUDIO', 'VIDEO', 'FILE') NOT NULL,
    reply_to_id     VARCHAR(50),
    is_pinned       BOOL     DEFAULT FALSE,
    is_deleted      BOOL     DEFAULT FALSE,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    created_by      VARCHAR(255),
    updated_by      VARCHAR(255),

    CONSTRAINT fk_message_conversation
        FOREIGN KEY (conversation_id)
            REFERENCES conversation (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_message_sender
        FOREIGN KEY (sender_id)
            REFERENCES member (id),

    CONSTRAINT fk_message_reply
        FOREIGN KEY (reply_to_id)
            REFERENCES message (id)
);

CREATE INDEX idx_message_conversation_created
    ON message (conversation_id, created_at);

CREATE INDEX idx_message_sender
    ON message (sender_id);


CREATE TABLE attachment
(
    id         VARCHAR(50) PRIMARY KEY,
    message_id VARCHAR(50) NOT NULL,
    url        VARCHAR(500),
    type       ENUM ( 'IMAGE', 'AUDIO', 'VIDEO', 'FILE'),
    file_name  VARCHAR(255),
    file_size  BIGINT,
    width      INT,
    height     INT,
    duration   INT,
    is_deleted BOOL     DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT fk_attachment_message
        FOREIGN KEY (message_id)
            REFERENCES message (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_attachment_message
    ON attachment (message_id);


CREATE TABLE message_history
(
    id          VARCHAR(50) PRIMARY KEY,
    message_id  VARCHAR(50) NOT NULL,
    editor_id   VARCHAR(50) NOT NULL,
    old_content TEXT,
    new_content TEXT,
    is_deleted  BOOL     DEFAULT FALSE,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255),

    CONSTRAINT fk_history_message
        FOREIGN KEY (message_id)
            REFERENCES message (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_history_editor
        FOREIGN KEY (editor_id)
            REFERENCES member (id)
);

CREATE TABLE message_reaction
(
    id         VARCHAR(50) PRIMARY KEY,
    message_id VARCHAR(50) NOT NULL,
    member_id  VARCHAR(50) NOT NULL,
    emoji      VARCHAR(20),
    is_deleted BOOL     DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT uq_reaction
        UNIQUE (message_id, member_id, emoji),

    CONSTRAINT fk_reaction_message
        FOREIGN KEY (message_id)
            REFERENCES message (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_reaction_member
        FOREIGN KEY (member_id)
            REFERENCES member (id)
            ON DELETE CASCADE
);
