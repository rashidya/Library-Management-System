package controller;

import db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRackController {


    public String getBookRackByCategory(String category) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT rackNo FROM BookRack WHERE category LIKE ?");
        preparedStatement.setObject(1,"%"+category+"%");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }
}
