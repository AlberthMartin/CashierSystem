package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;

public class Sale {

    //The price of the sale
    private double totalPrice;
    private List<Product> productList;
    private boolean BonusRegistered;

    public Sale(){
        //Price of the sale is initially 0
        totalPrice = 0;
        productList = new ArrayList<Product>();
    }
    public double getTotalPrice(){
        return totalPrice;
    }
    public void setTotalPrice(double totalprice){
        totalPrice = totalprice;
    }
    public List<Product> getProductList(){
        return productList;
    }

    //Adds a product to the sale and updates the total price
    public void addProduct(Product product){
        Date date = new Date();
        productList.add(product);
        totalPrice += product.getPrice();
        product.addToSoldProducts();
        product.saleHistory(date);
    }
    public void removeProduct(Product product){
        productList.remove(product);
        totalPrice -= product.getPrice();
        product.subToSoldProducts();
    }
    public void addDiscountedProduct(Product product){
        if(product.getDiscountType().equals("everyone")){
            productList.add(product);
            totalPrice += product.getDiscountForEveryonePrice();
        }
        else if(product.getDiscountType().equals("bonusc") && BonusRegistered){
            productList.add(product);
            totalPrice += product.getBonusCustomerPrice();
        }
        else{
            System.out.println("Someting went wrong when adding discounted product");
        }

    }
    public void removeDiscountedProduct(Product product){
        if(product.getDiscountType().equals("everyone")){
            productList.remove(product);
            totalPrice -= product.getDiscountForEveryonePrice();
        }
        else if(product.getDiscountType().equals("bonusc") && BonusRegistered){
            productList.remove(product);
            totalPrice -= product.getBonusCustomerPrice();
        }
        else{
            System.out.println("Something went wrong when removing discounted product");
        }
    }


    //Prints the receipt for the sale
    public void printReceipt(){
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Receipt.pdf"));
            document.open();
            document.add(new Paragraph("Receipt:\n"));
            document.add(new Paragraph(ReceiptProducts()));
            document.add(new Paragraph("Total sum:\n"));
            document.add(new Paragraph(ReceiptTotPrice()));
            document.close();
            if (Desktop.isDesktopSupported()) {
                File myFile = new File("Receipt.pdf");
                Desktop.getDesktop().open(myFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String ReceiptProducts(){
        String productsReceipt = "";
        for(Product product : productList){
            productsReceipt += product.getName() + " "+ product.getPrice() +"\n";
        }
        return productsReceipt;
    }

    public String ReceiptTotPrice(){
        float totalSum = 0;
        String totalSumString = "";
        for(Product product : productList){
            totalSum += product.getPrice();
        }
        totalSumString = String.valueOf(totalSum);
        return totalSumString;
    }

    public boolean isBonusRegistered() {
        return BonusRegistered;
    }

    public void setBonusRegistered(boolean bonusRegistered) {
        BonusRegistered = bonusRegistered;
    }
}
