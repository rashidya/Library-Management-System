package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {


    //create
    public boolean addUser(User user) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO User VALUES (?,?,?,?,?,?,?)");
        preparedStatement.setObject(1,user.getId());
        preparedStatement.setObject(2,user.getName());
        preparedStatement.setObject(3,user.getContactNo());
        preparedStatement.setObject(4,user.getEmail());
        preparedStatement.setObject(5,user.getJobTitle());
        preparedStatement.setObject(6,user.getSecurityLevel());
        preparedStatement.setObject(7,user.getUsername());

        return preparedStatement.executeUpdate()>0;

    }

    //read
    public String getUserLevel(String username) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM User WHERE userName=?");
        preparedStatement.setObject(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getString(6);
        }
        return null;
    }

    public String getUserId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement("SELECT id FROM User ORDER BY id DESC Limit 1").executeQuery();

        if (resultSet.next()) {
            int i = Integer.parseInt(resultSet.getString(1).split("-")[1]);
            i++;
            return (i<10)?"U-00"+i:(i<100)?"U-0"+i:"U-"+i;

        }else {
            return "U-001";
        }

    }

    public ObservableList<User> getUsers() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM User").executeQuery();
        ObservableList<User> userObservableList= FXCollections.observableArrayList();
        while (resultSet.next()){
            userObservableList.add(new User(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getString(7)));
        }
        return userObservableList;
    }

    //update
    public boolean UpdateUser(User user) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE User SET name=?,contactNo=?,email=?,jobTitle=?,securityLevel=?,userName=? WHERE id =?");
        preparedStatement.setObject(1,user.getName());
        preparedStatement.setObject(2,user.getContactNo());
        preparedStatement.setObject(3,user.getEmail());
        preparedStatement.setObject(4,user.getJobTitle());
        preparedStatement.setObject(5,user.getSecurityLevel());
        preparedStatement.setObject(6,user.getUsername());
        preparedStatement.setObject(7,user.getId());

        return preparedStatement.executeUpdate()>0;

    }
}
