create user schema identified by password;
grant create session to schema;
grant create table, create view to schema; 
grant create procedure to schema;
grant create synonym, create trigger to schema;
grant unlimited tablespace to schema;

create user test identified by password;
grant all privileges to test;