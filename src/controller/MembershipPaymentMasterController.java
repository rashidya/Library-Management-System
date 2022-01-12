package controller;

import db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MembershipPaymentMasterController {


    public String getPaymentType(String paymentId) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM PaymentMaster WHERE paymentId=?");
        preparedStatement.setObject(1,paymentId);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getString(2);
        }
        return null;
    }

    public double getPaymentAmount(String paymentId) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM PaymentMaster WHERE paymentId=?");
        preparedStatement.setObject(1,paymentId);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getDouble(3);
        }
        return 0;
    }
}
