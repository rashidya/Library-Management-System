package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import view.tm.BorrowedBooks;
import view.tm.Reservation;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class LibrarianFormController {

    public AnchorPane librarianFormContext;
    public JFXButton btnLogOut;
    public Label lblDate;
    public Label lblTime;
    public Tab tbMember;
    public Tab tbIssueRenewBook;
    public Tab tbReturnBook;
    public Tab tbReserveBook;

    //----------------Member----------
    public Label lblId_Member;
    public JFXButton btnRegisterOrUpdate;
    public TextField txtName_Member;
    public TextField txtAddress_Member;
    public TextField txtContactNo_Member;
    public TextField txtEmail_Member;
    public TextField txtAge_Member;
    public TextField txtDateOfMemberShip_Member;
    public JFXCheckBox chkBoxRegistrationFee_Member;
    public TableView<Member> tblMembers_Member;
    public TableColumn colId_member;
    public TableColumn colName_Member;
    public TableColumn colAddress_Member;
    public TableColumn colContact_Member;
    public TableColumn colEmail_Member;
    public TableColumn colDateOfMembership_Member;
    public TableColumn colAge_Member;
    public TextField txtIdSearch_Member;
    public TextField txtNameSearch_Member;
    public ImageView imgPrint_Member;
    public JFXButton btnPrint;
    public JFXButton btnDelete_Member;
    private Member newRegisteredMember;

//----------------Issue Books-----------------------
    public TextField txtName_Issue;
    public TextField txtAddress_Issue;
    public TextField txtContact_Issue;
    public TextField txtEmail_Issue;
    public TextField txtBookId_Issue;
    public TextField txtMemberId_Issue;
    public TextField txtAge_Issue;
    public TableView<BorrowedBooks> tblBorrowedBooks_Issue;
    public TableColumn colBookId_Issue;
    public TableColumn colBookName_Issue;
    public TableColumn colAuthor_Issue;
    public TableColumn colBorrowedDate_Issue;
    public TableColumn colDueDate_Issue;
    public JFXCheckBox chkBoxRefOnly_Issue;
    public TextField txtStatus_Issue;
    public TextField txtAuthor_Issue;
    public TextField txtBookName_Issue;
    public TextField txtISBN_Issue;
    public TextField txtDueDate_Issue;
    public TextField txtBorrowedDate_Issue;
    public TextField txtDateOfMembership_Issue;

    //---------Return Book------------------
    public JFXButton btnIssueOrRenew;
    public TextField txtMemberId_ReturnBook;
    public TableView<BorrowedBooks> tblBorrowedBooks_ReturnBook;
    public TableColumn colBookId_ReturnBook;
    public TableColumn colBookName_ReturnBook;
    public TableColumn colAuthor_returnBook;
    public TableColumn colBorrowedDate_ReturnBook;
    public TableColumn colDueDate_ReturnBook;
    public TextField txtISBN_ReturnBook;
    public TextField txtName_ReturnBook;
    public TextField txtAddress_ReturnBook;
    public TextField txtContact_ReturnBook;
    public TextField txtAge_ReturnBook;
    public TextField txtEmail_ReturnBook;
    public TextField txtStatus_ReturnBook;
    public TextField txtAuthor_ReturnBook;
    public TextField txtBookName_ReturnBook;
    public TextField txtReturnDate_ReturnBook;
    public TextField txtBookIdReturnBook_ReturnBook;
    public TextField txtMemberIdReturnBook_ReturnBook;
    public TextField txtDueDate_ReturnBook;
    public TextField txtBorrowDate_ReturnBook;
    public TextField txtOverDueFine_returnBook;
    public JFXCheckBox chkBoxOverDue_ReturnBook;
    public JFXRadioButton rdBtnLost_ReturnBook;
    public JFXRadioButton rdBtnDamaged_ReturnBook;
    public TextField txtLostFine_returnBook;
    public TextField txtDamagedFine_returnBook;
    public TextField txtPrice_ReturnBook;
    public TextField txtDateOfMembership_ReturnBook;

    //---------Reserve Book-----------
    public TextField txtMemberId_Reserve;
    public TextField txtISBN_ReserveBook;
    public TextField txtName_Reserve;
    public TextField txtAddress_Reserve;
    public TextField txtContact_Reserve;
    public TextField txtEmail_Reserve;
    public TextField txtDate_ReserveBook;
    public TextField txtStatus_Reserve;
    public TextField txtAuthor_Reserve;
    public TextField txtBookName_Reserve;
    public JFXCheckBox chkBoxRefOnly_Reserve;
    public TableView<ReservedBookDetails> tblReservations;
    public TableColumn colISBN_reserve;
    public TableColumn colMemberId_Reserve;
    public TableColumn colReservationDate_Reserve;
    public TableColumn colStatus_Reserve;
    public TableColumn colAvailableTill_Reserve;
    public JFXRadioButton rdBtnAll_Reserve;
    public JFXRadioButton rdBtnAvailable_reserve;
    public JFXRadioButton rdBtnPending_Reserve;
    public TextField txtMemberIdSearch_Reserve;
    public JFXButton btnReturn_Return;
    public AnchorPane lookupFormContext_Reserve;
    public TableView<Book> tblISBNTitle_Reserve;
    public TableColumn colISBNSearch_Reserve;
    public TableColumn colTitleSearch_reserve;
    public TextField txtTitleSearch_Reserve;
    public JFXButton btnReserve_reserve;
    public JFXButton btnDeleteReserve;

    ObservableList<BorrowedBooks> borrowedBooks = FXCollections.observableArrayList();
    LinkedHashMap<TextField,Pattern> map=new LinkedHashMap();

    Pattern personName=Pattern.compile("^([A-z\\s. ]{3,80})$");
    Pattern address=Pattern.compile("^([A-z0-9/,\\s]{3,})$");
    Pattern contactNo=Pattern.compile("^([0][0-9]{9}|[0][0-9]{2}[-\\s][0-9]{7})$");
    Pattern age=Pattern.compile("^([1-9][0-9]|[1-9])$");
    Pattern email=Pattern.compile("^([a-z0-9]{3,}\\@gmail.com)$");
    //--------------------------------------


    public void initialize(){
        btnPrint.setDisable(true);
        setLocalDateAndTime();

        storeValidations();

        try {

            lblId_Member.setText(new MemberController().getMemberId());
            setMembersTable();
            setLookUpTable();
            setReservationTable(new ReservedBookDetailsController().getAllReservations());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //------------------tblMembers-------------------------------

        colId_member.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName_Member.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress_Member.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact_Member.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colEmail_Member.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDateOfMembership_Member.setCellValueFactory(new PropertyValueFactory<>("dateOfMembership"));
        colAge_Member.setCellValueFactory(new PropertyValueFactory<>("age"));


        //-----------------tbl Borrowed Books_issue Book----------------------
        colBookId_Issue.setCellValueFactory(new PropertyValueFactory<>("bkItemId"));
        colBookName_Issue.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor_Issue.setCellValueFactory(new PropertyValueFactory<>("author"));
        colBorrowedDate_Issue.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        colDueDate_Issue.setCellValueFactory(new PropertyValueFactory<>("dueDate"));


        //-----------------tbl Borrowed Books_return Book----------------------
        colBookId_ReturnBook.setCellValueFactory(new PropertyValueFactory<>("bkItemId"));
        colBookName_ReturnBook.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor_returnBook.setCellValueFactory(new PropertyValueFactory<>("author"));
        colBorrowedDate_ReturnBook.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        colDueDate_ReturnBook.setCellValueFactory(new PropertyValueFactory<>("dueDate"));


        //-----------------tbl reservations----------------------------------------------
        colISBN_reserve.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colMemberId_Reserve.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colReservationDate_Reserve.setCellValueFactory(new PropertyValueFactory<>("dateOfReservation"));
        colStatus_Reserve.setCellValueFactory(new PropertyValueFactory<>("status"));
        colAvailableTill_Reserve.setCellValueFactory(new PropertyValueFactory<>("availableTill"));



        //------------------tblLookUp------------------------
        colISBNSearch_Reserve.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colTitleSearch_reserve.setCellValueFactory(new PropertyValueFactory<>("title"));


        //--------------------tab listeners------------------------------
        tbMember.selectedProperty().addListener(observable -> {
            clearAllReturn();
            clearAllReserve();
            clearAllIssue();
        });
        tbIssueRenewBook.selectedProperty().addListener(observable -> {
            clearAllReturn();
            clearAllReserve();
            clearAllMemberDetails();
        });
        tbReserveBook.selectedProperty().addListener(observable -> {
            try {
                setReservationTable(new ReservedBookDetailsController().getAllReservations());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            clearAllReturn();
            clearAllMemberDetails();
            clearAllIssue();
        });
        tbReturnBook.selectedProperty().addListener(observable -> {
            clearAllMemberDetails();
            clearAllMemberDetails();
            clearAllIssue();
        });

        //---------------------listeners member-----------------------------

        tblMembers_Member.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null) {
                setMemberDetails(newValue);
                btnRegisterOrUpdate.setText("Update");
                btnDelete_Member.setDisable(false);
                btnRegisterOrUpdate.setDisable(false);
            }
        });

        txtIdSearch_Member.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if(txtNameSearch_Member.getText().isEmpty()){
                    tblMembers_Member.setItems(new MemberController().searchMemberById(newValue));
                }else {
                    tblMembers_Member.setItems(new MemberController().searchMember(newValue, txtNameSearch_Member.getText()));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        txtNameSearch_Member.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (txtIdSearch_Member.getText().isEmpty()) {
                    tblMembers_Member.setItems(new MemberController().searchMemberByName(newValue));
                }else {
                    tblMembers_Member.setItems(new MemberController().searchMember(txtIdSearch_Member.getText(), newValue));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        txtEmail_Member.textProperty().addListener(observable -> {

            txtDateOfMemberShip_Member.setText(lblDate.getText());

        });

        for (TextField textField : map.keySet()) {
            textField.textProperty().addListener(observable -> {
                boolean validate = validateMember();
                if (validate && chkBoxRegistrationFee_Member.isSelected()){
                    btnRegisterOrUpdate.setDisable(false);
                }else {
                    btnRegisterOrUpdate.setDisable(true);
                }
            });
        }

        chkBoxRegistrationFee_Member.selectedProperty().addListener(observable -> {
            boolean validate = validateMember();
            if (validate && chkBoxRegistrationFee_Member.isSelected()){
                btnRegisterOrUpdate.setDisable(false);
            }else {
                btnRegisterOrUpdate.setDisable(true);
            }
        });



        //------------------listeners issue book-----------------------------------------------------
        txtMemberId_Issue.textProperty().addListener((observable, oldValue, newValue) -> {
            clearMemberDetails_Issue();
            if (newValue!=null) {

                try {
                    Member member = new MemberController().getMember(newValue);
                    if (member!=null) {
                        txtMemberId_Issue.setStyle("-fx-border-color:"+"#36b146"+";"+"-fx-border-width:2;");
                        setMemberDetailsIssueBook(member);
                    }else {
                        txtMemberId_Issue.setStyle("-fx-border-color:"+"#962121"+";"+"-fx-border-width:2;");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        txtBookId_Issue.textProperty().addListener((observable, oldValue, newValue) ->  {
            clearBookDetails_Issue();
            if (newValue !=null){
                try {
                    BookItem bookItem = new BookItemController().getBookItem(newValue);
                    if (bookItem!=null) {
                        txtBookId_Issue.setStyle("-fx-border-color:"+"#36b146"+";"+"-fx-border-width:2;");
                        Book book = new BookController().getBook(bookItem.getISBN());

                        if (doesMemberCanBorrowThisBook(book,bookItem.getStatus())){
                            setBookDetails_Issue(book,bookItem.getStatus());
                            btnIssueOrRenew.setDisable(false);
                        }


                    }else {
                        txtBookId_Issue.setStyle("-fx-border-color:"+"#962121"+";"+"-fx-border-width:2;");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        tblBorrowedBooks_Issue.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                txtBookId_Issue.setText(newValue.getBkItemId());
            }

        });


        //-----------------listeners return book-----------------------------------------------------
        txtMemberId_ReturnBook.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                clearMemberReturn();
                try {
                    Member member = new MemberController().getMember(newValue);

                    if (member!=null) {
                        txtMemberId_Issue.setStyle("-fx-border-color:"+"#36b146"+";"+"-fx-border-width:2;");
                        setMemberDetailsReturnBook(member);
                    }else {
                        txtMemberId_Issue.setStyle("-fx-border-color:"+"#962121"+";"+"-fx-border-width:2;");
                    }


                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });

        tblBorrowedBooks_ReturnBook.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null) {
                try {
                    BookItem bookItem = new BookItemController().getBookItem(newValue.getBkItemId());
                    Book book=new BookController().getBook(bookItem.getISBN());
                    setReturnBookDetails(newValue);
                    setBorrowedBookDetailsReturnBook(book,bookItem.getStatus());
                    btnReturn_Return.setDisable(false);
                    chkBoxOverDue_ReturnBook.setDisable(false);
                    rdBtnLost_ReturnBook.setDisable(false);
                    rdBtnDamaged_ReturnBook.setDisable(false);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        rdBtnDamaged_ReturnBook.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==true){
                rdBtnLost_ReturnBook.setSelected(false);
                try {
                    Double percentage=Double.parseDouble(new InformationController().getInformation("I-007").getData())/100.0;
                    Double bookPrice=new BookItemController().getBookItem(txtBookIdReturnBook_ReturnBook.getText()).getPrice();
                    Double fine=percentage*bookPrice;
                    txtDamagedFine_returnBook.setText(String.valueOf(fine));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                txtDamagedFine_returnBook.clear();
            }
        });

        rdBtnLost_ReturnBook.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==true){
                rdBtnDamaged_ReturnBook.setSelected(false);
                try {
                    Double percentage=Double.parseDouble(new InformationController().getInformation("I-006").getData())/100.0;
                    Double bookPrice=new BookItemController().getBookItem(txtBookIdReturnBook_ReturnBook.getText()).getPrice();
                    Double fine=percentage*bookPrice;
                    txtLostFine_returnBook.setText(String.valueOf(fine));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                txtLostFine_returnBook.clear();
            }
        });
        
        
        //--------------listeners reserve book-----------------------------------------------------------
        
        txtMemberId_Reserve.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                clearMemberReserve();
                try {
                    Member member = new MemberController().getMember(newValue);
                    if (member!=null){
                        txtMemberId_Reserve.setStyle("-fx-border-color:"+"#36b146"+";"+"-fx-border-width:2;");
                        setMemberDetailsReserveBook(member);
                    }else {
                        txtMemberId_Reserve.setStyle("-fx-border-color:"+"#962121"+";"+"-fx-border-width:2;");
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
        
        txtISBN_ReserveBook.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null) {
                clearBookReserve();
                try {
                    String status="Not Available";
                    for (BookItem temp:new BookItemController().getBookItemsFromISBN(newValue)
                         ) {
                        if (temp.getStatus().equals("Available")){
                            status="Available";
                        }
                    }

                    Book book=new BookController().getBook(newValue);
                    if(book!=null) {
                        txtISBN_Issue.setStyle("-fx-border-color:"+"#36b146"+";"+"-fx-border-width:2;");
                        setBookDetailsReserve(book, status);
                    }else {
                        txtISBN_Issue.setStyle("-fx-border-color:"+"#962121"+";"+"-fx-border-width:2;");
                    }
                    canReservationPlaced();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        tblReservations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null) {
                txtMemberId_Reserve.setText(newValue.getMemberId());
                txtISBN_ReserveBook.setText(newValue.getISBN());
                txtDate_ReserveBook.setText(newValue.getDateOfReservation());
                btnDeleteReserve.setDisable(true);
            }

        });

        txtMemberIdSearch_Reserve.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                try {
                    if (txtMemberIdSearch_Reserve.getText().isEmpty()){
                        setReservationTable(new ReservedBookDetailsController().getAllReservations());
                    }
                    ObservableList<ReservedBookDetails> reservedBookDetails = new ReservedBookDetailsController().searchReservation(newValue);
                    setReservationTable(reservedBookDetails);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        });

        rdBtnAll_Reserve.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==true){
                try {
                    rdBtnPending_Reserve.setSelected(false);
                    rdBtnAvailable_reserve.setSelected(false);
                    setReservationTable(new ReservedBookDetailsController().getAllReservations());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        rdBtnAvailable_reserve.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==true){
                rdBtnPending_Reserve.setSelected(false);
                rdBtnAll_Reserve.setSelected(false);
                try {
                    ObservableList<ReservedBookDetails> availableReservations=FXCollections.observableArrayList();
                    for (ReservedBookDetails allReservation : new ReservedBookDetailsController().getAllReservations()) {
                        if (allReservation.getStatus().equals("Available")){
                            availableReservations.add(allReservation);
                        }
                    }
                    setReservationTable(availableReservations);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        rdBtnPending_Reserve.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==true){
                rdBtnAll_Reserve.setSelected(false);
                rdBtnAvailable_reserve.setSelected(false);
                try {
                    ObservableList<ReservedBookDetails> availableReservations=FXCollections.observableArrayList();
                    for (ReservedBookDetails allReservation : new ReservedBookDetailsController().getAllReservations()) {
                        if (allReservation.getStatus().equals("Pending")){
                            availableReservations.add(allReservation);
                        }
                    }
                    setReservationTable(availableReservations);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        tblISBNTitle_Reserve.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                txtISBN_ReserveBook.setText(newValue.getISBN());
                lookupFormContext_Reserve.setVisible(false);
            }
        });

        txtTitleSearch_Reserve.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue!=null){
                    ObservableList<Book> searchedBooksFromTitle = new BookController().searchBookByName(newValue);
                    tblISBNTitle_Reserve.setItems(searchedBooksFromTitle);
                }
                if (txtTitleSearch_Reserve.getText().isEmpty()){
                    setLookUpTable();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void storeValidations() {
        map.put(txtName_Member,personName);
        map.put(txtAge_Member,age);
        map.put(txtAddress_Member,address);
        map.put(txtContactNo_Member,contactNo);
        map.put(txtEmail_Member,email);
    }

    private void setLocalDateAndTime(){
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



//********************************    Manage Member  ******************************************************************


    public void RegisterOrUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (validateMember()) {
            MembershipPayments membershipPayments = new MembershipPayments(new MembershipPaymentController().getMembershipPaymentId(), lblDate.getText(), lblId_Member.getText(), "P-01");
            Member member = new Member(lblId_Member.getText(), txtName_Member.getText(), txtAddress_Member.getText(), txtContactNo_Member.getText(), txtEmail_Member.getText(), Integer.parseInt(txtAge_Member.getText()), lblDate.getText(), membershipPayments);

            if (btnRegisterOrUpdate.getText().equals("Register")) {
                if (chkBoxRegistrationFee_Member.isSelected()) {
                    if (new MemberController().addMember(member)) {
                        new Alert(Alert.AlertType.INFORMATION, "Member has been registered successfully").show();
                        clearAllMemberDetails();
                        setMembersTable();
                        lblId_Member.setText(new MemberController().getMemberId());
                        newRegisteredMember=member;
                        btnPrint.setDisable(false);
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Something went wrong try again").show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Registration fees not paid.").show();
                }

            } else {
                //-----------------update------------------
                if (new MemberController().updateMember(member)) {
                    new Alert(Alert.AlertType.INFORMATION, "Member details have been updated successfully").show();
                    clearAllMemberDetails();
                    setMembersTable();
                    lblId_Member.setText(new MemberController().getMemberId());
                    txtDateOfMemberShip_Member.setText(lblDate.getText());
                    btnRegisterOrUpdate.setText("Register");
                } else {
                    new Alert(Alert.AlertType.WARNING, "Something went wrong try again").show();
                }
            }
        }
    }

    public void cancelMembershipOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ButtonType yes =new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no =new ButtonType("No", ButtonBar.ButtonData.OK_DONE);

        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this account ?",yes,no);
        alert.setTitle("Confirmation Alert");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.orElse(no)==yes){
            if (new MemberController().cancelMembership(lblId_Member.getText())){
                new Alert(Alert.AlertType.INFORMATION,"Membership cancelled successfully").show();
                clearAllMemberDetails();
                setMembersTable();
                lblId_Member.setText(new MemberController().getMemberId());
                btnRegisterOrUpdate.setText("Register");
            }else {
                new Alert(Alert.AlertType.WARNING, "Something went wrong try again").show();
            }
        }
    }

    private void setMemberDetails(Member member) {
        lblId_Member.setText(member.getId());
        txtName_Member.setText(member.getName());
        txtAge_Member.setText(String.valueOf(member.getAge()));
        txtContactNo_Member.setText(member.getContactNo());
        txtAddress_Member.setText(member.getAddress());
        txtEmail_Member.setText(member.getEmail());
        txtDateOfMemberShip_Member.setText(member.getDateOfMembership());
        chkBoxRegistrationFee_Member.setSelected(true);
        chkBoxRegistrationFee_Member.setDisable(true);

    }

    private void setMembersTable() throws SQLException, ClassNotFoundException {
        tblMembers_Member.setItems(new MemberController().getAllMembers());
    }

    private void clearAllMemberDetails(){
        txtName_Member.clear();
        txtAge_Member.clear();
        txtAddress_Member.clear();
        txtEmail_Member.clear();
        txtContactNo_Member.clear();
        chkBoxRegistrationFee_Member.setSelected(false);
        chkBoxRegistrationFee_Member.setDisable(false);
        txtDateOfMemberShip_Member.clear();
        btnPrint.setDisable(true);
        btnRegisterOrUpdate.setText("Register");
        btnRegisterOrUpdate.setDisable(true);
        btnDelete_Member.setDisable(true);
        txtDateOfMemberShip_Member.clear();

        for (TextField textField : map.keySet()) {
            textField.setStyle("-fx-border-color: silver;"+"-fx-border-width: 1");
        }


    }

    public void clearOnAction_Member(ActionEvent actionEvent) {
        clearAllMemberDetails();
    }

    public boolean validateMember(){
        for (TextField textField : map.keySet()) {
            Pattern pattern = map.get(textField);
            if (!pattern.matcher(textField.getText()).matches()){
                textField.setStyle("-fx-border-color: red;"+"-fx-border-width: 2;");
                return false;
            }else {
                textField.setStyle("-fx-border-color: green;"+"-fx-border-width: 2;");
            }
        }
        return true;
    }

    public void printLibraryCardOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("../view/reports/LibraryCard.jrxml"));
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            ArrayList<Member> member=new ArrayList<>();
            member.add(new MemberController().getMember(newRegisteredMember.getId()));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JRBeanCollectionDataSource(member));
            JasperViewer.viewReport(jasperPrint,false);
            btnPrint.setDisable(true);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

//*******************************   Issue/Renew     *****************************************************************


    public void IssueOrRenewBookOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, ParseException {
        String status;
        int renewedTimes=0;
        if (txtStatus_Issue.getText().equals("Borrowed")||txtStatus_Issue.getText().equals("Renewed")){
            for (BorrowedBookDetails borrowedBookDetails : new BorrowedBookDetailsController().getBorrowedBooksOfAMember(txtMemberId_Issue.getText())) {
                if (borrowedBookDetails.getBkItemId().equals(txtBookId_Issue.getText())){
                    renewedTimes=borrowedBookDetails.getRenewedTimes()+1;
                }
            }
            status="Renewed";
        }else{
            status="Borrowed";
        }
        BorrowedBookDetails borrowedBook=new BorrowedBookDetails(txtBookId_Issue.getText(),txtMemberId_Issue.getText(),txtBorrowedDate_Issue.getText(),txtDueDate_Issue.getText(),status,renewedTimes);

        if (status.equals("Borrowed")){
            if (new BorrowedBookDetailsController().addBorrowedBook(borrowedBook,txtStatus_Issue.getText())){
                issueBook();
            }else {
                new Alert(Alert.AlertType.WARNING,"Something went wrong Try Again").show();
            }
        }else{
            if (new BorrowedBookDetailsController().updateBorrowedBook(borrowedBook)){
                issueBook();
            }else {
                new Alert(Alert.AlertType.WARNING,"Something went wrong Try Again").show();
            }
        }


    }

    private void issueBook() throws SQLException, ClassNotFoundException {
        new Alert(Alert.AlertType.INFORMATION,"Issued").show();
        setTblBorrowedBooks_Issue(txtMemberId_Issue.getText());
        clearBookDetails_Issue();
        txtBookId_Issue.clear();
        txtBookId_Issue.setStyle(null);
        txtBorrowedDate_Issue.clear();
        txtDueDate_Issue.clear();
        btnIssueOrRenew.setText("Issue");
    }

    public void cancel_IssueOnAction(ActionEvent actionEvent) {
        clearAllIssue();
    }

    public void setMemberDetailsIssueBook(Member member) throws SQLException, ClassNotFoundException, ParseException {
        txtName_Issue.setText(member.getName());
        txtContact_Issue.setText(member.getContactNo());
        txtAddress_Issue.setText(member.getAddress());
        txtEmail_Issue.setText(member.getEmail());
        txtAge_Issue.setText(String.valueOf(member.getAge()));
        txtDateOfMembership_Issue.setText(member.getDateOfMembership());
        setTblBorrowedBooks_Issue(member.getId());

    }

    public void setTblBorrowedBooks_Issue(String id) throws SQLException, ClassNotFoundException {
        borrowedBooks.clear();
        for (BorrowedBookDetails temp:new BorrowedBookDetailsController().getBorrowedBooksOfAMember(id)
        ) {
            Book book = new BookController().getBook(new BookItemController().getBookItem(temp.getBkItemId()).getISBN());
            BorrowedBooks borrowedBook=new BorrowedBooks(temp.getBkItemId(),book.getTitle(),book.getAuthor(),temp.getBorrowedDate(),temp.getDueDate());
            borrowedBooks.add(borrowedBook);
        }
        tblBorrowedBooks_Issue.setItems(borrowedBooks);
    }

    public void setBookDetails_Issue(Book book,String status) throws SQLException, ClassNotFoundException {

        txtISBN_Issue.setText(book.getISBN());
        txtBookName_Issue.setText(book.getTitle());
        txtAuthor_Issue.setText(book.getAuthor());
        txtStatus_Issue.setText(status);
        if (book.getIsReferenceOnly().equals("Yes")){
            chkBoxRefOnly_Issue.setSelected(true);
        }else {
            chkBoxRefOnly_Issue.setSelected(false);
        }

        setDates();
    }

    private void setDates() throws SQLException, ClassNotFoundException {
        txtBorrowedDate_Issue.setText(lblDate.getText());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(cal.getTime());
        int days;
        if (btnIssueOrRenew.getText().equals("Issue")){
            days=Integer.parseInt(new InformationController().getInformation("I-002").getData());
        }else {
            days=Integer.parseInt(new InformationController().getInformation("I-003").getData());
        }
        cal.add(Calendar.DATE,days);
        String dueDate=format.format(cal.getTime());
        txtDueDate_Issue.setText(dueDate);
    }

    private boolean doesThisMemberHasFinesToPay() throws ParseException {
        for (BorrowedBooks temp:borrowedBooks
        ) {
            if (new Date().after(new SimpleDateFormat("yyyy-MM-dd").parse(temp.getDueDate()))){
                return true;
            }
        }
        return false;
    }

    private boolean doesMemberCanBorrowThisBook(Book book,String status) throws SQLException, ClassNotFoundException, ParseException {
       if (!doesThisMemberHasFinesToPay()){
           for (BorrowedBookDetails borrowedBookDetails : new BorrowedBookDetailsController().getBorrowedBooksOfAMember(txtMemberId_Issue.getText())) {
               if (borrowedBookDetails.getBkItemId().equals(txtBookId_Issue.getText())){
                   int maxNoOfTimesBookCanBeRenewed = Integer.parseInt(new InformationController().getInformation("I-004").getData());
                   if (borrowedBookDetails.getRenewedTimes()<maxNoOfTimesBookCanBeRenewed){
                       btnIssueOrRenew.setDisable(false);
                       btnIssueOrRenew.setText("Renew");
                       return true;
                   }else {
                       //*This book can't be renewed again
                       new Alert(Alert.AlertType.WARNING,"*This book can't be renewed again").show();
                       return false;
                   }

               }
           }

           if (borrowedBooks.size()<Integer.parseInt(new InformationController().getInformation("I-001").getData())){
               if (book.getIsReferenceOnly().equals("No")){
                   if (!doesThisMemberAlreadyHaveACopy()){
                       if (status.equals("Available")){
                           return true;
                       }else if (status.equals("Reserved")){
                           String reservationStatus = new ReservedBookDetailsController().getReservation(book.getISBN(), txtMemberId_Issue.getText()).getStatus();

                           if (reservationStatus!=null && reservationStatus.equals("Available")){
                               return true;
                           }else{
                               new Alert(Alert.AlertType.WARNING,"*This book is reserved by another member").show();
                               return false;
                           }
                       }else{

                           new Alert(Alert.AlertType.WARNING,"*This book is borrowed by another member").show();
                           return false;
                       }
                   }else {
                       new Alert(Alert.AlertType.WARNING,"*This Member has already borrowed a copy of this book").show();
                       return false;
                   }

               }else {
                   new Alert(Alert.AlertType.WARNING,"*This book is for reference only").show();
                   return false;
               }


           }else {

               //*This member has already borrowed maximum no of books.
               new Alert(Alert.AlertType.WARNING,"*This member has already borrowed maximum no of books.").show();
               return false;
           }
       }else {
           new Alert(Alert.AlertType.WARNING,"*This member has not returned books yet(Late).").show();
           return false;
       }



    }

    private boolean doesThisMemberAlreadyHaveACopy() throws SQLException, ClassNotFoundException {
        String ISBNSelectedBook = new BookItemController().getBookItem(txtBookId_Issue.getText()).getISBN();
        for (BorrowedBookDetails temp : new BorrowedBookDetailsController().getBorrowedBooksOfAMember(txtMemberId_Issue.getText())) {
            String ISBNBorrowedBook = new BookItemController().getBookItem(temp.getBkItemId()).getISBN();
            if (ISBNBorrowedBook.equals(ISBNSelectedBook)){
                return true;
            }
        }
        return false;
    }

    public void clearMemberDetails_Issue(){
        txtName_Issue.clear();
        txtContact_Issue.clear();
        txtAddress_Issue.clear();
        txtEmail_Issue.clear();
        txtDateOfMembership_Issue.clear();
        txtAge_Issue.clear();
        borrowedBooks.clear();
        tblBorrowedBooks_Issue.setItems(borrowedBooks);
        txtDateOfMembership_Issue.clear();



    }

    public void clearBookDetails_Issue(){
        txtISBN_Issue.clear();
        txtBookName_Issue.clear();
        txtAuthor_Issue.clear();
        txtStatus_Issue.clear();
        chkBoxRefOnly_Issue.setSelected(false);
        btnIssueOrRenew.setText("Issue");
        btnIssueOrRenew.setDisable(true);
    }

    private void clearAllIssue(){
        clearMemberDetails_Issue();
        clearBookDetails_Issue();
        txtMemberId_Issue.clear();
        txtMemberId_Issue.setStyle("-fx-border-color: "+"silver"+";"+"-fx-border-width:1;");
        txtBookId_Issue.clear();
        txtBookId_Issue.setStyle("-fx-border-color: "+"silver"+";"+"-fx-border-width:1;");
        txtBorrowedDate_Issue.clear();
        txtDueDate_Issue.clear();
    }

   //********************************     Return Book            *******************************************************


    public void returnBookOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
         if(txtOverDueFine_returnBook.getText().isEmpty()) {
                returnBook();
        }else{
            if(chkBoxOverDue_ReturnBook.isSelected()){
                returnBook();
            }else {
                new Alert(Alert.AlertType.WARNING,"Book Can't be returned without paying fines").show();
            }
        }

    }

    private void returnBook() throws SQLException, ClassNotFoundException {
        ArrayList<Fine> fines=new ArrayList<>();
        if (chkBoxOverDue_ReturnBook.isSelected()){
            fines.add(new Fine(txtReturnDate_ReturnBook.getText(),Double.valueOf(txtOverDueFine_returnBook.getText()),txtMemberIdReturnBook_ReturnBook.getText(),"F-01"));
        }
        if (rdBtnLost_ReturnBook.isSelected()){
            fines.add(new Fine(txtReturnDate_ReturnBook.getText(),Double.valueOf(txtLostFine_returnBook.getText()),txtMemberIdReturnBook_ReturnBook.getText(),"F-03"));
        }
        if (rdBtnDamaged_ReturnBook.isSelected()){
            fines.add(new Fine(txtReturnDate_ReturnBook.getText(),Double.valueOf(txtDamagedFine_returnBook.getText()),txtMemberIdReturnBook_ReturnBook.getText(),"F-02"));
        }
        String status;
        if (new ReservedBookDetailsController().getReservation(txtISBN_ReturnBook.getText())!=null){
            status="Reserved";
        }else {
            status="Available";
        }
        BorrowedBookDetails book =new BorrowedBookDetails(txtBookIdReturnBook_ReturnBook.getText(),txtMemberIdReturnBook_ReturnBook.getText(),txtBorrowDate_ReturnBook.getText(),txtDueDate_ReturnBook.getText(),status,0);

        if (new BorrowedBookDetailsController().returnBorrowedBook(book,fines)){
            String massage=(status.equals("Available"))?"Book Returned":"Book Returned\n\n* Book is Reserved";
            new Alert(Alert.AlertType.INFORMATION,massage).show();
            clearReturnedBookReturn();
            clearBookReturn();
            setBorrowedBookTblReturnBook(txtMemberId_ReturnBook.getText());
        }else{
            new Alert(Alert.AlertType.WARNING,"Something went wrong try again").show();
        }
    }

    public void cancelOnAction_Return(ActionEvent actionEvent) {
        clearAllReturn();
    }

    private void setMemberDetailsReturnBook(Member member) throws SQLException, ClassNotFoundException {
        txtName_ReturnBook.setText(member.getName());
        txtContact_ReturnBook.setText(member.getContactNo());
        txtAddress_ReturnBook.setText(member.getAddress());
        txtEmail_ReturnBook.setText(member.getEmail());
        txtAge_ReturnBook.setText(String.valueOf(member.getAge()));
        txtDateOfMembership_ReturnBook.setText(member.getDateOfMembership());
        setBorrowedBookTblReturnBook(txtMemberId_ReturnBook.getText());
    }

    private void setBorrowedBookTblReturnBook(String id) throws SQLException, ClassNotFoundException {
        borrowedBooks.clear();
        for (BorrowedBookDetails temp:new BorrowedBookDetailsController().getBorrowedBooksOfAMember(id)
        ) {
            Book book = new BookController().getBook(new BookItemController().getBookItem(temp.getBkItemId()).getISBN());
            BorrowedBooks borrowedBook=new BorrowedBooks(temp.getBkItemId(),book.getTitle(),book.getAuthor(),temp.getBorrowedDate(),temp.getDueDate());
            borrowedBooks.add(borrowedBook);
        }
        tblBorrowedBooks_ReturnBook.setItems(borrowedBooks);
    }

    private void setBorrowedBookDetailsReturnBook(Book book,String status) throws SQLException, ClassNotFoundException {
        txtISBN_ReturnBook.setText(book.getISBN());
        txtBookName_ReturnBook.setText(book.getTitle());
        txtAuthor_ReturnBook.setText(book.getAuthor());
        txtStatus_ReturnBook.setText(status);
        txtPrice_ReturnBook.setText(String.valueOf(new BookItemController().getBookItem(txtBookIdReturnBook_ReturnBook.getText()).getPrice()));

    }

    private void setReturnBookDetails(BorrowedBooks borrowedBook) throws ParseException, SQLException, ClassNotFoundException {
        txtMemberIdReturnBook_ReturnBook.setText(txtMemberId_ReturnBook.getText());
        txtBookIdReturnBook_ReturnBook.setText(borrowedBook.getBkItemId());
        txtBorrowDate_ReturnBook.setText(borrowedBook.getBorrowedDate());
        txtDueDate_ReturnBook.setText(borrowedBook.getDueDate());
        txtReturnDate_ReturnBook.setText(lblDate.getText());


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date firstDate = format.parse(txtDueDate_ReturnBook.getText());

        Date secondDate = format.parse(txtReturnDate_ReturnBook.getText());
        long difference = secondDate.getTime() - firstDate.getTime();
        TimeUnit time = TimeUnit.DAYS;
        long fineDays = time.convert(difference, TimeUnit.MILLISECONDS);

        if (fineDays>0){
            double fine=fineDays * Integer.parseInt(new InformationController().getInformation("I-005").getData());
            txtOverDueFine_returnBook.setText(String.valueOf(fine));
        }
    }

    private void clearReturnedBookReturn(){
        txtMemberIdReturnBook_ReturnBook.clear();
        txtBookIdReturnBook_ReturnBook.clear();
        txtBorrowDate_ReturnBook.clear();
        txtDueDate_ReturnBook.clear();
        txtReturnDate_ReturnBook.clear();
        txtOverDueFine_returnBook.clear();
        txtDamagedFine_returnBook.clear();
        txtLostFine_returnBook.clear();
        rdBtnDamaged_ReturnBook.setSelected(false);
        chkBoxOverDue_ReturnBook.setSelected(false);
        rdBtnLost_ReturnBook.setSelected(false);
        rdBtnDamaged_ReturnBook.setDisable(true);
        chkBoxOverDue_ReturnBook.setDisable(true);
        rdBtnLost_ReturnBook.setDisable(true);
        btnReturn_Return.setDisable(true);

    }

    private void clearBookReturn(){
        txtISBN_ReturnBook.clear();
        txtBookName_ReturnBook.clear();
        txtAuthor_ReturnBook.clear();
        txtStatus_ReturnBook.clear();
        txtPrice_ReturnBook.clear();
    }

    private void clearMemberReturn(){
        txtName_ReturnBook.clear();
        txtContact_ReturnBook.clear();
        txtAddress_ReturnBook.clear();
        txtEmail_ReturnBook.clear();
        txtDateOfMembership_ReturnBook.clear();
        txtAge_ReturnBook.clear();
        borrowedBooks.clear();
        tblBorrowedBooks_ReturnBook.setItems(borrowedBooks);
    }

    private void clearAllReturn(){
        txtMemberId_ReturnBook.clear();
        clearMemberReturn();
        clearReturnedBookReturn();
        clearBookReturn();
    }



    //****************************** Reserve Book ********************************************************************


    public void reserveBookOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ReservedBookDetails book =new ReservedBookDetails(txtISBN_ReserveBook.getText(),txtMemberId_Reserve.getText(),txtDate_ReserveBook.getText(),"Pending",null);

        if (new ReservedBookDetailsController().reserveBook(book)){
            new Alert(Alert.AlertType.INFORMATION,"Reservation placed").show();
            clearAllReserve();
            setReservationTable(new ReservedBookDetailsController().getAllReservations());
        }else{
            new Alert(Alert.AlertType.WARNING,"Something went wrong try again").show();
        }


    }

    public void canReservationPlaced() throws SQLException, ClassNotFoundException {
        if(!txtStatus_Reserve.getText().equals("Available")){
            Member member = new MemberController().getMember(txtMemberId_Reserve.getText());
            if (member!=null){
                for (BorrowedBookDetails borrowed : new BorrowedBookDetailsController().getBorrowedBooksOfAMember(txtMemberId_Reserve.getText())) {
                    String isbn = new BookItemController().getBookItem(borrowed.getBkItemId()).getISBN();
                    if (isbn.equals(txtISBN_ReserveBook.getText())){
                        new Alert(Alert.AlertType.INFORMATION,"This member has already borrowed a copy of this book").show();
                    }
                }
                btnReserve_reserve.setDisable(false);
            }else {
                new Alert(Alert.AlertType.INFORMATION,"No member found").show();
            }

        }else {
            new Alert(Alert.AlertType.INFORMATION,"This book is available").show();
        }
    }

    public void cancelReservationOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ButtonType yes =new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no =new ButtonType("No", ButtonBar.ButtonData.OK_DONE);

        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to cancel this reservation ?",yes,no);
        alert.setTitle("Confirmation Alert");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.orElse(no)==yes){
            ReservedBookDetails book =new ReservedBookDetailsController().getReservation(txtISBN_ReserveBook.getText(),txtMemberId_Reserve.getText(),txtDate_ReserveBook.getText());
            if (new ReservedBookDetailsController().deleteReservation(book)){
                new Alert(Alert.AlertType.INFORMATION,"Reservation cancelled").show();
                clearAllReserve();
                setReservationTable(new ReservedBookDetailsController().getAllReservations());
            }else{
                new Alert(Alert.AlertType.WARNING,"Something went wrong try again").show();
            }
        }
    }

    private void setMemberDetailsReserveBook(Member member){
        txtName_Reserve.setText(member.getName());
        txtContact_Reserve.setText(member.getContactNo());
        txtAddress_Reserve.setText(member.getAddress());
        txtEmail_Reserve.setText(member.getEmail());
        
    }

    private void setBookDetailsReserve(Book book, String status) {
        txtBookName_Reserve.setText(book.getTitle());
        txtAuthor_Reserve.setText(book.getAuthor());
        txtStatus_Reserve.setText(status);
        if(book.getIsReferenceOnly().equals("Yes")){
            chkBoxRefOnly_Reserve.setSelected(true);
        }
        txtDate_ReserveBook.setText(lblDate.getText());
    }

    private void setReservationTable(ObservableList<ReservedBookDetails> reservedBookDetails) throws SQLException, ClassNotFoundException {
        ObservableList<ReservedBookDetails> observableList =FXCollections.observableArrayList();
        for (ReservedBookDetails reservedBookDetail : reservedBookDetails) {
            String avilabletill= (reservedBookDetail.getAvailableTill()==null)?"-":reservedBookDetail.getAvailableTill();
            observableList.add(new ReservedBookDetails(reservedBookDetail.getISBN(),reservedBookDetail.getMemberId(),reservedBookDetail.getDateOfReservation(),reservedBookDetail.getStatus(),avilabletill));
        }
        tblReservations.setItems(observableList);
    }

    private void clearAllReserve(){
        btnReserve_reserve.setDisable(true);
        btnDeleteReserve.setDisable(true);
        txtMemberId_Reserve.clear();
        txtDate_ReserveBook.clear();
        txtISBN_ReserveBook.clear();
        clearBookReserve();
        clearMemberReserve();

    }

    private void clearMemberReserve(){
        txtName_Reserve.clear();
        txtContact_Reserve.clear();
        txtAddress_Reserve.clear();
        txtEmail_Reserve.clear();
    }

    private void clearBookReserve(){
        txtBookName_Reserve.clear();
        txtAuthor_Reserve.clear();
        txtStatus_Reserve.clear();
        chkBoxRefOnly_Reserve.setSelected(false);

    }

    private void setLookUpTable() throws SQLException, ClassNotFoundException {
        tblISBNTitle_Reserve.setItems(new BookController().getAllBooks());
    }

    public void clearReservationOnAction(ActionEvent actionEvent) {
        clearAllReserve();
    }





    //****************************************************************************************************************

    public void logOutOnAction(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) librarianFormContext.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/MainInterfaceForm.fxml"))));
        window.centerOnScreen();
    }

    public void openLookUpOnAction(ActionEvent actionEvent) {
        lookupFormContext_Reserve.setVisible(true);
    }

    public void closeLookUpOnMouseClick(MouseEvent mouseEvent) {
        lookupFormContext_Reserve.setVisible(false);
    }



}
