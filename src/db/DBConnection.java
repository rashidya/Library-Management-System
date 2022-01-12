package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection DBConnection=null;
    private static Connection connection;

    private DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManagementSystem",
                "root",
                "1234");

    }

    public static DBConnection getInstance() throws SQLException, ClassNotFoundException {
        if (DBConnection==null){
            DBConnection=new DBConnection();
        }
        return DBConnection;
    }

    public Connection getConnection(){
        return connection;
    }
}
