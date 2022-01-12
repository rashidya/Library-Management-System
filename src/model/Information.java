package model;

public class Information {
    private String id;
    private String description;
    private String data;

    public Information() {
    }

    public Information(String id, String description, String data) {
        this.id = id;
        this.description = description;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getData() {
        return data;
    }
}
