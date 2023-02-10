set @id1 = 2000000001;
set @id2 = 2000000002;
set @id3 = 2000000003;
set @id4 = 2000000004;
set @id5 = 2000000005;
set @id6 = 2000000006;
set @id7 = 2000000007;
set @id8 = 2000000008;

set @c0 = 'c4e61f12-e520-451f-83c1-e74a15ecc4a8';
set @c1 = 'ba0500f0-17b2-4885-918e-03781b6233d2';
set @c2 = 'ac21fcc8-61c7-47e1-87f9-83afde40c7af';
set @c3 = 'c861e35f-e81a-4dec-be7e-dfd9c852a630';
set @c4 = 'b3f4e44a-a7dd-45ef-9243-48873e3ca28a';


set @adm1 = '679349be-0942-4ee9-97ed-50f02a99b615';
set @adm2 = '7891e314-2be9-44ed-b6bd-95f56441cac3';


insert into role (id, name) values (@id2, 'CUSTOMER' );
insert into role (id, name) values (@id1, 'ADMIN' );


insert into users (id, email, first_name, last_name)
values ( @c0, 'customer0@customer-domain.com', 'Customer0', 'Customer' );
insert into users (id, email, first_name, last_name)
values ( @c1, 'customer1@customer-domain.com', 'Customer1', 'Customer' );
insert into users (id, email, first_name, last_name)
values ( @c2, 'customer2@customer-domain.com', 'Customer2', 'Customer' );
insert into users (id, email, first_name, last_name)
values ( @c3, 'customer3@customer-domain.com', 'Customer3', 'Customer' );
insert into users (id, email, first_name, last_name)
values ( @c4, 'customer4@customer-domain.com', 'Customer4', 'Customer' );


insert into users (id, email, first_name, last_name)
values ( @adm1, 'admin1@domain.com', 'admin1', 'admin' );
insert into users (id, email, first_name, last_name)
values ( @adm2, 'admin2@domain.com', 'admin2', 'admin' );


insert into users_roles (user_id, role_id) values ( @c0, @id2 );
insert into users_roles (user_id, role_id) values ( @c1, @id2 );
insert into users_roles (user_id, role_id) values ( @c2, @id2 );
insert into users_roles (user_id, role_id) values ( @c3, @id2 );
insert into users_roles (user_id, role_id) values ( @c4, @id2 );

insert into users_roles (user_id, role_id) values ( @adm1, @id1 );
insert into users_roles (user_id, role_id) values ( @adm2, @id1 );

insert into product (id, name, price) values ( @id1, 'iPhone 14 128GB', 799.0 );
insert into product (id, name, price) values ( @id2, 'iPhone 14 Plus 128GB', 899.0 );
insert into product (id, name, price) values ( @id3, 'iPhone 14 Pro 128GB', 999.0 );
insert into product (id, name, price) values ( @id4, 'iPhone 14 Pro Max 128GB', 1099.0 );
insert into product (id, name, price) values ( @id5, 'iPhone 14 256GB', 899.0 );
insert into product (id, name, price) values ( @id6, 'iPhone 14 Plus 256GB', 999.0 );
insert into product (id, name, price) values ( @id7, 'iPhone 14 Pro 256GB', 1099.0 );
insert into product (id, name, price) values ( @id8, 'iPhone 14 Pro Max 256GB', 1199.0 );

insert into deal (id, name, description) values ( @id1, 'BOGO50', 'BUY ONE GET ONE 50% OFF' );
insert into deal (id, name, description) values ( @id2, 'BOGO100', 'BUY ONE GET ONE FREE' );
insert into deal (id, name, description) values ( @id3, 'OFF35', '35% OFF' );
insert into deal (id, name, description) values ( @id4, 'OFF20', '20% OFF' );

insert into products_deals (product_id, deal_id) values ( @id1, @id1 );
insert into products_deals (product_id, deal_id) values ( @id2, @id1 );
insert into products_deals (product_id, deal_id) values ( @id3, @id3 );
insert into products_deals (product_id, deal_id) values ( @id4, @id3 );
insert into products_deals (product_id, deal_id) values ( @id5, @id2 );
insert into products_deals (product_id, deal_id) values ( @id6, @id4 );

insert into cart_item (user_id, product_id, quantity) values ( @c0, @id1, 2 );
insert into cart_item (user_id, product_id, quantity) values ( @c0, @id3, 1 );

insert into cart_item (user_id, product_id, quantity) values ( @c1, @id2, 3 );
insert into cart_item (user_id, product_id, quantity) values ( @c1, @id4, 2 );

insert into cart_item (user_id, product_id, quantity) values ( @c2, @id5, 4 );
insert into cart_item (user_id, product_id, quantity) values ( @c2, @id6, 1 );

insert into cart_item (user_id, product_id, quantity) values ( @c3, @id5, 5 );
insert into cart_item (user_id, product_id, quantity) values ( @c3, @id6, 3 );
insert into cart_item (user_id, product_id, quantity) values ( @c3, @id7, 2 );

insert into cart_item (user_id, product_id, quantity) values ( @c4, @id7, 1 );
insert into cart_item (user_id, product_id, quantity) values ( @c4, @id8, 2 );
