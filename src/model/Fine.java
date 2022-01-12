package model;

public class Fine {
    private String date;
    private double amount;
    private String memberId;
    private String fineMasterId;

    public Fine() {
    }

    public Fine(String date, double amount, String memberId, String fineMasterId) {
        this.date = date;
        this.amount = amount;
        this.memberId = memberId;
        this.fineMasterId = fineMasterId;
    }



    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getFineMasterId() {
        return fineMasterId;
    }
}
