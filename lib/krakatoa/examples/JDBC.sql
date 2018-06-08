drop database mydb;
create database mydb;

-- install the Java UDR jar file (customize the URL for your installation)

execute procedure install_jar(
	"file:$INFORMIXDIR/extend/krakatoa/examples/JDBC.jar", "jdbc_jar", 0);

-- register the Java UDRs

create function sum1to10() returns int
        external name 'jdbc_jar:JDBC.sum1to10()'
        language java;

-- test the Java UDR (should get back 55)

execute function sum1to10();

