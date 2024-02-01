/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cls;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;

/**
 *
 * @author R6100041-LE
 */
public class clsConnectafs {
    
    private Connection oConnect = null;
    private Statement sql_stat = null;
    private PreparedStatement sql_pre_stat = null;
    private ResultSet sql_rs = null;

    public clsConnectafs() throws ClassNotFoundException, SQLException {
        this.CrateConnection();
    }

    public clsConnectafs(String database_server, String database_name, String database_login, String database_pwd) throws ClassNotFoundException, SQLException {
        this.CrateConnection(database_server, database_name, database_login, database_pwd);
    }
    
    private String GetUrlConnection(String database_server, String database_host, String database_login, String database_pwd) {
        return "jdbc:postgresql://" + database_host + ":1527/" + database_server + ";user=" + database_login + ";password=" + database_pwd; 
       // return "jdbc:postgresql://hostname:port/dbname","username", "password");
    }

    private void CrateConnection() throws ClassNotFoundException, SQLException {
        try {
            ResourceBundle rb = new PropertyResourceBundle(new FileInputStream(getLocalDirName() + "/dbconfig.properties"));
            
            String db_server = rb.getString("posgre_url");
            String db_name = rb.getString("posgre_db");
            String db_user = rb.getString("posgre_user");
            String db_password = rb.getString("posgre_pass");
            
//            String db_server = "150.152.182.217";
//            String db_name = "alfresco";
//            String db_user = "alfresco";
//            String db_password = "welcome1";
           // String url = this.GetUrlConnection(db_server, db_name, db_user, db_password);
            Properties prop = new Properties();
            try {
            	
            	Class.forName("org.postgresql.Driver");
            	
                DriverManager.registerDriver(new org.postgresql.Driver());
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            try {
            	Class.forName("org.postgresql.Driver");     //Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            
            String url = "jdbc:postgresql://" + db_server  + ":5432/" +db_name + "";

            prop.setProperty("user", db_user);
            prop.setProperty("password", db_password);

            oConnect = DriverManager.getConnection(url, prop);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void CrateConnection(String database_server, String database_name, String database_login, String database_pwd) throws ClassNotFoundException, SQLException {
        try {
            String url = this.GetUrlConnection(database_server, database_name, database_login, database_pwd);

            try {
                DriverManager.registerDriver(new org.postgresql.Driver());
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            try {
                Class.forName("org.postgresql.Driver").newInstance();
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            oConnect = DriverManager.getConnection(url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return oConnect;
    }

    public Integer GetRowCount() {
        try {
            return sql_rs.getRow();
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean getNextRecord() {
        try {
            return sql_rs.next();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int ExecuteQuery(String sql_cmd) {
        int result = -1;

        try {
            sql_stat = oConnect.createStatement();
            result = sql_stat.executeUpdate(sql_cmd);
            Close();
        } catch (Exception ex) {
            Close();
            ex.printStackTrace();
        }

        return result;
    }

    public boolean getRecord(String sql_cmd) {
        try {
            sql_stat = oConnect.createStatement();
            sql_rs = sql_stat.executeQuery(sql_cmd);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return (sql_rs != null);
    }

    public ResultSet getResultSet(String sql_cmd) {
        ResultSet Rs = null;
        try {
            sql_stat = oConnect.createStatement();
            Rs = sql_stat.executeQuery(sql_cmd);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Rs;
    }

    public byte[] getByte(String field_name) {
        try {
            if (sql_rs.getBytes(field_name) != null) {
                return sql_rs.getBytes(field_name);
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Date getDate(String field_name) {
        try {
            if (sql_rs.getDate(field_name) != null) {
                return sql_rs.getDate(field_name);
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public double getDouble(String field_name) {
        try {
            return sql_rs.getDouble(field_name);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    public int getInteger(String field_name) {
        try {
            return sql_rs.getInt(field_name);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public String getString(String field_name) {
        try {
            if (sql_rs.getString(field_name) != null) {
                return sql_rs.getString(field_name);
            } else {
                return "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Date getTimeStamp(String field_name) {
        try {
            if (sql_rs.getString(field_name) != null) {
                return sql_rs.getTimestamp(field_name);
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public PreparedStatement BeginExecute(String sql_cmd) {
        try {
            sql_pre_stat = oConnect.prepareStatement(sql_cmd);
            return sql_pre_stat;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int EndExcute() {
        int result = -1;

        try {
            result = sql_pre_stat.executeUpdate();
        } catch (Exception ex) {
            result = -2;
            ex.printStackTrace();
        }

        return result;
    }

    public void Close() {
        try {
            sql_rs = null;
            sql_stat.close();
            oConnect.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

// <editor-fold defaultstate="collapsed" desc="Fuction">

    public String getClassName() {
        String thisClassName;

        //Build a string with executing class's name
        thisClassName = this.getClass().getName();
        thisClassName = thisClassName.substring(thisClassName.lastIndexOf(".") + 1, thisClassName.length());
        thisClassName += ".class";  //this is the name of the bytecode file that is executing

        return thisClassName;
    }

    public String getLocalDirName() {
        String localDirName;
        //Use that name to get a URL to the directory we are executing in
        //Open a URL to the our .class file
        java.net.URL myURL = this.getClass().getResource(getClassName());
        //Clean up the URL and make a String with absolute path name
        localDirName = myURL.getPath();  //Strip path to URL object out
        localDirName = myURL.getPath().replaceAll("%20", " ");  //change %20 chars to spaces
        //Get the current execution directory
        localDirName = localDirName.substring(0, localDirName.lastIndexOf("/"));  //clean off the file name

        return localDirName;
    }
    // </editor-fold>

    
}
