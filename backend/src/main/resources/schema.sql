CREATE TABLE IF NOT EXISTS announcement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    created_at DATE NOT NULL,
    expire_at DATE NOT NULL,
    content TEXT NOT NULL,
    attachments TEXT
);
