package project;

import java.sql.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class sqlconnection{
    Connection con;
    Statement st;
    ResultSet res;
    ResultSetMetaData md;
    public sqlconnection() throws Exception{
        con=DriverManager.getConnection("jdbc:mysql://localhost:3306/wool","root","SSS!A!55");
        st=con.createStatement();
    }
}
