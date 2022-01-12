package view.tm;

import com.jfoenix.controls.JFXButton;

import java.awt.*;

public class Reservation {
    private String ISBN;
    private String memberId;
    private String dateOfReservation;
    private String status;
    private JFXButton btn;

    public Reservation() {
    }

    public Reservation(String ISBN, String memberId, String dateOfReservation, String status, JFXButton btn) {
        this.ISBN = ISBN;
        this.memberId = memberId;
        this.dateOfReservation = dateOfReservation;
        this.status = status;
        this.btn = btn;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getDateOfReservation() {
        return dateOfReservation;
    }

    public String getStatus() {
        return status;
    }

    public JFXButton getBtn() {
        return btn;
    }
}
