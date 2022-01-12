package model;

public class ReservedBookDetails {
    private String ISBN;
    private String memberId;
    private String dateOfReservation;
    private String status;
    private String availableTill;

    public ReservedBookDetails() {
    }

    public ReservedBookDetails(String ISBN, String memberId, String dateOfReservation, String status, String availableTill) {
        this.ISBN = ISBN;
        this.memberId = memberId;
        this.dateOfReservation = dateOfReservation;
        this.status = status;
        this.availableTill = availableTill;
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

    public String getAvailableTill() {
        return availableTill;
    }
}
