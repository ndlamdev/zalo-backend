INSERT INTO roles (name, created_at, created_by, updated_at, updated_by, locked, deleted)
VALUES ('USER', '2025-07-16 14:00:00.000000', null, '2025-07-16 14:00:03.000000', null, false, false),
       ('ADMIN', '2025-07-16 14:00:00.000000', null, '2025-07-16 14:00:03.000000', null, false, false);

INSERT INTO permissions (name, created_at, created_by, updated_at, updated_by, locked, deleted)
VALUES ('USER_VALIDATE_TOKEN', '2025-07-17 11:46:58.000000', null, '2025-07-17 11:47:02.000000', null, false, false);
INSERT INTO permissions (name, created_at, created_by, updated_at, updated_by, locked, deleted)
VALUES ('USER_SEARCH_BY_PHONE_NUMBER', '2025-07-17 11:47:19.000000', null, '2025-07-17 11:47:21.000000', null, false,
        false);

INSERT INTO permissions_of_role (role_name, permission_name)
VALUES ('USER', 'USER_VALIDATE_TOKEN');
INSERT INTO permissions_of_role (role_name, permission_name)
VALUES ('USER', 'USER_SEARCH_BY_PHONE_NUMBER');


INSERT INTO users (phone_number, created_at, created_by, updated_at, updated_by, locked, deleted, password, active)
VALUES ('+84855354918', '2025-07-15 19:09:06.822731', null, '2025-07-15 19:09:06.822731', null, false, false,
        '$2a$10$K22r1xUiAUNzpTYUdyqsweI69.g0bzahK67gzzrfFzDAXgjy7lyna', true);
INSERT INTO users (phone_number, created_at, created_by, updated_at, updated_by, locked, deleted, password, active)
VALUES ('+84855354919', '2025-07-15 19:05:13.113674', null, '2025-07-15 19:05:13.113674', null, false, false,
        '$2a$10$MBO.P3W/SfpQV4aVRVSWL.Zy0fCFmwmFH7kIAgAyx/KKT4IfMtyzm', true);
INSERT INTO users (phone_number, created_at, created_by, updated_at, updated_by, locked, deleted, password, active)
VALUES ('+84855354917', '2025-07-15 19:05:13.113674', null, '2025-07-15 19:05:13.113674', null, false, false,
        '$2a$10$MBO.P3W/SfpQV4aVRVSWL.Zy0fCFmwmFH7kIAgAyx/KKT4IfMtyzm', true);
INSERT INTO users (phone_number, created_at, created_by, updated_at, updated_by, locked, deleted, password, active)
VALUES ('+84855354916', '2025-07-15 19:05:13.113674', null, '2025-07-15 19:05:13.113674', null, false, false,
        '$2a$10$MBO.P3W/SfpQV4aVRVSWL.Zy0fCFmwmFH7kIAgAyx/KKT4IfMtyzm', true);
INSERT INTO users (phone_number, created_at, created_by, updated_at, updated_by, locked, deleted, password, active)
VALUES ('+84855354915', '2025-07-15 19:05:13.113674', null, '2025-07-15 19:05:13.113674', null, false, false,
        '$2a$10$MBO.P3W/SfpQV4aVRVSWL.Zy0fCFmwmFH7kIAgAyx/KKT4IfMtyzm', true);
INSERT INTO users (phone_number, created_at, created_by, updated_at, updated_by, locked, deleted, password, active)
VALUES ('+84855354914', '2025-07-15 19:05:13.113674', null, '2025-07-15 19:05:13.113674', null, false, false,
        '$2a$10$MBO.P3W/SfpQV4aVRVSWL.Zy0fCFmwmFH7kIAgAyx/KKT4IfMtyzm', true);
INSERT INTO users (phone_number, created_at, created_by, updated_at, updated_by, locked, deleted, password, active)
VALUES ('+84855354913', '2025-07-15 19:05:13.113674', null, '2025-07-15 19:05:13.113674', null, false, false,
        '$2a$10$MBO.P3W/SfpQV4aVRVSWL.Zy0fCFmwmFH7kIAgAyx/KKT4IfMtyzm', true);
INSERT INTO users (phone_number, created_at, created_by, updated_at, updated_by, locked, deleted, password, active)
VALUES ('+84855354912', '2025-07-15 19:05:13.113674', null, '2025-07-15 19:05:13.113674', null, false, false,
        '$2a$10$MBO.P3W/SfpQV4aVRVSWL.Zy0fCFmwmFH7kIAgAyx/KKT4IfMtyzm', true);
INSERT INTO users (phone_number, created_at, created_by, updated_at, updated_by, locked, deleted, password, active)
VALUES ('+84855354911', '2025-07-15 19:05:13.113674', null, '2025-07-15 19:05:13.113674', null, false, false,
        '$2a$10$MBO.P3W/SfpQV4aVRVSWL.Zy0fCFmwmFH7kIAgAyx/KKT4IfMtyzm', true);
INSERT INTO users (phone_number, created_at, created_by, updated_at, updated_by, locked, deleted, password, active)
VALUES ('+84855354910', '2025-07-15 19:05:13.113674', null, '2025-07-15 19:05:13.113674', null, false, false,
        '$2a$10$MBO.P3W/SfpQV4aVRVSWL.Zy0fCFmwmFH7kIAgAyx/KKT4IfMtyzm', true);


INSERT INTO roles_of_user (role_name, user_phone_number)
VALUES ('USER', '+84855354918');
INSERT INTO roles_of_user (role_name, user_phone_number)
VALUES ('USER', '+84855354919');
INSERT INTO roles_of_user (role_name, user_phone_number)
VALUES ('USER', '+84855354917');
INSERT INTO roles_of_user (role_name, user_phone_number)
VALUES ('USER', '+84855354916');
INSERT INTO roles_of_user (role_name, user_phone_number)
VALUES ('USER', '+84855354915');
INSERT INTO roles_of_user (role_name, user_phone_number)
VALUES ('USER', '+84855354914');
INSERT INTO roles_of_user (role_name, user_phone_number)
VALUES ('USER', '+84855354913');
INSERT INTO roles_of_user (role_name, user_phone_number)
VALUES ('USER', '+84855354912');
INSERT INTO roles_of_user (role_name, user_phone_number)
VALUES ('USER', '+84855354911');
INSERT INTO roles_of_user (role_name, user_phone_number)
VALUES ('USER', '+84855354910');
