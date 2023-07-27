CREATE TABLE transactions (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    type ENUM('BUY', 'SELL') NOT NULL,
    company_name VARCHAR(64) NOT NULL,
    quantity DOUBLE NOT NULL,
    stock_price DOUBLE NOT NULL,
    user_id BIGINT UNSIGNED NOT NULL,
    CONSTRAINT fk_transactions_users_id FOREIGN KEY (user_id)
    REFERENCES users(id),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW() on UPDATE NOW()
);
