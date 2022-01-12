package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BookItem;
import model.BorrowedBookDetails;
import model.Fine;
import model.ReservedBookDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BorrowedBookDetailsController {

    //create
    public boolean addBorrowedBook(BorrowedBookDetails bookDetails,String status){
        Connection con=null;

        try {
            con=DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO BorrowedBookDetails VALUES (?,?,?,?,?,?)");
            preparedStatement.setObject(1,bookDetails.getBkItemId());
            preparedStatement.setObject(2,bookDetails.getMemberId());
            preparedStatement.setObject(3,bookDetails.getBorrowedDate());
            preparedStatement.setObject(4,bookDetails.getDueDate());
            preparedStatement.setObject(5,bookDetails.getStatus());
            preparedStatement.setObject(6,bookDetails.getRenewedTimes());

            if (preparedStatement.executeUpdate()>0){
                BookItem bookItem = new BookItemController().getBookItem(bookDetails.getBkItemId());
                BookItem updateItem=new BookItem(bookItem.getBkItemId(),bookDetails.getStatus(),bookItem.getNoOfPages(),bookItem.getPrice(),bookItem.getISBN(),bookItem.getRackNo(),bookItem.getDate());
                if (new BookItemController().updateBookItem(updateItem)){
                    if (status.equals("Reserved")){
                        ReservedBookDetails reservation = new ReservedBookDetailsController().getReservation(bookItem.getISBN(), bookDetails.getMemberId());
                        if (new ReservedBookDetailsController().deleteReservation(reservation)){
                            con.commit();
                            return true;
                        }else {
                            con.rollback();
                            return false;
                        }
                    }
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
    public ObservableList<BorrowedBookDetails> getBorrowedBooksOfAMember(String memberId) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM BorrowedBookDetails WHERE memberId=?");
        preparedStatement.setObject(1,memberId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<BorrowedBookDetails> borrowedBookDetails= FXCollections.observableArrayList();
        while (resultSet.next()){
            borrowedBookDetails.add(new BorrowedBookDetails(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getInt(6)));
        }
        return borrowedBookDetails;
    }
    public ArrayList<BorrowedBookDetails> getAllBorrowedBooks(){return null;}


    //update
    public boolean updateBorrowedBook(BorrowedBookDetails bookDetails) {
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE BorrowedBookDetails SET bkItemId=?,memberId=?,borrowedDate=?,dueDate=?,status=?,renewedTimes=? WHERE bkItemId=? && memberId=?");
            preparedStatement.setObject(1,bookDetails.getBkItemId());
            preparedStatement.setObject(2,bookDetails.getMemberId());
            preparedStatement.setObject(3,bookDetails.getBorrowedDate());
            preparedStatement.setObject(4,bookDetails.getDueDate());
            preparedStatement.setObject(5,bookDetails.getStatus());
            preparedStatement.setObject(6,bookDetails.getRenewedTimes());
            preparedStatement.setObject(7,bookDetails.getBkItemId());
            preparedStatement.setObject(8,bookDetails.getMemberId());


            if (preparedStatement.executeUpdate()>0){
                BookItem bookItem = new BookItemController().getBookItem(bookDetails.getBkItemId());
                BookItem updateItem=new BookItem(bookItem.getBkItemId(),bookDetails.getStatus(),bookItem.getNoOfPages(),bookItem.getPrice(),bookItem.getISBN(),bookItem.getRackNo(),bookItem.getDate());
                if (new BookItemController().updateBookItem(updateItem)){
                    con.commit();
                    return true;
                }else {
                    con.rollback();
                    return false;
                }
            }else{
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

    //delete
    public boolean returnBorrowedBook(BorrowedBookDetails returnBook, ArrayList<Fine> fines){

        Connection con= null;
        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM BorrowedBookDetails WHERE bkItemId=? && memberId=? && borrowedDate=?");
            preparedStatement.setObject(1,returnBook.getBkItemId());
            preparedStatement.setObject(2,returnBook.getMemberId());
            preparedStatement.setObject(3,returnBook.getBorrowedDate());

            if (preparedStatement.executeUpdate()>0){
                for (Fine fine:fines
                ) {
                    if (fines!=null) {
                        if (new FineController().payFine(fine)) {
                            continue;
                        } else {
                            con.rollback();
                            return false;
                        }
                    }
                }
                if (new BookItemController().updateBookItemStatus(returnBook.getBkItemId(),returnBook.getStatus())){
                    if (returnBook.getStatus().equals("Reserved")){
                        if (new ReservedBookDetailsController().setReservationAvailable(new BookItemController().getBookItem(returnBook.getBkItemId()).getISBN())){
                            con.commit();
                            return true;
                        }else{
                            con.rollback();
                            return false;
                        }
                    }else {
                        con.commit();
                        return true;
                    }
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
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
        return false;

    }
}
