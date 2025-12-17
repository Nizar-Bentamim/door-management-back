-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create doors table
CREATE TABLE doors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create access table (junction table for many-to-many relationship)
CREATE TABLE access (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    door_id BIGINT NOT NULL,
    granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_access_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_access_door FOREIGN KEY (door_id) REFERENCES doors(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_door UNIQUE (user_id, door_id)
);

-- Create indexes for better query performance
CREATE INDEX idx_access_user_id ON access(user_id);
CREATE INDEX idx_access_door_id ON access(door_id);


