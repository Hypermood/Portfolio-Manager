CREATE TABLE portfolio_items (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    company_name VARCHAR(128) NOT NULL,
    quantity INT NOT NULL,
    total_bought_price DOUBLE NOT NULL,
    user_id BIGINT UNSIGNED NOT NULL,
    CONSTRAINT fk_portfolio_items_users_id FOREIGN KEY (user_id)
    REFERENCES users(id),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW() ON UPDATE NOW()
);
