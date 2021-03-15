DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS role;

CREATE TABLE IF NOT EXISTS role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  login VARCHAR(150) UNIQUE NOT NULL,
  password VARCHAR(150) NOT NULL,
  email VARCHAR(320) UNIQUE NOT NULL,
  first_name VARCHAR(150) NOT NULL,
  last_name VARCHAR(150) NOT NULL,
  birthday DATE,
  role_id BIGINT NOT NULL,
  FOREIGN KEY(role_id) REFERENCES role(id)
);

INSERT INTO role (name) VALUES ('User'), ('Admin');

INSERT INTO user (login, password, email, first_name, last_name, birthday, role_id)
    VALUES ('ivanov72', 'qwerty', 'ivanov72@gmail.com', 'Ivan', 'Ivanov', '1972-02-06', 1);

INSERT INTO user (login, password, email, first_name, last_name, birthday, role_id)
    VALUES ('petrov80', '123456', 'petrov80@gmail.com', 'Peter', 'Petrov', '1980-12-08', 2);
