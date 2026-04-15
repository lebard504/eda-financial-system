DELETE FROM transactions;
DELETE FROM accounts;

-- =========================
-- CUSTOMER SNAPSHOTS
-- =========================
INSERT INTO customer_snapshots (customer_id, name, identification)
VALUES 
('11111111-1111-1111-1111-111111111111', 'Jose Lema', '12345678'),
('22222222-2222-2222-2222-222222222222', 'Marianela Montalvo', '87654321'),
('33333333-3333-3333-3333-333333333333', 'Juan Osorio', '11223344');

-- ACCOUNTS
INSERT INTO accounts (id, account_number, account_type, balance, status, customer_id)
VALUES 
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', '478758', 'SAVINGS', 1425, 'ACTIVE', '11111111-1111-1111-1111-111111111111'),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2', '225487', 'CURRENT', 700, 'ACTIVE', '22222222-2222-2222-2222-222222222222'),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3', '495878', 'SAVINGS', 150, 'ACTIVE', '33333333-3333-3333-3333-333333333333'),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa4', '496825', 'SAVINGS', 0, 'ACTIVE', '22222222-2222-2222-2222-222222222222');

-- TRANSACTIONS
INSERT INTO transactions (id, transaction_date, transaction_type, amount, balance, idempotency_key, account_id)
VALUES 
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1', CURRENT_TIMESTAMP, 'WITHDRAW', 575, 1425, 'tx-1', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2', CURRENT_TIMESTAMP, 'DEPOSIT', 600, 700, 'tx-2', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb3', CURRENT_TIMESTAMP, 'DEPOSIT', 150, 150, 'tx-3', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb4', CURRENT_TIMESTAMP, 'WITHDRAW', 540, 0, 'tx-4', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa4');