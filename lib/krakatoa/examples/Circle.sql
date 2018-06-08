drop database mydb;
create database mydb;

-- create the circle UDT

create opaque type circle (internallength = 24, alignment = 8);

create function circle_input(lvarchar) returns circle
     external name '$INFORMIXDIR/extend/krakatoa/examples/circle.so' 
	language c;
create function circle_output(circle) returns lvarchar
     external name '$INFORMIXDIR/extend/krakatoa/examples/circle.so' 
	language c;

create implicit cast (lvarchar as circle with circle_input);
create explicit cast (circle as lvarchar with circle_output);

-- create the circle UDT table

create table mytable (c circle);
insert into mytable values("1 1 1");
insert into mytable values("2 2 2");
insert into mytable values("3 3 3");
select * From mytable;


-- install the Java class Circle (customize the URL for your installation)

execute procedure install_jar(
   "file:$INFORMIXDIR/extend/krakatoa/examples/Circle.jar", "circle_jar", 0);

-- register the Java class that maps to the circle UDT

execute procedure setUDTExtName("circle", "Circle");

-- register the Java UDR 

create function area(circle) returns float
        external name 'circle_jar:Circle.area(Circle)'
        language java;

-- test the Java UDR

select c, area(c) from mytable;

