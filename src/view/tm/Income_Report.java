package view.tm;

public class Income_Report {
    private String memberId;
    private String paymentType;
    private double amount;
    private String date;

    public Income_Report() {
    }

    public Income_Report(String memberId, String paymentType, double amount, String date) {
        this.memberId = memberId;
        this.paymentType = paymentType;
        this.amount = amount;
        this.date = date;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
