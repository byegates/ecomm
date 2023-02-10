insert into role (id, name) values (2000000001, 'ADMIN' );
insert into role (id, name) values (2000000002, 'CUSTOMER' );

set @c0 = 'c4e61f12-e520-451f-83c1-e74a15ecc4a8';
set @c1 = 'ba0500f0-17b2-4885-918e-03781b6233d2';
set @c2 = 'ac21fcc8-61c7-47e1-87f9-83afde40c7af';
set @c3 = 'c861e35f-e81a-4dec-be7e-dfd9c852a630';
set @c4 = 'b3f4e44a-a7dd-45ef-9243-48873e3ca28a';

insert into users (id, email, first_name, last_name)
values ( UNHEX(REPLACE(@c0, '-', '')), 'customer0@customer-domain.com', 'Customer0', 'Customer' );
insert into users (id, email, first_name, last_name)
values ( UNHEX(REPLACE(@c1, '-', '')), 'customer1@customer-domain.com', 'Customer1', 'Customer' );
insert into users (id, email, first_name, last_name)
values ( UNHEX(REPLACE(@c2, '-', '')), 'customer2@customer-domain.com', 'Customer2', 'Customer' );
insert into users (id, email, first_name, last_name)
values ( UNHEX(REPLACE(@c3, '-', '')), 'customer3@customer-domain.com', 'Customer3', 'Customer' );
insert into users (id, email, first_name, last_name)
values ( UNHEX(REPLACE(@c4, '-', '')), 'customer4@customer-domain.com', 'Customer4', 'Customer' );


set @adm1 = '679349be-0942-4ee9-97ed-50f02a99b615';
set @adm2 = '7891e314-2be9-44ed-b6bd-95f56441cac3';

insert into users (id, email, first_name, last_name)
values ( UNHEX(REPLACE(@adm1, '-', '')), 'admin1@domain.com', 'admin1', 'admin' );
insert into users (id, email, first_name, last_name)
values ( UNHEX(REPLACE(@adm2, '-', '')), 'admin2@domain.com', 'admin2', 'admin' );


insert into users_roles (user_id, role_id) values ( UNHEX(REPLACE(@c0, '-', '')), 2000000002 );
insert into users_roles (user_id, role_id) values ( UNHEX(REPLACE(@c1, '-', '')), 2000000002 );
insert into users_roles (user_id, role_id) values ( UNHEX(REPLACE(@c2, '-', '')), 2000000002 );
insert into users_roles (user_id, role_id) values ( UNHEX(REPLACE(@c3, '-', '')), 2000000002 );
insert into users_roles (user_id, role_id) values ( UNHEX(REPLACE(@c4, '-', '')), 2000000002 );

insert into users_roles (user_id, role_id) values ( UNHEX(REPLACE(@adm1, '-', '')), 2000000001 );
insert into users_roles (user_id, role_id) values ( UNHEX(REPLACE(@adm2, '-', '')), 2000000001 );

insert into product (id, name, price) values ( 2000000001, 'iPhone 14 128GB', 799.0 );
insert into product (id, name, price) values ( 2000000002, 'iPhone 14 Plus 128GB', 899.0 );
insert into product (id, name, price) values ( 2000000003, 'iPhone 14 Pro 128GB', 999.0 );
insert into product (id, name, price) values ( 2000000004, 'iPhone 14 Pro Max 128GB', 1099.0 );
insert into product (id, name, price) values ( 2000000005, 'iPhone 14 256GB', 899.0 );
insert into product (id, name, price) values ( 2000000006, 'iPhone 14 Plus 256GB', 999.0 );
insert into product (id, name, price) values ( 2000000007, 'iPhone 14 Pro 256GB', 1099.0 );
insert into product (id, name, price) values ( 2000000008, 'iPhone 14 Pro Max 256GB', 1199.0 );

insert into deal (id, name, description) values ( 2000000001, 'BOGO50', 'BUY ONE GET ONE 50% OFF' );
insert into deal (id, name, description) values ( 2000000002, 'BOGO100', 'BUY ONE GET ONE FREE' );
insert into deal (id, name, description) values ( 2000000003, 'OFF35', '35% OFF' );
insert into deal (id, name, description) values ( 2000000004, 'OFF20', '20% OFF' );

insert into products_deals (product_id, deal_id) values ( 2000000001, 2000000001 );
insert into products_deals (product_id, deal_id) values ( 2000000002, 2000000001 );
insert into products_deals (product_id, deal_id) values ( 2000000003, 2000000003 );
insert into products_deals (product_id, deal_id) values ( 2000000004, 2000000003 );
insert into products_deals (product_id, deal_id) values ( 2000000005, 2000000002 );
insert into products_deals (product_id, deal_id) values ( 2000000006, 2000000004 );

insert into cart_item (user_id, product_id, quantity) values ( UNHEX(REPLACE(@c0, '-', '')), 2000000001, 2 );
insert into cart_item (user_id, product_id, quantity) values ( UNHEX(REPLACE(@c0, '-', '')), 2000000003, 1 );

insert into cart_item (user_id, product_id, quantity) values ( UNHEX(REPLACE(@c1, '-', '')), 2000000002, 3 );
insert into cart_item (user_id, product_id, quantity) values ( UNHEX(REPLACE(@c1, '-', '')), 2000000004, 2 );

insert into cart_item (user_id, product_id, quantity) values ( UNHEX(REPLACE(@c2, '-', '')), 2000000005, 4 );
insert into cart_item (user_id, product_id, quantity) values ( UNHEX(REPLACE(@c2, '-', '')), 2000000006, 1 );

insert into cart_item (user_id, product_id, quantity) values ( UNHEX(REPLACE(@c3, '-', '')), 2000000005, 5 );
insert into cart_item (user_id, product_id, quantity) values ( UNHEX(REPLACE(@c3, '-', '')), 2000000006, 3 );
insert into cart_item (user_id, product_id, quantity) values ( UNHEX(REPLACE(@c3, '-', '')), 2000000007, 2 );

insert into cart_item (user_id, product_id, quantity) values ( UNHEX(REPLACE(@c4, '-', '')), 2000000007, 1 );
insert into cart_item (user_id, product_id, quantity) values ( UNHEX(REPLACE(@c4, '-', '')), 2000000008, 2 );
