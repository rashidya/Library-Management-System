package controller;

import db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FineMasterController {
    public String getFineType(String fineMasterId) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT fineType FROM FineMaster WHERE fineMasterId=?");
        preparedStatement.setObject(1,fineMasterId);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }

}
