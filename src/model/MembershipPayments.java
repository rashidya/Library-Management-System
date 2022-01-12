package model;

public class MembershipPayments {
    private String id;
    private String date;
    private String memberId;
    private String paymentId;

    public MembershipPayments() {
    }

    public MembershipPayments(String id, String date, String memberId, String paymentId) {
        this.setId(id);
        this.setDate(date);
        this.setMemberId(memberId);
        this.setPaymentId(paymentId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
