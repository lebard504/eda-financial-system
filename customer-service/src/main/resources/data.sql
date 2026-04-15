DELETE FROM customers;

INSERT INTO customers (id, name, gender, age, identification, address, phone, password, status)
VALUES 
('11111111-1111-1111-1111-111111111111', 'Jose Lema', 'M', 30, '12345678', 'Otavalo sn y principal', '098254785', '1234', true),
('22222222-2222-2222-2222-222222222222', 'Marianela Montalvo', 'F', 28, '87654321', 'Amazonas y NNUU', '097548965', '5678', true),
('33333333-3333-3333-3333-333333333333', 'Juan Osorio', 'M', 35, '11223344', '13 junio y Equinoccial', '098874587', '1245', true);