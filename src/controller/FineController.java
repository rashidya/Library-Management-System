package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Fine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FineController {

    public boolean payFine(Fine fine) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO Fine(date,amount,memberId,fineMasterId) VALUES(?,?,?,?)  ");
        preparedStatement.setObject(1,fine.getDate());
        preparedStatement.setObject(2,fine.getAmount());
        preparedStatement.setObject(3,fine.getMemberId());
        preparedStatement.setObject(4,fine.getFineMasterId());

        return preparedStatement.executeUpdate()>0;

    }


    public ObservableList<Fine> getAllFinePayments() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Fine").executeQuery();
        ObservableList<Fine> finePayments = FXCollections.observableArrayList();
        while (resultSet.next()){
            finePayments.add(new Fine(resultSet.getString(2),resultSet.getDouble(3),resultSet.getString(4),resultSet.getString(5)));
        }
        return finePayments;
    }

    public ObservableList<Fine> getAllFinePaymentsAnnually(int year) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Fine WHERE date BETWEEN ? AND ?");
        preparedStatement.setObject(1,year+"-01-01");
        preparedStatement.setObject(2,year+"-12-31");
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<Fine> finePayments = FXCollections.observableArrayList();
        while (resultSet.next()){
            finePayments.add(new Fine(resultSet.getString(2),resultSet.getDouble(3),resultSet.getString(4),resultSet.getString(5)));
        }
        return finePayments;
    }

    public ObservableList<Fine> getAllFinePaymentsMonthly(int year,String month) throws SQLException, ClassNotFoundException {
        int monthInt=Integer.parseInt(month);
        String day;

        if (year/4==0){
            day=(monthInt/2==1||monthInt==8) ? "31" : (monthInt==2) ? "29":"30";
        }else {
            day=(monthInt/2==1||monthInt==8) ? "31" : (monthInt==2) ? "28":"30";
        }

        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Fine WHERE date BETWEEN ? AND ?");
        preparedStatement.setObject(1,year+"-"+month+"-01");
        preparedStatement.setObject(2,year+"-"+month+"-"+day);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<Fine> finePayments = FXCollections.observableArrayList();
        while (resultSet.next()){
            finePayments.add(new Fine(resultSet.getString(2),resultSet.getDouble(3),resultSet.getString(4),resultSet.getString(5)));
        }
        return finePayments;
    }

    public ObservableList<Fine> getAllFinePaymentsDaily(String day) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Fine WHERE date=?");
        preparedStatement.setObject(1,day);

        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<Fine> finePayments = FXCollections.observableArrayList();
        while (resultSet.next()){
            finePayments.add(new Fine(resultSet.getString(2),resultSet.getDouble(3),resultSet.getString(4),resultSet.getString(5)));
        }
        return finePayments;
    }
}
