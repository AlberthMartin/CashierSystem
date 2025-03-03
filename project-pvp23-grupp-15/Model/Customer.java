package Model;

import java.util.Date;

public class Customer {

    private String customerNo;
    private String optLockVersion;
    private String firstName;
    private String lastName;
    private Date birthDate;

    // address information
    private String streetAddress;
    private String postalCode;
    private String postOffice;
    private String country;

    // bonusCard information
    private String cardNumber;
    private int goodThruMonth;
    private int goodThruYear;
    private boolean blocked;
    private boolean expired;
    private String holderName;
    private String sex;
    // Points that the customer gets after a purchase, depending on how much the customer spent.
    // bonusPoint = 0.05*totalPrice of purchase
    private double bonusPoints;
    public Customer() {}
    public double getBonusPoints() {
        return bonusPoints;
    }

    public void addBonusPoints(double bonusPoints) {
        this.bonusPoints += bonusPoints;
    }


    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getOptLockVersion() {
        return optLockVersion;
    }

    public void setOptLockVersion(String optLockVersion) {
        this.optLockVersion = optLockVersion;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostOffice() {
        return postOffice;
    }

    public void setPostOffice(String postOffice) {
        this.postOffice = postOffice;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getGoodThruMonth() {
        return goodThruMonth;
    }

    public void setGoodThruMonth(int goodThruMonth) {
        this.goodThruMonth = goodThruMonth;
    }

    public int getGoodThruYear() {
        return goodThruYear;
    }

    public void setGoodThruYear(int goodThruYear) {
        this.goodThruYear = goodThruYear;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String toString() {
        return firstName + lastName;
    }
}

