CREATE TABLE IF NOT EXISTS Customer
(
    id      VARCHAR(8),
    name    VARCHAR(30),
    address VARCHAR(30),
    salary  double,
    CONSTRAINT PRIMARY KEY (id)
);