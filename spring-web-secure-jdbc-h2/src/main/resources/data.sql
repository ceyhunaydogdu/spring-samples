insert into users (username, password, enabled) values ('user', '{noop}password', true);
insert into users (username, password, enabled) values ('ceyhun', '{noop}ceyhun', true);

insert into authorities (username, authority) values ('ceyhun', 'ROLE_ADMIN');
insert into authorities (username, authority) values ('user', 'ROLE_USER')