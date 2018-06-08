/*
 *	The Java mapping of the SQL user-defined type "circle".
 *
 */

import java.sql.*;

public class Circle implements SQLData
{
    private static double PI	= 3.14159;

    /*
     *	JDBC 2.0 style SQL/Java object mapping
     */

    private double x;
    private double y;
    private double radius;

    private String type;

    public String getSQLTypeName() { return type; }

    public void readSQL (SQLInput stream, String typeName)
	throws SQLException
    {
	type = typeName;
	x = stream.readDouble();
	y = stream.readDouble();
	radius = stream.readDouble();
    }

    public void writeSQL (SQLOutput stream)
	throws SQLException
    {
	stream.writeDouble(x);
	stream.writeDouble(y);
	stream.writeDouble(radius);
    }

    /*	A Java UDR that returns the area of the circle.
     */
    public static double area(Circle c)
	{
	return PI * c.radius * c.radius;
	}

}
