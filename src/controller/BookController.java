package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Book;
import model.Member;
import view.tm.MainInterfaceBook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BookController {

    //create
    public boolean addBook(Book book){
        Connection con=null;

        try {
            con=DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO Book VALUES (?,?,?,?,?,?,?)");
            preparedStatement.setObject(1,book.getISBN());
            preparedStatement.setObject(2,book.getTitle());
            preparedStatement.setObject(3,book.getAuthor());
            preparedStatement.setObject(4,book.getCategory());
            preparedStatement.setObject(5,book.getIsReferenceOnly());
            preparedStatement.setObject(6,book.getLanguage());
            preparedStatement.setObject(7,book.getNoOfCopies());

            if (preparedStatement.executeUpdate()>0){
                if (new BookItemController().addBookItems(book.getBookItems())){
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

    //read
    public Book getBook(String ISBN) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Book WHERE ISBN=?");
        preparedStatement.setObject(1,ISBN);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return new Book(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getInt(7));
        }
        return null;
    }
    public ObservableList<Book> getAllBooks() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Book").executeQuery();
        ObservableList<Book> books= FXCollections.observableArrayList();
        while (resultSet.next()){
            books.add(new Book(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getInt(7)));
        }
        return books;
    }
    public ObservableList<String> getAllLanguages() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement("SELECT DISTINCT language FROM Book").executeQuery();
        ObservableList<String> languages= FXCollections.observableArrayList();
        while (resultSet.next()){
            languages.add(resultSet.getString(1));
        }
        return languages;
    }



    public ObservableList<Book> searchBookByName(String name) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Book WHERE title LIKE ?");
        preparedStatement.setObject(1,"%"+name+"%");


        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<Book> searchedBooks = FXCollections.observableArrayList();

        while (resultSet.next()){
            searchedBooks.add(new Book(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getInt(7)));
        }
        return searchedBooks;
    }
    public ObservableList<Book> searchBookByAuthor(String author) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Book WHERE author LIKE ?");
        preparedStatement.setObject(1,"%"+author+"%");


        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<Book> searchedBooks = FXCollections.observableArrayList();

        while (resultSet.next()){
            searchedBooks.add(new Book(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getInt(7)));
        }
        return searchedBooks;
    }

    public int getTotalNoOfBooksOfCategory(String category) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT SUM(noOfCopies) FROM Book WHERE category=?");
        preparedStatement.setObject(1,category);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }





    //update
    public boolean updateBook(Book book) {

        Connection con= null;
        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE Book SET ISBN=?,title=?,author=?,category=?,isReferenceOnly=?,language=?  WHERE ISBN=?");
            preparedStatement.setObject(1,book.getISBN());
            preparedStatement.setObject(2,book.getTitle());
            preparedStatement.setObject(3,book.getAuthor());
            preparedStatement.setObject(4,book.getCategory());
            preparedStatement.setObject(5,book.getIsReferenceOnly());
            preparedStatement.setObject(6,book.getLanguage());
            preparedStatement.setObject(7,book.getISBN());

            if(preparedStatement.executeUpdate()>0){
                if (new BookItemController().updateBookItem(book.getBookItems().get(0))){
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

    public boolean updateNoOfCopies(String ISBN,int noOfCopies) throws SQLException, ClassNotFoundException {
        if (noOfCopies!=0) {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE Book SET noOfCopies=? WHERE ISBN=?");
            preparedStatement.setObject(1,noOfCopies);
            preparedStatement.setObject(2,ISBN);
            return preparedStatement.executeUpdate()>0;
        }else {
           if (deleteBook(ISBN)){
               return true;
           }else {
               return false;
           }
        }

    }

    //delete
    public boolean deleteBook(String ISBN) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM Book WHERE ISBN=?");
        preparedStatement.setObject(1,ISBN);

        return preparedStatement.executeUpdate()>0;
    }
}
