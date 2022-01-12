package model;

import java.util.ArrayList;

public class Book {
    private String ISBN;
    private String title;
    private String author;
    private String category;
    private String isReferenceOnly;
    private String language;
    private int noOfCopies;
    private ArrayList<BookItem> bookItems;

    public Book() {
    }

    public Book(String ISBN, String title, String author, String category, String isReferenceOnly, String language, int noOfCopies) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isReferenceOnly = isReferenceOnly;
        this.language = language;
        this.noOfCopies = noOfCopies;
    }

    public Book(String ISBN, String title, String author, String category, String isReferenceOnly, String language, int noOfCopies, ArrayList<BookItem> bookItems) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isReferenceOnly = isReferenceOnly;
        this.language = language;
        this.noOfCopies = noOfCopies;
        this.bookItems = bookItems;
    }

    public String getISBN() {
        return ISBN;
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

    public String getIsReferenceOnly() {
        return isReferenceOnly;
    }

    public String getLanguage() {
        return language;
    }

    public int getNoOfCopies() {
        return noOfCopies;
    }

    public ArrayList<BookItem> getBookItems() {
        return bookItems;
    }
}
