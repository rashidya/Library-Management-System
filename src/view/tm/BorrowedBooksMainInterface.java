package view.tm;

public class BorrowedBooksMainInterface {
    private String bkItemId;
    private String title;
    private String borrowedDate;
    private String dueDate;
    private double overdueFine;

    public BorrowedBooksMainInterface() {
    }

    public BorrowedBooksMainInterface(String bkItemId, String title, String borrowedDate, String dueDate, double overdueFine) {
        this.bkItemId = bkItemId;
        this.title = title;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.overdueFine = overdueFine;
    }

    public String getBkItemId() {
        return bkItemId;
    }

    public String getTitle() {
        return title;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public double getOverdueFine() {
        return overdueFine;
    }
}
