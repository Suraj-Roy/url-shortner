CREATE TABLE click_records (
       id UUID PRIMARY KEY,
       short_code VARCHAR(50) NOT NULL,
       clicked_at TIMESTAMP NOT NULL,
       ip_address VARCHAR(45),
       user_agent TEXT,
       country VARCHAR(10),
       referrer TEXT
);