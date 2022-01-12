package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberController {

    //create
    public boolean addMember(Member member){
        Connection con=null;

        try {
            con=DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement preparedStatement =con.prepareStatement("INSERT INTO Member VALUES (?,?,?,?,?,?,?)");
            preparedStatement.setObject(1,member.getId());
            preparedStatement.setObject(2,member.getName());
            preparedStatement.setObject(3,member.getAddress());
            preparedStatement.setObject(4,member.getContactNo());
            preparedStatement.setObject(5,member.getEmail());
            preparedStatement.setObject(6,member.getAge());
            preparedStatement.setObject(7,member.getDateOfMembership());

            if (preparedStatement.executeUpdate()>0){
                if (new MembershipPaymentController().addMembershipPayment(member.getMembershipPayments())){
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
    public String getMemberId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement("SELECT id FROM Member ORDER BY id DESC Limit 1").executeQuery();

        if (resultSet.next()) {
            int i = Integer.parseInt(resultSet.getString(1).split("-")[1]);
            i++;
            return (i<10)?"M-000"+i:(i<100)?"M-00"+i:(i<1000)?"M-0"+i:"M-"+i;

        }else {
            return "M-0001";
        }

    }

    public ObservableList<Member> getAllMembers() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Member ").executeQuery();
        ObservableList<Member> members = FXCollections.observableArrayList();

        while (resultSet.next()){
            members.add(new Member(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getInt(6),resultSet.getString(7)));
        }
        return members;

    }

//    public ObservableList<Member> getAllMembersAnnually(String year) throws SQLException, ClassNotFoundException {
//        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Member WHERE").executeQuery();
//        ObservableList<Member> members = FXCollections.observableArrayList();
//
//        while (resultSet.next()){
//            members.add(new Member(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getInt(6),resultSet.getString(7),resultSet.getString(8)));
//        }
//        return members;
//
//    }



    public Member getMember(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Member WHERE id=?");
        preparedStatement.setObject(1,id);


        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            return new Member(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getInt(6),resultSet.getString(7));
        }
        return null;

    }



    public ObservableList<Member> searchMember(String id,String name) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Member WHERE id LIKE ? || name LIKE ?");
        preparedStatement.setObject(1,id+"%");
        preparedStatement.setObject(2,"%"+name+"%");

        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<Member> searchedMembers = FXCollections.observableArrayList();

        while (resultSet.next()){
            searchedMembers.add(new Member(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getInt(6),resultSet.getString(7)));
        }
        return searchedMembers;


    }

    public ObservableList<Member> searchMemberById(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Member WHERE id LIKE ?");
        preparedStatement.setObject(1,id+"%");


        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<Member> searchedMembers = FXCollections.observableArrayList();

        while (resultSet.next()){
            searchedMembers.add(new Member(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getInt(6),resultSet.getString(7)));
        }
        return searchedMembers;


    }

    public ObservableList<Member> searchMemberByName(String name) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Member WHERE name LIKE ?");
        preparedStatement.setObject(1,"%"+name+"%");

        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<Member> searchedMembers = FXCollections.observableArrayList();

        while (resultSet.next()){
            searchedMembers.add(new Member(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getInt(6),resultSet.getString(7)));
        }
        return searchedMembers;


    }


    public int getTotalNoOfMembers() throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT COUNT(id) FROM Member");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public int getTotalNoOfMembersInAYear(int year) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT COUNT(id) FROM Member WHERE dateOfMembership BETWEEN ?AND ?");
        preparedStatement.setObject(1,year+"-01-01");
        preparedStatement.setObject(2,year+"-12-31");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public ObservableList<Member> getNewMembersInAYear(int year) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Member WHERE dateOfMembership BETWEEN ?AND ?");
        preparedStatement.setObject(1,year+"-01-01");
        preparedStatement.setObject(2,year+"-12-31");
        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<Member> members = FXCollections.observableArrayList();
        while (resultSet.next()){
            members.add(new Member(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getInt(6),resultSet.getString(7)));

        }
        return members;
    }

    public int getTotalNoOfMembersInAMonth(int year,String month) throws SQLException, ClassNotFoundException {
        int monthInt=Integer.parseInt(month);
        String day;

        if (year/4==0){
            day=(monthInt/2==1||monthInt==8) ? "31" : (monthInt==2) ? "29":"30";
        }else {
            day=(monthInt/2==1||monthInt==8) ? "31" : (monthInt==2) ? "28":"30";
        }
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT COUNT(id) FROM Member WHERE dateOfMembership BETWEEN ?AND ?");

        preparedStatement.setObject(1,year+"-"+month+"-01");
        preparedStatement.setObject(2,year+"-"+monthInt+"-"+day);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public ObservableList<Member> getNewMembersInAMonth(int year,String month) throws SQLException, ClassNotFoundException {
        int monthInt=Integer.parseInt(month);
        String day;

        if (year/4==0){
            day=(monthInt/2==1||monthInt==8) ? "31" : (monthInt==2) ? "29":"30";
        }else {
            day=(monthInt/2==1||monthInt==8) ? "31" : (monthInt==2) ? "28":"30";
        }
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Member WHERE dateOfMembership BETWEEN ?AND ?");

        preparedStatement.setObject(1,year+"-"+month+"-01");
        preparedStatement.setObject(2,year+"-"+monthInt+"-"+day);
        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<Member> members = FXCollections.observableArrayList();
        while (resultSet.next()){
            members.add(new Member(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getInt(6),resultSet.getString(7)));

        }
        return members;
    }

    public int getTotalNoOfMembersInADay(String day) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT COUNT(id) FROM Member WHERE dateOfMembership=?");
        preparedStatement.setObject(1,day);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public ObservableList<Member> getNewMembersInADay(String day) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Member WHERE dateOfMembership=?");
        preparedStatement.setObject(1,day);

        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<Member> members = FXCollections.observableArrayList();
        while (resultSet.next()){
            members.add(new Member(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getInt(6),resultSet.getString(7)));

        }
        return members;
    }




    //Update
    public boolean updateMember(Member member) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE Member SET name=?,age=?,address=?,contactNo=?,email=? WHERE id=?");
        preparedStatement.setObject(1,member.getName());
        preparedStatement.setObject(2,member.getAge());
        preparedStatement.setObject(3,member.getAddress());
        preparedStatement.setObject(4,member.getContactNo());
        preparedStatement.setObject(5,member.getEmail());
        preparedStatement.setObject(6,member.getId());

        return preparedStatement.executeUpdate()>0;
    }

    //delete
    public boolean cancelMembership(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM  Member  WHERE id=? ");
        preparedStatement.setObject(1,id);


        return preparedStatement.executeUpdate()>0;
    }



}
