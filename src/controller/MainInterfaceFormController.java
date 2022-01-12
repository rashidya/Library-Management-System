package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.*;
import view.tm.BorrowedBooksMainInterface;
import view.tm.MainInterfaceBook;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainInterfaceFormController {
    public BorderPane mainInterfaceContext;
    public AnchorPane loginFormContext;
    public Tab tbSearchBooks;
    public Tab tbMembershipAccount;
    public Tab tbLibraryMap;
    public Tab tbHome;

    //--------login-----------------
    public Label lblTime;
    public TextField txtUserName;
    public PasswordField txtPassword;
    public JFXButton btnLogin;
    public Label lblWarning;
    public Label lblDate;
    public TextField txtPasswordText;
    public ImageView imageShowPassword;
    public ImageView imageHidePassword;

    //------member------
    public TextField txtMembershipId;
    public TextField txtName;
    public TextField txtAge;
    public TextField txtAddress;
    public TextField txtContactNo;
    public TextField txtEmail;
    public TextField txtDateOfMembership;
    public Label lblInvalidId;
    public TableView<BorrowedBooksMainInterface> tblBorrowedBooks;
    public TableColumn colBookId;
    public TableColumn colTitle;
    public TableColumn colBorrowedDate;
    public TableColumn colDueDate;
    public TableColumn colOverDueFine;
    public Label lblMaxNoOfBooksBorrowed;
    public Label lblMaxNoOfDaysBorrowedBookCanBeKept;
    public Label lblNoOfTimesABookCanBeRenewed;
    public Label lblMaxNoOfDaysRenewedBookCanBeKept;
    public Label lblOverdueFine;
    public Label lblLostFine;
    public Label lblDamagedFine;
    public Label lblRegistrationFee;
    public Label lblMaxNoOfDaysAReservationIsAvailable_member;

    //---------Books--------
    public TableView tblBooks;
    public TableColumn colBookName;
    public TableColumn colAuthor;
    public TableColumn colAvailability;
    public TableColumn colRackNo;
    public TableColumn colISBN;
    public TextField txtAuthorSearch;
    public TextField txtBookNameSearch;
    public JFXCheckBox chkBoxCategory1;
    public JFXCheckBox chkBoxCategory7;
    public JFXCheckBox chkBoxCategory6;
    public JFXCheckBox chkBoxCategory5;
    public JFXCheckBox chkBoxCategory4;
    public JFXCheckBox chkBoxCategory2;
    public JFXCheckBox chkBoxCategory8;
    public JFXCheckBox chkBoxCategory12;
    public JFXCheckBox chkBoxCategory11;
    public JFXCheckBox chkBoxCategory9;
    public JFXCheckBox chkBoxCategory14;
    public JFXCheckBox chkBoxCategory13;
    public JFXCheckBox chkBoxCategory16;
    public JFXCheckBox chkBoxCategory15;
    public JFXCheckBox chkBoxCategory3;
    public JFXCheckBox chkBoxCategory17;
    public JFXCheckBox chkBoxCategory10;
    public JFXCheckBox chkBoxAvailable;
    ObservableList<Book> allBooks;
    ObservableList<MainInterfaceBook> books= FXCollections.observableArrayList();
    ObservableList<MainInterfaceBook> filteredBooks= FXCollections.observableArrayList();
    ObservableList<JFXCheckBox> category= FXCollections.observableArrayList();


    public void initialize(){

        setLocalDateAndTime();

        try {
            setRules();
            allBooks=new BookController().getAllBooks();
            setBookTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        category.addAll(chkBoxCategory1,chkBoxCategory2,chkBoxCategory3,chkBoxCategory4,chkBoxCategory5,chkBoxCategory6,chkBoxCategory7,chkBoxCategory8
        ,chkBoxCategory9,chkBoxCategory10,chkBoxCategory11,chkBoxCategory12,chkBoxCategory13,chkBoxCategory14,chkBoxCategory15,chkBoxCategory16,chkBoxCategory17);

        //------------------listeners tabs-----------------------
        tbSearchBooks.selectedProperty().addListener(observable -> {
            lblInvalidId.setVisible(false);
            txtMembershipId.clear();
            tblBorrowedBooks.setItems(FXCollections.observableArrayList());
            clearMemberDetails();
        });

        tbMembershipAccount.selectedProperty().addListener(observable -> {
            clearSearch();
        });

        tbHome.selectedProperty().addListener(observable -> {
            clearSearch();
            tblBorrowedBooks.setItems(FXCollections.observableArrayList());
            lblInvalidId.setVisible(false);
            txtMembershipId.clear();
            clearMemberDetails();
        });

        tbLibraryMap.selectedProperty().addListener(observable -> {
            tblBorrowedBooks.setItems(FXCollections.observableArrayList());
            lblInvalidId.setVisible(false);
            txtMembershipId.clear();
            clearMemberDetails();
            clearSearch();
        });

        //--------------------listeners login--------------------------------------------
        txtPasswordText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                txtPassword.setText(newValue);
            }
        });
        txtPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                txtPasswordText.setText(newValue);
            }
        });

        //-----------------listeners search book--------------------------------------------
        txtBookNameSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            books.clear();
            try {
                searchBook();
                filterBooks();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        });

        txtAuthorSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            books.clear();
            try {
                searchBook();
                filterBooks();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        });

        chkBoxAvailable.selectedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                filterBooks();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        for (JFXCheckBox jfxCheckBox : category) {
            jfxCheckBox.selectedProperty().addListener(observable -> {
                try {
                    filterBooks();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }

        //-------------Book table(Search Book)---------------------------------------------
        colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
        colRackNo.setCellValueFactory(new PropertyValueFactory<>("rackNo"));


        //----------borrowed book table (Member)----------------------------------
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bkItemId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colBorrowedDate.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colOverDueFine.setCellValueFactory(new PropertyValueFactory<>("overdueFine"));

    }

    private void setLocalDateAndTime() {
        // load Date
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        // load Time
        {
            Thread clock = new Thread() {
                public void run() {
                    try {
                        while (true) {
                            Platform.runLater(() -> {
                                lblTime.setText(new SimpleDateFormat("HH:mm a").format(new Date()));
                            });
                            sleep(1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            clock.start();

        }
    }

    //******************************* LOGIN  ***************************************************************

    public void logInOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        if(new UserAccountController().getUserAccounts()!=null){
            int size =new UserAccountController().getUserAccounts().size();
            for (UserAccount temp:new UserAccountController().getUserAccounts()
                 ) {
                    if (temp.getUsername().equals(txtUserName.getText())){
                        if(temp.getPassword().equals(txtPassword.getText())){
                           if (new UserController().getUserLevel(temp.getUsername()).equals("Admin")){
                               Stage window = (Stage) loginFormContext.getScene().getWindow();
                               window.centerOnScreen();
                               window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AdminForm.fxml"))));
                           }else {
                               Stage window = (Stage) loginFormContext.getScene().getWindow();
                               window.centerOnScreen();
                               window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LibrarianForm.fxml"))));
                           }
                            lblWarning.setText("*STAFF ONLY");
                        }else {
                            lblWarning.setText("*Incorrect Password");
                        }
                    }else {
                        size--;
                        if (size==0){lblWarning.setText("*No User Found");}
                        continue;
                    }
            }
        }
    }

    public void VisiblePassword(MouseEvent mouseEvent) {
        txtPassword.setVisible(false);
        imageShowPassword.setVisible(false);
        txtPasswordText.setVisible(true);
        imageHidePassword.setVisible(true);
    }

    public void HidePassword(MouseEvent mouseEvent) {
        txtPasswordText.setVisible(false);
        imageHidePassword.setVisible(false);
        txtPassword.setVisible(true);
        imageShowPassword.setVisible(true);
    }


    //******************************* Member***************************************************************

    public void viewAccountOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, ParseException {
        Member member =new MemberController().getMember(txtMembershipId.getText());
        if (member!=null){
            setMemberDetails(member);
        }else{
            clearMemberDetails();
            lblInvalidId.setVisible(true);
        }

    }

    private void setMemberDetails(Member member) throws SQLException, ParseException, ClassNotFoundException {
        txtName.setText(member.getName());
        txtAge.setText(String.valueOf(member.getAge()));
        txtAddress.setText(member.getAddress());
        txtContactNo.setText(member.getContactNo());
        txtEmail.setText(member.getEmail());
        txtDateOfMembership.setText(member.getDateOfMembership());
        lblInvalidId.setVisible(false);
        setTblBorrowedBooks_Member(txtMembershipId.getText());
    }

    public  void setTblBorrowedBooks_Member(String id) throws ParseException, SQLException, ClassNotFoundException {

        ObservableList<BorrowedBooksMainInterface> borrowedBooks =FXCollections.observableArrayList();

        for (BorrowedBookDetails temp:new BorrowedBookDetailsController().getBorrowedBooksOfAMember(id)
        ) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date firstDate = format.parse(temp.getDueDate());
            Date secondDate = format.parse(temp.getBorrowedDate());

            long difference = secondDate.getTime() - firstDate.getTime();
            TimeUnit time = TimeUnit.DAYS;
            long fineDays = time.convert(difference, TimeUnit.MILLISECONDS);

            double fine=0;
            if (fineDays>0){
                fine=fineDays * Integer.parseInt(new InformationController().getInformation("I-005").getData());
            }

            Book book = new BookController().getBook(new BookItemController().getBookItem(temp.getBkItemId()).getISBN());
            BorrowedBooksMainInterface borrowedBook=new BorrowedBooksMainInterface(temp.getBkItemId(),book.getTitle(),temp.getBorrowedDate(),temp.getDueDate(),fine);
            borrowedBooks.add(borrowedBook);
        }

        tblBorrowedBooks.setItems(borrowedBooks);
    }

    public void clearMemberDetails(){
        txtName.clear();
        txtAge.clear();
        txtAddress.clear();
        txtContactNo.clear();
        txtEmail.clear();
        txtDateOfMembership.clear();
        tblBorrowedBooks.getItems().clear();
        txtMembershipId.clear();
        lblInvalidId.setVisible(false);

    }

    private void setRules() throws SQLException, ClassNotFoundException {
        for (Information temp:new InformationController().getAllInformation()
        ) {
            if (temp.getId().equals("I-001")){lblMaxNoOfBooksBorrowed.setText(temp.getData());}
            if (temp.getId().equals("I-002")){lblMaxNoOfDaysBorrowedBookCanBeKept.setText(temp.getData());}
            if (temp.getId().equals("I-003")){lblMaxNoOfDaysRenewedBookCanBeKept.setText(temp.getData());}
            if (temp.getId().equals("I-004")){lblNoOfTimesABookCanBeRenewed.setText(temp.getData());}
            if (temp.getId().equals("I-005")){lblOverdueFine.setText(temp.getData());}
            if (temp.getId().equals("I-006")){lblLostFine.setText(temp.getData());}
            if (temp.getId().equals("I-007")){lblDamagedFine.setText(temp.getData());}
            if (temp.getId().equals("I-008")){lblMaxNoOfDaysAReservationIsAvailable_member.setText(temp.getData());}

        }
        lblRegistrationFee.setText(String.valueOf(new MembershipPaymentMasterController().getPaymentAmount("P-01")));

    }

    public void clearMemberOnAction_Member(ActionEvent actionEvent) {
        clearMemberDetails();
    }

    //**********************************Search Book************************************************************
    
    
    public void setBookTable() throws SQLException, ClassNotFoundException {

        for (Book book:allBooks
             ) {
            String avilability=null;
            String rackNo=null;
           L1: for (BookItem bookItem:new BookItemController().getBookItemsFromISBN(book.getISBN())
                 ) {
                    if (bookItem.getStatus().equals("Available")){
                        avilability="Available";
                        rackNo=String.valueOf(bookItem.getRackNo());
                        break L1;
                    }
                    avilability="Not Available";
                    rackNo="-";
            }
            books.add(new MainInterfaceBook(book.getISBN(),book.getTitle(),book.getAuthor(),avilability,rackNo));
        }
        tblBooks.setItems(books);
    }

    public void searchBook() throws SQLException, ClassNotFoundException {
        ObservableList<Book> searchedBooksFromTitle = new BookController().searchBookByName(txtBookNameSearch.getText());
        ObservableList<Book> searchedBooksFromAuthor = new BookController().searchBookByAuthor(txtAuthorSearch.getText());
        if (!txtBookNameSearch.getText().isEmpty()&&!txtAuthorSearch.getText().isEmpty()){
            for (Book bookTitle:searchedBooksFromTitle)
            {
                for (Book bookAuthor : searchedBooksFromAuthor) {
                    if (bookAuthor.getISBN().equals(bookTitle.getISBN())) {
                        addSearchedBook(bookTitle);
                    }
                }
            }
        }else if (!txtBookNameSearch.getText().isEmpty()){
            for (Book book : searchedBooksFromTitle) {
                addSearchedBook(book);
            }
        }else if (!txtAuthorSearch.getText().isEmpty()){
            for (Book book : searchedBooksFromAuthor) {
                addSearchedBook(book);
            }
        }else {
            setBookTable();
        }
    }

    public void addSearchedBook(Book book) throws SQLException, ClassNotFoundException {
        String avilability = null;
        String rackNo = null;
        L1:
        for (BookItem bookItem : new BookItemController().getBookItemsFromISBN(book.getISBN())
        ) {
            if (bookItem.getStatus().equals("Available")) {
                avilability = "Available";
                rackNo = String.valueOf(bookItem.getRackNo());
                break L1;
            }
            avilability = "Not Available";
            rackNo = "-";
        }
        this.books.add(new MainInterfaceBook(book.getISBN(), book.getTitle(), book.getAuthor(), avilability, rackNo));

    }

    public void filterBooks() throws SQLException, ClassNotFoundException {
        filteredBooks.clear();
        if (chkBoxAvailable.isSelected()) {
            //available
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getAvailability().equals("Available")) {
                    boolean categorySelected =false;
                    for (int k = 0; k < category.size(); k++) {
                        if (category.get(k).isSelected()) {
                            //available category selected
                            categorySelected=true;
                            if (new BookController().getBook(books.get(i).getISBN()).getCategory().equals(category.get(k).getText())) {
                                filteredBooks.add(books.get(i));
                            }
                        }
                    }
                    //available category not selected
                    if (categorySelected == false) {
                        filteredBooks.add(books.get(i));
                    }
                }

            }
        }else {
            //not available
            boolean categorySelected =false;
            for (int i = 0; i < books.size(); i++) {
                for (int k = 0; k < category.size(); k++) {
                    //not available category selected
                    if (category.get(k).isSelected()) {
                        categorySelected=true;
                        if (new BookController().getBook(books.get(i).getISBN()).getCategory().equals(category.get(k).getText())) {
                            filteredBooks.add(books.get(i));
                        }
                    }
                }
                //not available category not selected
                if (categorySelected==false){
                    filteredBooks.add(books.get(i));
                }

            }

        }

        tblBooks.setItems(filteredBooks);
    }

    private void clearSearch(){
        txtBookNameSearch.clear();
        txtAuthorSearch.clear();
        chkBoxCategory1.setSelected(false);
        chkBoxCategory2.setSelected(false);
        chkBoxCategory3.setSelected(false);
        chkBoxCategory4.setSelected(false);
        chkBoxCategory5.setSelected(false);
        chkBoxCategory6.setSelected(false);
        chkBoxCategory7.setSelected(false);
        chkBoxCategory8.setSelected(false);
        chkBoxCategory9.setSelected(false);
        chkBoxCategory10.setSelected(false);
        chkBoxCategory11.setSelected(false);
        chkBoxCategory12.setSelected(false);
        chkBoxCategory13.setSelected(false);
        chkBoxCategory14.setSelected(false);
        chkBoxCategory15.setSelected(false);
        chkBoxCategory16.setSelected(false);
        chkBoxCategory17.setSelected(false);
        chkBoxAvailable.setSelected(false);
    }





}
