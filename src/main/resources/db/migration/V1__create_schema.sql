CREATE TABLE users (
    id VARCHAR(36) NOT NULL,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
);

CREATE TABLE listings (
    id VARCHAR(64) NOT NULL,
    title VARCHAR(160) NOT NULL,
    category VARCHAR(80) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    item_condition VARCHAR(80) NOT NULL,
    location VARCHAR(160) NOT NULL,
    description TEXT NOT NULL,
    seller_id VARCHAR(36) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    photo_icon_key VARCHAR(80) NOT NULL,
    photo_color_value BIGINT NOT NULL,
    likes INT NOT NULL DEFAULT 0,
    is_mine TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_listings_seller
        FOREIGN KEY (seller_id) REFERENCES users (id)
);

CREATE INDEX ix_listings_category ON listings (category);
CREATE INDEX ix_listings_created_at ON listings (created_at);
CREATE INDEX ix_listings_seller ON listings (seller_id);

CREATE TABLE conversations (
    id VARCHAR(96) NOT NULL,
    listing_id VARCHAR(64) NOT NULL,
    buyer_id VARCHAR(36) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_conversations_listing_buyer UNIQUE (listing_id, buyer_id),
    CONSTRAINT fk_conversations_listing
        FOREIGN KEY (listing_id) REFERENCES listings (id),
    CONSTRAINT fk_conversations_buyer
        FOREIGN KEY (buyer_id) REFERENCES users (id)
);

CREATE INDEX ix_conversations_buyer ON conversations (buyer_id);

CREATE TABLE chat_messages (
    id BIGINT NOT NULL AUTO_INCREMENT,
    conversation_id VARCHAR(96) NOT NULL,
    author_name VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    sent_at DATETIME(6) NOT NULL,
    from_current_user TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_chat_messages_conversation
        FOREIGN KEY (conversation_id) REFERENCES conversations (id)
);

CREATE INDEX ix_chat_messages_conversation_sent_at
    ON chat_messages (conversation_id, sent_at);
