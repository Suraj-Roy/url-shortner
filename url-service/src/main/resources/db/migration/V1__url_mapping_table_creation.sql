create table url_mapping(
    id UUID PRIMARY KEY,
    short_url VARCHAR(10) NOT NULL UNIQUE,
    url TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

create Index idx_url_mapping_short_url on url_mapping(short_url);