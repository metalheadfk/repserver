import java.lang.*;
import java.io.*;
import java.sql.*;
import com.informix.jdbc.*;
import com.informix.udr.*;


/**
 *	Examples of accessing large objects using JDBC. 
 *
 */

public class LO
{
    /**
     *  Make a Clob in a table and return it. 
     */
    public static Clob lo() throws SQLException
    {
        Connection myConn = null;
        String connURL = "jdbc:informix-direct:";

        try
        {
	    // Loading the JDBC driver 

            Class.forName("com.informix.jdbc.IfxDriver");

	    // Establising the connection

            myConn = DriverManager.getConnection(connURL);

	    // create a LO table

	    PreparedStatement pstmt = myConn.prepareStatement(
		"delete from mytable");
	    pstmt.executeUpdate();
	    pstmt.close();

	    //	insert a new CLOB object made from a String

	    pstmt = myConn.prepareStatement( "insert into mytable values(?)");
	    String inp = "This was a String -- now it's a large object";
	    byte ba[] = inp.getBytes();
	    InputStream is = new ByteArrayInputStream(ba);

	    pstmt.setAsciiStream(1, is, ba.length);
	    pstmt.executeUpdate();	
	    pstmt.close();

	    // retrieve the LO from the table and return it.
	
	    Statement stmt = myConn.createStatement();
            ResultSet rs = stmt.executeQuery("select c from mytable");
	    rs.next();

	    // notice the use of ResultSet2 -- this is a JDBC2.0 version
	    // of the ResultSet interface
            Clob c = ((ResultSet2)rs).getClob(1);

	    stmt.close();

	    return (Clob)c;
	}
	catch (Exception ex)
	{
	    throw new SQLException(ex.toString());
	}
    }
}
