package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BookItem;
import model.ReservedBookDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReservedBookDetailsController {

    public ReservedBookDetails getReservation(String ISBN) throws SQLException, ClassNotFoundException {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM ReservedBookDetails WHERE ISBN=? && status=? ");
            preparedStatement.setObject(1,ISBN);
            preparedStatement.setObject(2,"Pending");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                return new ReservedBookDetails(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5));
            }
            return null;
    }
    public ReservedBookDetails getReservation(String ISBN,String memberId) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM ReservedBookDetails WHERE ISBN=? && memberId=? ");
        preparedStatement.setObject(1,ISBN);
        preparedStatement.setObject(2,memberId);


        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            return new ReservedBookDetails(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5));
        }else {
            return null;
        }
    }
    public ReservedBookDetails getReservation(String ISBN,String memberId,String dateOfReservation) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM ReservedBookDetails WHERE ISBN=? && memberId=? && dateOfReservation=?");
        preparedStatement.setObject(1,ISBN);
        preparedStatement.setObject(2,memberId);
        preparedStatement.setObject(3,dateOfReservation);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            return new ReservedBookDetails(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5));
        }else {
            return null;
        }
    }

    public ObservableList<ReservedBookDetails> searchReservation(String searchText) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM ReservedBookDetails WHERE memberId LIKE ?");
        preparedStatement.setObject(1,"%"+searchText+"%");
        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<ReservedBookDetails> reservedBookDetails =FXCollections.observableArrayList();
        while (resultSet.next()){
            reservedBookDetails.add(new ReservedBookDetails(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5)));
        }
        return reservedBookDetails;
    }

    public boolean setReservationAvailable(String ISBN) throws SQLException, ClassNotFoundException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(cal.getTime());
        int days=Integer.parseInt(new InformationController().getInformation("I-008").getData());
        cal.add(Calendar.DATE,days);
        String availableTill=format.format(cal.getTime());
        ReservedBookDetails reservation = getReservation(ISBN);
        System.out.println(reservation.getMemberId());
            PreparedStatement preparedStatement1 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE ReservedBookDetails SET ISBN=?,memberId=?,dateOfReservation=?,status=? ,availableTill=? WHERE ISBN=? && memberId=?");
            preparedStatement1.setObject(1,reservation.getISBN());
            preparedStatement1.setObject(2,reservation.getMemberId());
            preparedStatement1.setObject(3,reservation.getDateOfReservation());
            preparedStatement1.setObject(4,"Available");
            preparedStatement1.setObject(5,availableTill);
            preparedStatement1.setObject(6,reservation.getISBN());
            preparedStatement1.setObject(7,reservation.getMemberId());
            return preparedStatement1.executeUpdate()>0;
    }

    public boolean reserveBook(ReservedBookDetails book) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO ReservedBookDetails VALUES (?,?,?,?,?)");
        preparedStatement.setObject(1,book.getISBN());
        preparedStatement.setObject(2,book.getMemberId());
        preparedStatement.setObject(3,book.getDateOfReservation());
        preparedStatement.setObject(4,book.getStatus());
        preparedStatement.setObject(5,book.getAvailableTill());

        return preparedStatement.executeUpdate()>0;
    }

    public boolean deleteReservation(ReservedBookDetails reservedBookDetails){
        Connection con= null;
        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM ReservedBookDetails WHERE ISBN=? && memberId=?");
            preparedStatement.setObject(1,reservedBookDetails.getISBN());
            preparedStatement.setObject(2,reservedBookDetails.getMemberId());


            if(preparedStatement.executeUpdate()>0){
                ReservedBookDetails reservation = getReservation(reservedBookDetails.getISBN());
                if (reservedBookDetails.getStatus().equals("Available")){
                    if (reservation!=null){
                        if (setReservationAvailable(reservedBookDetails.getISBN())){
                            con.commit();
                            return true;
                        }else {
                            con.rollback();
                            return false;
                        }
                    }else {
                        for (BookItem bookItem : new BookItemController().getBookItemsFromISBN(reservedBookDetails.getISBN())) {
                            if (bookItem.getStatus().equals("Reserved")){
                                if (new BookItemController().updateBookItemStatus(bookItem.getBkItemId(),"Available")){
                                    con.commit();
                                    return true;
                                }else {
                                    con.rollback();
                                    return false;
                                }
                            }
                        }

                    }
                }else {
                    con.commit();
                    return true;
                }

            }else {
                con.rollback();
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ObservableList<ReservedBookDetails>  getAllReservations() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM ReservedBookDetails").executeQuery();
        ObservableList<ReservedBookDetails> reservedBooks = FXCollections.observableArrayList();

        while (resultSet.next()){
            reservedBooks.add(new ReservedBookDetails(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5)));
        }
            return reservedBooks;

    }



}
