insert into role (role_id, role_name, role_description) values (1,'Admin','manage everything');
insert into role (role_id, role_name, role_description) values (2,'Salesperson','manage product price, customers, shipping, orders and sales report');
insert into role (role_id, role_name, role_description) values (3,'Editor','manage categories, brands, products, articles and menus');
insert into role (role_id, role_name, role_description) values (4,'Shipper','view products, view orders and update order status');
insert into role (role_id, role_name, role_description) values (5,'Assistant','manage questions and reviews');

insert into users (user_id, user_email, is_active, first_name, last_name, user_password)
values (2, 'test2@gmail.com', 1, 'test2', 'test2', 'test2');

insert into user_role (user_id, role_id)
values (2, 2);
insert into user_role (user_id, role_id)
values (2, 3);
insert into user_role (user_id, role_id)
values (2, 5);