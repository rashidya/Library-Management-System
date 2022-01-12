package view.tm;

import model.BookItem;

import java.util.ArrayList;

public class AdminBooks_Books {
    private String ISBN;
    private String bkId;
    private String title;
    private String author;
    private String category;
    private String language;
    private String status;
    private int rackNo;

    public AdminBooks_Books() {
    }

    public AdminBooks_Books(String ISBN, String bkId, String title, String author, String category, String language) {
        this.ISBN = ISBN;
        this.bkId = bkId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.language = language;
    }

    public AdminBooks_Books(String ISBN, String bkId, String title, String author, String category, String language, String status, int rackNo) {
        this.ISBN = ISBN;
        this.bkId = bkId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.language = language;
        this.status = status;
        this.rackNo = rackNo;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getBkId() {
        return bkId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getLanguage() {
        return language;
    }

    public String getStatus() {
        return status;
    }

    public int getRackNo() {
        return rackNo;
    }
}
