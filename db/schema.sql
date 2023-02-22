create user schema identified by password;
grant create session to schema;
grant create table, create view to schema; 
grant create procedure to schema;
grant create synonym, create trigger to schema;
grant unlimited tablespace to schema;

create user test identified by password;
grant create session to test;
grant create table, create view to test;
grant create procedure to test;
grant create synonym, create trigger to test;
grant unlimited tablespace to test;