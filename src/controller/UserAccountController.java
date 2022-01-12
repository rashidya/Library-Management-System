package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import model.UserAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAccountController {

    //create
    public boolean addUserAccount(UserAccount userAccount){
        Connection con= null;

        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO UserAccount VALUES (?,?)");
            preparedStatement.setObject(1,userAccount.getUsername());
            preparedStatement.setObject(2,userAccount.getPassword());

            if (preparedStatement.executeUpdate()>0){
                if(new UserController().addUser(userAccount.getUser())){
                    con.commit();
                    return true;
                }else {
                    con.rollback();
                    return false;
                }
            }else {
                con.rollback();
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;

    }

    //read
    public ObservableList<UserAccount> getUserAccounts() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM UserAccount").executeQuery();
        ObservableList<UserAccount> userAccounts = FXCollections.observableArrayList();
        while (resultSet.next()){
            userAccounts.add(new UserAccount(resultSet.getString(1),resultSet.getString(2)));
        }
        return userAccounts;
    }

    public String getPassword(String userName) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM UserAccount WHERE userName=?");
        preparedStatement.setObject(1,userName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getString(2);
        }
        return null;
    }

    //update
    public boolean updateUserAccount(UserAccount userAccount) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE UserAccount SET password=? WHERE userName=?");
        preparedStatement.setObject(1,userAccount.getPassword());
        preparedStatement.setObject(2,userAccount.getUsername());

        return preparedStatement.executeUpdate()>0;

    }

    //delete
    public boolean deleteUserAccount(String userName) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM UserAccount WHERE userName=?");
        preparedStatement.setObject(1,userName);

        return preparedStatement.executeUpdate()>0;

    }


}
