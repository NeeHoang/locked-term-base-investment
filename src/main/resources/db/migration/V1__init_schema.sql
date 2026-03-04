-- ============================================================
-- V1__init_schema.sql
-- Initial schema for locked-term investment application
-- ============================================================

-- ============================================================
-- WALLET BOUNDED CONTEXT
-- ============================================================

CREATE TABLE user_wallets (
                              wallet_id           UUID            NOT NULL,
                              total_balance       NUMERIC(18, 8)  NOT NULL,
                              balance_available   NUMERIC(18, 8)  NOT NULL,
                              balance_frozen      NUMERIC(18, 8)  NOT NULL,
                              status              VARCHAR(50)     NOT NULL,
                              version             BIGINT          NOT NULL DEFAULT 0,
                              created_at          TIMESTAMPTZ     NOT NULL,
                              updated_at          TIMESTAMPTZ     NOT NULL,

                              CONSTRAINT pk_user_wallets PRIMARY KEY (wallet_id)
);

-- ============================================================
-- SAVING BOUNDED CONTEXT
-- ============================================================

CREATE TABLE locked_products (
                                 product_id          BIGSERIAL       PRIMARY KEY,
                                 term_days           INTEGER         NOT NULL,
                                 interest_rate       NUMERIC(5, 4)   NOT NULL,
                                 min_amount          NUMERIC(18, 8),
                                 max_amount          NUMERIC(18, 8),
                                 description         TEXT,
                                 status              VARCHAR(50)     NOT NULL,
                                 available_quota     NUMERIC(18, 8),
                                 total_quota         NUMERIC(18, 8),
                                 version             BIGINT          NOT NULL DEFAULT 0,
                                 created_at          TIMESTAMPTZ     NOT NULL,
                                 updated_at          TIMESTAMPTZ     NOT NULL
);

CREATE TABLE subscriptions (
                               subscription_id     UUID            NOT NULL,
                               wallet_id           UUID            NOT NULL,
                               product_id          BIGINT          NOT NULL,
                               principal           NUMERIC(18, 8)  NOT NULL,
                               start_date          DATE            NOT NULL,
                               maturity_date       DATE            NOT NULL,
                               status              VARCHAR(50)     NOT NULL,
                               total_interest      NUMERIC(18, 8),
                               created_at          TIMESTAMPTZ     NOT NULL,
                               updated_at          TIMESTAMPTZ     NOT NULL,

                               CONSTRAINT pk_subscriptions PRIMARY KEY (subscription_id),
                               CONSTRAINT fk_subscription_product
                                   FOREIGN KEY (product_id) REFERENCES locked_products (product_id)
);

CREATE TABLE earnings (
                          earning_id          BIGSERIAL,
                          subscription_id     UUID            NOT NULL,
                          principal           NUMERIC(18, 8)  NOT NULL,
                          available           NUMERIC(18, 8)  NOT NULL,
                          total_interest      NUMERIC(18, 8)  NOT NULL,
                          interest_per_day    NUMERIC(18, 8)  NOT NULL,
                          progress            NUMERIC(5, 2)   NOT NULL,
                          penalty_rate        NUMERIC(5, 4)   NOT NULL,
                          penalty_amount      NUMERIC(18, 8)  NOT NULL,  -- scale 8
                          holding_days        INTEGER         NOT NULL,
                          term_days           INTEGER         NOT NULL,
                          version             BIGINT          NOT NULL DEFAULT 0,
                          created_at          TIMESTAMPTZ     NOT NULL,
                          updated_at          TIMESTAMPTZ     NOT NULL,

                          CONSTRAINT pk_earnings PRIMARY KEY (earning_id),
                          CONSTRAINT fk_earning_subscription
                              FOREIGN KEY (subscription_id) REFERENCES subscriptions (subscription_id),
                          CONSTRAINT uq_earning_subscription UNIQUE (subscription_id)
);

CREATE TABLE earning_transactions (
                                      tx_id               BYTEA           NOT NULL,
                                      earning_id          BIGINT          NOT NULL,
                                      tx_type             VARCHAR(50)     NOT NULL,   -- DAILY_INTEREST, EARLY_REDEEMED, REDEEMED
                                      status              VARCHAR(50)     NOT NULL,   -- PENDING, SUCCESS, FAILED
                                      available_before    NUMERIC(18, 8)  NOT NULL,
                                      amount              NUMERIC(18, 8)  NOT NULL,
                                      available_after     NUMERIC(18, 8)  NOT NULL,
                                      created_at          TIMESTAMPTZ,

                                      CONSTRAINT pk_earning_transactions PRIMARY KEY (tx_id),
                                      CONSTRAINT fk_earning_tx_earning
                                          FOREIGN KEY (earning_id) REFERENCES earnings (earning_id)
);

CREATE TABLE interest_transactions (
                                       tx_id               BYTEA           NOT NULL,
                                       earning_id          BIGINT          NOT NULL,
                                       date                DATE            NOT NULL,
                                       amount              NUMERIC(18, 8)  NOT NULL,
                                       created_at          TIMESTAMPTZ,

                                       CONSTRAINT pk_interest_transactions PRIMARY KEY (tx_id),
                                       CONSTRAINT fk_interest_tx_earning
                                           FOREIGN KEY (earning_id) REFERENCES earnings (earning_id),
                                       CONSTRAINT uk_interest_tx_earning_date
                                           UNIQUE (earning_id, date)
);

CREATE TABLE withdraw_transactions (
                                       tx_id               BYTEA           NOT NULL,
                                       earning_id          BIGINT          NOT NULL,
                                       date                DATE            NOT NULL,
                                       available_before    NUMERIC(18, 8)  NOT NULL,
                                       available_after     NUMERIC(18, 8)  NOT NULL,
                                       amount              NUMERIC(18, 8)  NOT NULL,
                                       created_at          TIMESTAMPTZ,

                                       CONSTRAINT pk_withdraw_transactions PRIMARY KEY (tx_id),
                                       CONSTRAINT fk_withdraw_tx_earning
                                           FOREIGN KEY (earning_id) REFERENCES earnings (earning_id)
);

-- ============================================================
-- ADMIN BOUNDED CONTEXT
-- ============================================================

CREATE TABLE liquidity_pool (
                                pool_id             BYTEA           NOT NULL,
                                total_amount        NUMERIC(18, 8)  NOT NULL,
                                min_threshold       NUMERIC(18, 8)  NOT NULL,
                                status              VARCHAR(50)     NOT NULL,
                                last_injected_at    TIMESTAMPTZ     NOT NULL,
                                updated_at          TIMESTAMPTZ     NOT NULL,
                                version             BIGINT          NOT NULL DEFAULT 0,

                                CONSTRAINT pk_liquidity_pool PRIMARY KEY (pool_id)
);

CREATE TABLE liquidity_ledger (
                                  tx_id               BYTEA           NOT NULL,
                                  tx_type             VARCHAR(50)     NOT NULL,
                                  liquidity_before    NUMERIC(18, 8)  NOT NULL,
                                  amount              NUMERIC(18, 8)  NOT NULL,
                                  liquidity_after     NUMERIC(18, 8)  NOT NULL,
                                  reference_id        BYTEA           NOT NULL,
                                  created_at          TIMESTAMPTZ     NOT NULL,

                                  CONSTRAINT pk_liquidity_ledger PRIMARY KEY (tx_id)
);

CREATE TABLE admin_injection (
                                 tx_id               BYTEA           NOT NULL,
                                 amount              NUMERIC(18, 8)  NOT NULL,
                                 admin_id            BYTEA           NOT NULL,
                                 note                TEXT            NOT NULL,
                                 created_at          TIMESTAMPTZ     NOT NULL,

                                 CONSTRAINT pk_admin_injection PRIMARY KEY (tx_id)
);

-- ============================================================
-- INDEXES
-- ============================================================

-- subscriptions
CREATE INDEX idx_subscriptions_wallet_id   ON subscriptions (wallet_id);
CREATE INDEX idx_subscriptions_product_id  ON subscriptions (product_id);
CREATE INDEX idx_subscriptions_status      ON subscriptions (status);

-- earnings
CREATE INDEX idx_earnings_subscription_id ON earnings (subscription_id);

-- earning_transactions
CREATE INDEX idx_earning_tx_earning_id    ON earning_transactions (earning_id);
CREATE INDEX idx_earning_tx_status        ON earning_transactions (status);

-- interest_transactions
CREATE INDEX idx_interest_tx_earning_id   ON interest_transactions (earning_id);
CREATE INDEX idx_interest_tx_date         ON interest_transactions (date);

-- withdraw_transactions
CREATE INDEX idx_withdraw_tx_earning_id   ON withdraw_transactions (earning_id);

-- liquidity_ledger
CREATE INDEX idx_liquidity_ledger_tx_type ON liquidity_ledger (tx_type);