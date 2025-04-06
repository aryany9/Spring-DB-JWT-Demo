INSERT INTO customer (id, name, email)
VALUES (1, 'John Doe', 'john.doe@example.com');


INSERT INTO customer_credentials (id, username, password, role, customer_id)
VALUES (
           1,
           'johnny',
           '$2a$10$Dow1j0JGjY7r9IPoEfU0xu3cgph4Z1RMejLRVdlD0Zprp/B5OaIgy',
           'ROLE_USER',
           1
       );
