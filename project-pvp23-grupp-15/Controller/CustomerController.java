package Controller;

import Model.Product;
import Model.Sale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    private Sale sale;
    @FXML
    private ListView<Product> productListView;
    @FXML
    private Label priceLabel;
    private ObservableList<Product> listItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //To be able to display the products that are added to the list
        productListView.setItems(listItems);
        productListView.setCellFactory(param -> new ListCell<>(){
            @Override
            protected void updateItem(Product product, boolean empty){
                super.updateItem(product, empty);
                if (empty || product == null) {
                    setText(null);
                } else {
                    String priceText = "";
                    if (sale.isBonusRegistered() && product.getDiscountType().equals("bonusc")) {
                        priceText = product.getBonusCustomerPrice()+ " € ";
                    }
                    else if(product.getDiscountType().equals("everyone")){
                        priceText = product.getDiscountForEveryonePrice()+ " € ";
                    }
                    else if(product.getDiscountType().equals("temp")){
                        priceText = product.getTemporaryDiscountPrice()+ " € ";
                    }
                    else {
                        priceText = product.getPrice()+ " €";
                    }
                    setText(product.getName() + " " + priceText);
                }
            }
        });
    }
    public CustomerController(){

    }
    public void setSale(Sale sale){
        this.sale = sale;
    }

    public void updateUI(){
        listItems.setAll(sale.getProductList());
        //Makes the price only have 2 decimals
        String totalPrice = doubleToStringWithTwoDecimals(sale.getTotalPrice());
        priceLabel.setText("Total price: "+ totalPrice + " €");
        productListView.refresh();

    }
    public void resetUI(){
        listItems.clear();
        priceLabel.setText("Price: 0 €");
        productListView.refresh();
    }

    /****
     * Helper methods
     */
    public String doubleToStringWithTwoDecimals(double Double){
        // Define the format to limit to two decimal places
        DecimalFormat df = new DecimalFormat("#.##");
        // Format the number
        String formattedNumber = df.format(Double);
        return formattedNumber;
    }
}

