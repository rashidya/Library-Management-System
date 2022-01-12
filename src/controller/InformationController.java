package controller;

import db.DBConnection;
import model.Information;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InformationController {

    public ArrayList<Information> getAllInformation() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Information").executeQuery();
        ArrayList<Information> information=new ArrayList<>();
        while (resultSet.next()){
            information.add(new Information(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)));
        }
        return information;
    }

    public Information getInformation(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Information WHERE id=?");
        preparedStatement.setObject(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return new Information(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3));
        }
        return null;
    }

    public boolean updateInfo(String id,String data) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE  Information SET data=? WHERE id=? ");
        preparedStatement.setObject(1,data);
        preparedStatement.setObject(2,id);

        return preparedStatement.executeUpdate()>0;

    }
}
