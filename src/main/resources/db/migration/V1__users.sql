CREATE TABLE users (
    id BIGINT UNSIGNED PRIMARY KEY auto_increment,
    username VARCHAR(64) not null,
    email VARCHAR(64) not null,
    first_name VARCHAR(32),
    last_name VARCHAR(32),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW() ON UPDATE NOW()
);
