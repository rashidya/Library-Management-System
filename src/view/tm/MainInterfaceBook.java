package view.tm;

public class MainInterfaceBook {
    private String ISBN;
    private String bookName;
    private String author;
    private String availability;
    private String rackNo;

    public MainInterfaceBook() {
    }

    public MainInterfaceBook(String ISBN, String bookName, String author, String availability, String rackNo) {
        this.ISBN = ISBN;
        this.bookName = bookName;
        this.author = author;
        this.availability = availability;
        this.rackNo = rackNo;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public String getAvailability() {
        return availability;
    }

    public String getRackNo() {
        return rackNo;
    }
}
