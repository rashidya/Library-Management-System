package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Book;
import model.BookItem;
import view.tm.AdminBooks_Books;

import java.sql.*;
import java.util.ArrayList;

public class BookItemController {

    //create
    public boolean addBookItems(ArrayList<BookItem> bookItems) {

        for (BookItem temp : bookItems
        ) {
            Connection con = null;
            try {
                con = DBConnection.getInstance().getConnection();

                PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO BookItem VALUES (?,?,?,?,?,?,?)");
                preparedStatement.setObject(1, temp.getBkItemId());
                preparedStatement.setObject(2, temp.getStatus());
                preparedStatement.setObject(3, temp.getNoOfPages());
                preparedStatement.setObject(4, temp.getPrice());
                preparedStatement.setObject(5, temp.getISBN());
                preparedStatement.setObject(6, temp.getRackNo());
                preparedStatement.setObject(7, temp.getDate());
                System.out.println(temp.getISBN());
                if (preparedStatement.executeUpdate() > 0) {
                    int copies = new BookController().getBook(temp.getISBN()).getNoOfCopies() + 1;
                    if (new BookController().updateNoOfCopies(temp.getISBN(), copies)) {
                        continue;
                    } else {
                        con.rollback();
                        return false;
                    }
                } else {
                    con.rollback();
                    return false;
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    //read
    public int getBookItemId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement("SELECT bkItemId FROM BookItem ORDER BY bkItemId DESC Limit 1").executeQuery();

        if (resultSet.next()) {

            return Integer.parseInt(resultSet.getString(1).split("-")[1]);

        }else {
            return 0;
        }
    }

    public BookItem getBookItem(String bkItemId) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM BookItem WHERE bkItemId=?");
        preparedStatement.setObject(1,bkItemId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return new BookItem(resultSet.getString(1),resultSet.getString(2),resultSet.getInt(3),resultSet.getDouble(4),resultSet.getString(5),resultSet.getInt(6),resultSet.getString(7));
        }
        return null;
    }

    public ObservableList<AdminBooks_Books> getAllBooksItems() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM BookItem").executeQuery();
        ObservableList<AdminBooks_Books> bookItems = FXCollections.observableArrayList();
        while (resultSet.next()){
            Book book=new BookController().getBook(resultSet.getString(5));
            AdminBooks_Books item =new AdminBooks_Books(resultSet.getString(5),resultSet.getString(1),book.getTitle(),book.getAuthor(),book.getCategory(),book.getLanguage(),resultSet.getString(2),resultSet.getInt(6));
            bookItems.add(item);
        }
        return bookItems;
    }

    public int getNoOfBookItemsFromABook(String ISBN) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT COUNT(bkItemId) FROM BookItem WHERE ISBN=?");
        preparedStatement.setObject(1,ISBN);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public int getTotalNoOfBooks() throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT COUNT(bkItemId) FROM BookItem ");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }


    public int getTotalNoOfBooksByStatus(String status) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT COUNT(bkItemId) FROM BookItem WHERE status=?");
        preparedStatement.setObject(1,status);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public ObservableList<AdminBooks_Books> searchBookByISBN(String ISBN) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM BookItem WHERE ISBN LIKE ?");
        preparedStatement.setObject(1,"%"+ISBN+"%");


        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<AdminBooks_Books> searchedBooks = FXCollections.observableArrayList();

        while (resultSet.next()){
            Book book=new BookController().getBook(resultSet.getString(5));
            AdminBooks_Books item =new AdminBooks_Books(resultSet.getString(5),resultSet.getString(1),book.getTitle(),book.getAuthor(),book.getCategory(),book.getLanguage(),resultSet.getString(2),resultSet.getInt(6));
            searchedBooks.add(item);
        }
        return searchedBooks;
    }

    //update
    public boolean updateBookItem(BookItem bookItem) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE BookItem SET noOfPages=?,RackNo=?,status=?,price=?  WHERE bkItemId=?");
        preparedStatement.setObject(1,bookItem.getNoOfPages());
        preparedStatement.setObject(2,bookItem.getRackNo());
        preparedStatement.setObject(3,bookItem.getStatus());
        preparedStatement.setObject(4,bookItem.getPrice());
        preparedStatement.setObject(5,bookItem.getBkItemId());

        return preparedStatement.executeUpdate()>0;
    }

    public boolean updateBookItemStatus(String bkItemId,String status) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE BookItem SET status=? WHERE bkItemId=?");
        preparedStatement.setObject(1,status);
        preparedStatement.setObject(2,bkItemId);

        return preparedStatement.executeUpdate()>0;
    }

    //delete
    public boolean deleteBookItem(AdminBooks_Books bkItem) {
        Connection con=null;

        try {
            con=DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM BookItem WHERE bkItemId=?");
            preparedStatement.setObject(1,bkItem.getBkId());
            if (preparedStatement.executeUpdate()>0){
                int copies=new BookController().getBook(bkItem.getISBN()).getNoOfCopies()-1;
                if (new BookController().updateNoOfCopies(bkItem.getISBN(),copies)){
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

    public ObservableList<BookItem> getBookItemsFromISBN(String isbn) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM BookItem WHERE ISBN=?");
        preparedStatement.setObject(1,isbn);
        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<BookItem> bookItems = FXCollections.observableArrayList();

        while (resultSet.next()){
            BookItem item =new BookItem(resultSet.getString(1),resultSet.getString(2),resultSet.getInt(3),resultSet.getDouble(4),resultSet.getString(5),resultSet.getInt(6),resultSet.getString(7));
            bookItems.add(item);
        }
        return bookItems;
    }
}
