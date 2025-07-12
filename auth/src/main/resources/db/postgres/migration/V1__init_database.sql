CREATE TABLE "users"
(
    phone_number VARCHAR(15) PRIMARY KEY UNIQUE,
    created_at   TIMESTAMP,
    created_by   VARCHAR(255),
    updated_at   TIMESTAMP,
    updated_by   VARCHAR(255),
    locked       BOOL DEFAULT false,
    deleted      BOOL DEFAULT false,
    email        VARCHAR(255) NULL,
    password     VARCHAR      NOT NULL
);

CREATE TABLE "roles"
(
    name       VARCHAR(255) NOT NULL PRIMARY KEY UNIQUE,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),
    locked     BOOL DEFAULT false,
    deleted    BOOL DEFAULT false
);

CREATE TABLE "permissions"
(
    name       VARCHAR(255) NOT NULL PRIMARY KEY UNIQUE,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),
    locked     BOOL DEFAULT false,
    deleted    BOOL DEFAULT false
);

CREATE TABLE "roles_of_user"
(
    role_name         VARCHAR(255),
    user_phone_number VARCHAR(15),
    CONSTRAINT "roles_of_user-pk" PRIMARY KEY (role_name, user_phone_number),
    CONSTRAINT "roles_of_user-and-roles-fk" FOREIGN KEY (role_name) REFERENCES roles (name),
    CONSTRAINT "roles_of_user-and-users-fk" FOREIGN KEY (user_phone_number) REFERENCES users (phone_number)
);

CREATE TABLE "permissions_of_role"
(
    role_name       VARCHAR(255),
    permission_name VARCHAR(255),
    CONSTRAINT "permissions_of_role-pk" PRIMARY KEY (role_name, permission_name),
    CONSTRAINT "permissions_of_role-and-roles-fk" FOREIGN KEY (role_name) REFERENCES roles (name),
    CONSTRAINT "permissions_of_role-and-permission-fk" FOREIGN KEY (permission_name) REFERENCES permissions (name)
);