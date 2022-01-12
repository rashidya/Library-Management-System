package model;

public class Member {
    private String id;
    private String name;
    private String address;
    private String contactNo;
    private String email;
    private int age;
    private String dateOfMembership;
    private MembershipPayments membershipPayments;

    public Member() {
    }

    public Member(String id, String name, String address, String contactNo, String email, int age, String dateOfMembership) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contactNo = contactNo;
        this.email = email;
        this.age = age;
        this.dateOfMembership = dateOfMembership;
    }

    public Member(String id, String name, String address, String contactNo, String email, int age, String dateOfMembership, MembershipPayments membershipPayments) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contactNo = contactNo;
        this.email = email;
        this.age = age;
        this.dateOfMembership = dateOfMembership;
        this.membershipPayments = membershipPayments;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getDateOfMembership() {
        return dateOfMembership;
    }

    public MembershipPayments getMembershipPayments() {
        return membershipPayments;
    }
}
