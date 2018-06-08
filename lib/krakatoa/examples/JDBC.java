import java.lang.*;
import java.sql.*;

/**
 *	An example UDR using the server-side JDBC driver.
 */

public class JDBC
{
    public static int sum1to10() throws SQLException
    {
        Connection myConn = null;
        String connURL = "jdbc:informix-direct:";
	int res = 0;
                                                         
       	try
        {
            // Loading JDBC Driver 

            Class.forName("com.informix.jdbc.IfxDriver");

            // Establishing a connection

            myConn = DriverManager.getConnection(connURL);

	    // create the test table

	    PreparedStatement pstmt = myConn.prepareStatement(
					"create table foo (i int)");
	    pstmt.executeUpdate();
	    pstmt.close();

	    // insert 10 rows using PreparedStatement

	    pstmt = myConn.prepareStatement("insert into foo values(?)");
	    for (int i = 1; i <= 10; ++ i)
		{
	    	pstmt.setInt(1, i);
	        pstmt.executeUpdate();
		}
	    pstmt.close();

	    // retrieve the rows using Statement and ResultSet

            Statement stmt = myConn.createStatement();
            ResultSet rs = stmt.executeQuery( "select i from foo");

            while (rs.next())
                {
                int intval = rs.getInt(1);
		res += intval;
		}
	    stmt.close();

        } catch (Exception e)
        {
            throw new SQLException(e.toString());
        }

	return res;
    };
}
