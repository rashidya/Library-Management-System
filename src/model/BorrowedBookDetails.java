package model;

public class BorrowedBookDetails {
    private String bkItemId;
    private String memberId;
    private String borrowedDate;
    private String dueDate;
    private String status;
    private int renewedTimes;

    public BorrowedBookDetails() {
    }

    public BorrowedBookDetails(String bkItemId, String memberId, String borrowedDate, String dueDate, String status, int renewedTimes) {
        this.bkItemId = bkItemId;
        this.memberId = memberId;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.status = status;
        this.renewedTimes = renewedTimes;
    }

    public String getBkItemId() {
        return bkItemId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public int getRenewedTimes() {
        return renewedTimes;
    }
}
