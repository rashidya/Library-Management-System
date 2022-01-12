package model;

public class BookRack {
    private int rackNo;
    private String category;
    private String location;

    public BookRack() {
    }

    public BookRack(int rackNo, String category, String location) {
        this.rackNo = rackNo;
        this.category = category;
        this.location = location;
    }

    public int getRackNo() {
        return rackNo;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }
}
