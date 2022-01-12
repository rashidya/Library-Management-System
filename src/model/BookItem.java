package model;

public class BookItem {
    private String bkItemId;
    private String status;
    private int noOfPages;
    private double price;
    private String ISBN;
    private int rackNo;
    private String date;

    public BookItem() {
    }

    public BookItem(String bkItemId, String status, int noOfPages, double price, String ISBN, int rackNo, String date) {
        this.bkItemId = bkItemId;
        this.status = status;
        this.noOfPages = noOfPages;
        this.price = price;
        this.ISBN = ISBN;
        this.rackNo = rackNo;
        this.date = date;
    }

    public String getBkItemId() {
        return bkItemId;
    }

    public String getStatus() {
        return status;
    }

    public int getNoOfPages() {
        return noOfPages;
    }

    public double getPrice() {
        return price;
    }

    public String getISBN() {
        return ISBN;
    }

    public int getRackNo() {
        return rackNo;
    }

    public String getDate() {
        return date;
    }
}
