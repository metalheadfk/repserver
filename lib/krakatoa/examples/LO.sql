drop database mydb;
create database mydb;

-- install the Java UDR jar file (customize the URL for your installation)

execute procedure install_jar(
	"file:$INFORMIXDIR/extend/krakatoa/examples/LO.jar", "lo_jar", 0);

-- register the Java UDRs

create function lo() returns clob external name 'lo_jar:LO.lo()'
        language java;

-- test the Java UDRs 

create table mytable (c clob);

execute function lo();

drop table mytable;


