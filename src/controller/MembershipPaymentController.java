package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.MembershipPayments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MembershipPaymentController {

    //create
    public boolean addMembershipPayment(MembershipPayments membershipPayments) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO MembershipPayments VALUES (?,?,?,?)");
        preparedStatement.setObject(1,membershipPayments.getId());
        preparedStatement.setObject(2,membershipPayments.getDate());
        preparedStatement.setObject(3,membershipPayments.getMemberId());
        preparedStatement.setObject(4,membershipPayments.getPaymentId());

        return preparedStatement.executeUpdate()>0;
    }

    //read
    public String getMembershipPaymentId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement("SELECT id FROM MembershipPayments ORDER BY id DESC Limit 1").executeQuery();

        if (resultSet.next()) {
            int i = Integer.parseInt(resultSet.getString(1).split("-")[1]);
            i++;
            return (i<10)?"P-000"+i:(i<100)?"P-00"+i:(i<1000)?"P-0"+i:"P-"+i;

        }else {
            return "P-0001";
        }

    }


    public ObservableList<MembershipPayments> getAllPayments() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM MembershipPayments").executeQuery();
        ObservableList<MembershipPayments> payments =FXCollections.observableArrayList();
        while (resultSet.next()){
            payments.add(new MembershipPayments(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
        }
        return payments;
    }

    public ObservableList<MembershipPayments> getAllPaymentsAnnually(int year) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM MembershipPayments WHERE date BETWEEN ? AND ?");
        preparedStatement.setObject(1,year+"-01-01");
        preparedStatement.setObject(2,year+"-12-31");
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<MembershipPayments> payments =FXCollections.observableArrayList();
        while (resultSet.next()){
            payments.add(new MembershipPayments(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
        }
        return payments;
    }

    public ObservableList<MembershipPayments> getAllPaymentsMonthly(int year,String month) throws SQLException, ClassNotFoundException {
        int monthInt=Integer.parseInt(month);
        String day;

        if (year/4==0){
            day=(monthInt/2==1||monthInt==8) ? "31" : (monthInt==2) ? "29":"30";
        }else {
            day=(monthInt/2==1||monthInt==8) ? "31" : (monthInt==2) ? "28":"30";
        }

        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM MembershipPayments WHERE date BETWEEN ? AND ?");
        preparedStatement.setObject(1,year+"-"+month+"-01");
        preparedStatement.setObject(2,year+"-"+month+"-"+day);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<MembershipPayments> payments =FXCollections.observableArrayList();
        while (resultSet.next()){
            payments.add(new MembershipPayments(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
        }
        return payments;
    }

    public ObservableList<MembershipPayments> getAllPaymentsDaily(String day) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM MembershipPayments WHERE date=?");
        preparedStatement.setObject(1,day);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<MembershipPayments> payments =FXCollections.observableArrayList();
        while (resultSet.next()){
            payments.add(new MembershipPayments(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
        }
        return payments;
    }
}
