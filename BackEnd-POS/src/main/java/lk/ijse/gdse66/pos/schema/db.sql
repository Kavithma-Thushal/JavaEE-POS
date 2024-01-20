DROP DATABASE IF EXISTS javaee_pos;
CREATE DATABASE IF NOT EXISTS javaee_pos;
USE javaee_pos;

CREATE TABLE Customer
(
    id      VARCHAR(8),
    name    VARCHAR(30),
    address VARCHAR(30),
    salary  double,
    CONSTRAINT PRIMARY KEY (id)
);

CREATE TABLE Item
(
    code        VARCHAR(8),
    description VARCHAR(50),
    qty         INT(5)         DEFAULT 0,
    unitPrice   DECIMAL(10, 2) DEFAULT 0.00,
    CONSTRAINT PRIMARY KEY (code)
);

CREATE TABLE `Orders`
(
    orderId   VARCHAR(8),
    orderDate DATE,
    cusId     VARCHAR(8),
    CONSTRAINT PRIMARY KEY (orderId, cusId),
    CONSTRAINT FOREIGN KEY (cusId) REFERENCES Customer (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `OrderDetail`
(
    orderId  VARCHAR(8),
    itemCode VARCHAR(8),
    qty      INT(5)         DEFAULT 0,
    total    DECIMAL(10, 2) DEFAULT 0.00,

    CONSTRAINT PRIMARY KEY (orderId, itemCode),
    CONSTRAINT FOREIGN KEY (orderId) REFERENCES `Orders` (orderId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (itemCode) REFERENCES Item (code) ON DELETE CASCADE ON UPDATE CASCADE

);

INSERT INTO Customer
VALUES ('C00-001', 'Thushal', 'Galle', 95000),
       ('C00-002', 'Kamal', 'Matara', 55000),
       ('C00-003', 'Nimal', 'Colombo', 45000),
       ('C00-004', 'Sunil', 'Negambo', 75000),
       ('C00-005', 'Jagath', 'Kegalle', 45000);

INSERT INTO Item
VALUES ('I00-001', 'Sugar', 50, 110.00),
       ('I00-002', 'Samba Rice', 40, 140.00),
       ('I00-003', 'Flour', 30, 80.00),
       ('I00-004', 'Potato', 50, 120.00),
       ('I00-005', 'Onions', 30, 110.00),
       ('I00-006', 'Chocolate Biscuit', 30, 170.00),
       ('I00-007', 'Cream Cracker', 40, 160.00),
       ('I00-008', 'Chicken', 40, 630.00),
       ('I00-009', 'Kakulu Rice', 60, 105.00),
       ('I00-010', 'Noodles', 80, 190.00);