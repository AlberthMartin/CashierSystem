package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Product {
    private double price;
    private String name;
    private String vat;
    private String keyword;
    private String barCode;
    private double temporaryDiscountPrice;
    private double bonusCustomerPrice;
    private double discountForEveryonePrice;
    private String discountType;
    private double DiscountPrice;
    private List<String> productSaleHistoryList;

    public List<String> getProductSaleHistoryList() {
        return productSaleHistoryList;
    }


    //The number of this product that have been sold
    private int soldProducts;

    public int getSoldProducts() {
        return soldProducts;
    }

    public void addToSoldProducts() {
        this.soldProducts += 1;
    }

    public void subToSoldProducts(){
        if(soldProducts != 0) {
            this.soldProducts -= 1;
        }
        else{
            this.soldProducts = soldProducts;
        }
    }






    public Product(String name, String vat, String keyword, String barCode) {
        this.name = name;
        this.vat = vat;
        this.keyword = keyword;
        this.barCode = barCode;
        discountType="";
        productSaleHistoryList = new ArrayList<String>();
    }

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getName() {
        return name;
    }

    public String getVat() {
        return vat;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getBarCode() {
        return barCode;
    }

    public String toString(){
            return name + " " + price + " €";
    }

    public void saleHistory(Date date){
        productSaleHistoryList.add("Date: "+date + " " + name + " was sold");
    }

    public double getTemporaryDiscountPrice() {
        return temporaryDiscountPrice;
    }

    public void setTemporaryDiscountPrice(double temporaryDiscountPrice) {
        this.temporaryDiscountPrice = temporaryDiscountPrice;
    }

    public double getBonusCustomerPrice() {
        return bonusCustomerPrice;
    }

    public void setBonusCustomerPrice(double bonusCustomerPrice) {
        this.bonusCustomerPrice = bonusCustomerPrice;
    }

    public double getDiscountForEveryonePrice() {
        return discountForEveryonePrice;
    }

    public void setDiscountForEveryonePrice(double discountForEveryonePrice) {
        this.discountForEveryonePrice = discountForEveryonePrice;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getDiscountPrice() {
        return DiscountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        DiscountPrice = discountPrice;
    }
}