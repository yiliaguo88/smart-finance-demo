CREATE TABLE IF NOT EXISTS voucher (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    voucher_no    VARCHAR(32) NOT NULL UNIQUE,
    company_id    VARCHAR(16) NOT NULL,
    period        VARCHAR(7)  NOT NULL,
    voucher_type  VARCHAR(16) NOT NULL,
    total_amount  DECIMAL(18,3) NOT NULL,
    status        VARCHAR(16) NOT NULL DEFAULT 'DRAFT',
    created_by    VARCHAR(32),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS voucher_entry (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    voucher_id    BIGINT NOT NULL,
    account_code  VARCHAR(16) NOT NULL,
    account_name  VARCHAR(64) NOT NULL,
    debit_amount  DECIMAL(18,2) NOT NULL DEFAULT 0,
    credit_amount DECIMAL(18,2) NOT NULL DEFAULT 0,
    remark        VARCHAR(128),
    FOREIGN KEY (voucher_id) REFERENCES voucher(id)
);
