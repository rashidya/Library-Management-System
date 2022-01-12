package controller;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.*;
import view.tm.AdminBooks_Books;
import view.tm.Income_Report;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class AdminFormController {
    public JFXButton btnLogOut;
    public AnchorPane adminFormContext;
    public Label lblTime;
    public Label lblDate;
    public Tab tbAdministration;
    public Tab tbReports;
    public Tab tbBook;

    //-------User/Administration-----------
    public TextField txtUserName_User;
    public TextField txtName_User;
    public TextField txtContact_User;
    public TextField txtEmail_User;
    public TextField txtJobTitle_User;
    public JFXRadioButton rdBtnAdmin;
    public JFXRadioButton rdBtnUser;
    public TextField  txtPassword_User;
    public TableView<User> tblUsers_User;
    public TableColumn colName_User;
    public TableColumn colContact_user;
    public TableColumn colEmail_USer;
    public TableColumn colJobTitle_User;
    public TableColumn colSecurityLevel_User;
    public JFXButton btnCreateAndUpdateAccount_User;
    public TextField txtRegistrationFee_Administration;
    public JFXButton btnDeleteAccount_Administration;
    public JFXButton btnUpdateRules;

    //---------------Books--------------------
    public TextField txtISBN_Books;
    public JFXButton btnAddOrUpdate_Books;
    public TextField txtAuthor_Books;
    public TextField txtTitle_Books;
    public TextField txtNoOfCopies_Books;
    public TextField txtLanguage_Books;
    public TextField txtNoOfPages_Books;
    public TextField txtPriceBooks;
    public TextField txtRackNo_Books;
    public TextField txtStatus_Books;
    public JFXCheckBox chkBoxIsRefOnly_Book;
    public ComboBox<String> cmbCategory_Books;
    public Label lblSuggestRack_Book;
    public ComboBox<String> cmbLanguage_Books;
    public TableView<AdminBooks_Books> tblBooks_Books;
    public TableColumn colISBN_Books;
    public TableColumn colBookId_Books;
    public TableColumn colTitle_Books;
    public TableColumn colAuthor_Books;
    public TableColumn colCategory_Books;
    public TableColumn colLanguage_Books;
    public TextField txtFineBookDamaged_Books;
    public TextField txtMaxNoOfBooksCanBeBorrowed_Books;
    public TextField txtMaxNoOfDaysBorrowedBookCanBeKept_Books;
    public TextField txtMaxNoOfDaysRenewedBookCanBeKept_Books;
    public TextField txtMaxNoOfTimesABookCanBeRenewed_Books;
    public TextField txtFinePerDay_Books;
    public TextField txtFineBookLost_Books;
    public TextField txtISBNSearch_Books;
    public TextField txtTitleSearch_Book;
    public JFXButton btnDeleteBook_Book;
    public TextField txtDate_Books;
    public TextField txtMaxNoOfDayaAReservationIsAvailable_Books;
    private AdminBooks_Books selectedBookItem;

    //----------Report
    public Label lbl1_Report;
    public Label lbl2_Report;
    public Label lbl3_Report;
    public PieChart pieChartMember_Report;
    public PieChart pieChartBooks_Report;
    public JFXRadioButton rdBtnMember_Report;
    public JFXRadioButton rdBtnBooks_Report;
    public JFXRadioButton rdBtnIncome_Report;
    public JFXRadioButton rdBtnAnnually_Report;
    public JFXRadioButton rdBtnMonthly_Report;
    public JFXRadioButton rdBtnDaily_Report;
    public JFXComboBox<Integer> cmbYear_Report;
    public JFXComboBox<String> cmbMonth_Report;
    public JFXDatePicker dtPickerDay_Report;
    public JFXDatePicker dtPickerFrom_Report;
    public JFXDatePicker dtPickerTo_Report;
    public TableView<Member> tblMember_Report;
    public TableColumn colNameMember_Report;
    public TableColumn colAddressMember_Report;
    public TableColumn colContactMember_Report;
    public TableColumn colEmail_Report;
    public TableColumn colAgeMember_Report;
    public TableColumn colDateMember_Report;
    public TableView<AdminBooks_Books> tblBook_Report;
    public TableColumn colISBNBook_Report;
    public TableColumn colBookIdBook_Report;
    public TableColumn colTitleBook_Report;
    public TableColumn colAuthorBookReport;
    public TableColumn colCategoryBook_Report;
    public TableColumn colLanguageBook_Report;
    public TableColumn colStatusBook_Report;
    public TableColumn colRackNoBook_Report;
    public JFXRadioButton rdBtnAllBook_Report;
    public JFXRadioButton rdBtnAvailableBook_Report;
    public JFXRadioButton rdBtnBorrowedBook_Report;
    public JFXRadioButton rdBtnRenewedBook_Report;
    public TableView<Income_Report> tblIncome_Report;
    public TableColumn colMemberIdIncome_Report;
    public TableColumn colPaymentTypeIncome_Report;
    public TableColumn colAmountIncome_Report;
    public TableColumn colDateIncome_Report;
    public JFXRadioButton rdBtnAllIncome_Report;
    public JFXRadioButton rdBtnRegistrationFeeIncome_Report;
    public JFXRadioButton rdBtnFineIncome_Report;

    private ObservableList<Member> members =FXCollections.observableArrayList();
    private User selectedUser_User;
    private ObservableList<Income_Report> income =FXCollections.observableArrayList();
    ObservableList<AdminBooks_Books> allBookItems =FXCollections.observableArrayList();
    ObservableList<AdminBooks_Books> searchList=FXCollections.observableArrayList();

    LinkedHashMap<TextField, Pattern> usersMap=new LinkedHashMap();
    LinkedHashMap<TextField, Pattern> RulesMap=new LinkedHashMap();
    LinkedHashMap<TextField, Pattern> BookMap=new LinkedHashMap();

    Pattern personName=Pattern.compile("^([A-z\\s. ]{3,80})$");
    Pattern language=Pattern.compile("^([A-z]{3,15})$");
    Pattern jobTitle=Pattern.compile("^([A-z\\s ]{3,20})$");
    Pattern contactNo=Pattern.compile("^([0][0-9]{9}|[0][0-9]{2}[-\\s][0-9]{7})$");
    Pattern email=Pattern.compile("^([a-z0-9]{3,}\\@gmail.com)$");
    Pattern number=Pattern.compile("^([0-9]{1,})$");
    Pattern decimal=Pattern.compile("^([0-9.]{1,})$");
    Pattern ISBN=Pattern.compile("^([0-9]{3}[-s][0-9]{1,5}[-s][0-9]{1,7}[-s][0-9]{1,6}[-s][0-9]{1,3})$|^([0-9]{1,5}[-s][0-9]{1,7}[-s][0-9]{1,6}[-s][0-9]{1,3})$");
    Pattern bookName=Pattern.compile("^([A-z0-9/.,?>\";:|<'\\s!@#$%^&*()_+=-]{1,})$");



    public void initialize(){
        storeValidations();
        setLocalDateAndTime();


        try {
            //----------set Book Tab Details-------------------------
            ObservableList<String> bookCategories= FXCollections.observableArrayList("Action and Adventure",
                    "Detective and Mystery","True Crime","Horror","Literary Fiction","Poetry","Comic","Cooking,Health and Fitness","Romance","Science Fiction",
                    "Short Stories","Classic","Religion","Biographies and Memoir","Reference","Children's","Other");
            cmbCategory_Books.setItems(bookCategories);

            ObservableList<String> bookLanguages=new BookController().getAllLanguages();
            cmbLanguage_Books.setItems(bookLanguages);
            allBookItems=new BookItemController().getAllBooksItems();
            tblBooks_Books.setItems(allBookItems);

            //----------set Report Tab Details-------------------------
            setYearAndMonth();
            setMemberTable_Report(new MemberController().getAllMembers());
            ObservableList<PieChart.Data> categories=FXCollections.observableArrayList();
            for (String temp:bookCategories
                 ) {
                categories.add(new PieChart.Data(temp,new BookController().getTotalNoOfBooksOfCategory(temp)));
            }
            pieChartMember_Report.setData(categories);
            rdBtnMember_Report.setSelected(true);

            //----------set Administration Tb/UserTb Tab Details-------------------------
            setUsersTable();
            setRules();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



        //-------------------set User table-----------------------------
        colName_User.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact_user.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colEmail_USer.setCellValueFactory(new PropertyValueFactory<>("email"));
        colJobTitle_User.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        colSecurityLevel_User.setCellValueFactory(new PropertyValueFactory<>("securityLevel"));

        //-----------------set Book table_Books-------------------------------------
        colISBN_Books.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colBookId_Books.setCellValueFactory(new PropertyValueFactory<>("bkId"));
        colTitle_Books.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor_Books.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCategory_Books.setCellValueFactory(new PropertyValueFactory<>("category"));
        colLanguage_Books.setCellValueFactory(new PropertyValueFactory<>("language"));


        //------------------Member_report---------------------------------------
        colNameMember_Report.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddressMember_Report.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContactMember_Report.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colEmail_Report.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAgeMember_Report.setCellValueFactory(new PropertyValueFactory<>("age"));
        colDateMember_Report.setCellValueFactory(new PropertyValueFactory<>("dateOfMembership"));


        //-------------------Book Report---------------------------------------------
        colISBNBook_Report.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colBookIdBook_Report.setCellValueFactory(new PropertyValueFactory<>("bkId"));
        colTitleBook_Report.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthorBookReport.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCategoryBook_Report.setCellValueFactory(new PropertyValueFactory<>("category"));
        colLanguageBook_Report.setCellValueFactory(new PropertyValueFactory<>("language"));
        colStatusBook_Report.setCellValueFactory(new PropertyValueFactory<>("status"));
        colRackNoBook_Report.setCellValueFactory(new PropertyValueFactory<>("rackNo"));


        //-------------------Income report-------------------------------------
        colMemberIdIncome_Report.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colPaymentTypeIncome_Report.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        colAmountIncome_Report.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDateIncome_Report.setCellValueFactory(new PropertyValueFactory<>("date"));


        //---------------------tab listeners----------------------------------------
        ObservableList<Tab> tabs=FXCollections.observableArrayList(tbAdministration,tbBook,tbReports);
        for (Tab tab : tabs) {
            tab.selectedProperty().addListener(observable -> {
                clearAllUser();
                try {
                    clearBookDetails_Book();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
        

        //-------------------listeners User-------------------------------------
        tblUsers_User.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null) {
                clearAllUser();
                try {
                    setUserDetails(newValue);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                btnCreateAndUpdateAccount_User.setText("Update Account");
                selectedUser_User = newValue;
                txtPassword_User.setDisable(true);
                txtUserName_User.setDisable(true);
                btnDeleteAccount_Administration.setDisable(false);

            }
        });

        rdBtnUser.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue==true){
                rdBtnAdmin.setSelected(false);
                validateUser();
            }
        });

        rdBtnAdmin.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue==true){
                rdBtnUser.setSelected(false);
                validateUser();
            }
        });

        txtPassword_User.textProperty().addListener(observable -> {
            validateUser();
        });

        txtUserName_User.textProperty().addListener(observable -> {
            validateUser();
        });

        for (TextField textField : usersMap.keySet()) {
            textField.textProperty().addListener(observable -> {
                validateUser();
            });
        }

        for (TextField textField : RulesMap.keySet()) {
            textField.textProperty().addListener(observable -> {
                if (validateRules()){
                    btnUpdateRules.setDisable(false);
                }else {
                    btnUpdateRules.setDisable(true);
                }
            });
        }


        //-------------------listeners Book-------------------------------------
        cmbCategory_Books.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null) {
                //txtCategory_Books.setText(newValue);
                try {
                    txtRackNo_Books.setText(new BookRackController().getBookRackByCategory(newValue));
                    validateBook();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        cmbLanguage_Books.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null) {
                txtLanguage_Books.setText(newValue);

            }

        });

        tblBooks_Books.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue!=null) {
                    setBookDetails(newValue);
                    btnAddOrUpdate_Books.setText("Update");
                    btnDeleteBook_Book.setDisable(false);
                    selectedBookItem = newValue;
                    txtNoOfCopies_Books.setEditable(false);
                    txtDate_Books.setEditable(false);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        txtISBN_Books.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Book book=new BookController().getBook(newValue);
                if(book!=null){
                    txtTitle_Books.setText(book.getTitle());
                    txtAuthor_Books.setText(book.getAuthor());
                    cmbCategory_Books.setValue(book.getCategory());
                    cmbLanguage_Books.setValue(book.getLanguage());
                    if (book.getIsReferenceOnly().equals("Yes")){
                        chkBoxIsRefOnly_Book.setSelected(true);
                    }else {
                        chkBoxIsRefOnly_Book.setSelected(false);
                    }
                }else {
                    txtTitle_Books.clear();
                    txtAuthor_Books.clear();
                    cmbCategory_Books.setValue(null);
                    cmbLanguage_Books.setValue(null);
                    txtStatus_Books.clear();
                    chkBoxIsRefOnly_Book.setSelected(false);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        txtISBNSearch_Books.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue!=null){
                   setBooksTbl();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }



        });

        txtTitleSearch_Book.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue!=null){
                    setBooksTbl();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }



        });

        txtPriceBooks.textProperty().addListener((observable, oldValue, newValue) -> {

                if (newValue!=null){
                    txtStatus_Books.setText("Available");
                    txtDate_Books.setText(lblDate.getText());
                }
        });

        txtNoOfCopies_Books.textProperty().addListener(observable -> {
            cmbLanguage_Books.getSelectionModel().clearSelection();
        });

        for (TextField textField : BookMap.keySet()) {
            textField.textProperty().addListener(observable -> {
                validateBook();
            });
        }


        //-------------------listeners Report-------------------------------------

        rdBtnMember_Report.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==true){
                rdBtnBooks_Report.setSelected(false);
                rdBtnIncome_Report.setSelected(false);
                rdBtnAnnually_Report.setDisable(false);
                rdBtnMonthly_Report.setDisable(false);
                rdBtnDaily_Report.setDisable(false);
                memberVisible_Report();
                bookInvisible_Report();
                incomeInvisible();

                try {
                    tblMember_Report.setItems(new MemberController().getAllMembers());
                    setLabels();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        rdBtnBooks_Report.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==true){
                rdBtnMember_Report.setSelected(false);
                rdBtnIncome_Report.setSelected(false);
                rdBtnAllBook_Report.setSelected(true);
                rdBtnAnnually_Report.setDisable(true);
                rdBtnMonthly_Report.setDisable(true);
                rdBtnDaily_Report.setDisable(true);
                cmbYear_Report.setDisable(true);
                cmbMonth_Report.setDisable(true);
                dtPickerDay_Report.setDisable(true);
                bookVisible_Report();
                memberInvisible_Report();
                incomeInvisible();

                setLabels();
            }
        });

        rdBtnIncome_Report.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==true){
                rdBtnMember_Report.setSelected(false);
                rdBtnBooks_Report.setSelected(false);
                rdBtnAnnually_Report.setDisable(false);
                rdBtnMonthly_Report.setDisable(false);
                rdBtnDaily_Report.setDisable(false);
                rdBtnAllIncome_Report.setSelected(true);
                incomeVisible();
                memberInvisible_Report();
                bookInvisible_Report();
                setLabels();
            }
        });

        rdBtnAnnually_Report.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==true){
                rdBtnMonthly_Report.setSelected(false);
                rdBtnDaily_Report.setSelected(false);
                cmbYear_Report.setDisable(false);
                cmbMonth_Report.setDisable(true);
                dtPickerDay_Report.setDisable(true);

            }else {
                if (!rdBtnAnnually_Report.isSelected() && !rdBtnMonthly_Report.isSelected() && !rdBtnDaily_Report.isSelected()){
                    try {
                        tblMember_Report.setItems(new MemberController().getAllMembers());

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                cmbYear_Report.setDisable(true);
            }
        });

        rdBtnMonthly_Report.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==true){
                rdBtnAnnually_Report.setSelected(false);
                rdBtnDaily_Report.setSelected(false);
                cmbYear_Report.setDisable(false);
                cmbMonth_Report.setDisable(false);
                dtPickerDay_Report.setDisable(true);


            }else {
                if (!rdBtnAnnually_Report.isSelected() && !rdBtnMonthly_Report.isSelected() && !rdBtnDaily_Report.isSelected()){
                    try {
                        tblMember_Report.setItems(new MemberController().getAllMembers());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                cmbYear_Report.setDisable(true);
                cmbMonth_Report.setDisable(true);
            }

        });

        rdBtnDaily_Report.selectedProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue==true){
                rdBtnAnnually_Report.setSelected(false);
                rdBtnMonthly_Report.setSelected(false);
                cmbYear_Report.setDisable(true);
                cmbMonth_Report.setDisable(true);
                dtPickerDay_Report.setDisable(false);


            }else {
                if (!rdBtnAnnually_Report.isSelected() && !rdBtnMonthly_Report.isSelected() && !rdBtnDaily_Report.isSelected()){
                    try {
                        tblMember_Report.setItems(new MemberController().getAllMembers());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                dtPickerDay_Report.setDisable(true);
            }
        });


        cmbYear_Report.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            try {
                if (rdBtnMember_Report.isSelected()) {
                    tblMember_Report.setItems(new MemberController().getNewMembersInAYear(newValue));

                }
                if(rdBtnIncome_Report.isSelected()){
                    setIncomeAnnually(newValue);
                    rdBtnAllIncome_Report.setSelected(true);

                }
                setLabels();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });

        cmbMonth_Report.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            try {
                if (rdBtnMember_Report.isSelected()) {
                    tblMember_Report.setItems(new MemberController().getNewMembersInAMonth(cmbYear_Report.getValue(),newValue));

                }
                if(rdBtnIncome_Report.isSelected()){
                    setIncomeMonthly(cmbYear_Report.getValue(),newValue);
                    rdBtnAllIncome_Report.setSelected(true);
                }
                setLabels();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });

        dtPickerDay_Report.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (rdBtnMember_Report.isSelected()) {
                    tblMember_Report.setItems(new MemberController().getNewMembersInADay(newValue.toString()));

                }
                if (rdBtnIncome_Report.isSelected()){
                    setIncomeDaily(newValue);
                    rdBtnAllIncome_Report.setSelected(true);
                }
                setLabels();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });


        rdBtnAllBook_Report.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==true) {
                rdBtnRenewedBook_Report.setSelected(false);
                rdBtnAvailableBook_Report.setSelected(false);
                rdBtnBorrowedBook_Report.setSelected(false);

                try {
                    allBookItems = new BookItemController().getAllBooksItems();
                    tblBook_Report.setItems(allBookItems);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        rdBtnAvailableBook_Report.selectedProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<AdminBooks_Books> availableBooks=FXCollections.observableArrayList();
            if (newValue==true) {
                rdBtnRenewedBook_Report.setSelected(false);
                rdBtnAllBook_Report.setSelected(false);
                rdBtnBorrowedBook_Report.setSelected(false);

                for (AdminBooks_Books temp:allBookItems
                     ) {
                    if (temp.getStatus().equals("Available")){
                        availableBooks.add(temp);
                    }
                }
                tblBook_Report.setItems(availableBooks);
            }
        });

        rdBtnBorrowedBook_Report.selectedProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<AdminBooks_Books> borrowedBooks=FXCollections.observableArrayList();
            if (newValue==true) {

                rdBtnRenewedBook_Report.setSelected(false);
                rdBtnAvailableBook_Report.setSelected(false);
                rdBtnAllBook_Report.setSelected(false);

                for (AdminBooks_Books temp:allBookItems
                ) {
                    if (temp.getStatus().equals("Borrowed")){
                        borrowedBooks.add(temp);
                    }
                }
                tblBook_Report.setItems(borrowedBooks);
            }
        });

        rdBtnRenewedBook_Report.selectedProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<AdminBooks_Books> renewedBooks=FXCollections.observableArrayList();
            if (newValue==true) {

                rdBtnAllBook_Report.setSelected(false);
                rdBtnAvailableBook_Report.setSelected(false);
                rdBtnBorrowedBook_Report.setSelected(false);

                for (AdminBooks_Books temp:allBookItems
                ) {
                    if (temp.getStatus().equals("Renewed")){
                        renewedBooks.add(temp);
                    }
                }
                tblBook_Report.setItems(renewedBooks);
            }
        });

        rdBtnAllIncome_Report.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==true) {
                rdBtnFineIncome_Report.setSelected(false);
                rdBtnRegistrationFeeIncome_Report.setSelected(false);

                try {
                    if (rdBtnAnnually_Report.isSelected()||rdBtnMonthly_Report.isSelected()||rdBtnDaily_Report.isSelected()){
                        tblIncome_Report.setItems(income);
                    }else {
                        ObservableList<Income_Report> allIncome=FXCollections.observableArrayList();
                        for (MembershipPayments temp:new MembershipPaymentController().getAllPayments()
                        ) {
                            String paymentType=new MembershipPaymentMasterController().getPaymentType(temp.getPaymentId());
                            double paymentAmount=new MembershipPaymentMasterController().getPaymentAmount(temp.getPaymentId());
                            Income_Report payment = new Income_Report(temp.getMemberId(),paymentType,paymentAmount,temp.getDate());
                            allIncome.add(payment);

                        }

                        for (Fine temp:new FineController().getAllFinePayments()
                        ) {
                            String paymentType=new FineMasterController().getFineType(temp.getFineMasterId());
                            double paymentAmount=temp.getAmount();

                            Income_Report payment = new Income_Report(temp.getMemberId(),paymentType,paymentAmount,temp.getDate());
                            allIncome.add(payment);

                        }
                        tblIncome_Report.setItems(allIncome);



                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        rdBtnFineIncome_Report.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==true) {
                rdBtnAllIncome_Report.setSelected(false);
                rdBtnRegistrationFeeIncome_Report.setSelected(false);

                try {
                    ObservableList<Income_Report> fineIncome=FXCollections.observableArrayList();
                    if (rdBtnAnnually_Report.isSelected()||rdBtnMonthly_Report.isSelected()||rdBtnDaily_Report.isSelected()){
                        for (Income_Report income_report : income) {
                            if (!income_report.getPaymentType().equals("Registration Fee")){
                                fineIncome.add(income_report);
                            }
                        }

                    }else {

                        for (Fine temp:new FineController().getAllFinePayments()
                        ) {
                            String paymentType=new FineMasterController().getFineType(temp.getFineMasterId());
                            double paymentAmount=temp.getAmount();

                            Income_Report payment = new Income_Report(temp.getMemberId(),paymentType,paymentAmount,temp.getDate());
                            fineIncome.add(payment);

                        }

                    }
                    tblIncome_Report.setItems(fineIncome);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        rdBtnRegistrationFeeIncome_Report.selectedProperty().addListener((observable, oldValue, newValue) ->  {
            if (newValue==true) {
                rdBtnAllIncome_Report.setSelected(false);
                rdBtnFineIncome_Report.setSelected(false);
                try {
                    ObservableList<Income_Report> registrationFeeIncome=FXCollections.observableArrayList();
                    if (rdBtnAnnually_Report.isSelected()||rdBtnMonthly_Report.isSelected()||rdBtnDaily_Report.isSelected()){
                        for (Income_Report income_report : income) {
                            if (income_report.getPaymentType().equals("Registration Fee")){
                                registrationFeeIncome.add(income_report);
                            }
                        }
                    }else {

                        for (MembershipPayments temp:new MembershipPaymentController().getAllPayments()
                        ) {
                            String paymentType=new MembershipPaymentMasterController().getPaymentType(temp.getPaymentId());
                            double paymentAmount=new MembershipPaymentMasterController().getPaymentAmount(temp.getPaymentId());
                            Income_Report payment = new Income_Report(temp.getMemberId(),paymentType,paymentAmount,temp.getDate());
                            registrationFeeIncome.add(payment);

                        }


                    }
                    tblIncome_Report.setItems(registrationFeeIncome);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });




    }

    private void storeValidations() {
        usersMap.put(txtName_User,personName);
        usersMap.put(txtEmail_User,email);
        usersMap.put(txtContact_User,contactNo);
        usersMap.put(txtJobTitle_User,jobTitle);

        RulesMap.put(txtRegistrationFee_Administration,decimal);
        RulesMap.put(txtMaxNoOfBooksCanBeBorrowed_Books,number);
        RulesMap.put(txtMaxNoOfDaysBorrowedBookCanBeKept_Books,number);
        RulesMap.put(txtMaxNoOfTimesABookCanBeRenewed_Books,number);
        RulesMap.put(txtMaxNoOfDaysRenewedBookCanBeKept_Books,number);
        RulesMap.put(txtFinePerDay_Books,decimal);
        RulesMap.put(txtFineBookLost_Books,decimal);
        RulesMap.put(txtFineBookDamaged_Books,decimal);
        RulesMap.put(txtMaxNoOfDayaAReservationIsAvailable_Books,number);

        BookMap.put(txtISBN_Books,ISBN);
        BookMap.put(txtTitle_Books,bookName);
        BookMap.put(txtAuthor_Books,personName);
        BookMap.put(txtLanguage_Books,language);
        BookMap.put(txtNoOfCopies_Books,number);
        BookMap.put(txtNoOfPages_Books,number);
        BookMap.put(txtPriceBooks,decimal);
        BookMap.put(txtRackNo_Books,number);
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

    public  void logOutOnAction(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) adminFormContext.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/MainInterfaceForm.fxml"))));
        window.centerOnScreen();
    }

    //******************************  Administration*****************************************************************************

    public void createAndUpdateUserAccountOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String securityLevel = (rdBtnAdmin.isSelected()) ? "Admin" : (rdBtnUser.isSelected()) ? "User" : null;
        if (btnCreateAndUpdateAccount_User.getText().equals("Create Account")) {
            User user = new User(new UserController().getUserId(), txtName_User.getText(), txtContact_User.getText(), txtEmail_User.getText(), txtJobTitle_User.getText(), securityLevel, txtUserName_User.getText());
            UserAccount userAccount = new UserAccount(txtUserName_User.getText(), txtPassword_User.getText(), user);
            if (new UserAccountController().addUserAccount(userAccount)) {
                new Alert(Alert.AlertType.INFORMATION, "User Account Created Successfully").show();
                setUsersTable();
                clearAllUser();
            } else {
                new Alert(Alert.AlertType.WARNING, "Something went wrong.Try Again.").show();
            }
        }
        else {
            //-----update acc------------------------

            if (new UserController().UpdateUser(new User(selectedUser_User.getId(),txtName_User.getText(), txtContact_User.getText(), txtEmail_User.getText(), txtJobTitle_User.getText(), securityLevel, txtUserName_User.getText()))){
                new Alert(Alert.AlertType.INFORMATION, "User Account Updated Successfully").show();
                setUsersTable();
                clearAllUser();
            } else {
                new Alert(Alert.AlertType.WARNING, "Something went wrong.Try Again.").show();
            }


        }




    }

    public void deleteAccountOnAction_User(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ButtonType yes =new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no =new ButtonType("No", ButtonBar.ButtonData.OK_DONE);
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this account ?",yes,no);
        alert.setTitle("Confirmation Alert");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.orElse(no)==yes){
            if (new UserAccountController().deleteUserAccount(selectedUser_User.getUsername())){
                new Alert(Alert.AlertType.INFORMATION, "User account has been deleted successfully").show();
                setUsersTable();
                clearAllUser();
            } else {
                new Alert(Alert.AlertType.WARNING, "Something went wrong.Try Again.").show();
            }
        }

    }

    private void setUsersTable() throws SQLException, ClassNotFoundException {
       tblUsers_User.setItems(new UserController().getUsers());
    }

    private void setUserDetails(User user) throws SQLException, ClassNotFoundException {
        txtName_User.setText(user.getName());
        txtContact_User.setText(user.getContactNo());
        txtEmail_User.setText(user.getEmail());
        txtJobTitle_User.setText(user.getJobTitle());
        if(user.getSecurityLevel().equals("Admin")){rdBtnAdmin.setSelected(true);}else {rdBtnUser.setSelected(true);}
        txtUserName_User.setText(user.getUsername());
        txtPassword_User.setText(new UserAccountController().getPassword(user.getUsername()));

    }

    public void setRules() throws SQLException, ClassNotFoundException {
        for (Information temp:new InformationController().getAllInformation()
        ) {
            if (temp.getId().equals("I-001")){txtMaxNoOfBooksCanBeBorrowed_Books.setText(temp.getData());}
            if (temp.getId().equals("I-002")){txtMaxNoOfDaysBorrowedBookCanBeKept_Books.setText(temp.getData());}
            if (temp.getId().equals("I-003")){txtMaxNoOfDaysRenewedBookCanBeKept_Books.setText(temp.getData());}
            if (temp.getId().equals("I-004")){txtMaxNoOfTimesABookCanBeRenewed_Books.setText(temp.getData());}
            if (temp.getId().equals("I-005")){txtFinePerDay_Books.setText(temp.getData());}
            if (temp.getId().equals("I-006")){txtFineBookLost_Books.setText(temp.getData());}
            if (temp.getId().equals("I-007")){txtFineBookDamaged_Books.setText(temp.getData());}
            if (temp.getId().equals("I-008")){txtMaxNoOfDayaAReservationIsAvailable_Books.setText(temp.getData());}

        }

        txtRegistrationFee_Administration.setText(String.valueOf(new MembershipPaymentMasterController().getPaymentAmount("P-01")));
    }

    private void clearAllUser(){
        txtName_User.clear();
        txtUserName_User.clear();
        txtContact_User.clear();
        txtEmail_User.clear();
        txtJobTitle_User.clear();
        rdBtnAdmin.setSelected(false);
        rdBtnUser.setSelected(false);
        txtUserName_User.clear();
        txtPassword_User.clear();
        btnCreateAndUpdateAccount_User.setText("Create Account");
        txtPassword_User.setDisable(false);
        txtUserName_User.setDisable(false);
        btnDeleteAccount_Administration.setDisable(true);
        for (TextField textField : usersMap.keySet()) {
            textField.setStyle("-fx-border-color: silver;"+"-fx-border-width: 1");
        }
        btnUpdateRules.setDisable(true);
        for (TextField textField : RulesMap.keySet()) {
            textField.setStyle("-fx-border-color: silver;"+"-fx-border-width: 1");
        }

    }

    public void cancelOnActionCreateAccount_User(ActionEvent actionEvent) {
        clearAllUser();
    }

    public void cancelOnActionRules_Admin(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        setRules();
        btnUpdateRules.setDisable(true);
        for (TextField textField : RulesMap.keySet()) {
            textField.setStyle("-fx-border-color: silver;"+"-fx-border-width: 1");
        }
    }

    public boolean validateUserDetails(){
        for (TextField textField : usersMap.keySet()) {
            Pattern pattern = usersMap.get(textField);
            if (!pattern.matcher(textField.getText()).matches()){
                textField.setStyle("-fx-border-color: red;"+"-fx-border-width: 2;");
                return false;
            }else {
                textField.setStyle("-fx-border-color: green;"+"-fx-border-width: 2;");
            }
        }

        return true;
    }

    public void validateUser(){
        boolean validate = validateUserDetails();
        boolean userLevelSelected=(rdBtnUser.isSelected()|rdBtnAdmin.isSelected())?true:false;
        boolean userNameGiven=(!txtUserName_User.getText().isEmpty())?true:false;
        boolean passwordGiven=(!txtPassword_User.getText().isEmpty())?true:false;

        if (validate && userLevelSelected && userNameGiven && passwordGiven){
            btnCreateAndUpdateAccount_User.setDisable(false);
        }else {
            btnCreateAndUpdateAccount_User.setDisable(true);
        }
    }

    public boolean validateRules(){
        for (TextField textField : RulesMap.keySet()) {
            Pattern pattern = RulesMap.get(textField);
            if (!pattern.matcher(textField.getText()).matches()){
                textField.setStyle("-fx-border-color: red;"+"-fx-border-width: 2;");
                return false;
            }else {
                textField.setStyle("-fx-border-color: green;"+"-fx-border-width: 2;");
            }
        }
        return true;
    }

    public void updateRulesOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(
                new InformationController().updateInfo("I-001",txtMaxNoOfBooksCanBeBorrowed_Books.getText()) &&
                        new InformationController().updateInfo("I-002",txtMaxNoOfDaysBorrowedBookCanBeKept_Books.getText()) &&
                        new InformationController().updateInfo("I-003",txtMaxNoOfDaysRenewedBookCanBeKept_Books.getText()) &&
                        new InformationController().updateInfo("I-004",txtMaxNoOfTimesABookCanBeRenewed_Books.getText()) &&
                        new InformationController().updateInfo("I-005",txtFinePerDay_Books.getText()) &&
                        new InformationController().updateInfo("I-006",txtFineBookLost_Books.getText()) &&
                        new InformationController().updateInfo("I-007",txtFineBookDamaged_Books.getText())&&
                        new InformationController().updateInfo("I-008",txtMaxNoOfDayaAReservationIsAvailable_Books.getText())
        ){

            new Alert(Alert.AlertType.INFORMATION, "Updated Successfully").show();
            setRules();
            btnUpdateRules.setDisable(true);
            for (TextField textField : RulesMap.keySet()) {
                textField.setStyle("-fx-border-color: silver;"+"-fx-border-width: 1");
            }

        }else {
            new Alert(Alert.AlertType.WARNING, "Something went wrong.Try Again.").show();
        }
    }

    //******************************** BOOK ****************************************************************************

    public void addOrUpdateBookOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ArrayList<BookItem> bookItems = new ArrayList<>();

        int copies = Integer.parseInt(txtNoOfCopies_Books.getText());
        int noOfPages = Integer.parseInt(txtNoOfPages_Books.getText());
        String status = txtStatus_Books.getText();
        double price = Double.parseDouble(txtPriceBooks.getText());
        String ISBN = txtISBN_Books.getText();
        String category = cmbCategory_Books.getValue();
        int rackNo = Integer.parseInt(txtRackNo_Books.getText());
        String isRefOnly = (chkBoxIsRefOnly_Book.isSelected()) ? "Yes" : "No";
        String title = txtTitle_Books.getText();
        String author = txtAuthor_Books.getText();
        String language = txtLanguage_Books.getText();
        String date=txtDate_Books.getText();

        if (btnAddOrUpdate_Books.getText().equals("Add")){
            int newId=new BookItemController().getBookItemId()+1;
            for (int i=0;i<copies;i++){
                int index=newId+i;
                String id=(index<10)?"B-0000"+index:(index<100)?"B-000"+index:(index<1000)?"B-00"+index:(index<10000)?"B-0"+index:"B-"+index;
                bookItems.add(new BookItem(id,status,noOfPages, price,ISBN, rackNo,date));
            }

            Book book=new Book(ISBN, title, author,category,isRefOnly, language,copies,bookItems);

            if (new BookController().getBook(ISBN)==null) {
                if (new BookController().addBook(book)) {
                    System.out.println(1);
                    new Alert(Alert.AlertType.INFORMATION, "Book has been added Successfully").show();
                    setBooksTbl();
                    clearBookDetails_Book();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Something went wrong.Try Again.").show();
                }
            }else {

                if (new BookItemController().addBookItems(bookItems)) {
                    System.out.println(2);
                    new Alert(Alert.AlertType.INFORMATION, "Book has been added Successfully").show();
                    setBooksTbl();
                    clearBookDetails_Book();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Something went wrong.Try Again.").show();
                }
            }
        }else {
            //-------------------------update book-----------------------

            BookItem bookItem = new BookItem(selectedBookItem.getBkId(), status,noOfPages, price, ISBN, rackNo,date);
            bookItems.add(bookItem);
            Book book=new Book(ISBN, title, author,category,isRefOnly, language,copies,bookItems);
            if (new BookController().updateBook(book)) {
                new Alert(Alert.AlertType.INFORMATION, "Book has been updated").show();
                setBooksTbl();
                clearBookDetails_Book();
                txtNoOfCopies_Books.setEditable(true);
            } else {
                new Alert(Alert.AlertType.WARNING, "Something went wrong.Try Again.").show();
            }

        }

    }

    public void setBooksTbl() throws SQLException, ClassNotFoundException{
        searchList.clear();
        ObservableList<AdminBooks_Books> searchedBooksFromISBN = new BookItemController().searchBookByISBN(txtISBNSearch_Books.getText());
        ObservableList<Book> searchedBooksFromTitle = new BookController().searchBookByName(txtTitleSearch_Book.getText());

        if (!txtISBNSearch_Books.getText().isEmpty()&&!txtTitleSearch_Book.getText().isEmpty()){
            for (AdminBooks_Books ISBNSearchedBook : searchedBooksFromISBN) {
                for (Book book : searchedBooksFromTitle) {
                    if (ISBNSearchedBook.getISBN().equals(book.getISBN())){
                        searchList.add(ISBNSearchedBook);
                    }
                }
            }
        }else if (!txtISBNSearch_Books.getText().isEmpty()){
            searchList=searchedBooksFromISBN;

        }else if (!txtTitleSearch_Book.getText().isEmpty()){
            for (AdminBooks_Books search : allBookItems) {
                for (Book titleSearch : searchedBooksFromTitle) {
                    if (search.getISBN().equals(titleSearch.getISBN())) {
                        searchList.add(search);
                    }
                }
            }
        }else {
            for (AdminBooks_Books allBookItem :new BookItemController().getAllBooksItems()) {
                searchList.add(allBookItem);
            }
        }
        tblBooks_Books.setItems(searchList);

    }

    public void setBookDetails(AdminBooks_Books AdminBook) throws SQLException, ClassNotFoundException {
        BookItem bookItem=new BookItemController().getBookItem(AdminBook.getBkId());
        Book book=new BookController().getBook(AdminBook.getISBN());

        txtISBN_Books.setText(book.getISBN());
        txtTitle_Books.setText(book.getTitle());
        txtAuthor_Books.setText(book.getAuthor());
        cmbCategory_Books.setValue(book.getCategory());
        txtLanguage_Books.setText(book.getLanguage() );
        txtStatus_Books.setText(bookItem.getStatus());
        txtNoOfPages_Books.setText(String.valueOf(bookItem.getNoOfPages()));
        txtPriceBooks.setText(String.valueOf(bookItem.getPrice()));
        txtRackNo_Books.setText(String.valueOf(bookItem.getRackNo()));
        txtDate_Books.setText(bookItem.getDate());
        if (book.getIsReferenceOnly().equals("Yes")){
            chkBoxIsRefOnly_Book.setSelected(true);
        }else {
            chkBoxIsRefOnly_Book.setSelected(false);
        }

        txtNoOfCopies_Books.setText(String.valueOf(new BookItemController().getNoOfBookItemsFromABook(book.getISBN())));

    }

    public void clearBookDetails_Book() throws SQLException, ClassNotFoundException {

        txtISBN_Books.clear();
        txtISBN_Books.clear();
        txtTitle_Books.clear();
        txtAuthor_Books.clear();
        cmbCategory_Books.setValue(null);
        cmbLanguage_Books.setValue(null);
        txtNoOfPages_Books.clear();
        txtPriceBooks.clear();
        txtRackNo_Books.clear();
        txtNoOfCopies_Books.clear();
        txtNoOfCopies_Books.setEditable(true);
        txtDate_Books.setEditable(true);
        chkBoxIsRefOnly_Book.setSelected(false);
        txtNoOfCopies_Books.setDisable(false);
        btnAddOrUpdate_Books.setText("Add");
        btnDeleteBook_Book.setDisable(true);
        btnAddOrUpdate_Books.setDisable(true);
        txtStatus_Books.clear();
        txtLanguage_Books.clear();
        txtDate_Books.clear();

        ObservableList<String> bookLanguages=new BookController().getAllLanguages();

        cmbLanguage_Books.setItems(bookLanguages);
        for (TextField textField : BookMap.keySet()) {
            textField.setStyle("-fx-border-color: silver;"+"-fx-border-width: 1");
        }

    }

    public void deleteBookItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ButtonType yes =new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no =new ButtonType("No", ButtonBar.ButtonData.OK_DONE);
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this Book ?",yes,no);
        alert.setTitle("Confirmation Alert");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.orElse(no)==yes){
            if (new BookItemController().deleteBookItem(selectedBookItem)){
                new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully").show();
                setBooksTbl();
                clearBookDetails_Book();
                txtNoOfCopies_Books.setEditable(true);
            }else {
                new Alert(Alert.AlertType.WARNING, "Something went wrong.Try Again.").show();
            }
        }

    }

    public void cancelOnAction_Book(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearBookDetails_Book();
    }

    public boolean validateBookDetails(){
        for (TextField textField : BookMap.keySet()) {
            Pattern pattern = BookMap.get(textField);
            if (!pattern.matcher(textField.getText()).matches()){
                textField.setStyle("-fx-border-color: red;"+"-fx-border-width: 2;");
                return false;
            }else {
                textField.setStyle("-fx-border-color: green;"+"-fx-border-width: 2;");
            }
        }

        return true;
    }

    public void validateBook(){
        boolean validate = validateBookDetails();
        boolean categorySelected=cmbCategory_Books.getValue()!=null;


        if (validate && categorySelected){
            btnAddOrUpdate_Books.setDisable(false);
        }else {
            btnAddOrUpdate_Books.setDisable(true);
        }
    }

    //********************************   Report ************************************************************************

    private void memberInvisible_Report(){
        tblMember_Report.setVisible(false);
    }

    private void memberVisible_Report(){

        tblMember_Report.setVisible(true);
    }

    private void setMemberTable_Report(ObservableList<Member> member){

        tblMember_Report.setItems(member);
    }



    private void setIncomeAnnually(int year) throws SQLException, ClassNotFoundException {
        income.clear();
        for (MembershipPayments temp:new MembershipPaymentController().getAllPaymentsAnnually(year)
        ) {
            String paymentType=new MembershipPaymentMasterController().getPaymentType(temp.getPaymentId());
            double paymentAmount=new MembershipPaymentMasterController().getPaymentAmount(temp.getPaymentId());
            Income_Report payment = new Income_Report(temp.getMemberId(),paymentType,paymentAmount,temp.getDate());
            income.add(payment);

        }

        for (Fine temp:new FineController().getAllFinePaymentsAnnually(year)
        ) {
            String paymentType=new FineMasterController().getFineType(temp.getFineMasterId());
            double paymentAmount=temp.getAmount();
            Income_Report payment = new Income_Report(temp.getMemberId(),paymentType,paymentAmount,temp.getDate());
            income.add(payment);

        }
        tblIncome_Report.setItems(income);
    }

    private void setIncomeMonthly(int year,String month) throws SQLException, ClassNotFoundException {
        income.clear();
        for (MembershipPayments temp:new MembershipPaymentController().getAllPaymentsMonthly(year,month)
        ) {
            String paymentType=new MembershipPaymentMasterController().getPaymentType(temp.getPaymentId());
            double paymentAmount=new MembershipPaymentMasterController().getPaymentAmount(temp.getPaymentId());
            Income_Report payment = new Income_Report(temp.getMemberId(),paymentType,paymentAmount,temp.getDate());
            income.add(payment);

        }

        for (Fine temp:new FineController().getAllFinePaymentsMonthly(year,month)
        ) {
            String paymentType=new FineMasterController().getFineType(temp.getFineMasterId());
            double paymentAmount=temp.getAmount();
            Income_Report payment = new Income_Report(temp.getMemberId(),paymentType,paymentAmount,temp.getDate());
            income.add(payment);

        }
        tblIncome_Report.setItems(income);

    }

    private void setIncomeDaily(LocalDate date) throws SQLException, ClassNotFoundException {
        income.clear();
        for (MembershipPayments temp:new MembershipPaymentController().getAllPaymentsDaily(date.toString())
        ) {
            String paymentType=new MembershipPaymentMasterController().getPaymentType(temp.getPaymentId());
            double paymentAmount=new MembershipPaymentMasterController().getPaymentAmount(temp.getPaymentId());
            Income_Report payment = new Income_Report(temp.getMemberId(),paymentType,paymentAmount,temp.getDate());
            income.add(payment);

        }

        for (Fine temp:new FineController().getAllFinePaymentsDaily(date.toString())
        ) {
            String paymentType=new FineMasterController().getFineType(temp.getFineMasterId());
            double paymentAmount=temp.getAmount();
            Income_Report payment = new Income_Report(temp.getMemberId(),paymentType,paymentAmount,temp.getDate());
            income.add(payment);

        }
        tblIncome_Report.setItems(income);

    }

    private void bookInvisible_Report(){
        tblBook_Report.setVisible(false);
        rdBtnAllBook_Report.setVisible(false);
        rdBtnBorrowedBook_Report.setVisible(false);
        rdBtnRenewedBook_Report.setVisible(false);
        rdBtnAvailableBook_Report.setVisible(false);
    }

    private void bookVisible_Report(){
        tblBook_Report.setVisible(true);
        rdBtnAllBook_Report.setVisible(true);
        rdBtnBorrowedBook_Report.setVisible(true);
        rdBtnRenewedBook_Report.setVisible(true);
        rdBtnAvailableBook_Report.setVisible(true);
    }

    private void incomeInvisible(){
        tblIncome_Report.setVisible(false);
        rdBtnAllIncome_Report.setVisible(false);
        rdBtnRegistrationFeeIncome_Report.setVisible(false);
        rdBtnFineIncome_Report.setVisible(false);
    }

    private void incomeVisible(){
        tblIncome_Report.setVisible(true);
        rdBtnAllIncome_Report.setVisible(true);
        rdBtnRegistrationFeeIncome_Report.setVisible(true);
        rdBtnFineIncome_Report.setVisible(true);
    }

    private void setYearAndMonth(){
        ObservableList<Integer> year=FXCollections.observableArrayList();
        for (int i = 2020; i <=Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())); i++) {
            year.add(i);
        }
        ObservableList<String> months = FXCollections.observableArrayList("01","02","03","04","05","06","07","08","09","10","11","12");

        cmbMonth_Report.setItems(months);
        cmbYear_Report.setItems(year);
    }

    public void setLabels(){
        if(rdBtnMember_Report.isSelected()){
            lbl3_Report.setVisible(true);
            lbl1_Report.setVisible(false);
            lbl1_Report.setVisible(false);
            lbl3_Report.setText("TOTAL : "+tblMember_Report.getItems().size());
        }else if(rdBtnBooks_Report.isSelected()){
            lbl1_Report.setVisible(true);
            lbl2_Report.setVisible(true);
            lbl3_Report.setVisible(true);
            lbl1_Report.setText("ALL : "+tblBook_Report.getItems().size());
            int available=0;
            for (AdminBooks_Books item : tblBook_Report.getItems()) {
                if (item.getStatus().equals("Available")){
                    available++;
                }
            }
            lbl2_Report.setText("AVAILABLE : "+available);
            lbl3_Report.setText("NOT AVAILABLE : "+(tblBook_Report.getItems().size()-available));

        }else if (rdBtnIncome_Report.isSelected()){
            lbl1_Report.setVisible(true);
            lbl2_Report.setVisible(true);
            lbl3_Report.setVisible(true);

            double total=0;
            double fine=0;
            double registrationFees=0;
            for (Income_Report item : tblIncome_Report.getItems()) {
                total+=item.getAmount();
                if(item.getPaymentType().equals("Registration Fee")){
                    registrationFees+=item.getAmount();
                }else {
                    fine+=item.getAmount();
                }
            }
            lbl1_Report.setText("TOTAL : "+total);
            lbl1_Report.setText("FINE : "+fine);
            lbl1_Report.setText("REGISTRATION FEE : "+registrationFees);

        }else {
            incomeInvisible();
            bookInvisible_Report();
            memberInvisible_Report();

        }
    }
}
